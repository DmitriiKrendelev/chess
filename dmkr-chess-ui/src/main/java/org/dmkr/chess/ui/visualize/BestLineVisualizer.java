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
import org.dmkr.chess.ui.helpers.UIBoardCoordsHelper.MovingItem;

public abstract class BestLineVisualizer implements Consumer<Graphics> {
	private final UIBoardCoordsHelper coordsHelper;
	private final Iterator<Move> movesIterator;
	protected final Board board;
	protected final AtomicReference<MovingItem> movingItemHolder = new AtomicReference<>();
	
	
	public BestLineVisualizer(BestLine bestLine, BoardEngine board, UIBoardCoordsHelper coordsHelper) {
		this.movesIterator = bestLine.getMoves().iterator();
		this.board = board.clone();
		this.board.rollback();
		this.coordsHelper = coordsHelper;
		
		setNextMovingItem();
	}
	
	private void setNextMovingItem() {
		if (!movesIterator.hasNext()) {
			onFinihed();
			return;
		}
		
		final Move nextMove = movesIterator.next();
		final MovingItem movingItem = coordsHelper.new MovingItem(nextMove, board, this::setNextMovingItem);
		board.applyMove(nextMove);
		movingItemHolder.set(movingItem);
	}
	
	public abstract void onFinihed();
}
