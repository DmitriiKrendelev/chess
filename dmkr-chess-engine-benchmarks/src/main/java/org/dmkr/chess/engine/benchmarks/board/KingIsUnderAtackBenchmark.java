package org.dmkr.chess.engine.benchmarks.board;

import java.util.concurrent.TimeUnit;

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
public class KingIsUnderAtackBenchmark {

	@Benchmark
	public void boardIsKingUnderAtack(Blackhole blackHole) {
		blackHole.consume(getNextBoardPosition().calculateIsKingUnderAtack());
	}
	
	@Benchmark
	public void bitIsKingUnderAtack(Blackhole blackHole) {
		blackHole.consume(getNextBitBoardPosition().calculateIsKingUnderAtack());
	}
}
