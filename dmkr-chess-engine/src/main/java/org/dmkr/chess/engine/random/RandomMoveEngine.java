package org.dmkr.chess.engine.random;

import com.google.common.collect.ImmutableSortedSet;
import lombok.Getter;
import lombok.SneakyThrows;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.ProgressProvider;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.minimax.BestLine;

import java.util.Random;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.*;
import static org.dmkr.chess.api.model.Move.*;

@Getter
public class RandomMoveEngine<T extends BoardEngine> implements AsyncEngine<T>, ProgressProvider, AutoCloseable {
    private final long SLEEP_TIME = 2000L;
    private final EvaluationFunction<T> RANDOM_EVALUATION_FUNCTION = new EvaluationFunction<T>() {
        @Override
        public int value(T board) {
            return 0;
        }

        @Override
        public String toString() {
            return "Random";
        }

    };

    private final Random random = new Random();
    private volatile SortedSet<BestLine> currentEvaluation;
    private final int currentProgressPercents = 100;
    private final long currentCount = 0;
    private final Move currentMove = null;
    private final long speed = 0;
    private volatile long evaluationStartTime;
    private volatile long evaluationFinishedTime;
    private final double parallelLevel = 1.0;
    private final long fullCount = 0;
    private final long fullTime = 0;
    private final long fullTotalTime = 0;
    private final long fullSpeed = 0;
    private final double fullParallelLevel = 1;
    private final EvaluationFunction<T> evaluationFunction = RANDOM_EVALUATION_FUNCTION;
    private final EvaluationFunction<T> noMovesFunction = RANDOM_EVALUATION_FUNCTION;
    private volatile boolean isInProgress;

    @SneakyThrows
    @Override
    public void run(T board) {
        isInProgress = true;
        evaluationStartTime = System.currentTimeMillis();
        final int[] moves = board.allowedMoves();
        final int move = moves[random.nextInt(moves.length)];

        currentEvaluation = ImmutableSortedSet.copyOf((b1, b2) -> 0, singleton(BestLine.of(moveOf(move, board.isInverted()))));

        TimeUnit.MILLISECONDS.sleep(SLEEP_TIME);
        evaluationFinishedTime = System.currentTimeMillis();
        isInProgress = false;
    }

    @Override
    public void interrupt() {

    }

    @Override
    public void join() {

    }

    @Override
    public int getCurrentTimeInProgress() {
        return (int) (isInProgress ? System.currentTimeMillis() - evaluationStartTime : evaluationFinishedTime - evaluationStartTime);
    }

    @Override
    public boolean isInProgress() {
        return false;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public void close() {

    }
}
