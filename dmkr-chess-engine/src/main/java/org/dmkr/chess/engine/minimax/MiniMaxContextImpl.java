package org.dmkr.chess.engine.minimax;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.common.collections.ValueAutoCreateList;
import org.dmkr.chess.common.primitives.IntStack;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.api.MiniMaxListener;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.common.EvaluationFunctionDeepPenalty;
import org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@Getter
class MiniMaxContextImpl<T extends BoardEngine> implements MiniMaxContext<T> {
	private final IntStack currentPath = new IntStack();
	private final List<MoveValue> minimaxValuesStack = new ValueAutoCreateList<>(MoveValue::new);;

	private final Optional<MiniMaxListener> miniMaxListener;
	private final BestLineBuilder bestLineBuilder;
	
	@Delegate
	private final EvaluationFunctionAware<T> evaluationFunctionAware;
	private final TreeBuildingStrategy treeBuildingStrategy;
	
	private MiniMaxContextImpl(
			int maxBestLineLenght,
			MiniMaxListener miniMaxListener,
			EvaluationFunctionAware<T> evaluationFunctionAware,
			TreeBuildingStrategy treeBuildingStrategy) {
		
		this.bestLineBuilder = new BestLineBuilder(maxBestLineLenght, currentPath);
		this.miniMaxListener = Optional.ofNullable(miniMaxListener);
		
		final EvaluationFunction<T> deepnessPenaltyFunction = new EvaluationFunctionDeepPenalty<>(this::deep, this::isOponentMove);
		this.evaluationFunctionAware = evaluationFunctionAware.and(deepnessPenaltyFunction);
		this.treeBuildingStrategy = treeBuildingStrategy;
	}
	
	@Override
	public void reset(boolean inverted, int initialPositionValue) {
		this.currentPath.clear();
		this.minimaxValuesStack.clear();
		this.bestLineBuilder.clear();
		this.bestLineBuilder.setInverted(inverted);
		this.bestLineBuilder.setInitialPositionValue(initialPositionValue);
	}
	
	@Override
	public int deep() {
		return currentPath.size();
	}
	
	@Override
	public void applyMove(BoardEngine board, int move) {
		miniMaxListener.ifPresent(listener -> listener.onMove(Move.moveOf(move, board.isInverted())));
		
		currentPath.push(move);
		board.applyMove(move);
		board.invert();
	}
	
	@Override
	public void rollbackMove(BoardEngine board, int moveValue) {
		board.invert();
		board.rollbackMove();
		currentPath.pop();
		
		miniMaxListener.ifPresent(listener -> listener.onEvaluation(moveValue));
	}
	
	@Override
	public boolean isOponentMove() {
		return (deep() & 1) == 1;
	}

	@RequiredArgsConstructor
	static class MiniMaxContextFactory<T extends BoardEngine> implements Supplier<MiniMaxContext<T>>, EvaluationFunctionAware<T> {
		
		@Delegate
		private final EvaluationFunctionAware<T> evaluationFunctionAware;

		private final int maxBestLineLenght;
		private final MiniMaxListener miniMaxListener;
		private final Supplier<TreeBuildingStrategy> treeBuildingStrategyCreator;

		@Override
		public MiniMaxContext<T> get() {
			return new MiniMaxContextImpl<>(maxBestLineLenght, miniMaxListener, evaluationFunctionAware, treeBuildingStrategyCreator.get());
		}
	}
}
