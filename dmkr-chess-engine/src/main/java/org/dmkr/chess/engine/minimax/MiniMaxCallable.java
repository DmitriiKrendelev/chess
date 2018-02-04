package org.dmkr.chess.engine.minimax;

import java.util.List;
import java.util.concurrent.Callable;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MiniMaxCallable<T extends BoardEngine> implements Callable<BestLine> {
	private final T board;
	private final EvaluationFunctionAware<T> evaluationFunctionAvare;
	private final int startingMove;
	private final MiniMaxContext<T> context;
	private final MiniMaxCalculator<T> miniMaxCalculator;
	private final boolean inverted;
	
	@FunctionalInterface
	static interface MiniMaxCalculator<T extends BoardEngine> {
		int miniMax(T board, MiniMaxContext<T> context) throws Exception;
	}
	
	@Override
	public BestLine call() throws Exception {
		context.reset(inverted, evaluationFunctionAvare.getEvaluationFunction().value(board));
		
		final BestLineBuilder bestLineBuilder = context.getBestLineBuilder();
		final List<MoveValue> minimaxValuesStack = context.getMinimaxValuesStack();
		final MoveValue deepMoveValue = minimaxValuesStack.get(0).resetToMax();
		
		context.applyMove(board, startingMove);
		
		final int positionValue;
		try {
			positionValue = miniMaxCalculator.miniMax(board, context);
		} catch (InterruptedException e) {
			System.out.println(this + " was interrupted.");
			throw e;
		}
		
		deepMoveValue.minimize(startingMove, positionValue);
		bestLineBuilder.update(true, false, 1);
		
		context.rollbackMove(board, positionValue);
		
		bestLineBuilder.setLineValue(-deepMoveValue.getValue());
		deepMoveValue.resetToMax();
		
		return bestLineBuilder.build();
	}
	
	@Override
	public String toString() {
		return "Calculate: " + Move.moveOf(startingMove, inverted);
	}
}
