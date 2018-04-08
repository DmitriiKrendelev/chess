package org.dmkr.chess.api.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static org.dmkr.chess.api.model.Constants.SIZE;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Field;
import org.dmkr.chess.api.model.Piece;
import org.dmkr.chess.api.model.Move;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BoardUtils {

	public static boolean isOnBoard(int index) {
		return index >= 0 && index < SIZE * SIZE;
	}
	
	public static boolean isOnBoard(int x, int y) {
		return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
	}
	
	public static int getX(int index) {
		return index & 7;
	}
	
	public static int getY(int index) {
		return index >> 3;
	}
	
	public static byte index(int x, int y) {
		return (byte) (x + (y << 3));
	}
	
	public static int invertIndex(int index) {
		return SIZE * SIZE - 1 - index;
	}
	
	public static int invertIndex(int index, boolean isInverted) {
		return isInverted ? invertIndex(index) : index;
	}
	
	public static int invertCoord(int coord, boolean inverted) {
		return inverted ? (SIZE - 1 - coord) : coord;
	}
	
	public static int[] toBoardArray(String ... lines) {
		checkArgument(lines != null && lines.length == SIZE);
		final int[] result = new int[SIZE * SIZE];
		
		for (int y = 0; y < SIZE; y ++ ) {
			final String[] values = lines[y].split(",");

			for (int x = 0; x < SIZE; x ++ ) {
				result[index(x, SIZE - y - 1)] = Integer.valueOf(values[x].trim());
			}
		}
		
		return result;
	}
	
	public static String movesToString(BoardEngine board) {
		final boolean inverted = board.isInverted();
		final Set<Move> allowedMoves = board.getAllowedMoves(); 
		final StringBuilder sb = new StringBuilder(256);	
		
		allowedMoves.forEach(move -> {
			final byte pieceValue = board.at(invertCoord(move.fromX(), inverted), invertCoord(move.fromY(), inverted));
			final Piece piece = Piece.withValue(pieceValue);
			
			sb.append('\n').append(piece).append(": ").append(move);
		});
		
		return sb.toString();
	}
	
	public static String toString(BoardEngine board) {
		final boolean inverted = board.isInverted();
		final StringBuilder sb = new StringBuilder(256);
		sb.append("\n").append(board.getClass().getSimpleName()).append("\n");
		
		for (int y = SIZE - 1; y >= 0; y --) {
			sb.append(Field.xName(invertCoord(y, inverted))).append(' ');
			
			for (int x = 0; x < SIZE; x ++) 
				sb.append(Piece.shortName(board.at(x, y))).append(' ');

			sb.append("\n");
		}
		
		sb.append("  ");
		for (int x = 0; x < SIZE; x ++) 
			sb.append(Field.yName(invertCoord(x, inverted))).append(' ');
		
		return sb.toString();		
	}
	
	public static void applayMoves(BoardEngine board, Iterable<Move> moves) {
		moves.forEach(board::applyMove);
	}
	
	public static void applayMoves(BoardEngine board, Move ... moves) {
		applayMoves(board, Arrays.asList(moves));
	}
	
	public static void applayMoves(BoardEngine board, String ... moves) {
		applayMoves(board, Arrays.asList(moves));
	}

	public static void applayMoves(BoardEngine board, List<String> moves) {
		for (String move : moves) {
			final Move m = Move.moveOf(move);
			board.applyMove(m);
		}
	}
}
