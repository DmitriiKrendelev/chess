package org.dmkr.chess.api.utils;

import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import static org.dmkr.chess.api.model.Constants.SIZE;


@UtilityClass
public class ItemGoesFunctions {
	private static final IntUnaryOperator GO_UP_FUNCTION = i -> i + SIZE;
	private static final IntUnaryOperator GO_DOWN_FUNCTION = i -> i - SIZE;
	private static final IntUnaryOperator GO_LEFT_FUNCTION = i -> i - 1;
	private static final IntUnaryOperator GO_RIGHT_FUNCTION = i -> i + 1;
	
	private static final IntUnaryOperator GO_UP_LEFT_FUNCTION = i -> i + SIZE - 1;
	private static final IntUnaryOperator GO_UP_RIGHT_FUNCTION = i -> i + SIZE + 1;
	private static final IntUnaryOperator GO_DOWN_LEFT_FUNCTION = i -> i - SIZE - 1;
	private static final IntUnaryOperator GO_DOWN_RIGHT_FUNCTION = i -> i - SIZE + 1;

	private static final IntPredicate UNTIL_UP_BREAK = i -> i > SIZE * SIZE - 1;
	private static final IntPredicate UNTIL_DOWN_BREAK = i -> i < 0;
	private static final IntPredicate UNTIL_RIGHT_BREAK = i -> (i & 7) == 0;
	private static final IntPredicate UNTIL_LEFT_BREAK = i -> (i & 7) == 7;
	
	private static final IntPredicate UNTIL_UP_RIGHT_BREAK = UNTIL_UP_BREAK.or(UNTIL_RIGHT_BREAK);
	private static final IntPredicate UNTIL_UP_LEFT_BREAK = UNTIL_UP_BREAK.or(UNTIL_LEFT_BREAK);
	private static final IntPredicate UNTIL_DOWN_RIGHT_BREAK = UNTIL_DOWN_BREAK.or(UNTIL_RIGHT_BREAK);
	private static final IntPredicate UNTIL_DOWN_LEFT_BREAK = UNTIL_DOWN_BREAK.or(UNTIL_LEFT_BREAK);
	
	@Getter
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static enum ItemGoesFunction {
		GO_UP(GO_UP_FUNCTION, UNTIL_UP_BREAK),
		GO_DOWN(GO_DOWN_FUNCTION, UNTIL_DOWN_BREAK),
		GO_LEFT(GO_LEFT_FUNCTION, UNTIL_LEFT_BREAK),
		GO_RIGHT(GO_RIGHT_FUNCTION, UNTIL_RIGHT_BREAK),
		GO_UP_LEFT(GO_UP_LEFT_FUNCTION, UNTIL_UP_LEFT_BREAK),
		GO_UP_RIGHT(GO_UP_RIGHT_FUNCTION, UNTIL_UP_RIGHT_BREAK),
		GO_DOWN_LEFT(GO_DOWN_LEFT_FUNCTION, UNTIL_DOWN_LEFT_BREAK),
		GO_DOWN_RIGHT(GO_DOWN_RIGHT_FUNCTION, UNTIL_DOWN_RIGHT_BREAK);
		
		private final IntUnaryOperator goFunction;
		private final IntPredicate stopPredicate;
	}
}
