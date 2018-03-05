package org.dmkr.chess.engine.function;

import static org.dmkr.chess.api.utils.BoardUtils.applayMoves;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.common.EvaluationFunctionBasedBoardInversion;
import org.dmkr.chess.engine.minimax.BestLine;

import com.google.common.collect.ImmutableMap;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EvaluationFunctionUtils {
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	private static class CompositeEvaluationFunction<T extends BoardEngine> implements EvaluationFunction<T> {
		final EvaluationFunction<T>[] functions;
		
		@Override
		public int value(T board) {
			int result = 0;
			for (EvaluationFunction<T> function : functions) {
				result += function.value(board);
			}
			return result;
		}
		
		@Override
		public EvaluationFunction<T> and(EvaluationFunction<T> other) {
			return new CompositeEvaluationFunction<>(ArrayUtils.add(functions, other));
		}

		@Override
		public Map<String, Integer> getEvaluationDetails(T board) {
			final Map<String, Integer> result = new HashMap<>();
			for (EvaluationFunction<T> func : functions) {
				result.putAll(func.getEvaluationDetails(board));
			}
			return result;
		}
	}

	public static <T extends BoardEngine> EvaluationFunction<T> emptyFunction() {
		return new EvaluationFunction<T>() {
			@Override
			public int value(T board) {
				return 0;
			}

			@Override
			public Map<String, Integer> getEvaluationDetails(T board) {
				return Collections.emptyMap();
			}
		};
	}
	
	public static <T extends BoardEngine> EvaluationFunction<T> composite(EvaluationFunction<T> function) {
		return function;
	}
	
	@SafeVarargs
	public static <T extends BoardEngine> EvaluationFunction<T> composite(EvaluationFunction<T> ... functions) {
		final List<EvaluationFunctionBasedBoardInversion<T>> intvertFunctions = new ArrayList<>();
		final List<EvaluationFunction> others = new ArrayList<>();

		for (EvaluationFunction<T> func : functions) {
			if (func instanceof EvaluationFunctionBasedBoardInversion) {
				intvertFunctions.add((EvaluationFunctionBasedBoardInversion<T>) func);
			} else {
				others.add(func);
			}
		}

		final EvaluationFunction<T> compositeIntvertFunction = compositeBoardInversionFunction(intvertFunctions);
		final EvaluationFunction<T> compositeOthers;
		if (others.isEmpty()) {
			compositeOthers = emptyFunction();
		} else if (others.size() == 1) {
			compositeOthers = others.get(0);
		} else {
			compositeOthers = new CompositeEvaluationFunction<>(others.stream().toArray(EvaluationFunction[]::new));
		}

		return compositeOthers.and(compositeIntvertFunction);
	}

	public static <T extends BoardEngine> EvaluationFunction<T> compositeBoardInversionFunction(List<EvaluationFunctionBasedBoardInversion<T>> funcs) {
		if (funcs == null || funcs.isEmpty()) {
			return EvaluationFunctionUtils.emptyFunction();
		} else if (funcs.size() == 1) {
			return funcs.get(0);
		} else {
			final List<EvaluationFunctionBasedBoardInversion<T>> funcsCopy = new ArrayList<>(funcs);
			return new EvaluationFunctionBasedBoardInversion<T>() {
				@Override
				public int calculateOneSidedValue(T board) {
					int result = 0;
					for (EvaluationFunctionBasedBoardInversion<T> func : funcsCopy) {
						result += func.calculateOneSidedValue(board);
					}
					return result;
				}

				@Override
				public Map<String, Integer> getEvaluationDetails(T board) {
					final Map<String, Integer> result = new HashMap<>();
					for (EvaluationFunction<T> func : funcsCopy) {
						result.putAll(func.getEvaluationDetails(board));
					}
					return result;
				}
			};
		}
	}
	
	public static Map<String, Integer> getEvaluationDetails(EvaluationFunction<BoardEngine> function, BoardEngine board) {
		final Map<String, Integer> functionDetails = function.getEvaluationDetails(board);

	    final Map<String, Integer> result = new LinkedHashMap<>(functionDetails);
		result.put("Total", functionDetails.values().stream().reduce(0, Integer::sum));
	    return result;
	}

	public static Map<String, Integer> getEvaluationDetails(EvaluationFunction<BoardEngine> function, BoardEngine board, BestLine bestLine) {
		final BoardEngine boardCopy =  board.clone();
		applayMoves(boardCopy, bestLine.getMoves());
		return getEvaluationDetails(function, boardCopy);
	}
}
