package org.dmkr.chess.engine.function;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.function.bit.*;
import org.dmkr.chess.engine.function.impl.*;

import lombok.RequiredArgsConstructor;

import static java.util.Arrays.*;
import static org.dmkr.chess.engine.function.EvaluationFunctionUtils.composite;
import static org.dmkr.chess.engine.board.BoardFactory.getBoardType;

@RequiredArgsConstructor
public enum Functions {
	QUEEN_IN_THE_CENTER(
			EvaluationFunctionQueenInTheCenter.INSTANCE,
			EvaluationFunctionQueenInTheCenterBit.INSTANCE,
			true
	),
	PIECE_VALUES(
			EvaluationFunctionPiecesValues.INSTANCE,
			EvaluationFunctionPiecesValuesBit.INSTANCE,
			true
	),
	PIECE_POSITIONS(
			EvaluationFunctionPiecesPositions.INSTANCE,
			EvaluationFunctionPiecesPositionsBit.INSTANCE,
			true
	),
	@SuppressWarnings("unchecked")
	PIECE_MOVES(
			EvaluationFunctionMoves.INSTANCE_NOT_CHECK_KING_UNDER_ATACKS,
			EvaluationFunctionMovesBit.INSTANCE_NOT_CHECK_KING_UNDER_ATACKS,
			true
	),
	PAWN_STRUCTURE(
			EvaluationFunctionPawnsStructure.INSTANCE,
			EvaluationFunctionPawnsStructureBit.INSTANCE,
			true
	),
	ROOKS(
			EvaluationFunctionRooks.INSTANCE,
			EvaluationFunctionRooksBit.INSTANCE,
			true
	),
	AVAN_POSTES(
			EvaluationFunctionAvanPoste.INSTANCE,
			EvaluationFunctionAvanPosteBit.INSTANCE,
			true
	);

	private static final Functions[] enabledValues;

	static {
		enabledValues = stream(Functions.values()).filter(f -> f.enabled).toArray(Functions[]::new);
	}

	private final EvaluationFunction<BoardEngine> evaluationFunction;
	private final EvaluationFunction<BitBoard> evaluationFunctionBit;
	private final boolean enabled;

	Functions(EvaluationFunction<BoardEngine> evaluationFunction, EvaluationFunction<BitBoard> evaluationFunctionBit) {
		this(evaluationFunction, evaluationFunctionBit, true);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BoardEngine> EvaluationFunction<T> getFunction(Class<? extends BoardEngine> boardType) {
		return (EvaluationFunction<T>) (BitBoard.class.isAssignableFrom(boardType) ? evaluationFunctionBit : evaluationFunction);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends BoardEngine> EvaluationFunction<T> getDefaultEvaluationFunction(Class<? extends BoardEngine> boardType) {
		return composite(stream(Functions.enabledValues()).map(func -> func.getFunction(boardType)).toArray(EvaluationFunction[]::new));
	}
	
	public static <T extends BoardEngine> EvaluationFunction<T> getDefaultEvaluationFunction() {
		return getDefaultEvaluationFunction(getBoardType());
	}

	public static <T extends BoardEngine> EvaluationFunctionAware<T> getDefaultEvaluationFunctionAware() {
		return EvaluationFunctionAware.of(getDefaultEvaluationFunction());
	}

	public static Functions[] enabledValues() {
		return enabledValues;
	}
}
