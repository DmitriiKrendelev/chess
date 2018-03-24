package org.dmkr.chess.engine.benchmarks.board;

import static org.dmkr.chess.engine.board.impl.MovesSelectorImpl.movesSelector;

import java.util.concurrent.TimeUnit;

import org.dmkr.chess.api.MovesSelector;
import org.dmkr.chess.engine.board.impl.MovesSelectorImpl;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import static org.dmkr.chess.engine.benchmarks.data.PositionsProvider.*;

@Measurement(iterations = 3)
@Warmup(iterations = 1)
@BenchmarkMode(value = Mode.AverageTime)
@Fork(value = 3, warmups = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class GetMovesBenchmark {
	private static final MovesSelector MOVES_SELECTOR_ALLOW_CHECKS = movesSelector()
			.checkKingUnderAtack(false)
			.build();
	
	@Benchmark
	public void boardGetMoves(Blackhole blackHole) {
		blackHole.consume(getNextBoardPosition().calculateAllowedMoves(MovesSelectorImpl.DEFAULT));
	}
	
	@Benchmark
	public void bitGetMoves(Blackhole blackHole) {
		blackHole.consume(getNextBitBoardPosition().calculateAllowedMoves(MovesSelectorImpl.DEFAULT));
	}
	
	@Benchmark
	public void boardGetMovesAllowChecks(Blackhole blackHole) {
		blackHole.consume(getNextBoardPosition().calculateAllowedMoves(MOVES_SELECTOR_ALLOW_CHECKS));
	}
	
	@Benchmark
	public void bitGetMovesAllowChecks(Blackhole blackHole) {
		blackHole.consume(getNextBitBoardPosition().calculateAllowedMoves(MOVES_SELECTOR_ALLOW_CHECKS));
	}
}
