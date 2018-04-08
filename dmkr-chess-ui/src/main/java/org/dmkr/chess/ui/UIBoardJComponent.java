package org.dmkr.chess.ui;

import com.google.inject.Inject;
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
import org.dmkr.chess.ui.listeners.BestLineVisualizerListener;
import org.dmkr.chess.ui.listeners.PiecesDragAndDropListener;
import org.dmkr.chess.ui.visualize.BestLineVisualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptySet;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.dmkr.chess.ui.helpers.UIColorsHelper.getPositionChangedColor;

@SuppressWarnings("serial")
public class UIBoardJComponent extends JComponent {
	@Inject private Player player;
	@Inject private UIBoardConfig config;
	@Inject private BoardEngine board;
	@Inject private AsyncEngine<BoardEngine> engine;
	@Inject private UIMousePositionHelper mousePositionHelper;

	@Inject private UIBoardImagesHelper imagesHelper;
	@Inject private UIBoardCoordsHelper coordsHelper;
	@Inject private UIBoardTextHelper textHelper;
	
	@Inject private PiecesDragAndDropListener mouseListener;
	@Inject private BestLineVisualizerListener bestLineVisualizerListener;
   
	private final AtomicReference<MovingPiece> movingPieceHolder = new AtomicReference<>();
	private final AtomicReference<BestLineVisualizer> paintComponentOverride = new AtomicReference<>();
	
	public void run() {
	    if (!player.isWhite()) {
            engine.run(board);
        }

		newScheduledThreadPool(1).scheduleWithFixedDelay(this::doOponentMove, 0, config.getRepaintTimeoutMillis(), MILLISECONDS);
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
			drawPiece(board.at(pressedField), mousePosition.x(), mousePosition.y(), g);
		}
		
		resetCursor(pressedField, mouseAtField, isPressed);

		drawProgressBar(g);
		textHelper.drawText(engine, board, (Graphics2D) g);
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
		if (pressedField == null) {
			return;
		}
		
	    draw(pressedField, config.getPressedFieldColor(), g);
	    
	    if (!engine.isInProgress()) {
	    	board.getAllowedMovesFields().getOrDefault(pressedField, emptySet()).forEach(f -> draw(f, config.getAllowedMovesColor(), g));
	    }
	}
	
	private void draw(Field field, Color color, Graphics g) {
		if (field == null) {
			return;
		}
		
		coordsHelper.getFieldRect(field).draw((Graphics2D) g, color);
	}
	
	private void drawMovingPiece(MovingPiece piece, Graphics g) {
		if (piece != null) {
			drawPiece(piece.getColoredPiece(), piece.getLocation().x(), piece.getLocation().y(), g);
		}
	}
	
	private void drawPiece(ColoredPiece coloredPiece, Field field, Graphics g) {
		final UIPoint fieldCenter = coordsHelper.getFieldCenter(field);
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
		if (!engine.isInProgress() && (config.getArrowDrawDelayMillis() + engine.getEvaluationFinishedTime()) < currentTimeMillis()) {
			return;
		}

		final BestLine bestLine = engine.getBestLine();
		final Move bestMove = Optional.ofNullable(bestLine)
				.map(BestLine::getMoves)
				.map(moves -> moves.isEmpty() ? null : moves.get(0))
				.orElse(null);

		if (bestMove != null) {
			final Color arrowColor = config.getArrowColor() != null ? config.getArrowColor() : getPositionChangedColor(bestLine.getLineValueChange());
			coordsHelper.arrow(bestMove).draw(arrowColor, (Graphics2D) g);
		}
	}
	
	private void drawProgressBar(Graphics g) {
		final UIRect progressBar = config.getProgressBar();
		final Color progressBarColor = config.getProgressBarColor();
		final Color progressBarBorderColor = config.getProgressBarColor();
		
		if (progressBar == null || progressBarColor == null || progressBarBorderColor == null) {
			return;
		}
		
		progressBar.draw((Graphics2D) g, progressBarBorderColor);
		
		g.setColor(progressBarColor);
		g.fillRect(progressBar.x(), progressBar.y(), (int) (progressBar.getWidth() * engine.getCurrentProgressPerUnit()), progressBar.getHight());
	
		textHelper.drawSingleRow(engine.getCurrentProgressPercents() + "%",
				progressBar.x() + (progressBar.getWidth() / 2),
				progressBar.y() + (progressBar.getHight() / 2),
				(Graphics2D) g);
	}
	
	private void resetCursor(Field pressedField, Field mouseAtField, boolean isPressed) {
		final boolean mouseAtPiece = mouseAtField != null && !board.at(mouseAtField).isNull();
		
		if (isPressed || mouseAtPiece) {
			setCursor(isPressed ? imagesHelper.getClosedHandCursor() : imagesHelper.getOpenHandCursor());
		} else {
			setCursor(imagesHelper.getDefaultCursor());
		}
	}
	
	public void onMove(Move move) {
		if (engine.isInProgress() || !board.getAllowedMoves().contains(move)) {
			return;
		}
		
		bestLineVisualizerListener.clear();
		board.applyMove(move);
		repaint();
		engine.run(board);
	}
	
	private void doOponentMove() {
		final boolean isBoardInvertedForPlayer = board.isInverted() != player.isWhite();
		if (isBoardInvertedForPlayer || engine.isInProgress()) {
			return;
		}
		final Move oponentMove = engine.getBestMove();
		final ColoredPiece piece = board.at(oponentMove.from());
		movingPieceHolder.set(coordsHelper.new MovingPiece(oponentMove, piece, () -> movingPieceHolder.set(null)));
		board.applyMove(oponentMove);
	}

	public boolean isBestLineVisualisationEnabled() {
		return paintComponentOverride.get() == null && !engine.isInProgress();
	}
	
	public void startBestLineVisualisation(BestLine bestLine) {
		paintComponentOverride.set(
				new BestLineVisualizer(bestLine, board, coordsHelper) {
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
}
