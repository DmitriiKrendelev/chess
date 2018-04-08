package org.dmkr.chess.engine.board.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.apache.commons.lang3.ArrayUtils.reverse;
import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_DISSALOW_CASTELING_LEFT;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_DISSALOW_CASTELING_RGHT;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_NO;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_PAWN_EN_PASSANT;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_PAWN_GOES_TWO_STEPS;
import static org.dmkr.chess.api.model.Constants.VALUE_BISHOP;
import static org.dmkr.chess.api.model.Constants.VALUE_EMPTY;
import static org.dmkr.chess.api.model.Constants.VALUE_KING;
import static org.dmkr.chess.api.model.Constants.VALUE_KNIGHT;
import static org.dmkr.chess.api.model.Constants.VALUE_PAWN;
import static org.dmkr.chess.api.model.Constants.VALUE_QUEEN;
import static org.dmkr.chess.api.model.Constants.VALUE_ROOK;
import static org.dmkr.chess.api.utils.BoardUtils.getX;
import static org.dmkr.chess.api.utils.BoardUtils.getY;
import static org.dmkr.chess.api.utils.BoardUtils.index;
import static org.dmkr.chess.api.utils.BoardUtils.invertIndex;
import static org.dmkr.chess.api.utils.BoardUtils.isOnBoard;
import static org.dmkr.chess.api.utils.PieceGoesFunctions.PieceGoesFunction.GO_DOWN;
import static org.dmkr.chess.api.utils.PieceGoesFunctions.PieceGoesFunction.GO_DOWN_LEFT;
import static org.dmkr.chess.api.utils.PieceGoesFunctions.PieceGoesFunction.GO_DOWN_RIGHT;
import static org.dmkr.chess.api.utils.PieceGoesFunctions.PieceGoesFunction.GO_LEFT;
import static org.dmkr.chess.api.utils.PieceGoesFunctions.PieceGoesFunction.GO_RIGHT;
import static org.dmkr.chess.api.utils.PieceGoesFunctions.PieceGoesFunction.GO_UP;
import static org.dmkr.chess.api.utils.PieceGoesFunctions.PieceGoesFunction.GO_UP_LEFT;
import static org.dmkr.chess.api.utils.PieceGoesFunctions.PieceGoesFunction.GO_UP_RIGHT;
import static org.dmkr.chess.common.primitives.Bytes.apply;
import static org.dmkr.chess.common.primitives.Bytes.byte2;
import static org.dmkr.chess.common.primitives.Bytes.intByte4;

import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

import org.dmkr.chess.api.utils.BoardUtils;
import org.dmkr.chess.api.utils.PieceGoesFunctions.PieceGoesFunction;
import org.dmkr.chess.engine.board.AbstractBoard;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@EqualsAndHashCode(of = {"board"}, callSuper = true)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class BoardImpl extends AbstractBoard {
	private final byte[] board;
	
	BoardImpl(@NonNull byte[] board, boolean canCastleLeft, boolean canCastleRght, boolean canOponentCastleLeft, boolean canOponentCastleRght, boolean inverted) {
		super(canCastleLeft, canCastleRght, canOponentCastleLeft, canOponentCastleRght);
		
		checkArgument(board.length == SIZE * SIZE);
		
		this.board = board.clone();
		this.inverted = inverted;
	}
	
	@Override
	public byte at(int index) {
		return board[index];
	}

	@Override
	protected void set(int index, byte value) {
		board[index] = value;
	}
	
	@Override
	public boolean isEmpty(int index) {
		return at(index) == VALUE_EMPTY;
	}

	@Override
	protected void reverseBoardRepresentation() {
		reverse(board);
		apply(board, piece -> -piece);
	}
	
	protected void doApplyMove(int from, int to) {
 		set(to, at(from));
		set(from, VALUE_EMPTY);
	}
	
	protected void doRollbackMove(int from, int to, byte captured) {
		set(from, at(to));
		set(to, captured);
	}
	
	protected int[] calculateAllowedMoves() {
		for (int index = 0; index < SIZE * SIZE; index ++) {
			final byte piece = at(index);
			final int x = BoardUtils.getX(index);
			final int y = BoardUtils.getY(index);

			if (piece <= 0 || !movesSelector.selectMoves(piece)) {
				continue;
			}
				
			switch (piece) {
				case VALUE_KING: {
					final int specialMove = getSpecialMoveForKingMove(false, false);
					collectMove(index, x + 1, y + 1, specialMove);
					collectMove(index, x + 1, y - 1, specialMove);
					collectMove(index, x + 1, y, specialMove);
					collectMove(index, x - 1, y + 1, specialMove);
					collectMove(index, x - 1, y - 1, specialMove);
					collectMove(index, x - 1, y, specialMove);
					collectMove(index, x, y + 1, specialMove);
					collectMove(index, x, y - 1, specialMove);
					
					collectCastelingMoves(index);
					
					break;
				}
				case VALUE_QUEEN: {
					collectMoves(index, GO_UP);
					collectMoves(index, GO_UP_RIGHT);
					collectMoves(index, GO_UP_LEFT);
					collectMoves(index, GO_LEFT);
					collectMoves(index, GO_RIGHT);
					collectMoves(index, GO_DOWN);
					collectMoves(index, GO_DOWN_RIGHT);
					collectMoves(index, GO_DOWN_LEFT);
					
					break;
				}
				case VALUE_ROOK: {
					final int specialMove;
						
					if (canCastleLeft && index == 0)
						specialMove = SPECIAL_MOVE_DISSALOW_CASTELING_LEFT;
					else if (canCastleRght && index == 7)
						specialMove = SPECIAL_MOVE_DISSALOW_CASTELING_RGHT;
					else 
						specialMove = SPECIAL_MOVE_NO;
						
					collectMoves(index, GO_UP, specialMove);
					collectMoves(index, GO_DOWN, specialMove);
					collectMoves(index, GO_LEFT, specialMove);
					collectMoves(index, GO_RIGHT, specialMove);
						
					break;
				}
				case VALUE_BISHOP: {
					collectMoves(index, GO_UP_RIGHT);
					collectMoves(index, GO_UP_LEFT);
					collectMoves(index, GO_DOWN_RIGHT);
					collectMoves(index, GO_DOWN_LEFT);
						
					break;
				}
				case VALUE_KNIGHT: {
					collectMove(index, x + 2, y + 1);
					collectMove(index, x + 1, y + 2);
					collectMove(index, x - 2, y + 1);
					collectMove(index, x - 1, y + 2);
					collectMove(index, x + 2, y - 1);
					collectMove(index, x + 1, y - 2);
					collectMove(index, x - 2, y - 1);
					collectMove(index, x - 1, y - 2);
					
					break;
				}
				case VALUE_PAWN: {
					final boolean promotion = y == 6;

					// ordinary moves
					if (x > 0 && at(index + SIZE - 1) < 0)
						collectPawnMoves(moveOf(index, index + SIZE - 1), promotion);
					if (x < 7 && at(index + SIZE + 1) < 0)
						collectPawnMoves(moveOf(index, index + SIZE + 1), promotion);
					if (at(index + SIZE) == VALUE_EMPTY)
						collectPawnMoves(moveOf(index, index + SIZE), promotion);
						
					// en passant
					if (!movesSelector.skipEnPassenMoves()) {
						final int lastMove = movesHistory.empty() ? 0 : movesHistory.peek();
						if (y == 4 && intByte4(lastMove) == SPECIAL_MOVE_PAWN_GOES_TWO_STEPS) {
							final int to = invertIndex(byte2(lastMove));

							if ((to == index - 1 && x > 0) || (to == index + 1 && x < SIZE - 1)) {
								final int move = specialMoveOf(index, to + SIZE, SPECIAL_MOVE_PAWN_EN_PASSANT);
								if (!isKingUnderAtack(move))
									movesBuilder.add(move);
							}
						}
					}
						
					// goes two steps
					if (y == 1 && at(index + SIZE) == VALUE_EMPTY && at(index + SIZE + SIZE) == VALUE_EMPTY) {
						final int move = specialMoveOf(index, index + SIZE + SIZE, SPECIAL_MOVE_PAWN_GOES_TWO_STEPS);
						if (!isKingUnderAtack(move)) 
							movesBuilder.add(move);
					}
						
					break;
				}
				default:
					throw new IllegalStateException("x = " + x + " y = " + y + " piece = " + piece);
			}
		}
		
		return movesBuilder.build();
	}
	
	@Override
	public boolean calculateIsKingUnderAtack() {
		int kingIndex = -1;
		
		for (int i = 0; i < SIZE * SIZE; i ++)
			if (at(i) == VALUE_KING) {
				kingIndex = i;
				break;
			}
		
		checkState(kingIndex != -1, "King not found:\n%s", this);
		final int kingX = getX(kingIndex);
		final int kingY = getY(kingIndex);
		
		// king
		if (test(kingX + 1, kingY + 1, -VALUE_KING) ||
			test(kingX + 1, kingY + 0, -VALUE_KING) ||
			test(kingX + 1, kingY - 1, -VALUE_KING) ||
			test(kingX - 1, kingY + 1, -VALUE_KING) ||
			test(kingX - 1, kingY + 0, -VALUE_KING) ||
			test(kingX - 1, kingY - 1, -VALUE_KING) ||
			test(kingX + 0, kingY + 1, -VALUE_KING) ||
			test(kingX + 0, kingY - 1, -VALUE_KING)) {
			return true;
		}

		// knight
		if (test(kingX + 1, kingY + 2, -VALUE_KNIGHT) ||
			test(kingX + 2, kingY + 1, -VALUE_KNIGHT) ||
			test(kingX + 1, kingY - 2, -VALUE_KNIGHT) ||
			test(kingX + 2, kingY - 1, -VALUE_KNIGHT) ||
			test(kingX - 1, kingY + 2, -VALUE_KNIGHT) ||
			test(kingX - 2, kingY + 1, -VALUE_KNIGHT) ||
			test(kingX - 1, kingY - 2, -VALUE_KNIGHT) ||
			test(kingX - 2, kingY - 1, -VALUE_KNIGHT)) {
			return true;
		}
		
		// pawn
		if (test(kingX + 1, kingY + 1, -VALUE_PAWN) ||
			test(kingX - 1, kingY + 1, -VALUE_PAWN)) {
			return true;
		}
		
		// bishop, rook and queen
		if (test(kingIndex, GO_RIGHT, -VALUE_QUEEN, -VALUE_ROOK) ||
		    test(kingIndex, GO_LEFT, -VALUE_QUEEN, -VALUE_ROOK) ||
		    test(kingIndex, GO_UP, -VALUE_QUEEN, -VALUE_ROOK) ||
		    test(kingIndex, GO_DOWN, -VALUE_QUEEN, -VALUE_ROOK) ||
		    test(kingIndex, GO_UP_RIGHT, -VALUE_QUEEN, -VALUE_BISHOP) ||
		    test(kingIndex, GO_UP_LEFT, -VALUE_QUEEN, -VALUE_BISHOP) ||
		    test(kingIndex, GO_DOWN_RIGHT, -VALUE_QUEEN, -VALUE_BISHOP) ||
		    test(kingIndex, GO_DOWN_LEFT, -VALUE_QUEEN, -VALUE_BISHOP))
			return true;

		return false;
	}
	
	private void collectCastelingMoves(int kingIndex) {
		if (canCastleLeft && isEmpty(kingIndex - 1) && isEmpty(kingIndex - 2) && (getX(kingIndex) == 3 || isEmpty(kingIndex - 3)) && at(0) == VALUE_ROOK)
			if (!isKingUnderAtack() && !isKingUnderAtack(moveOf(kingIndex, kingIndex - 1)) && !isKingUnderAtack(moveOf(kingIndex, kingIndex - 2)))	
				movesBuilder.add(specialMoveOf(kingIndex, kingIndex - 2, getSpecialMoveForKingMove(true, true)));
		
		if (canCastleRght && isEmpty(kingIndex + 1) && isEmpty(kingIndex + 2) && (getX(kingIndex) == 4 || isEmpty(kingIndex + 3)) && at(7) == VALUE_ROOK)
			if (!isKingUnderAtack() && !isKingUnderAtack(moveOf(kingIndex, kingIndex + 1)) && !isKingUnderAtack(moveOf(kingIndex, kingIndex + 2)))	
				movesBuilder.add(specialMoveOf(kingIndex, kingIndex + 2, getSpecialMoveForKingMove(true, false)));
	}
		
	@Override
	protected BoardImpl cloneImpl() {
		return new BoardImpl(board, canCastleLeft, canCastleRght, canOponentCastleLeft, canOponentCastleRght, inverted);
	}
	
	private void collectMove(int fromIndex, int toX, int toY) {
		collectMove(fromIndex, toX, toY, SPECIAL_MOVE_NO);
	}
	
	private void collectMove(int fromIndex, int toX, int toY, int specialMove) {
		if (!isOnBoard(toX, toY))
			return;
		
		final int toIndex = index(toX, toY);
		if (at(toIndex) > 0)
			return;
		
		final int move = moveOf(fromIndex, toIndex);
		if (isKingUnderAtack(move))
			return;
		
		movesBuilder.add(specialMoveOf(move, specialMove));
	}
	
	private void collectMoves(int index, PieceGoesFunction pieceGoesFunction) {
		collectMoves(index, pieceGoesFunction, SPECIAL_MOVE_NO);
	}
	
	private void collectMoves(int index, PieceGoesFunction pieceGoesFunction, int specialMove) {
		final IntUnaryOperator goFunction = pieceGoesFunction.goFunction();
		final IntPredicate stopPredicate = pieceGoesFunction.stopPredicate();
		final int startIndex = index;
		while (true) {
			index = goFunction.applyAsInt(index);
			if (!isOnBoard(index) || at(index) > 0 || stopPredicate.test(index))
				return;

			final int move = specialMoveOf(startIndex, index, specialMove);
			if (!isKingUnderAtack(move))
				movesBuilder.add(move);
		
			if (at(index) < 0)
				return;
		}
	}
	
	private boolean test(int index, PieceGoesFunction pieceGoesFunction, int ... piecesToFind) {
		final IntUnaryOperator goFunction = pieceGoesFunction.goFunction();
		final IntPredicate stopPredicate = pieceGoesFunction.stopPredicate();
		
		while (true) {
			index = goFunction.applyAsInt(index);
			if (index < 0 || index >= SIZE * SIZE || stopPredicate.test(index))
				return false;
			
			final byte piece = at(index);
			if (piece == VALUE_EMPTY)
				continue;
			
			for (int pieceToFind: piecesToFind)
				if (piece == pieceToFind)
					return true;
			
			return false;
		}
	}
	
	private boolean test(int x, int y, int piece) {
		return isOnBoard(x, y) && at(index(x, y)) == piece;
	}
	
}
