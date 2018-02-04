package org.dmkr.chess.api.model;

import static com.google.common.base.Preconditions.checkArgument;
import static org.dmkr.chess.api.model.Field.resolve;
import static org.dmkr.chess.api.utils.BoardUtils.getX;
import static org.dmkr.chess.api.utils.BoardUtils.getY;
import static org.dmkr.chess.api.utils.BoardUtils.invertCoord;
import static org.dmkr.chess.api.utils.BoardUtils.isOnBoard;
import static org.dmkr.chess.common.primitives.Bytes.byte1;
import static org.dmkr.chess.common.primitives.Bytes.byte2;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Move {
	private final int fromX, fromY, toX, toY;
	
	public Move(Field from, Field to) {
		this(from.x(), from.y(), to.x(), to.y());
	}
	
	public static Move moveOf(Field from, Field to) {
		return new Move(from, to);
	}
	
	public static Move moveOf(String move) {
		try {
			final String[] fields = move.split("-");
			return moveOf(Field.valueOf(fields[0]), Field.valueOf(fields[1]));
		} catch (Exception e) {
			throw new IllegalArgumentException("Move: " + move);
		}
	}
	
	public static Move moveOf(int move, boolean inverted) {
		final int from = byte1(move);
		final int to = byte2(move);

		final int fromX = getX(from);
		final int fromY = getY(from);
		final int toX = getX(to);
		final int toY = getY(to);

		checkArgument(isOnBoard(fromX, fromY));
		checkArgument(isOnBoard(toX, toY));
		
		return new Move(
				invertCoord(fromX, inverted), 
				invertCoord(fromY, inverted), 
				invertCoord(toX, inverted), 
				invertCoord(toY, inverted));
	}
	
	public Field from() {
		return resolve(fromX, fromY);
	}
	
	public Field to() {
		return resolve(toX, toY);
	}
	
	@Override
	public String toString() {
		return from() + "-" + to();
	}
}
