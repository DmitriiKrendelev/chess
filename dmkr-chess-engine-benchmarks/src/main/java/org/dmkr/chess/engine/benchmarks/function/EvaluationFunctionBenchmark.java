package org.dmkr.chess.engine.benchmarks.function;

import static org.dmkr.chess.engine.board.AbstractBoard.resetCache;
import static org.dmkr.chess.engine.function.Functions.*;

import java.util.concurrent.TimeUnit;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.board.bit.BitBoardBuilder;
import org.dmkr.chess.engine.board.impl.BoardBuilder;
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
public class EvaluationFunctionBenchmark {
	
	private static final Class<BoardEngine> BOARD_TYPE = BoardEngine.class;
	
	private static final BoardEngine BOARD = BoardBuilder.of(
			"- - - - r - - -", 
			"- B Q - p R - -", 		
			"- - - r - - - -", 		
			"- p - P k p - -", 		
			"- - - - - - - B", 		
			"- - - - - P P -",
			"- N N - - - - K", 		
			"- - n n - - - -")
			.build();

	private static final EvaluationFunction<BoardEngine> ROOKS_FUNCTION = ROOKS.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BoardEngine> POWNS_STRUCTURE_FUNCTION = POWN_STRUCTURE.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BoardEngine> ITEM_VALUES_FUNCTION = ITEM_VALUES.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BoardEngine> ITEM_POSITIONS_FUNCTION = ITEM_POSITIONS.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BoardEngine> ITEM_MOVES_FUNCTION = ITEM_MOVES.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BoardEngine> QUEEN_IN_THE_CENTER_FUNCTION = QUEEN_IN_THE_CENTER.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BoardEngine> COMPOSITE_FUNCTION = Functions.getDefaultEvaluationFunction(BOARD_TYPE);

	@Benchmark
	public void boardEvaluationFunctionRooks(Blackhole blackHole) {
		resetCache(BOARD);
		blackHole.consume(ROOKS_FUNCTION.value(BOARD));
	}

	@Benchmark
	public void boardEvaluationFunctionPownsStructure(Blackhole blackHole) {
		resetCache(BOARD);
		blackHole.consume(POWNS_STRUCTURE_FUNCTION.value(BOARD));
	}

	@Benchmark
	public void boardEvaluationFunctionItemValues(Blackhole blackHole) {
		resetCache(BOARD);
		blackHole.consume(ITEM_VALUES_FUNCTION.value(BOARD));
	}
	
	@Benchmark
	public void boardEvaluationFunctionItemPositions(Blackhole blackHole) {
		resetCache(BOARD);
		blackHole.consume(ITEM_POSITIONS_FUNCTION.value(BOARD));
	}
	
	@Benchmark
	public void boardEvaluationFunctionItemMoves(Blackhole blackHole) {
		resetCache(BOARD);
		blackHole.consume(ITEM_MOVES_FUNCTION.value(BOARD));
	}
	
	@Benchmark
	public void boardEvaluationFunctionQueenInTheCenter(Blackhole blackHole) {
		resetCache(BOARD);
		blackHole.consume(QUEEN_IN_THE_CENTER_FUNCTION.value(BOARD));
	}
	
	@Benchmark
	public void boardEvaluationFunctionComposite(Blackhole blackHole) {
		resetCache(BOARD);
		blackHole.consume(COMPOSITE_FUNCTION.value(BOARD));
	}




	private static final Class<BitBoard> BIT_BOARD_TYPE = BitBoard.class;

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

	private static final EvaluationFunction<BitBoard> BIT_ROOKS_FUNCTION = ROOKS.getFunction(BIT_BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> BIT_POWNS_STRUCTURE_FUNCTION = POWN_STRUCTURE.getFunction(BIT_BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> BIT_ITEM_VALUES_FUNCTION = ITEM_VALUES.getFunction(BIT_BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> BIT_ITEM_POSITIONS_FUNCTION = ITEM_POSITIONS.getFunction(BIT_BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> BIT_ITEM_MOVES_FUNCTION = ITEM_MOVES.getFunction(BIT_BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> BIT_QUEEN_IN_THE_CENTER_FUNCTION = QUEEN_IN_THE_CENTER.getFunction(BIT_BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> BIT_COMPOSITE_FUNCTION = Functions.getDefaultEvaluationFunction(BIT_BOARD_TYPE);

	@Benchmark
	public void bitEvaluationFunctionRooks(Blackhole blackHole) {
		resetCache(BIT_BOARD);
		blackHole.consume(BIT_ROOKS_FUNCTION.value(BIT_BOARD));
	}

	@Benchmark
	public void bitEvaluationFunctionPownsStructure(Blackhole blackHole) {
		resetCache(BIT_BOARD);
		blackHole.consume(BIT_POWNS_STRUCTURE_FUNCTION.value(BIT_BOARD));
	}

	@Benchmark
	public void bitEvaluationFunctionItemValues(Blackhole blackHole) {
		resetCache(BIT_BOARD);
		blackHole.consume(BIT_ITEM_VALUES_FUNCTION.value(BIT_BOARD));
	}

	@Benchmark
	public void bitEvaluationFunctionItemPositions(Blackhole blackHole) {
		resetCache(BIT_BOARD);
		blackHole.consume(BIT_ITEM_POSITIONS_FUNCTION.value(BIT_BOARD));
	}

	@Benchmark
	public void bitEvaluationFunctionItemMoves(Blackhole blackHole) {
		resetCache(BIT_BOARD);
		blackHole.consume(BIT_ITEM_MOVES_FUNCTION.value(BIT_BOARD));
	}

	@Benchmark
	public void bitEvaluationFunctionQueenInTheCenter(Blackhole blackHole) {
		resetCache(BIT_BOARD);
		blackHole.consume(BIT_QUEEN_IN_THE_CENTER_FUNCTION.value(BIT_BOARD));
	}

	@Benchmark
	public void bitEvaluationFunctionComposite(Blackhole blackHole) {
		resetCache(BIT_BOARD);
		blackHole.consume(BIT_COMPOSITE_FUNCTION.value(BIT_BOARD));
	}
}
