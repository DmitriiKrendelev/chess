package org.dmkr.chess.engine.function;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.function.bit.EvaluationFunctionItemsPositionsBit;
import org.dmkr.chess.engine.function.bit.EvaluationFunctionItemsValuesBit;
import org.dmkr.chess.engine.function.bit.EvaluationFunctionPownsStructureBit;
import org.dmkr.chess.engine.function.bit.EvaluationFunctionQueenInTheCenterBit;
import org.dmkr.chess.engine.function.common.EvaluationFunctionMoves;
import org.dmkr.chess.engine.function.impl.EvaluationFunctionItemsPositions;
import org.dmkr.chess.engine.function.impl.EvaluationFunctionItemsValues;
import org.dmkr.chess.engine.function.impl.EvaluationFunctionPownsStructure;
import org.dmkr.chess.engine.function.impl.EvaluationFunctionQueenInTheCenter;

import lombok.RequiredArgsConstructor;
import static org.dmkr.chess.engine.function.EvaluationFunctionUtils.composite;
import static org.dmkr.chess.engine.board.BoardFactory.getBoardType;

import java.util.ArrayList;
import java.util.List;

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
			EvaluationFunctionPownsStructureBit.INSTANCE);
	
	
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
	
	public static <T extends BoardEngine> EvaluationFunctionAware<T> getDefaultEvaluationFunctionAware() {
		return EvaluationFunctionAware.of(getDefaultEvaluationFunction());
	}
}
