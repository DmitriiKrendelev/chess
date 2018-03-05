package org.dmkr.chess.engine.function;

import com.google.common.collect.ImmutableSet;
import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.function.bit.*;
import org.dmkr.chess.engine.function.common.EvaluationFunctionMoves;
import org.dmkr.chess.engine.function.impl.*;

import lombok.RequiredArgsConstructor;
import static org.dmkr.chess.engine.function.EvaluationFunctionUtils.composite;
import static org.dmkr.chess.engine.board.BoardFactory.getBoardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public enum Functions {
	QUEEN_IN_THE_CENTER(
			EvaluationFunctionQueenInTheCenter.INSTANCE,
			EvaluationFunctionQueenInTheCenterBit.INSTANCE),
	ITEM_VALUES(
			EvaluationFunctionItemsValues.INSTANCE,
			EvaluationFunctionItemsValuesBit.INSTANCE),
	ITEM_POSITIONS(
			EvaluationFunctionItemsPositions.INSTANCE,
			EvaluationFunctionItemsPositionsBit.INSTANCE),
	@SuppressWarnings("unchecked")
	ITEM_MOVES(
			(EvaluationFunction<BoardEngine>) EvaluationFunctionMoves.INSTANCE,
			(EvaluationFunction<BitBoard>) EvaluationFunctionMoves.INSTANCE),
	POWN_STRUCTURE(
			EvaluationFunctionPownsStructure.INSTANCE,
			EvaluationFunctionPownsStructureBit.INSTANCE),
	ROOKS(
			EvaluationFunctionRooks.INSTANCE,
			EvaluationFunctionRooksBit.INSTANCE)
	;

	private static final Set<Functions> LIGHT_FUNCTIONS = ImmutableSet.of(ITEM_VALUES, ITEM_POSITIONS);
	
	
	private final EvaluationFunction<BoardEngine> evaluationFunction;
	private final EvaluationFunction<BitBoard> evaluationFunctionBit;
	
	@SuppressWarnings("unchecked")
	public <T extends BoardEngine> EvaluationFunction<T> getFunction(Class<? extends BoardEngine> boardType) {
		return (EvaluationFunction<T>) (BitBoard.class.isAssignableFrom(boardType) ? evaluationFunctionBit : evaluationFunction);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends BoardEngine> EvaluationFunction<T> getDefaultEvaluationFunction(Class<? extends BoardEngine> boardType) {
		final List<EvaluationFunction<T>> functions = new ArrayList<>();
		for (Functions funcs : Functions.values()) {
			functions.add(funcs.getFunction(boardType));
		}
		return composite(functions.toArray(new EvaluationFunction[functions.size()]));
	}
	
	public static <T extends BoardEngine> EvaluationFunction<T> getDefaultEvaluationFunction() {
		return getDefaultEvaluationFunction(getBoardType());
	}

	public static <T extends BoardEngine> EvaluationFunction<T> getLightEvaluationFunction() {
		final Class<? extends BoardEngine> boardType = getBoardType();
		return composite(LIGHT_FUNCTIONS.stream().map(func -> func.getFunction(boardType)).toArray(EvaluationFunction[]::new));
	}
	
	public static <T extends BoardEngine> EvaluationFunctionAware<T> getDefaultEvaluationFunctionAware() {
		return EvaluationFunctionAware.of(getDefaultEvaluationFunction());
	}

	public static <T extends BoardEngine> EvaluationFunctionAware<T> getLightEvaluationFunctionAware() {
		return EvaluationFunctionAware.of(getLightEvaluationFunction());
	}
}
