package org.dmkr.chess.engine.minimax;

import java.util.Arrays;

import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.common.collections.ArrayStack;
import org.dmkr.chess.engine.api.MiniMaxListener;

public abstract class MiniMaxFilteringListener implements MiniMaxListener {
	protected static final int DEFAULT_CAPACITY = 10;
	protected final ArrayStack<Move> path = new ArrayStack<>(DEFAULT_CAPACITY);
	protected final Move[] filterMoves;
	protected boolean down;
	
	protected MiniMaxFilteringListener(Move... filterMoves) {
		this.filterMoves = Arrays.copyOf(filterMoves, filterMoves.length);
	}
	
	protected abstract void onMoveFiltered(Move move);
	
	protected abstract void onEvaluationFiltered(int moveValue);
	
	@Override
	public void onMove(Move move) {
		path.push(move);
		
		if (path.startsWith(filterMoves)) {
			onMoveFiltered(move);
		}
		down = true;
	}
	
	@Override
	public void onEvaluation(int moveValue) {
		if (path.startsWith(filterMoves)) {
			onEvaluationFiltered(moveValue);
		}
		
		path.pop();
		down = false;
	}

}
