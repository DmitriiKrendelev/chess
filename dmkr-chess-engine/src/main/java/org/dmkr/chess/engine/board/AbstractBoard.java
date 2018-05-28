package org.dmkr.chess.engine.board;

import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.model.Constants.*;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_DISSALOW_CASTELING_LEFT;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_DISSALOW_CASTELING_RGHT;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_NO;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_PAWN_EN_PASSANT;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_PAWN_GOES_TWO_STEPS;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_PAWN_TO_BISHOP;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_PAWN_TO_KNIGHT;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_PAWN_TO_QUEEN;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_PAWN_TO_ROOK;
import static org.dmkr.chess.api.model.Constants.VALUE_EMPTY;
import static org.dmkr.chess.api.model.Constants.VALUE_PAWN;
import static org.dmkr.chess.common.primitives.Bytes.byte1;
import static org.dmkr.chess.common.primitives.Bytes.byte2;
import static org.dmkr.chess.common.primitives.Bytes.byte3;
import static org.dmkr.chess.common.primitives.Bytes.byte4;
import static org.dmkr.chess.common.primitives.Bytes.intByte4;
import static org.dmkr.chess.common.primitives.Bytes.toInt;

import java.util.function.Function;
import java.util.function.IntPredicate;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.MovesSelector;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.api.utils.BoardUtils;
import org.dmkr.chess.common.primitives.IntArrayBuilder;
import org.dmkr.chess.common.primitives.IntStack;
import org.dmkr.chess.engine.board.impl.MovesSelectorImpl;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(of = {"canCastleLeft", "canCastleRght", "canOponentCastleLeft", "canOponentCastleRght", "inverted"})
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public abstract class AbstractBoard implements BoardEngine {
	protected final IntArrayBuilder movesBuilder = new IntArrayBuilder();
	protected final IntStack movesHistory = new IntStack();

	@Getter
	protected boolean canCastleLeft, canCastleRght, canOponentCastleLeft, canOponentCastleRght;
	protected boolean inverted;
	
	// moves to collect 
	protected MovesSelector movesSelector;
	
	// caching 
	private int[] cachedAllowedMoves = null;
	private int[] cachedAllowedMovesOponent = null;
	private Boolean cachedIsKingUnderAtack = null;
	
	private void resetCache() {
		cachedAllowedMoves = null;
		cachedIsKingUnderAtack = null;
	}
	
	public static void resetCache(BoardEngine board) {
		((AbstractBoard) board).resetCache();
	}
	
	protected AbstractBoard(boolean canCastleLeft, boolean canCastleRght, boolean canOponentCastleLeft, boolean canOponentCastleRght) {
		this.canCastleLeft = canCastleLeft;
		this.canCastleRght = canCastleRght;
		this.canOponentCastleLeft = canOponentCastleLeft;
		this.canOponentCastleRght = canOponentCastleRght;
	}

	@Override
	public boolean isInverted() {
		return inverted;
	}
	
	@Override
	public abstract byte at(int index);
	
	protected abstract void set(int index, byte value);
	
	protected abstract void reverseBoardRepresentation();
	
	protected abstract void doApplyMove(int from, int to);
	
	protected abstract void doRollbackMove(int from, int to, byte captured);
	
	@Override
	public int[] movesHistory() {
		return movesHistory.array();
	}
	
	@Override
	public int moveNumber() {
		return (movesHistory.size() >> 1) + 1;
	}

	@Override
	public int movesHistorySize() {
		return movesHistory.size();
	}
	
	@Override
	public int[] calculateAllowedMoves(MovesSelector movesSelector) {
		this.movesSelector = movesSelector;
		final int[] moves = calculateAllowedMoves();
		this.movesSelector = MovesSelectorImpl.DEFAULT;
		
		return moves;
	}
	
	@Override
	public int[] calculateAllowedMovesOponent(MovesSelector movesSelector) {
		invert();
		movesHistory.push(0);
		final int[] moves = calculateAllowedMoves(movesSelector);
		movesHistory.pop();
		invert();
		
		return moves;
	}
	
	@Override
	public int[] allowedMoves() {
		if (cachedAllowedMoves == null)
			cachedAllowedMoves = calculateAllowedMoves(MovesSelectorImpl.DEFAULT);
			
		return cachedAllowedMoves;
	}
	
	@Override
	public int[] allowedMoves(Function<int[], int[]> movesFilter) {
		return movesFilter.apply(allowedMoves());
	}
	
	@Override
	public int[] allowedMovesOponent() {
		if (cachedAllowedMovesOponent == null) {
			invert();
			movesHistory.push(0);
			final int[] result = calculateAllowedMoves();
			movesHistory.pop();
			invert();

			cachedAllowedMovesOponent = result;
		}
		
		return cachedAllowedMovesOponent;
	}
	
	protected abstract int[] calculateAllowedMoves();
	
	@Override
	public boolean isKingUnderAtack() {
		if (cachedIsKingUnderAtack == null) {
			cachedIsKingUnderAtack = calculateIsKingUnderAtack();
		}
		return cachedIsKingUnderAtack;
	}
	
	@Override
	public boolean isKingUnderAtackAfterMove(int move) {
		applyMove(move);
		final boolean isKingUnderAtack = calculateIsKingUnderAtack();
		rollbackMove();
		
		return isKingUnderAtack;		
	}
	
	protected boolean isKingUnderAtack(int move) {
		return movesSelector.checkKingUnderAtack() && isKingUnderAtackAfterMove(move);
	}
	
	protected void collectPawnMoves(int move, boolean promotion) {
		if (isKingUnderAtack(move))
			return;
		
		if (promotion) {
			movesBuilder.add(specialMoveOf(move, SPECIAL_MOVE_PAWN_TO_QUEEN));
			movesBuilder.add(specialMoveOf(move, SPECIAL_MOVE_PAWN_TO_ROOK));
			movesBuilder.add(specialMoveOf(move, SPECIAL_MOVE_PAWN_TO_BISHOP));
			movesBuilder.add(specialMoveOf(move, SPECIAL_MOVE_PAWN_TO_KNIGHT));
		}
		else
			movesBuilder.add(move);
	}
	
	protected int moveOf(int from, int to) {
		return toInt(from, to, (byte) -at(to));
	}

	protected int specialMoveOf(int move, int specialMove) {
		return move | specialMove;
	}
	
	protected int specialMoveOf(int from, int to, int specialMove) {
		return specialMoveOf(moveOf(from, to), specialMove);
	}
	
	@Override
	public void applyMove(int move) {
		applyMove(move, true);
		resetCache();
	}
	
	private void applyMove(int move, boolean safeHistory) {
		if (safeHistory) {
			movesHistory.push(move);
		}

		final byte from = byte1(move);
		final byte to = byte2(move);
		final int specialMove = intByte4(move);
	
		doApplyMove(from, to);
		
		switch (specialMove) {
			case SPECIAL_MOVE_NO:
			case SPECIAL_MOVE_PAWN_GOES_TWO_STEPS:
				return;
			
			case SPECIAL_MOVE_PAWN_EN_PASSANT:
				set(to - SIZE, VALUE_EMPTY);
				return;
			
			case SPECIAL_MOVE_PAWN_TO_QUEEN:
			case SPECIAL_MOVE_PAWN_TO_ROOK:
			case SPECIAL_MOVE_PAWN_TO_BISHOP:
			case SPECIAL_MOVE_PAWN_TO_KNIGHT:
				set(to, byte4(specialMove));
				return;
			
			case SPECIAL_MOVE_CASTELING_LEFT_DISSALOW_CASTELING_BOTH:
			case SPECIAL_MOVE_CASTELING_LEFT_DISSALOW_CASTELING_LEFT:
				applyMove(moveOf(0, from - 1), false);
				changeCastelingFlags(specialMove, false);
				return;
			
			case SPECIAL_MOVE_CASTELING_RGHT_DISSALOW_CASTELING_BOTH:
			case SPECIAL_MOVE_CASTELING_RGHT_DISSALOW_CASTELING_RGHT:
				applyMove(moveOf(7, from + 1), false);
				changeCastelingFlags(specialMove, false);
				return;	
				
			case SPECIAL_MOVE_DISSALOW_CASTELING_LEFT:
			case SPECIAL_MOVE_DISSALOW_CASTELING_RGHT:
			case SPECIAL_MOVE_DISSALOW_CASTELING_BOTH:
				changeCastelingFlags(specialMove, false);
				return;		

			default:
				throw new IllegalArgumentException("Unsupported special move: " + ( specialMove >>> 24) + "\nMove: " + Move.moveOf(move, inverted) + "\n" + this);
		}
	}
	
	@Override
	public void rollbackMove() {
		if (movesHistory.size() != 0) {
			rollbackMove(movesHistory.pop());
			resetCache();
		}
	}
	
	private void rollbackMove(int move) {
		final byte from = byte1(move);
		final byte to = byte2(move);
		final byte captured = (byte) -byte3(move); 
		final int specialMove = intByte4(move);
		
		doRollbackMove(from, to, captured);
		
		switch (specialMove) {
			case SPECIAL_MOVE_NO:
			case SPECIAL_MOVE_PAWN_GOES_TWO_STEPS:
				return;
			case SPECIAL_MOVE_PAWN_EN_PASSANT:
				set(to - SIZE, (byte) -VALUE_PAWN);
			
			case SPECIAL_MOVE_PAWN_TO_QUEEN:
			case SPECIAL_MOVE_PAWN_TO_ROOK:
			case SPECIAL_MOVE_PAWN_TO_BISHOP:
			case SPECIAL_MOVE_PAWN_TO_KNIGHT:
				set(from, VALUE_PAWN);
				return;

			case SPECIAL_MOVE_CASTELING_LEFT_DISSALOW_CASTELING_BOTH:
			case SPECIAL_MOVE_CASTELING_LEFT_DISSALOW_CASTELING_LEFT:
				applyMove(moveOf(from - 1, 0), false);
				changeCastelingFlags(specialMove, true);
				return;
			case SPECIAL_MOVE_CASTELING_RGHT_DISSALOW_CASTELING_BOTH:
			case SPECIAL_MOVE_CASTELING_RGHT_DISSALOW_CASTELING_RGHT:
				applyMove(moveOf(from + 1, 7), false);
				changeCastelingFlags(specialMove, true);
				return;	
				
			case SPECIAL_MOVE_DISSALOW_CASTELING_LEFT:
			case SPECIAL_MOVE_DISSALOW_CASTELING_RGHT:
			case SPECIAL_MOVE_DISSALOW_CASTELING_BOTH:
				changeCastelingFlags(specialMove, true);
				return;		

			default:
				throw new IllegalArgumentException("Unsupported special move: " + specialMove + "\nMove: " + Move.moveOf(move, inverted) + "\n" + this);
		}
	}
	
	protected int getSpecialMoveForKingMove(boolean isCasteling, boolean isCastelingLeft) {
		if (isCasteling) {
			if (isCastelingLeft) {
				return canCastleRght ? SPECIAL_MOVE_CASTELING_LEFT_DISSALOW_CASTELING_BOTH : SPECIAL_MOVE_CASTELING_LEFT_DISSALOW_CASTELING_LEFT;
			} else {
				return canCastleLeft ? SPECIAL_MOVE_CASTELING_RGHT_DISSALOW_CASTELING_BOTH : SPECIAL_MOVE_CASTELING_RGHT_DISSALOW_CASTELING_RGHT;
			}
		} else {
			if (canCastleLeft && canCastleRght)
				return SPECIAL_MOVE_DISSALOW_CASTELING_BOTH;
			else if (canCastleLeft)
				return SPECIAL_MOVE_DISSALOW_CASTELING_LEFT;
			else if (canCastleRght)
				return SPECIAL_MOVE_DISSALOW_CASTELING_RGHT;
			else
				return SPECIAL_MOVE_NO;
		}
	}
	
	private void changeCastelingFlags(int specialMove, boolean allowCasteling) {
		switch (specialMove) {
			case SPECIAL_MOVE_CASTELING_LEFT_DISSALOW_CASTELING_BOTH:
			case SPECIAL_MOVE_CASTELING_RGHT_DISSALOW_CASTELING_BOTH:
			case SPECIAL_MOVE_DISSALOW_CASTELING_BOTH:
				canCastleLeft = allowCasteling;
				canCastleRght = allowCasteling;
				return;
	
			case SPECIAL_MOVE_CASTELING_LEFT_DISSALOW_CASTELING_LEFT:
			case SPECIAL_MOVE_DISSALOW_CASTELING_LEFT:
				canCastleLeft = allowCasteling;
				return;
			
			case SPECIAL_MOVE_CASTELING_RGHT_DISSALOW_CASTELING_RGHT:
			case SPECIAL_MOVE_DISSALOW_CASTELING_RGHT:
				canCastleRght = allowCasteling;
				return;
				
			default:
				throw new IllegalArgumentException("Move: " + specialMove + " does not change castling flags");
		}
	}
	
	@Override
	public BoardEngine invert() {
		inverted = !inverted;
		
		boolean tmp;
		tmp = canCastleLeft;
		canCastleLeft = canOponentCastleRght;
		canOponentCastleRght = tmp;
		tmp = canCastleRght;
		canCastleRght = canOponentCastleLeft;
		canOponentCastleLeft = tmp;
		
		reverseBoardRepresentation();
		
		resetCache();
		return this;
	}	
	
	protected abstract AbstractBoard cloneImpl();
	
	@Override
	public AbstractBoard clone() {
		final AbstractBoard clone = cloneImpl();

		clone.canCastleLeft = canCastleLeft;
		clone.canCastleRght = canCastleRght;
		clone.canOponentCastleLeft = canOponentCastleLeft;
		clone.canOponentCastleRght = canOponentCastleRght;
		
		clone.movesHistory.reset(this.movesHistory);
		clone.movesBuilder.reset(this.movesBuilder);
		clone.inverted = inverted;
		
		return clone;
	}
	
	@Override
	public String toString() {
		return new StringBuilder(256)
		.append(BoardUtils.toString(this))
		.append("\nCasteling Flags:")
		.append(" L = ").append(canCastleLeft)
		.append(" R = ").append(canCastleRght)
		.append(" OL = ").append(canOponentCastleLeft)
		.append(" OR = ").append(canOponentCastleRght)
		.append("\nMoves History: ").append(getMovesHistory())
		.toString();
	}
}
