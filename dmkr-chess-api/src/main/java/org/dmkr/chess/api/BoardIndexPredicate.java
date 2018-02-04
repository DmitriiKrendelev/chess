package org.dmkr.chess.api;

import java.util.function.IntPredicate;
import static org.dmkr.chess.api.utils.BoardUtils.*;

@FunctionalInterface
public interface BoardIndexPredicate {

	public boolean test(int i1, int i2);

	static IntPredicate fieldPredicate(BoardIndexPredicate boardIndexPredicate) {
		return i -> boardIndexPredicate.test(getX(i), getY(i)); 
	}
}
