package org.dmkr.chess.api.utils;

import static org.dmkr.chess.api.model.Constants.VALUE_EMPTY;
import static org.dmkr.chess.api.utils.BoardUtils.index;
import static org.dmkr.chess.api.utils.BoardUtils.invertIndex;
import static org.dmkr.chess.common.primitives.Bytes.byte1;
import static org.dmkr.chess.common.primitives.Bytes.byte2;
import static org.dmkr.chess.common.primitives.Bytes.byte3;
import static org.dmkr.chess.common.primitives.Bytes.toInt;

import java.util.List;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.common.collections.StreamUtils;

import com.google.common.collect.ImmutableList;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MoveUtils {
	public static final IntPredicate IS_CAPTURE_MOVE = move -> byte3(move) != VALUE_EMPTY;
	
	@RequiredArgsConstructor
	public static class LimitedMovesSelector implements IntPredicate {
		private final IntPredicate wrappedSelector;
		private final int maxNumberOfMoves;
		private int currentNumberOfMoves;
		
		@Override
		public boolean test(int move) {
			if (wrappedSelector.test(move)) {
				if (currentNumberOfMoves == maxNumberOfMoves) {
					return false;
				}
				
				currentNumberOfMoves ++;
				return true;
			}
		
			return true;
		}
		
		public void reset() {
			this.currentNumberOfMoves = 0;
		}
	}
	
	public static int valueOf(Move move, BoardEngine board) {
		final boolean isInverted = board.isInverted();
		final int from = invertIndex(index(move.fromX(), move.fromY()), isInverted);
		final int to = invertIndex(index(move.toX(), move.toY()), isInverted);
		
		return IntStream.of(board.allowedMoves())
			.filter(intMove -> from == byte1(intMove) && to == byte2(intMove))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Not allowed move: " + move + "\n" + board));
	}
	
	public static int valueOf(Move move) {
		final int from = index(move.fromX(), move.fromY());
		final int to = index(move.toX(), move.toY());
	
		return toInt(from, to);
	}
	
	public static boolean isCapturedMove(int move) {
		return IS_CAPTURE_MOVE.test(move);
	}
	
	public static int[] getCaptureMoves(BoardEngine board) {
		return board.allowedMoves(IS_CAPTURE_MOVE);
	}
	
	public static Set<Move> toSet(int[] moves, boolean isInverted) {
		return IntStream.of(moves).mapToObj(move -> Move.moveOf(move, isInverted)).collect(StreamUtils.toImmutableSet());
	}
	
	public static List<Move> toMovesHistory(int[] moves, boolean isInverted) {
		if ((moves.length & 1) == 1) {
			isInverted = !isInverted;
		}
		
		final ImmutableList.Builder<Move> movesBuilder = ImmutableList.builder();
		for (int move : moves) {
			final Move m = Move.moveOf(move, isInverted);
			isInverted = !isInverted;
			movesBuilder.add(m);
		}
		
		return movesBuilder.build();
	}
}
