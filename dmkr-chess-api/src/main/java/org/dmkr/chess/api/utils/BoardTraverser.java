package org.dmkr.chess.api.utils;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.utils.BoardUtils.getX;
import static org.dmkr.chess.api.utils.BoardUtils.getY;

@UtilityClass
public class BoardTraverser {
	private static final int[] NATURAL_ORDER_ARRAY =
			Stream.iterate(0, i -> i + 1)
				.limit(SIZE * SIZE)	
				.mapToInt(Integer::intValue)
				.toArray();
	
	private static final double X0 = (0d + SIZE) / 2d;
	private static final double Y0 = SIZE;
	private static final int[] UP_CENTER_DESC_ORDER_ARRAY =
			Stream.iterate(0, i -> i + 1)
				.limit(SIZE * SIZE)	
				.sorted((i1, i2) -> Double.compare(distance(i1, X0, Y0), distance(i2, X0, Y0)))
				.mapToInt(Integer::intValue)
				.toArray();
		
	public static interface BoardCoordsConsumer {
		public void accept(int x, int y);
	}

	public static void traverse(int[] indexes, BoardCoordsConsumer boardCoordsConsumer) {
		IntStream.of(indexes).forEach(index -> boardCoordsConsumer.accept(getX(index), getY(index)));
	}
	
	public static void traverseNaturalOrder(BoardCoordsConsumer boardCoordsConsumer) {
		traverse(NATURAL_ORDER_ARRAY, boardCoordsConsumer);
	}
	
	public static void traverseUpCenterDescOrder(BoardCoordsConsumer boardCoordsConsumer) {
		traverse(UP_CENTER_DESC_ORDER_ARRAY, boardCoordsConsumer);
	}
	
	private static double distance(int index, double x0, double y0) {
		final int x = getX(index);
		final int y = getY(index);
		
		return Math.sqrt((x - x0) * (x - x0) + (y - y0) * (y - y0));
	}
}
