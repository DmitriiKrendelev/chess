package org.dmkr.chess.engine.minimax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.*;
import org.dmkr.chess.api.model.Move;

import com.google.common.collect.ImmutableList;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = "duration")
public class BestLine {
	private static final long CACHED_BEST_LINE_DURATION = -1L;
	private final List<Move> moves;
	private final int lineValue;
	private final int lineValueChange;
	private final long duration;
	
	static BestLine newBestLine(int[] movesArray, boolean inverted, int lineValue, int lineValueChange, long duration) {
		final List<Move> moves = new ArrayList<>();
		
		for (int move : movesArray) {
			if (move == 0) {
				break;
			}
			
			moves.add(Move.moveOf(move, inverted));
			inverted = !inverted;
		}

		return new BestLine(moves, lineValue, lineValueChange, duration);
	}

	public static BestLine of(Move move) {
		return new BestLine(ImmutableList.of(move), 0, 0, 0);
	}

	public boolean isCached() {
		return duration == CACHED_BEST_LINE_DURATION;
	}

	public BestLine cloneSubstitutingFirst(Move newFirstMove, int initialPositionValue) {
		final List<Move> moves = new ArrayList<>(this.moves);
		moves.set(0, newFirstMove);
		return new BestLine(ImmutableList.copyOf(moves), lineValue, lineValue - initialPositionValue, CACHED_BEST_LINE_DURATION);
	}
	
	@Override
	public String toString() {
		final Iterator<Move> movesIterator = moves.iterator();
		final StringBuilder sb = new StringBuilder();
		sb.append("Best Line:\n");
		
		int index = 0;
		
		while (movesIterator.hasNext()) {
			sb.append((++ index) + ". " + movesIterator.next() + " : ");
			sb.append(movesIterator.hasNext() ? movesIterator.next() : "...").append('\n');
		}
		sb.append("Line Value: " + lineValue + "\n");
		
		return sb.toString();
	}
}
