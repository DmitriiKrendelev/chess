package org.dmkr.chess.ui;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.dmkr.chess.api.Board;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.ColoredPiece;
import org.dmkr.chess.api.model.Field;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.minimax.BestLine;
import org.dmkr.chess.ui.api.model.UIPoint;
import org.dmkr.chess.ui.api.model.UIRect;
import org.dmkr.chess.ui.config.UIBoardConfig;
import org.dmkr.chess.ui.helpers.UIBoardCoordsHelper;
import org.dmkr.chess.ui.helpers.UIBoardCoordsHelper.MovingPiece;
import org.dmkr.chess.ui.helpers.UIBoardImagesHelper;
import org.dmkr.chess.ui.helpers.UIBoardTextHelper;
import org.dmkr.chess.ui.helpers.UIMousePositionHelper;
import org.dmkr.chess.ui.listeners.impl.BestLineVisualizerListener;
import org.dmkr.chess.ui.listeners.impl.PiecesDragAndDropListener;
import org.dmkr.chess.ui.visualize.BestLineVisualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptySet;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.dmkr.chess.ui.helpers.UIColorsHelper.getPositionChangedColor;

@SuppressWarnings("serial")
public class UIBoardJComponent extends JComponent implements AutoCloseable {
	@Inject private Player player;
	@Inject private UIBoardConfig config;
	@Inject private BoardEngine board;
	@Inject @Named("engine1") private AsyncEngine<BoardEngine> engine1;
	@Inject @Named("engine2") private AsyncEngine<BoardEngine> engine2;

	@Inject private UIMousePositionHelper mousePositionHelper;

	@Inject private UIBoardImagesHelper imagesHelper;
	@Inject private UIBoardCoordsHelper coordsHelper;
	@Inject private UIBoardTextHelper textHelper;

	@Inject private PiecesDragAndDropListener mouseListener;
	@Inject private BestLineVisualizerListener bestLineVisualizerListener;

	@Inject private AtomicReference<MovingPiece> movingPieceHolder = new AtomicReference<>();
	@Inject private AtomicReference<BestLineVisualizer> paintComponentOverride = new AtomicReference<>();

	private final ScheduledExecutorService oponentMoveExecutorService = newScheduledThreadPool(1);

	public void run() {
	    if (player.isBoardInvertedForPlayer(board)) {
			engine1.run(board);
        } else if (player.isReadOnly()) {
	    	engine2.run(board);
		}

		oponentMoveExecutorService.scheduleWithFixedDelay(this::doEngineMove, 0, config.getRepaintTimeoutMillis(), MILLISECONDS);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(imagesHelper.getBackgroundImage(player.getColor()), 0, 0, this);

		final Optional<Consumer<Graphics>> override = Optional.ofNullable(paintComponentOverride.get());
		if (override.isPresent()) {
			override.get().accept(g);
			repaint();
			return;
		}

		final Field pressedField = mouseListener.getPressedField();
		final Field mouseAtField = mouseListener.getMouseAtField();
		final boolean isPressed = mouseListener.isPressed();
		
		drawBoardPieces(board, movingPieceHolder.get(), pressedField, g);
		
		if (isPressed) {
			final UIPoint mousePosition = mousePositionHelper.getMouseLocation();
			final ColoredPiece takenPiece = board.at(pressedField);

			if (!takenPiece.isNull()) {
                final boolean drawAllowedMoveField = board.getAllowedMovesFields().getOrDefault(pressedField, emptySet()).contains(mouseAtField);

                if (drawAllowedMoveField) {
                    draw(mouseAtField, config.getPressedFieldColor(), g);
                }

                drawPiece(takenPiece, mousePosition.x(), mousePosition.y(), g);
            }
		}
		
		resetCursor(mouseAtField, isPressed);

		drawProgressBar(g);

		textHelper.drawText((Graphics2D) g, engine1, config.getTextPosition1());
		if (player.isReadOnly()) {
			textHelper.drawText((Graphics2D) g, engine2, config.getTextPosition2());
		}
		drawBestMove(g);

		repaint();
	}
	
	private void drawBoardPieces(Board board, MovingPiece movingPiece, Field pressedField, Graphics g) {
		drawPossibleMoves(pressedField, g);
		
		final Field movingPieceTo = movingPiece == null ? null : movingPiece.getTo();
		board.forEach(
				(field, coloredPiece) -> (field != pressedField) && field != movingPieceTo,
				(field, coloredPiece) -> drawPiece(coloredPiece, field, g)
			);

		drawMovingPiece(movingPiece, g);
	}
	
	private void drawPossibleMoves(Field pressedField, Graphics g) {
		if (pressedField == null || player.isReadOnly()) {
			return;
		}
		
	    draw(pressedField, config.getPressedFieldColor(), g);
	    
	    if (!engine1.isInProgress()) {
	    	board.getAllowedMovesFields().getOrDefault(pressedField, emptySet()).forEach(f -> draw(f, config.getAllowedMovesColor(), g));
	    }
	}
	
	private void draw(Field field, Color color, Graphics g) {
		if (field == null) {
			return;
		}
		
		coordsHelper.getFieldRect(field, player).draw((Graphics2D) g, color);
	}

	private void drawMovingPiece(MovingPiece piece, Graphics g) {
		if (piece != null) {
			drawPiece(piece.getColoredPiece(), piece.getLocation().x(), piece.getLocation().y(), g);
		}
	}

	private void drawPiece(ColoredPiece coloredPiece, Field field, Graphics g) {
		final UIPoint fieldCenter = coordsHelper.getFieldCenter(field, player);
		drawPiece(coloredPiece, fieldCenter.x(), fieldCenter.y(), g);
	}

	private void drawPiece(ColoredPiece coloredPiece, int xCenter, int yCenter, Graphics g) {
		if (coloredPiece.isNull()) {
			return;
		}

		final BufferedImage pieceImage = imagesHelper.getImage(coloredPiece);
		final UIPoint imagePoint = coordsHelper.getImageCoords(pieceImage, xCenter, yCenter);

		g.drawImage(pieceImage, imagePoint.x(), imagePoint.y(), this);
	}

	private void drawBestMove(Graphics g) {
		// TODO
		if (!engine1.isInProgress() && (config.getArrowDrawDelayMillis() + engine1.getEvaluationFinishedTime()) < currentTimeMillis()) {
			return;
		}

		final BestLine bestLine = engine1.getBestLine();
		final Move bestMove = Optional.ofNullable(bestLine)
				.map(BestLine::getMoves)
				.map(moves -> moves.isEmpty() ? null : moves.get(0))
				.orElse(null);

		if (bestMove != null) {
			final Color arrowColor = config.getArrowColor() != null ? config.getArrowColor() : getPositionChangedColor(bestLine.getLineValueChange());
			coordsHelper.arrow(bestMove, player).draw(arrowColor, (Graphics2D) g);
		}
	}
	
	private void drawProgressBar(Graphics g) { // TODO
		final UIRect progressBar = config.getProgressBar();
		final Color progressBarColor = config.getProgressBarColor();
		final Color progressBarBorderColor = config.getProgressBarColor();
		
		if (progressBar == null || progressBarColor == null || progressBarBorderColor == null) {
			return;
		}
		
		progressBar.draw((Graphics2D) g, progressBarBorderColor);
		
		g.setColor(progressBarColor);
		g.fillRect(progressBar.x(), progressBar.y(), (int) (progressBar.getWidth() * engine1.getCurrentProgressPerUnit()), progressBar.getHight());
	
		textHelper.drawSingleRow(engine1.getCurrentProgressPercents() + "%",
				progressBar.x() + (progressBar.getWidth() / 2),
				progressBar.y() + (progressBar.getHight() / 2),
				(Graphics2D) g);
	}
	
	private void resetCursor(Field mouseAtField, boolean isPressed) {
		final boolean mouseAtPiece = mouseAtField != null && !board.at(mouseAtField).isNull();
		
		if (isPressed || mouseAtPiece) {
			setCursor(isPressed ? imagesHelper.getClosedHandCursor() : imagesHelper.getOpenHandCursor());
		} else {
			setCursor(imagesHelper.getDefaultCursor());
		}
	}
	
	public void doPlayerMove(Move move) {
		if (player.isReadOnly() || engine1.isInProgress() || !board.getAllowedMoves().contains(move)) {
			return;
		}
		
		bestLineVisualizerListener.clear();
		board.applyMove(move);
		repaint();
		engine1.run(board);
	}

	private void doEngineMove() {
		doEngine1Move();
		doEngine2Move();
	}

	private void doEngine1Move() {
		if (!player.isBoardInvertedForPlayer(board) || engine1.isInProgress()) {
			return;
		}
		final Move move = engine1.getBestMove();
		final ColoredPiece piece = board.at(move.from());
		movingPieceHolder.set(coordsHelper.new MovingPiece(move, piece, player, () -> movingPieceHolder.set(null)));
		board.applyMove(move);
		if (player.isReadOnly()) {
			engine2.run(board);
		}
	}
	
	private void doEngine2Move() {
		if (player.isBoardInvertedForPlayer(board) || engine2.isInProgress()) {
			return;
		}
		final Move move = engine2.getBestMove();
		final ColoredPiece piece = board.at(move.from());
		movingPieceHolder.set(coordsHelper.new MovingPiece(move, piece, player, () -> movingPieceHolder.set(null)));
		board.applyMove(move);
		if (player.isReadOnly()) {
			engine1.run(board);
		}
	}

	private void validateComponent() {

    }

	public boolean isBestLineVisualisationEnabled() {
		return !player.isReadOnly() && paintComponentOverride.get() == null && !engine1.isInProgress();
	}
	
	public void startBestLineVisualisation(BestLine bestLine) {
		paintComponentOverride.set(
				new BestLineVisualizer(bestLine, board, player, coordsHelper) {
					@Override
					public void accept(Graphics g) {
						drawBoardPieces(board, movingPieceHolder.get(), null, g);
					}
					@Override
					public void onFinihed() {
						paintComponentOverride.set(null);
					}
				});
	}

	@Override
	public void close() throws Exception {
        System.out.println("Close: " + getClass().getSimpleName());
		oponentMoveExecutorService.shutdownNow();
		engine1.close();
		engine2.close();
		setVisible(false);
	}
}
