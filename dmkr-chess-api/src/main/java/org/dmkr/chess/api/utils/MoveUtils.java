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
import java.util.stream.Stream;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.common.collections.StreamUtils;

import com.google.common.collect.ImmutableList;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.dmkr.chess.common.primitives.IntArrayBuilder;

@UtilityClass
public class MoveUtils {
	public static final IntPredicate IS_CAPTURE_MOVE = move -> byte3(move) != VALUE_EMPTY;
	
	@RequiredArgsConstructor
	public static class CaptureMovesFilter implements MovesFilter {
		private final IntArrayBuilder capturedMovesBuilder = new IntArrayBuilder();
		private final IntArrayBuilder notCapturedMovesBuilder = new IntArrayBuilder();
		private final int maxNumberOfNotCaptured;
		private final int maxNumberOfCaptured;

		@Override
		public void addMove(int move) {
			final byte captured = byte3(move);

			if (captured == VALUE_EMPTY) {
				if (notCapturedMovesBuilder.size() < maxNumberOfNotCaptured) {
					notCapturedMovesBuilder.add(move);
				}
			} else {
				if (capturedMovesBuilder.size() < maxNumberOfCaptured) {
					capturedMovesBuilder.add(move);
				} else {
					capturedMovesBuilder.substituteLowest(previousMove -> byte3(previousMove), move);
				}
			}
		}

		@Override
		public int[] build() {
			return IntArrayBuilder.build(capturedMovesBuilder, notCapturedMovesBuilder);
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
	
	public static Set<Move> toSet(int[] moves, boolean isInverted) {
		return IntStream.of(moves).mapToObj(move -> Move.moveOf(move, isInverted)).collect(StreamUtils.toImmutableSet());
	}

	public static Set<Move> toSet(String ... moves) {
		return Stream.of(moves).map(Move::moveOf).collect(StreamUtils.toImmutableSet());
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
