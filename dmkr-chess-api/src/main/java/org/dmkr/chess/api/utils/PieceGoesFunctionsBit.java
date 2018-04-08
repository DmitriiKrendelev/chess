package org.dmkr.chess.api.utils;


import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.utils.BitBoardMasks.NOT_A;
import static org.dmkr.chess.api.utils.BitBoardMasks.NOT_H;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;


public class PieceGoesFunctionsBit {
	private static final LongUnaryOperator GO_UP_FUNCTION = l -> l << SIZE;
	private static final LongUnaryOperator GO_DOWN_FUNCTION = l -> l >>> SIZE;
	private static final LongUnaryOperator GO_LEFT_FUNCTION = l -> l << 1;
	private static final LongUnaryOperator GO_RIGHT_FUNCTION = l -> l >>> 1;
	
	private static final LongUnaryOperator GO_UP_LEFT_FUNCTION = l -> l << (SIZE + 1);
	private static final LongUnaryOperator GO_UP_RIGHT_FUNCTION = l -> l << (SIZE - 1);
	private static final LongUnaryOperator GO_DOWN_LEFT_FUNCTION = l -> l >>> (SIZE - 1);
	private static final LongUnaryOperator GO_DOWN_RIGHT_FUNCTION = l -> l >>> (SIZE + 1);

	private static final LongPredicate UNTIL_UP_BREAK = l -> l == 0;
	private static final LongPredicate UNTIL_DOWN_BREAK = l -> l == 0;
	private static final LongPredicate UNTIL_RIGHT_BREAK = l -> (l & NOT_A) == 0;
	private static final LongPredicate UNTIL_LEFT_BREAK = l -> (l & NOT_H) == 0;
	
	private static final LongPredicate UNTIL_UP_RIGHT_BREAK = l -> (l & NOT_A) == 0;
	private static final LongPredicate UNTIL_UP_LEFT_BREAK = l -> (l & NOT_H) == 0;
	private static final LongPredicate UNTIL_DOWN_RIGHT_BREAK = l -> (l & NOT_A) == 0;
	private static final LongPredicate UNTIL_DOWN_LEFT_BREAK = l -> (l & NOT_H) == 0;

	public enum PieceGoesFunctionBit {
		GO_UP(GO_UP_FUNCTION, UNTIL_UP_BREAK),
		GO_DOWN(GO_DOWN_FUNCTION, UNTIL_DOWN_BREAK),
		GO_LEFT(GO_LEFT_FUNCTION, UNTIL_LEFT_BREAK),
		GO_RIGHT(GO_RIGHT_FUNCTION, UNTIL_RIGHT_BREAK),
		GO_UP_LEFT(GO_UP_LEFT_FUNCTION, UNTIL_UP_LEFT_BREAK),
		GO_UP_RIGHT(GO_UP_RIGHT_FUNCTION, UNTIL_UP_RIGHT_BREAK),
		GO_DOWN_LEFT(GO_DOWN_LEFT_FUNCTION, UNTIL_DOWN_LEFT_BREAK),
		GO_DOWN_RIGHT(GO_DOWN_RIGHT_FUNCTION, UNTIL_DOWN_RIGHT_BREAK);

		PieceGoesFunctionBit(LongUnaryOperator goFunction, LongPredicate stopPredicate) {
			this.goFunction = goFunction;
			this.stopPredicate = stopPredicate;
		}

		private final LongUnaryOperator goFunction;
		private final LongPredicate stopPredicate;

		public LongUnaryOperator goFunction() {
			return goFunction;
		}

		public LongPredicate stopPredicate() {
			return stopPredicate;
		}
	}
}
