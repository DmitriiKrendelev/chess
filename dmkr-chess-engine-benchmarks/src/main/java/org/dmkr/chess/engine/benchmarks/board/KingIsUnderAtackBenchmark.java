package org.dmkr.chess.engine.benchmarks.board;

import java.util.concurrent.TimeUnit;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.board.bit.BitBoardBuilder;
import org.dmkr.chess.engine.board.impl.BoardBuilder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@Measurement(iterations = 3)
@Warmup(iterations = 1)
@BenchmarkMode(value = Mode.AverageTime)
@Fork(value = 3, warmups = 1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class KingIsUnderAtackBenchmark {

	private static final BoardEngine BOARD = BoardBuilder.of(
			"- - - - r - - -", 
			"- B Q - p R - -", 		
			"- - - b - - - -", 		
			"- p - P k p - -", 		
			"- - - - - - - B", 		
			"- - - - - P P p",
			"r N N - - - - K", 		
			"- - n n - - - -")
			.build();

	private static final BoardEngine BIT_BOARD = BitBoardBuilder.of(
			"- - - - r - - -", 
			"- B Q - p R - -", 		
			"- - - b - - - -", 		
			"- p - P k p - -", 		
			"- - - - - - - B", 		
			"- - - - - P P p",
			"r N N - - - - K", 		
			"- - n n - - - -")
			.build();	
	
	@Benchmark
	public void boardIsKingUnderAtack(Blackhole blackHole) {
		blackHole.consume(BOARD.calculateIsKingUnderAtack());
	}
	
	@Benchmark
	public void bitIsKingUnderAtack(Blackhole blackHole) {
		blackHole.consume(BIT_BOARD.calculateIsKingUnderAtack());
	}
}
