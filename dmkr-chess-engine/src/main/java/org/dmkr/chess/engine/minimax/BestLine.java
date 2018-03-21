package org.dmkr.chess.engine.minimax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.EqualsAndHashCode;
import org.dmkr.chess.api.model.Move;

import com.google.common.collect.ImmutableList;

import lombok.Value;

@Value
@EqualsAndHashCode(exclude = "duration")
public class BestLine {
	List<Move> moves;
	int lineValue;
	int lineValueChange;
	long duration;
	
	BestLine(int[] movesArray, boolean inverted, int lineValue, int lineValueChange, long duration) {
		final List<Move> moves = new ArrayList<>();
		
		for (int move : movesArray) {
			if (move == 0) {
				break;
			}
			
			moves.add(Move.moveOf(move, inverted));
			inverted = !inverted;
		}
		
		this.moves = ImmutableList.copyOf(moves);
		this.lineValue = lineValue;
		this.lineValueChange = lineValueChange;
		this.duration = duration;
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
