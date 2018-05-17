package org.dmkr.chess.engine.benchmarks.board;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.benchmarks.data.PositionsProvider;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.*;

@Measurement(iterations = 3)
@Warmup(iterations = 1)
@BenchmarkMode(value = Mode.AverageTime)
@Fork(value = 3, warmups = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ApplayRollbackMoveBenchmark {
    private static final BoardEngine A_BOARD;
    private static final BoardEngine A_BIT_BOARD;

    private static final int[] A_BOARD_MOVES;
    private static final int[] A_BIT_BOARD_MOVES;

    private static int i = 0;

    static {
        A_BOARD = PositionsProvider.aBoard();
        A_BIT_BOARD = PositionsProvider.aBitBoard();

        A_BOARD_MOVES = A_BOARD.allowedMoves();
        A_BIT_BOARD_MOVES = A_BIT_BOARD.allowedMoves();

        checkState(A_BOARD_MOVES.length == A_BIT_BOARD_MOVES.length,
                "%s\n%s\n%s\n%s\n",
                A_BOARD,
                Arrays.toString(A_BOARD_MOVES),
                A_BIT_BOARD,
                Arrays.toString(A_BIT_BOARD_MOVES));
    }

    @Benchmark
    public void boardApplayRollbackMove(Blackhole blackHole) {
        final int i = (++ ApplayRollbackMoveBenchmark.i);
        final int index = i % A_BOARD_MOVES.length;
        final int move = A_BOARD_MOVES[index];

        A_BOARD.applyMove(move);
        blackHole.consume(A_BOARD);
        A_BOARD.rollbackMove();
        blackHole.consume(A_BOARD);
    }

    @Benchmark
    public void bitGetMoves(Blackhole blackHole) {
        final int i = (++ this.i);
        final int index = i % A_BOARD_MOVES.length;
        final int move = A_BOARD_MOVES[index];

        A_BIT_BOARD.applyMove(move);
        blackHole.consume(A_BIT_BOARD);
        A_BIT_BOARD.rollbackMove();
        blackHole.consume(A_BIT_BOARD);
    }

}
