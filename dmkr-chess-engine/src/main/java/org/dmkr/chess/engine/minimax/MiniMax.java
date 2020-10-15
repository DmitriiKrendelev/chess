package org.dmkr.chess.engine.minimax;

import com.google.common.collect.ImmutableSortedSet;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.api.*;
import org.dmkr.chess.engine.minimax.MiniMaxContextImpl.MiniMaxContextFactory;
import org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategy;
import org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.*;
import static java.lang.Integer.*;
import static java.lang.Math.max;
import static org.dmkr.chess.engine.api.EvaluationHistoryManager.*;
import static org.dmkr.chess.engine.function.EvaluationFunctionUtils.*;

public class MiniMax<T extends BoardEngine> implements AsyncEngine<T>, ProgressProvider, AutoCloseable {
    private final TreeBuildingStrategy rootLevelTreeStrategy;
	private final boolean isAsynchronous;
	private final boolean enableLinesCutOff;
	private final ExecutorService executorService;
	private final ExecutorService movesCalculationExecutor;
	
	@Delegate(types = EvaluationFunctionAware.class)
	private final MiniMaxContextFactory<T> miniMaxContextFactory;
	
	// State variables
	@Delegate(types = ProgressProvider.class)
	private final ProgressProviderImpl progress = new ProgressProviderImpl();
	private final EvaluationHistoryManager<T> evaluationHistoryManager;

	private final List<Pair<Move, Future<BestLine>>> moveCalculationTasks = new CopyOnWriteArrayList<>();
	private final AtomicInteger zeroLevelBestValue = new AtomicInteger(MIN_VALUE + 1);
	
	private Future<?> task;
	
	public static class MiniMaxBuilder<T extends BoardEngine> {
		private MiniMaxListener miniMaxListener = MiniMaxListener.NOPE;
		private EvaluationFunctionAware<T> evaluationFunctionAware;
		private Integer maxBestLineLenght = 10;
		private Boolean isAsynchronous = false;
		private Integer parallelLevel = 4;
		private Boolean enableLinesCutOff = true;
		private EvaluationHistoryManager<T> evaluationHistoryManager = newForgetHistoryManager();
	}
	
	@Builder(builderClassName = "MiniMaxBuilder", builderMethodName = "minimax")
	MiniMax(@NonNull MiniMaxListener miniMaxListener,
			@NonNull EvaluationFunctionAware<T> evaluationFunctionAware, 
			@NonNull Supplier<TreeBuildingStrategyImpl.TreeBuildingStrategyBuilder> treeStrategyCreator,
			@NonNull Integer maxBestLineLenght, 
			@NonNull Boolean isAsynchronous,
			@NonNull Integer parallelLevel,
			@NonNull Boolean enableLinesCutOff,
			@NonNull EvaluationHistoryManager<T> evaluationHistoryManager) {
		
		checkArgument(parallelLevel > 0);

		final Supplier<TreeBuildingStrategy> treeStrategyCreatorWithEvaluationFunction = () -> {
			TreeBuildingStrategyImpl.TreeBuildingStrategyBuilder strategyBuilder = treeStrategyCreator.get();
			strategyBuilder.evaluationFunction(evaluationFunctionAware.getEvaluationFunction());
			return strategyBuilder.build();
		};

		this.rootLevelTreeStrategy = treeStrategyCreatorWithEvaluationFunction.get();
		this.isAsynchronous = isAsynchronous;
		this.enableLinesCutOff = enableLinesCutOff;
		this.executorService = isAsynchronous ? Executors.newSingleThreadExecutor() : null;
		this.movesCalculationExecutor = parallelLevel == 1 ? Executors.newSingleThreadExecutor() : Executors.newFixedThreadPool(parallelLevel);
		this.miniMaxContextFactory = new MiniMaxContextFactory<>(evaluationFunctionAware, maxBestLineLenght, miniMaxListener, treeStrategyCreatorWithEvaluationFunction);
		this.evaluationHistoryManager = evaluationHistoryManager;
	}
	
	@Override
	public void run(T board) {
		@SuppressWarnings("unchecked")
		final T boardCopy = (T) board.clone();
		
		progress.start();
		final Runnable runner = () -> {
			try {
			    ImmutableSortedSet<BestLine> cachedEvaluation = evaluationHistoryManager.get(boardCopy);

				if (cachedEvaluation != null) {
				    if (cachedEvaluation.size() > 1) {
				        final Iterator<BestLine> iterator = cachedEvaluation.iterator();
				        final BestLine first = iterator.next();
				        final BestLine second = iterator.next();

				        final int secondLineEvaluation = second.getLineValue();
				        if (secondLineEvaluation > REPEAT_MOVES_TREASHOLD) {
				            final ImmutableSortedSet<BestLine> newEvaluation = cachedEvaluation.tailSet(first, false);
				            evaluationHistoryManager.put(boardCopy, newEvaluation);
				            cachedEvaluation = newEvaluation;
                            System.out.println("Second line evaluation: " + secondLineEvaluation + ". Don't repeat moves.");
                        } else {
				            System.out.println("Second line evaluation: " + secondLineEvaluation + ". Repeat moves.");
                        }

                    }
					progress.setFinalEvalution(cachedEvaluation);
					System.out.println("Use cached evaluation:\n" + cachedEvaluation.first());
				} else {
					submit(boardCopy);
				}

				progress.finish();
				evaluationHistoryManager.put(board, (ImmutableSortedSet<BestLine>) progress.getCurrentEvaluation());
			} catch (Exception e) {
				e.printStackTrace();
				progress.cleanup();
			}
		};
		
		if (isAsynchronous) {
			task = executorService.submit(runner);
		} else {
			runner.run();
		}
	}
	
	private void submit(T board) throws Exception {
		final int[] moves = getSortedMoves(rootLevelTreeStrategy.getSubtreeMoves(board, () -> 0), board, getEvaluationFunction());
		final double progressPercent = 1d / moves.length;
		if (moves.length == 0) {
			System.out.println(board.isKingUnderAtack() ? "Chackmate" : "Stalemate");
			
		} else {
			final List<Pair<Move, Future<BestLine>>> moveCalculationTasks = new ArrayList<>();
			final boolean isInverted = board.isInverted();
			for (int moveValue : moves) {
				final Move move = Move.moveOf(moveValue, isInverted);
				final ImmutableSortedSet<BestLine> afterMoveEvaluation = evaluationHistoryManager.getAfterMove(board, moveValue);
				final Callable<BestLine> moveCalculation;
				if (afterMoveEvaluation != null) {
					final BestLine cachedBestLine = afterMoveEvaluation.first();
					final int currentEvaluation = getEvaluationFunction().value(board);
					final BestLine adaptedEvaluation = cachedBestLine.cloneSubstitutingFirst(move, currentEvaluation);
					moveCalculation = () -> adaptedEvaluation;
					System.out.println("Use cached evaluation for move: " + move + " value: " + adaptedEvaluation.getLineValue());
				} else {
					moveCalculation = new MiniMaxCallable<T>((T) board.clone(), this, moveValue, miniMaxContextFactory.get(), this::miniMax, isInverted);
				}

				moveCalculationTasks.add(ImmutablePair.of(move, movesCalculationExecutor.submit(moveCalculation)));
			}

			this.moveCalculationTasks.addAll(moveCalculationTasks);
			
			zeroLevelBestValue.set(MIN_VALUE + 1);
			for (Pair<Move, Future<BestLine>> moveAndResult : moveCalculationTasks) {
				progress.update(moveAndResult.getKey());
				
				final BestLine moveBestLine = moveAndResult.getValue().get();
				final int bestLineValue = moveBestLine.getLineValue();
				zeroLevelBestValue.updateAndGet(bestValue -> max(bestValue, bestLineValue));
				progress.update(moveBestLine, progressPercent);
			}
			
			this.moveCalculationTasks.clear();
		}
	}
	
	private int miniMax(T board, MiniMaxContext<T> context) throws InterruptedException {
		if (!context.isOponentMove()) {
			checkInterrupted();
		}
		
		final List<MoveValue> minimaxValuesStack = context.getMinimaxValuesStack();
		final BestLineBuilder bestLineBuilder = context.getBestLineBuilder();
		final int deep = context.deep();
		
		final MoveValue deepMoveValue = minimaxValuesStack.get(deep).resetToMax();
		final TreeBuildingStrategy treeStrategy = context.getTreeBuildingStrategy();
		
		final int[] moves = treeStrategy.getSubtreeMoves(board, () -> deep);
		if (moves.length == 0) {
			bestLineBuilder.update(true, true, deep + 1);
			return context.getNoMovesFunction().value(board);
		} else {
			for (int i = 0; i < moves.length; i ++) {
				final int move = moves[i];
				final boolean isLeaf = treeStrategy.isLeaf(move, () -> deep);

				context.applyMove(board, move);
				
				final int positionValue = isLeaf ? context.getEvaluationFunction().value(board) : miniMax(board, context);
				final boolean optimized = deepMoveValue.minimize(move, positionValue);
				bestLineBuilder.update(optimized, isLeaf, deep + 1);
				
				context.rollbackMove(board, positionValue);
				
				if (enableLinesCutOff && -deepMoveValue.getValue() > minimaxValuesStack.get(deep - 1).getValue()) {
					break;
				}
				
				if (enableLinesCutOff && deep == 1 && -deepMoveValue.getValue() > -zeroLevelBestValue.get()) {
					break;
				}
			}
			
			progress.addCount(moves.length);
		}
		
		return -deepMoveValue.getValue();
	}
	
	@Override
	public void interrupt() {
		checkState(isAsynchronous, "Cannot interrupt synchronous minimax");
		final Consumer<Future<?>> cancel = future -> future.cancel(true);
		
		if (task != null) {
			cancel.accept(task);
		}
		
		moveCalculationTasks.forEach(moveAndTask -> moveAndTask.getValue().cancel(true));
		moveCalculationTasks.clear();
	}
	
	private void checkInterrupted() throws InterruptedException {
		if (Thread.currentThread().isInterrupted()) {
			throw new InterruptedException();
		}
	}
	
	@Override
	public void join() {
		checkState(isAsynchronous, "Cannot join synchronous minimax");
		if (task != null) {
			try {
				task.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} catch (CancellationException e) {
				System.out.println("Minimax finding move was cancelled");
			}
		}
	}

	@Override
	public void close() {
        System.out.println("Close: " + getClass().getSimpleName());
		this.executorService.shutdownNow();
		this.movesCalculationExecutor.shutdownNow();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(256);
		sb.append("Time: ").append(getCurrentTimeInProgress()).append("ms,  Count: ").append(getCurrentCount()).append('\n');
		sb.append(getBestLine()).append('\n');
		int index = 0;
		for (BestLine bestLine : Optional.ofNullable(getCurrentEvaluation()).orElse(Collections.emptySortedSet())) {
			sb.append((++ index) + ". " + bestLine.getMoves() + " : " + bestLine.getLineValue() + '\n');
		}
		return sb.toString();
	}
}
