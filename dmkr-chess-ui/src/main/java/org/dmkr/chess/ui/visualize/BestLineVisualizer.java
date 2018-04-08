package org.dmkr.chess.ui.visualize;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.dmkr.chess.api.Board;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.minimax.BestLine;
import org.dmkr.chess.ui.helpers.UIBoardCoordsHelper;
import org.dmkr.chess.ui.helpers.UIBoardCoordsHelper.MovingPiece;

public abstract class BestLineVisualizer implements Consumer<Graphics> {
	private final UIBoardCoordsHelper coordsHelper;
	private final Iterator<Move> movesIterator;
	protected final Board board;
	protected final AtomicReference<MovingPiece> movingPieceHolder = new AtomicReference<>();
	
	
	public BestLineVisualizer(BestLine bestLine, BoardEngine board, UIBoardCoordsHelper coordsHelper) {
		this.movesIterator = bestLine.getMoves().iterator();
		this.board = board.clone();
		this.board.rollback();
		this.coordsHelper = coordsHelper;
		
		setNextMovingPiece();
	}
	
	private void setNextMovingPiece() {
		if (!movesIterator.hasNext()) {
			onFinihed();
			return;
		}
		
		final Move nextMove = movesIterator.next();
		final MovingPiece movingPiece = coordsHelper.new MovingPiece(nextMove, board, this::setNextMovingPiece);
		board.applyMove(nextMove);
		movingPieceHolder.set(movingPiece);
	}
	
	public abstract void onFinihed();
}
