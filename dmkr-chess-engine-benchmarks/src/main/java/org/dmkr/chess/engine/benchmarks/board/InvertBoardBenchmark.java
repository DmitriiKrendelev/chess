package org.dmkr.chess.engine.benchmarks.board;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.BoardEngine;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

import static org.dmkr.chess.engine.benchmarks.data.PositionsProvider.getNextBitBoardPosition;
import static org.dmkr.chess.engine.benchmarks.data.PositionsProvider.getNextBoardPosition;

@Measurement(iterations = 3)
@Warmup(iterations = 1)
@BenchmarkMode(value = Mode.AverageTime)
@Fork(value = 3, warmups = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class InvertBoardBenchmark {
    private static final BoardEngine BOARD = getNextBoardPosition().clone();
    private static final BitBoard BIT_BOARD = (BitBoard) getNextBitBoardPosition().clone();

    @Benchmark
    public void boardInvertionTest(Blackhole blackHole) {
        blackHole.consume(BOARD.invert());
    }

    @Benchmark
    public void bitBoardInvertionTest(Blackhole blackHole) {
        blackHole.consume(BIT_BOARD.invert());
    }
}
