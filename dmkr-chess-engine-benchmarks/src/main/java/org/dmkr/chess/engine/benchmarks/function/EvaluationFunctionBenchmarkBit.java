package org.dmkr.chess.engine.benchmarks.function;

import static org.dmkr.chess.engine.board.AbstractBoard.resetCache;
import static org.dmkr.chess.engine.function.Functions.ITEM_MOVES;
import static org.dmkr.chess.engine.function.Functions.ITEM_POSITIONS;
import static org.dmkr.chess.engine.function.Functions.ITEM_VALUES;
import static org.dmkr.chess.engine.function.Functions.POWN_STRUCTURE;
import static org.dmkr.chess.engine.function.Functions.QUEEN_IN_THE_CENTER;

import java.util.concurrent.TimeUnit;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.engine.board.bit.BitBoardBuilder;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.Functions;
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
public class EvaluationFunctionBenchmarkBit {
	
	private static final Class<BitBoard> BOARD_TYPE = BitBoard.class;
	
	private static final BitBoard BIT_BOARD = BitBoardBuilder.of(
			"- - - - r - - -", 
			"- B Q - p R - -", 		
			"- - - r - - - -", 		
			"- p - P k p - -", 		
			"- - - - - - - B", 		
			"- - - - - P P -",
			"- N N - - - - K", 		
			"- - n n - - - -")
			.build();
	
	private static final EvaluationFunction<BitBoard> POWNS_STRUCTURE_FUNCTION = POWN_STRUCTURE.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> ITEM_VALUES_FUNCTION = ITEM_VALUES.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> ITEM_POSITIONS_FUNCTION = ITEM_POSITIONS.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> ITEM_MOVES_FUNCTION = ITEM_MOVES.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> QUEEN_IN_THE_CENTER_FUNCTION = QUEEN_IN_THE_CENTER.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> COMPOSITE_FUNCTION = Functions.getDefaultEvaluationFunction(BOARD_TYPE);
	
	@Benchmark
	public void testEvaluationFunctionPownsStructureBit(Blackhole blackHole) {
		resetCache(BIT_BOARD);
		blackHole.consume(POWNS_STRUCTURE_FUNCTION.value(BIT_BOARD));
	}
	
	@Benchmark
	public void testEvaluationFunctionItemValuesBit(Blackhole blackHole) {
		resetCache(BIT_BOARD);
		blackHole.consume(ITEM_VALUES_FUNCTION.value(BIT_BOARD));
	}
	
	@Benchmark
	public void testEvaluationFunctionItemPositionsBit(Blackhole blackHole) {
		resetCache(BIT_BOARD);
		blackHole.consume(ITEM_POSITIONS_FUNCTION.value(BIT_BOARD));
	}
	
	@Benchmark
	public void testEvaluationFunctionItemMovesBit(Blackhole blackHole) {
		resetCache(BIT_BOARD);
		blackHole.consume(ITEM_MOVES_FUNCTION.value(BIT_BOARD));
	}
	
	@Benchmark
	public void testEvaluationFunctionQueenInTheCenterBit(Blackhole blackHole) {
		resetCache(BIT_BOARD);
		blackHole.consume(QUEEN_IN_THE_CENTER_FUNCTION.value(BIT_BOARD));
	}
	
	@Benchmark
	public void testEvaluationFunctionCompositeBit(Blackhole blackHole) {
		resetCache(BIT_BOARD);
		blackHole.consume(COMPOSITE_FUNCTION.value(BIT_BOARD));
	}
	
}
