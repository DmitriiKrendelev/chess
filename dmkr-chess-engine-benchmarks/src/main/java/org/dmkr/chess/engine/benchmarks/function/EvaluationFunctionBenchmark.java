package org.dmkr.chess.engine.benchmarks.function;

import static org.dmkr.chess.engine.function.Functions.*;

import java.util.concurrent.TimeUnit;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.bit.EvaluationFunctionAllBit;
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
public class EvaluationFunctionBenchmark {
	
	private static final Class<BoardEngine> BOARD_TYPE = BoardEngine.class;
	private static final EvaluationFunction<BoardEngine> ROOKS_FUNCTION = ROOKS.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BoardEngine> PAWNS_STRUCTURE_FUNCTION = PAWN_STRUCTURE.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BoardEngine> PIECE_VALUES_FUNCTION = PIECE_VALUES.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BoardEngine> PIECE_POSITIONS_FUNCTION = PIECE_POSITIONS.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BoardEngine> PIECE_MOVES_FUNCTION = PIECE_MOVES.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BoardEngine> QUEEN_IN_THE_CENTER_FUNCTION = QUEEN_IN_THE_CENTER.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BoardEngine> AVAN_POSTE_FUNCTION = AVAN_POSTES.getFunction(BOARD_TYPE);
	private static final EvaluationFunction<BoardEngine> COMPOSITE_FUNCTION = getDefaultEvaluationFunction(BOARD_TYPE);


	@Benchmark
	public void boardEvaluationRooks(Blackhole blackHole) {
		blackHole.consume(ROOKS_FUNCTION.value(getNextBoardPosition()));
	}

	@Benchmark
	public void boardEvaluationPawnsStructure(Blackhole blackHole) {
		blackHole.consume(PAWNS_STRUCTURE_FUNCTION.value(getNextBoardPosition()));
	}

	@Benchmark
	public void boardEvaluationPieceValues(Blackhole blackHole) {
		blackHole.consume(PIECE_VALUES_FUNCTION.value(getNextBoardPosition()));
	}
	
	@Benchmark
	public void boardEvaluationPiecePositions(Blackhole blackHole) {
		blackHole.consume(PIECE_POSITIONS_FUNCTION.value(getNextBoardPosition()));
	}
	
	@Benchmark
	public void boardEvaluationPieceMoves(Blackhole blackHole) {
		blackHole.consume(PIECE_MOVES_FUNCTION.value(getNextBoardPosition()));
	}

	@Benchmark
	public void boardEvaluationAvanPostes(Blackhole blackHole) {
		blackHole.consume(AVAN_POSTE_FUNCTION.value(getNextBoardPosition()));
	}

	@Benchmark
	public void boardEvaluationQueenInTheCenter(Blackhole blackHole) {
		blackHole.consume(QUEEN_IN_THE_CENTER_FUNCTION.value(getNextBoardPosition()));
	}
	
	@Benchmark
	public void boardEvaluationComposite(Blackhole blackHole) {
		blackHole.consume(COMPOSITE_FUNCTION.value(getNextBoardPosition()));
	}




	private static final Class<BitBoard> BIT_BOARD_TYPE = BitBoard.class;
	private static final EvaluationFunction<BitBoard> BIT_ROOKS_FUNCTION = ROOKS.getFunction(BIT_BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> BIT_PAWNS_STRUCTURE_FUNCTION = PAWN_STRUCTURE.getFunction(BIT_BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> BIT_PIECE_VALUES_FUNCTION = PIECE_VALUES.getFunction(BIT_BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> BIT_PIECE_POSITIONS_FUNCTION = PIECE_POSITIONS.getFunction(BIT_BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> BIT_PIECE_MOVES_FUNCTION = PIECE_MOVES.getFunction(BIT_BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> BIT_QUEEN_IN_THE_CENTER_FUNCTION = QUEEN_IN_THE_CENTER.getFunction(BIT_BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> BIT_AVAN_POSTE_FUNCTION = AVAN_POSTES.getFunction(BIT_BOARD_TYPE);
	private static final EvaluationFunction<BitBoard> BIT_COMPOSITE_FUNCTION = getDefaultEvaluationFunction(BIT_BOARD_TYPE);


	@Benchmark
	public void bitEvaluationRooks(Blackhole blackHole) {
		blackHole.consume(BIT_ROOKS_FUNCTION.value(getNextBitBoardPosition()));
	}

	@Benchmark
	public void bitEvaluationPawnsStructure(Blackhole blackHole) {
		blackHole.consume(BIT_PAWNS_STRUCTURE_FUNCTION.value(getNextBitBoardPosition()));
	}

	@Benchmark
	public void bitEvaluationPieceValues(Blackhole blackHole) {
		blackHole.consume(BIT_PIECE_VALUES_FUNCTION.value(getNextBitBoardPosition()));
	}

	@Benchmark
	public void bitEvaluationPiecePositions(Blackhole blackHole) {
		blackHole.consume(BIT_PIECE_POSITIONS_FUNCTION.value(getNextBitBoardPosition()));
	}

	@Benchmark
	public void bitEvaluationPieceMoves(Blackhole blackHole) {
		blackHole.consume(BIT_PIECE_MOVES_FUNCTION.value(getNextBitBoardPosition()));
	}

	@Benchmark
	public void bitEvaluationQueenInTheCenter(Blackhole blackHole) {
		blackHole.consume(BIT_QUEEN_IN_THE_CENTER_FUNCTION.value(getNextBitBoardPosition()));
	}

	@Benchmark
	public void bitEvaluationAvanPostes(Blackhole blackHole) {
		blackHole.consume(BIT_AVAN_POSTE_FUNCTION.value(getNextBitBoardPosition()));
	}

	@Benchmark
	public void bitEvaluationComposite(Blackhole blackHole) {
		blackHole.consume(BIT_COMPOSITE_FUNCTION.value(getNextBitBoardPosition()));
	}

	@Benchmark
	public void allBitEvaluation(Blackhole blackHole) {
		blackHole.consume(EvaluationFunctionAllBit.INSTANCE.value(getNextBitBoardPosition()));
	}

}
