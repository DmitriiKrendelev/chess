package org.dmkr.chess.engine.function;

import static org.dmkr.chess.api.utils.BoardUtils.applayMoves;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.dmkr.chess.api.BoardEngine;
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
			return new CompositeEvaluationFunction<T>((EvaluationFunction<T>[]) ArrayUtils.add(functions, other));
		}
		
	}
	
	public static <T extends BoardEngine> EvaluationFunction<T> composite(EvaluationFunction<T> function) {
		return function;
	}
	
	@SafeVarargs
	public static <T extends BoardEngine> EvaluationFunction<T> composite(EvaluationFunction<T> ... functions) {
		return new CompositeEvaluationFunction<T>(functions);
	}
	
	public static Map<String, Integer> getEvaluationDetails(EvaluationFunction<BoardEngine> function, BoardEngine board) {
		if (function instanceof CompositeEvaluationFunction) {
			int total = 0;
			final ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();
			for (EvaluationFunction<BoardEngine> detailFunction : ((CompositeEvaluationFunction<BoardEngine>) function).functions) {
				final String name = detailFunction.toString();
				final int value = detailFunction.value(board);
				if (name != null) { 
					builder.put(detailFunction.toString(), value);
				}
				total += value;
			}
			builder.put("Total", total);
			return builder.build();
		} else {
			return ImmutableMap.of(function.toString(), function.value(board));
		}
	}
	
	public static Map<String, Integer> getEvaluationDetails(EvaluationFunction<BoardEngine> function, BoardEngine board, BestLine bestLine) {
		final BoardEngine boardCopy =  board.clone();
		applayMoves(boardCopy, bestLine.getMoves());
		return getEvaluationDetails(function, boardCopy);
	}
}
