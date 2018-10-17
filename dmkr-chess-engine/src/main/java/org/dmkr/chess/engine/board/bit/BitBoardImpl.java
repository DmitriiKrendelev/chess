package org.dmkr.chess.engine.board.bit;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.Long.numberOfTrailingZeros;
import static java.lang.Long.reverse;
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
import static org.dmkr.chess.api.utils.BitBoardMasks.BISHOP_ATACKS;
import static org.dmkr.chess.api.utils.BitBoardMasks.BOARD_FIELDS;
import static org.dmkr.chess.api.utils.BitBoardMasks.BOARD_INDEX_TO_LONG_INDEX;
import static org.dmkr.chess.api.utils.BitBoardMasks.EMPTY_FOR_CASTELING_LEFT_LONG;
import static org.dmkr.chess.api.utils.BitBoardMasks.EMPTY_FOR_CASTELING_LEFT_SHORT;
import static org.dmkr.chess.api.utils.BitBoardMasks.EMPTY_FOR_CASTELING_RGHT_LONG;
import static org.dmkr.chess.api.utils.BitBoardMasks.EMPTY_FOR_CASTELING_RGHT_SHORT;
import static org.dmkr.chess.api.utils.BitBoardMasks.KING_ATACKS;
import static org.dmkr.chess.api.utils.BitBoardMasks.KNIGHT_ATACKS;
import static org.dmkr.chess.api.utils.BitBoardMasks.LINE_2;
import static org.dmkr.chess.api.utils.BitBoardMasks.NOT_A;
import static org.dmkr.chess.api.utils.BitBoardMasks.NOT_H;
import static org.dmkr.chess.api.utils.BitBoardMasks.PAWN_ATACKS;
import static org.dmkr.chess.api.utils.BitBoardMasks.PAWN_CAN_GO_TWO_STEPS;
import static org.dmkr.chess.api.utils.BitBoardMasks.ROOK_ATACKS;
import static org.dmkr.chess.api.utils.BitBoardUtils.doWithUpBits;
import static org.dmkr.chess.api.utils.BoardUtils.getX;
import static org.dmkr.chess.api.utils.BoardUtils.getY;
import static org.dmkr.chess.api.utils.BoardUtils.invertIndex;
import static org.dmkr.chess.api.utils.PieceGoesFunctionsBit.PieceGoesFunctionBit.GO_DOWN;
import static org.dmkr.chess.api.utils.PieceGoesFunctionsBit.PieceGoesFunctionBit.GO_DOWN_LEFT;
import static org.dmkr.chess.api.utils.PieceGoesFunctionsBit.PieceGoesFunctionBit.GO_DOWN_RIGHT;
import static org.dmkr.chess.api.utils.PieceGoesFunctionsBit.PieceGoesFunctionBit.GO_LEFT;
import static org.dmkr.chess.api.utils.PieceGoesFunctionsBit.PieceGoesFunctionBit.GO_RIGHT;
import static org.dmkr.chess.api.utils.PieceGoesFunctionsBit.PieceGoesFunctionBit.GO_UP;
import static org.dmkr.chess.api.utils.PieceGoesFunctionsBit.PieceGoesFunctionBit.GO_UP_LEFT;
import static org.dmkr.chess.api.utils.PieceGoesFunctionsBit.PieceGoesFunctionBit.GO_UP_RIGHT;
import static org.dmkr.chess.common.primitives.Bytes.byte2;
import static org.dmkr.chess.common.primitives.Bytes.intByte4;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.model.Piece;
import org.dmkr.chess.api.utils.BitBoardUtils;
import org.dmkr.chess.api.utils.PieceGoesFunctionsBit.PieceGoesFunctionBit;
import org.dmkr.chess.engine.board.AbstractBoard;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@EqualsAndHashCode(of = {"pieces", "oponentPieces"}, callSuper = true)
public class BitBoardImpl extends AbstractBoard implements BitBoard {
	public static final int INDEX_PAWN = VALUE_PAWN - 1;
	public static final int INDEX_KNIGHT = VALUE_KNIGHT - 1;
	public static final int INDEX_BISHOP = VALUE_BISHOP - 1;
	public static final int INDEX_ROOK = VALUE_ROOK - 1;
	public static final int INDEX_QUEEN = VALUE_QUEEN - 1;
	public static final int INDEX_KING = VALUE_KING - 1;
	public static final int PIECES_LENGTH = 6;
	
	private final long[] pieces;
	private final long[] oponentPieces;
	private long empty;

	@Deprecated // Used for deserializetion
	public BitBoardImpl() {
		this(new long[PIECES_LENGTH], new long[PIECES_LENGTH], false, false, false, false, false, false);
	}


	protected BitBoardImpl(@NonNull long[] pieces, @NonNull long[] oponentPieces, boolean canCastleLeft, boolean canCastleRght, boolean canOponentCastleLeft, boolean canOponentCastleRght, boolean inverted) {
		this(pieces, oponentPieces, canCastleLeft, canCastleRght, canOponentCastleLeft, canOponentCastleRght, inverted, false);
	}
	
	private BitBoardImpl(@NonNull long[] pieces, @NonNull long[] oponentPieces, boolean canCastleLeft, boolean canCastleRght, boolean canOponentCastleLeft, boolean canOponentCastleRght, boolean inverted, boolean isDummy) {
		super(canCastleLeft, canCastleRght, canOponentCastleLeft, canOponentCastleRght, isDummy);
		
		this.pieces = pieces.clone();
		this.oponentPieces = oponentPieces.clone();
		this.empty = calculateEmpty();
		this.inverted = inverted;
	}
	
	@Override
	public byte at(int index) {
		final long field = BOARD_FIELDS[index];
		
		if (isEmpty(field)) {
			return VALUE_EMPTY;
		}

		for (int i = 0; i < PIECES_LENGTH; i ++) {
			if ((pieces[i] & field) != 0) {
				return (byte) (i + 1);
			} else if ((oponentPieces[i] & field) != 0) {
				return (byte) (-i - 1);
			}
		}

		throw new IllegalStateException("index = " + index + "\n");
	}
	
	@Override
	public boolean isEmpty(int index) {
		return isEmpty(BOARD_FIELDS[index]);
	}
	
	private boolean isEmpty(long field) {
		return (field & empty) != 0;
	}

	@Override
	protected void set(int index, byte value) {
		final long field = BOARD_FIELDS[index];
		
		if (!isEmpty(field)) {
			final long emptyField = ~field;
			for (int i = 0; i < PIECES_LENGTH; i ++) {
				pieces[i] &= emptyField;
				oponentPieces[i] &= emptyField;
			}
		}
		
		if (value > 0) {
			pieces[value - 1] |= field;
		} else if (value < 0) {
			oponentPieces[-value - 1] |= field;
		}
		
		if (value == VALUE_EMPTY) {
			empty |= field;
		} else {
			empty &= ~field;
		}
	}
	
	//	set(to, at(from));
	//	set(from, VALUE_EMPTY);
	protected void doApplyMove(int from, int to) {
		final long fromField = BOARD_FIELDS[from];
		final long toField = BOARD_FIELDS[to];
		
		byte fromPieceIndex = -1;
		for (int i = 0; i < PIECES_LENGTH; i ++) {
			if ((pieces[i] & fromField) != 0) {
				fromPieceIndex = (byte) i;
				break;
			}
		}
		
		pieces[fromPieceIndex] &= ~fromField;
		empty |= fromField;

		pieces[fromPieceIndex] |= toField;
		
		final long emptyToField = ~toField;
		if ((empty & toField) != 0) {
			empty &= emptyToField;
		} else {
			for (int i = 0; i < PIECES_LENGTH; i ++) {
				oponentPieces[i] &= emptyToField;
			}
		}
	}

	//	set(from, at(to));
	//	set(to, captured);
	protected void doRollbackMove(int from, int to, byte captured) {
		final long fromField = BOARD_FIELDS[from];
		final long toField = BOARD_FIELDS[to];
		
		if (captured == VALUE_EMPTY) {
			empty |= toField;
		} else {
			if (captured > 0) {
				pieces[captured - 1] |= toField;
			} else {
				oponentPieces[-captured - 1] |= toField;
			}
		}
		
		byte toPieceIndex = -1;
		for (int i = 0; i < PIECES_LENGTH; i ++) {
			if ((pieces[i] & toField) != 0) {
				toPieceIndex = (byte) i;
				break;
			}
		}

		empty &= ~fromField;
		pieces[toPieceIndex] |= fromField;
		pieces[toPieceIndex] &= ~toField;
	}

	@Override
	protected int[] calculateAllowedMoves() {
		final long allPieces = piecePositions();
		final long emptyAndOponentPositions = empty | (~allPieces);
		final long allOponentPieces = emptyAndOponentPositions & ~empty;
		
		// calculate king moves
		if (movesSelector.selectMoves(VALUE_KING)) {
			final long king = pieces[INDEX_KING];
			checkState(king != 0, "King is not found:\n%s", this);
			
			
			final int kingIndex = BOARD_INDEX_TO_LONG_INDEX[numberOfTrailingZeros(king)];
			final long kingGoesPositions = KING_ATACKS[kingIndex] & emptyAndOponentPositions;
			if (kingGoesPositions != 0) {	
				final int specialMove = getSpecialMoveForKingMove(false, false);
				doWithUpBits(kingGoesPositions, to -> {
					final int move = specialMoveOf(kingIndex, to, specialMove);
					if (!isKingUnderAtack(move)) {
						movesBuilder.add(move);
					}
				});
				
				collectCastelingMoves(kingIndex);
			}
		}
		
		// calculate knight moves
		if (movesSelector.selectMoves(VALUE_KNIGHT)) {
			doWithUpBits(pieces[INDEX_KNIGHT], knightIndex -> {
				final long knightGoesPositions = KNIGHT_ATACKS[knightIndex] & emptyAndOponentPositions;
				doWithUpBits(knightGoesPositions, to -> {
					final int move = moveOf(knightIndex, to);
					if (!isKingUnderAtack(move)) {
						movesBuilder.add(move);
					}
				});
			});
		}
		
		// calculate pawn moves
		if (movesSelector.selectMoves(VALUE_PAWN)) {
			final long pawns = pieces[INDEX_PAWN];
			final long captureLeft = (pawns << (SIZE + 1) & NOT_H) & allOponentPieces;
			// pawn capture left
			doWithUpBits(captureLeft, to -> {
				final boolean promotion = getY(to) == SIZE - 1;
				collectPawnMoves(moveOf(to - SIZE + 1, to), promotion);
			});
			// pawn capture rght
			final long captureRght = (pawns << (SIZE - 1) & NOT_A) & allOponentPieces;
			doWithUpBits(captureRght, to -> {
				final boolean promotion = getY(to) == SIZE - 1;
				collectPawnMoves(moveOf(to - SIZE - 1, to), promotion);
			});
			// pawn goes up
			final long goUp = (pawns << SIZE) & empty;
			doWithUpBits(goUp, to -> {
				final boolean promotion = getY(to) == SIZE - 1;
				collectPawnMoves(moveOf(to - SIZE, to), promotion);
			});
			// pawn goes two steps
			final long goTwoSteps = (pawns & LINE_2);
			doWithUpBits(goTwoSteps, index -> {
				if ((PAWN_CAN_GO_TWO_STEPS[index - SIZE] & ~empty) == 0) {
					final int move = specialMoveOf(index, index + SIZE + SIZE, SPECIAL_MOVE_PAWN_GOES_TWO_STEPS);
					if (!isKingUnderAtack(move)) 
						movesBuilder.add(move);
				}
			});

			// en passen
			if (!movesSelector.skipEnPassenMoves()) {
				final int lastMove = movesHistory.empty() ? 0 : movesHistory.peek();
				if (intByte4(lastMove) == SPECIAL_MOVE_PAWN_GOES_TWO_STEPS) {
					final int to = invertIndex(byte2(lastMove));
					final int x = getX(to);

					// capture right
					if (x != SIZE - 1 && (pawns & BOARD_FIELDS[to + 1]) != 0) {
						final int move = specialMoveOf(to + 1, to + SIZE, SPECIAL_MOVE_PAWN_EN_PASSANT);
						if (!isKingUnderAtack(move))
							movesBuilder.add(move);
					}

					// capture left
					if (x != 0 && (pawns & BOARD_FIELDS[to - 1]) != 0) {
						final int move = specialMoveOf(to - 1, to + SIZE, SPECIAL_MOVE_PAWN_EN_PASSANT);
						if (!isKingUnderAtack(move))
							movesBuilder.add(move);
					}
				}
			}
		}
		

		// calculate bishop moves
		if (movesSelector.selectMoves(VALUE_BISHOP)) {
			doWithUpBits(pieces[INDEX_BISHOP], bishopIndex -> {
				final long bishopField = BOARD_FIELDS[bishopIndex];
				collectMoves(bishopField, allPieces, allOponentPieces, GO_UP_RIGHT);
				collectMoves(bishopField, allPieces, allOponentPieces, GO_UP_LEFT);
				collectMoves(bishopField, allPieces, allOponentPieces, GO_DOWN_RIGHT);
				collectMoves(bishopField, allPieces, allOponentPieces, GO_DOWN_LEFT);
			});
		}
		
		// calculate rook moves
		if (movesSelector.selectMoves(VALUE_ROOK)) {
			doWithUpBits(pieces[INDEX_ROOK], rookIndex -> {
				final int specialMove;
				
				if (canCastleLeft && rookIndex == 0)
					specialMove = SPECIAL_MOVE_DISSALOW_CASTELING_LEFT;
				else if (canCastleRght && rookIndex == SIZE - 1)
					specialMove = SPECIAL_MOVE_DISSALOW_CASTELING_RGHT;
				else 
					specialMove = SPECIAL_MOVE_NO;
				
				final long rookField = BOARD_FIELDS[rookIndex];
				collectMoves(rookField, allPieces, allOponentPieces, GO_UP, specialMove);
				collectMoves(rookField, allPieces, allOponentPieces, GO_DOWN, specialMove);
				collectMoves(rookField, allPieces, allOponentPieces, GO_LEFT, specialMove);
				collectMoves(rookField, allPieces, allOponentPieces, GO_RIGHT, specialMove);
			});
		}
		
		// calculate queen moves
		if (movesSelector.selectMoves(VALUE_QUEEN)) {
			doWithUpBits(pieces[INDEX_QUEEN], queenIndex -> {
				final long queenField = BOARD_FIELDS[queenIndex];
				collectMoves(queenField, allPieces, allOponentPieces, GO_UP_RIGHT);
				collectMoves(queenField, allPieces, allOponentPieces, GO_UP_LEFT);
				collectMoves(queenField, allPieces, allOponentPieces, GO_DOWN_RIGHT);
				collectMoves(queenField, allPieces, allOponentPieces, GO_DOWN_LEFT);
				collectMoves(queenField, allPieces, allOponentPieces, GO_UP);
				collectMoves(queenField, allPieces, allOponentPieces, GO_DOWN);
				collectMoves(queenField, allPieces, allOponentPieces, GO_LEFT);
				collectMoves(queenField, allPieces, allOponentPieces, GO_RIGHT);
			});
		}
		
		return movesBuilder.build();
	}
	
	@Override
	public boolean calculateIsKingUnderAtack() {
		final long king = pieces[INDEX_KING];
		
		checkState(king != 0, "King is not found:\n%s", this);
		
		final int kingIndex = BOARD_INDEX_TO_LONG_INDEX[numberOfTrailingZeros(king)];
		
		// find pawn atacks
		if ((PAWN_ATACKS[kingIndex] & oponentPieces[INDEX_PAWN]) != 0) {
			return true;
		}
		
		// find knight atacks
		if ((KNIGHT_ATACKS[kingIndex] & oponentPieces[INDEX_KNIGHT]) != 0) {
			return true;
		}
		
		// find king atacks
		if ((KING_ATACKS[kingIndex] & oponentPieces[INDEX_KING]) != 0) {
			return true;
		}
		
		// find bishop atacks
		final long oponentBishopsAtacks = oponentPieces[INDEX_BISHOP] | oponentPieces[INDEX_QUEEN];
		
		if ((oponentBishopsAtacks & BISHOP_ATACKS[kingIndex]) != 0) {
			if (findPieceAtacks(king, oponentBishopsAtacks, GO_UP_LEFT) ||
				findPieceAtacks(king, oponentBishopsAtacks, GO_UP_RIGHT) ||
				findPieceAtacks(king, oponentBishopsAtacks, GO_DOWN_LEFT) ||
				findPieceAtacks(king, oponentBishopsAtacks, GO_DOWN_RIGHT)) {
				
				return true;
			}
		}
		
		// find rook atacks
		final long oponentRookAtacks = oponentPieces[INDEX_ROOK] | oponentPieces[INDEX_QUEEN];
		if ((oponentRookAtacks & ROOK_ATACKS[kingIndex]) != 0) {
			if (findPieceAtacks(king, oponentRookAtacks, GO_UP) ||
				findPieceAtacks(king, oponentRookAtacks, GO_LEFT) ||
				findPieceAtacks(king, oponentRookAtacks, GO_RIGHT) ||
				findPieceAtacks(king, oponentRookAtacks, GO_DOWN)) {
				
				return true;
			}
		}
		
		return false;
	}
	
	private boolean findPieceAtacks(long field, long pieceAtacks, PieceGoesFunctionBit pieceGoesFunction) {
		final LongUnaryOperator goFunction = pieceGoesFunction.goFunction();
		final LongPredicate stopPredicate = pieceGoesFunction.stopPredicate();
		
		while (true) {
			field = goFunction.applyAsLong(field);
			if (stopPredicate.test(field)) {
				return false;
			}
			if ((pieceAtacks & field) != 0) {
				return true;
			}	
			if (!isEmpty(field)) {
				return false;
			}
		}
	}
	
	private void collectCastelingMoves(int kingIndex) {
		if (canCastleLeft && ((EMPTY_FOR_CASTELING_LEFT_SHORT & ~empty) == 0L) && (getX(kingIndex) == 3 || (EMPTY_FOR_CASTELING_LEFT_LONG & ~empty) == 0L) && (BOARD_FIELDS[0] & pieces[INDEX_ROOK]) != 0)
			if (!isKingUnderAtack() && !isKingUnderAtack(moveOf(kingIndex, kingIndex - 1)) && !isKingUnderAtack(moveOf(kingIndex, kingIndex - 2)))	
				movesBuilder.add(specialMoveOf(kingIndex, kingIndex - 2, getSpecialMoveForKingMove(true, true)));
		
		if (canCastleRght && (EMPTY_FOR_CASTELING_RGHT_SHORT & ~empty) == 0L && (getX(kingIndex) == 4 || (EMPTY_FOR_CASTELING_RGHT_LONG & ~empty) == 0L) && (BOARD_FIELDS[7] & pieces[INDEX_ROOK]) != 0)
			if (!isKingUnderAtack() && !isKingUnderAtack(moveOf(kingIndex, kingIndex + 1)) && !isKingUnderAtack(moveOf(kingIndex, kingIndex + 2)))	
				movesBuilder.add(specialMoveOf(kingIndex, kingIndex + 2, getSpecialMoveForKingMove(true, false)));
	}

	private void collectMoves(long field, long allPieces, long allOponentPieces, PieceGoesFunctionBit pieceGoesFunction) {
		collectMoves(field, allPieces, allOponentPieces, pieceGoesFunction, SPECIAL_MOVE_NO);
	}
	
	private void collectMoves(long field, long allPieces, long allOponentPieces, PieceGoesFunctionBit pieceGoesFunction, int specialMove) {
		final LongUnaryOperator goFunction = pieceGoesFunction.goFunction();
		final LongPredicate stopPredicate = pieceGoesFunction.stopPredicate();
		final int startIndex = BOARD_INDEX_TO_LONG_INDEX[numberOfTrailingZeros(field)];
		while (true) {
			field = goFunction.applyAsLong(field);
			if (stopPredicate.test(field) || (allPieces & field) != 0) {
				return;
			}

			final int move = specialMoveOf(startIndex, BOARD_INDEX_TO_LONG_INDEX[numberOfTrailingZeros(field)], specialMove);
			if (!isKingUnderAtack(move))
				movesBuilder.add(move);
		
			if ((allOponentPieces & field) != 0)
				return;
		}
	}
	
	@Override
	protected void reverseBoardRepresentation() {
		long tmp;
		for (int i = 0; i < PIECES_LENGTH; i ++) {
			tmp = reverse(pieces[i]);
			pieces[i] = reverse(oponentPieces[i]);
			oponentPieces[i] = tmp;
		}
		
		empty = reverse(empty);
	}

	@Override
	public long piecePositions() {
		long result = 0;
		for (long piecePositions : pieces) {
			result |= piecePositions;
		}
		return result;
	}

	@Override
	public long piecePositionsOponent() {
		long result = 0;
		for (long piecePositionsOponent : oponentPieces) {
			result |= piecePositionsOponent;
		}
		return result;
	}
	
	private long calculateEmpty() {
		long nonEmpty = 0;
		for (int i = 0; i < PIECES_LENGTH; i ++) {
			nonEmpty |= pieces[i] | oponentPieces[i];
		}
		return ~nonEmpty;
	}
	
	@Override
	protected BitBoardImpl cloneImpl(boolean isDummy) {
		return new BitBoardImpl(pieces, oponentPieces, canCastleLeft, canCastleRght, canOponentCastleLeft, canOponentCastleRght, inverted, isDummy);
	}
	
	public String toBinaryString() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("EMPTY\n").append(BitBoardUtils.toBinaryString(empty)).append("\n");
		
		for (int i = 0; i < PIECES_LENGTH; i ++) {
			final long piecesPositions = pieces[i];
			if (piecesPositions != 0) {
				sb.append(Piece.withValue((byte) (i + 1)).toString() + "S:\n" + BitBoardUtils.toBinaryString(piecesPositions) + "\n\n");
			}
		}
		
		for (int i = 0; i < PIECES_LENGTH; i ++) {
			final long oponentPiecesPositions = oponentPieces[i];
			if (oponentPiecesPositions != 0) {
				sb.append("OPONENT " + Piece.withValue((byte) (i + 1)) + "S:\n" + BitBoardUtils.toBinaryString(oponentPiecesPositions) + "\n\n");
			}
		}
		
		return sb.toString();
	}
	
	@Deprecated
	public void checkSum() {
		long allFields = 0;
		final long[] all = new long[2 * PIECES_LENGTH + 1];
		System.arraycopy(pieces, 0, all, 0, PIECES_LENGTH);
		System.arraycopy(oponentPieces, 0, all, PIECES_LENGTH, PIECES_LENGTH);
		all[2 * PIECES_LENGTH] = empty;
		
		for (long positions : all) {
			allFields |= positions;
		}
		
		checkState(allFields == -1L);
		for (int i = 0; i < all.length; i ++) {
			for (int j = 0; j < all.length; j ++) {
				checkState(i == j || (all[i] & all[j]) == 0);
			}
		}
	}

	@Override
	public long pieces(byte pieceType) {
		return pieces[pieceType - 1];
	}

	@Override
	public long oponentPieces(byte pieceType) {
		return oponentPieces[pieceType - 1];
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeObject(this.pieces);
		out.writeObject(this.oponentPieces);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		System.arraycopy(in.readObject(), 0, this.pieces, 0, pieces.length);
		System.arraycopy(in.readObject(), 0, this.oponentPieces, 0, oponentPieces.length);
		this.empty = calculateEmpty();
	}

	@Override
	public long emptyPositions() {
		return empty;
	}
}
