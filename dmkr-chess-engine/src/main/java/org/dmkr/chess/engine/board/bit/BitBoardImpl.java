package org.dmkr.chess.engine.board.bit;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.Long.numberOfTrailingZeros;
import static java.lang.Long.reverse;
import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_DISSALOW_CASTELING_LEFT;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_DISSALOW_CASTELING_RGHT;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_NO;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_POWN_EN_PASSANT;
import static org.dmkr.chess.api.model.Constants.SPECIAL_MOVE_POWN_GOES_TWO_STEPS;
import static org.dmkr.chess.api.model.Constants.VALUE_BISHOP;
import static org.dmkr.chess.api.model.Constants.VALUE_EMPTY;
import static org.dmkr.chess.api.model.Constants.VALUE_KING;
import static org.dmkr.chess.api.model.Constants.VALUE_KNIGHT;
import static org.dmkr.chess.api.model.Constants.VALUE_POWN;
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
import static org.dmkr.chess.api.utils.BitBoardMasks.POWN_ATACKS;
import static org.dmkr.chess.api.utils.BitBoardMasks.POWN_CAN_GO_TWO_STEPS;
import static org.dmkr.chess.api.utils.BitBoardMasks.ROOK_ATACKS;
import static org.dmkr.chess.api.utils.BitBoardUtils.doWithUpBits;
import static org.dmkr.chess.api.utils.BoardUtils.getX;
import static org.dmkr.chess.api.utils.BoardUtils.getY;
import static org.dmkr.chess.api.utils.BoardUtils.invertIndex;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_DOWN;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_DOWN_LEFT;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_DOWN_RIGHT;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_LEFT;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_RIGHT;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_UP;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_UP_LEFT;
import static org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit.GO_UP_RIGHT;
import static org.dmkr.chess.common.primitives.Bytes.byte2;
import static org.dmkr.chess.common.primitives.Bytes.intByte4;

import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.model.Item;
import org.dmkr.chess.api.utils.BitBoardUtils;
import org.dmkr.chess.api.utils.ItemGoesFunctionsBit.ItemGoesFunctionBit;
import org.dmkr.chess.engine.board.AbstractBoard;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"items", "oponentItems"}, callSuper = true)
public class BitBoardImpl extends AbstractBoard implements BitBoard {
	public static final int INDEX_POWN = VALUE_POWN - 1;
	public static final int INDEX_KNIGHT = VALUE_KNIGHT - 1;
	public static final int INDEX_BISHOP = VALUE_BISHOP - 1;
	public static final int INDEX_ROOK = VALUE_ROOK - 1;
	public static final int INDEX_QUEEN = VALUE_QUEEN - 1;
	public static final int INDEX_KING = VALUE_KING - 1;
	public static final int ITEMS_LENGTH = 6;
	
	private final long[] items;
	private final long[] oponentItems;
	private long empty;
	
	
	protected BitBoardImpl(@NonNull long[] items, @NonNull long[] oponentItems, boolean canCastleLeft, boolean canCastleRght, boolean canOponentCastleLeft, boolean canOponentCastleRght) {
		super(canCastleLeft, canCastleRght, canOponentCastleLeft, canOponentCastleRght);
		
		this.items = items.clone();
		this.oponentItems = oponentItems.clone();
		this.empty = calculateEmpty();
	}
	
	@Override
	public byte at(int index) {
		final long field = BOARD_FIELDS[index];
		
		if (isEmpty(field)) {
			return VALUE_EMPTY;
		} else if ((field & itemPositions()) != 0) {
			for (int i = 0; i < ITEMS_LENGTH; i ++) {
				if ((items[i] & field) != 0) {
					return (byte) (i + 1);
				}
			}
		} else {
			for (int i = 0; i < ITEMS_LENGTH; i ++) {
				if ((oponentItems[i] & field) != 0) {
					return (byte) (-i - 1);
				}
			}
		}
		
		throw new IllegalStateException("index = " + index + "\n");
	}
	
	@Override
	protected boolean isEmpty(int index) {
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
			for (int i = 0; i < ITEMS_LENGTH; i ++) {
				items[i] &= emptyField;
				oponentItems[i] &= emptyField;
			}
		}
		
		if (value > 0) {
			items[value - 1] |= field;
		} else if (value < 0) {
			oponentItems[-value - 1] |= field;
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
		
		byte fromItemIndex = -1;
		for (int i = 0; i < ITEMS_LENGTH; i ++) {
			if ((items[i] & fromField) != 0) {
				fromItemIndex = (byte) i;
				break;
			}
		}
		
		items[fromItemIndex] &= ~fromField;
		empty |= fromField;

		items[fromItemIndex] |= toField;
		
		final long emptyToField = ~toField;
		if ((empty & toField) != 0) {
			empty &= emptyToField;
		} else {
			for (int i = 0; i < ITEMS_LENGTH; i ++) {
				oponentItems[i] &= emptyToField;
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
				items[captured - 1] |= toField;
			} else {
				oponentItems[-captured - 1] |= toField;
			}
		}
		
		byte toItemIndex = -1;
		for (int i = 0; i < ITEMS_LENGTH; i ++) {
			if ((items[i] & toField) != 0) {
				toItemIndex = (byte) i;
				break;
			}
		}

		empty &= ~fromField;
		items[toItemIndex] |= fromField;
		items[toItemIndex] &= ~toField;
	}

	@Override
	protected int[] calculateAllowedMoves() {
		final long allItems = itemPositions();
		final long emptyAndOponentPositions = empty | (~allItems);
		final long allOponentItems = emptyAndOponentPositions & ~empty;
		
		// calculate king moves
		if (movesSelector.selectMoves(VALUE_KING)) {
			final long king = items[INDEX_KING];
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
			doWithUpBits(items[INDEX_KNIGHT], knightIndex -> {
				final long knightGoesPositions = KNIGHT_ATACKS[knightIndex] & emptyAndOponentPositions;
				doWithUpBits(knightGoesPositions, to -> {
					final int move = moveOf(knightIndex, to);
					if (!isKingUnderAtack(move)) {
						movesBuilder.add(move);
					}
				});
			});
		}
		
		// calculate pown moves
		if (movesSelector.selectMoves(VALUE_POWN)) {
			final long powns = items[INDEX_POWN];
			final long captureLeft = (powns << (SIZE + 1) & NOT_H) & allOponentItems;
			// pown capture left
			doWithUpBits(captureLeft, to -> {
				final boolean promotion = getY(to) == SIZE - 1;
				collectPownMoves(moveOf(to - SIZE + 1, to), promotion);
			});
			// pown capture rght
			final long captureRght = (powns << (SIZE - 1) & NOT_A) & allOponentItems;
			doWithUpBits(captureRght, to -> {
				final boolean promotion = getY(to) == SIZE - 1;
				collectPownMoves(moveOf(to - SIZE - 1, to), promotion);
			});
			// pown goes up
			final long goUp = (powns << SIZE) & empty;
			doWithUpBits(goUp, to -> {
				final boolean promotion = getY(to) == SIZE - 1;
				collectPownMoves(moveOf(to - SIZE, to), promotion);
			});
			// pown goes two steps
			final long goTwoSteps = (powns & LINE_2);
			doWithUpBits(goTwoSteps, index -> {
				if ((POWN_CAN_GO_TWO_STEPS[index - SIZE] & ~empty) == 0) {
					final int move = specialMoveOf(index, index + SIZE + SIZE, SPECIAL_MOVE_POWN_GOES_TWO_STEPS);
					if (!isKingUnderAtack(move)) 
						movesBuilder.add(move);
				}
			});
			// en passen
			final int lastMove = movesHistory.empty() ? 0 : movesHistory.peek();
			if (intByte4(lastMove) == SPECIAL_MOVE_POWN_GOES_TWO_STEPS) {
				final int to = invertIndex(byte2(lastMove));
				final int x = getX(to);
				
				// capture right
				if (x != SIZE - 1 && (powns & BOARD_FIELDS[to + 1]) != 0) {
					final int move = specialMoveOf(to + 1, to + SIZE, SPECIAL_MOVE_POWN_EN_PASSANT);
					if (!isKingUnderAtack(move)) 
						movesBuilder.add(move);
				}
				
				// capture left
				if (x != 0 && (powns & BOARD_FIELDS[to - 1]) != 0) {
					final int move = specialMoveOf(to - 1, to + SIZE, SPECIAL_MOVE_POWN_EN_PASSANT);
					if (!isKingUnderAtack(move)) 
						movesBuilder.add(move);
				}
			}
		}
		

		// calculate bishop moves
		if (movesSelector.selectMoves(VALUE_BISHOP)) {
			doWithUpBits(items[INDEX_BISHOP], bishopIndex -> {
				final long bishopField = BOARD_FIELDS[bishopIndex];
				collectMoves(bishopField, allItems, allOponentItems, GO_UP_RIGHT);
				collectMoves(bishopField, allItems, allOponentItems, GO_UP_LEFT);
				collectMoves(bishopField, allItems, allOponentItems, GO_DOWN_RIGHT);
				collectMoves(bishopField, allItems, allOponentItems, GO_DOWN_LEFT);
			});
		}
		
		// calculate rook moves
		if (movesSelector.selectMoves(VALUE_ROOK)) {
			doWithUpBits(items[INDEX_ROOK], rookIndex -> {
				final int specialMove;
				
				if (canCastleLeft && rookIndex == 0)
					specialMove = SPECIAL_MOVE_DISSALOW_CASTELING_LEFT;
				else if (canCastleRght && rookIndex == SIZE - 1)
					specialMove = SPECIAL_MOVE_DISSALOW_CASTELING_RGHT;
				else 
					specialMove = SPECIAL_MOVE_NO;
				
				final long rookField = BOARD_FIELDS[rookIndex];
				collectMoves(rookField, allItems, allOponentItems, GO_UP, specialMove);
				collectMoves(rookField, allItems, allOponentItems, GO_DOWN, specialMove);
				collectMoves(rookField, allItems, allOponentItems, GO_LEFT, specialMove);
				collectMoves(rookField, allItems, allOponentItems, GO_RIGHT, specialMove);
			});
		}
		
		// calculate queen moves
		if (movesSelector.selectMoves(VALUE_QUEEN)) {
			doWithUpBits(items[INDEX_QUEEN], queenIndex -> {
				final long queenField = BOARD_FIELDS[queenIndex];
				collectMoves(queenField, allItems, allOponentItems, GO_UP_RIGHT);
				collectMoves(queenField, allItems, allOponentItems, GO_UP_LEFT);
				collectMoves(queenField, allItems, allOponentItems, GO_DOWN_RIGHT);
				collectMoves(queenField, allItems, allOponentItems, GO_DOWN_LEFT);
				collectMoves(queenField, allItems, allOponentItems, GO_UP);
				collectMoves(queenField, allItems, allOponentItems, GO_DOWN);
				collectMoves(queenField, allItems, allOponentItems, GO_LEFT);
				collectMoves(queenField, allItems, allOponentItems, GO_RIGHT);
			});
		}
		
		return movesBuilder.build();
	}
	
	@Override
	public boolean calculateIsKingUnderAtack() {
		final long king = items[INDEX_KING];
		
		checkState(king != 0, "King is not found:\n%s", this);
		
		final int kingIndex = BOARD_INDEX_TO_LONG_INDEX[numberOfTrailingZeros(king)];
		
		// find pown atacks
		if ((POWN_ATACKS[kingIndex] & oponentItems[INDEX_POWN]) != 0) {
			return true;
		}
		
		// find knight atacks
		if ((KNIGHT_ATACKS[kingIndex] & oponentItems[INDEX_KNIGHT]) != 0) {
			return true;
		}
		
		// find king atacks
		if ((KING_ATACKS[kingIndex] & oponentItems[INDEX_KING]) != 0) {
			return true;
		}
		
		// find bishop atacks
		final long oponentBishopsAtacks = oponentItems[INDEX_BISHOP] | oponentItems[INDEX_QUEEN];
		
		if ((oponentBishopsAtacks & BISHOP_ATACKS[kingIndex]) != 0) {
			if (findItemAtacks(king, oponentBishopsAtacks, GO_UP_LEFT) ||
				findItemAtacks(king, oponentBishopsAtacks, GO_UP_RIGHT) ||
				findItemAtacks(king, oponentBishopsAtacks, GO_DOWN_LEFT) ||
				findItemAtacks(king, oponentBishopsAtacks, GO_DOWN_RIGHT)) {
				
				return true;
			}
		}
		
		// find rook atacks
		final long oponentRookAtacks = oponentItems[INDEX_ROOK] | oponentItems[INDEX_QUEEN];
		if ((oponentRookAtacks & ROOK_ATACKS[kingIndex]) != 0) {
			if (findItemAtacks(king, oponentRookAtacks, GO_UP) ||
				findItemAtacks(king, oponentRookAtacks, GO_LEFT) ||
				findItemAtacks(king, oponentRookAtacks, GO_RIGHT) ||
				findItemAtacks(king, oponentRookAtacks, GO_DOWN)) {
				
				return true;
			}
		}
		
		return false;
	}
	
	private boolean findItemAtacks(long field, long itemAtacks, ItemGoesFunctionBit itemGoesFunction) {
		final LongUnaryOperator goFunction = itemGoesFunction.goFunction();
		final LongPredicate stopPredicate = itemGoesFunction.stopPredicate();
		
		while (true) {
			field = goFunction.applyAsLong(field);
			if (stopPredicate.test(field)) {
				return false;
			}
			if ((itemAtacks & field) != 0) {
				return true;
			}	
			if (!isEmpty(field)) {
				return false;
			}
		}
	}
	
	private void collectCastelingMoves(int kingIndex) {
		if (canCastleLeft && ((EMPTY_FOR_CASTELING_LEFT_SHORT & ~empty) == 0L) && (getX(kingIndex) == 3 || (EMPTY_FOR_CASTELING_LEFT_LONG & ~empty) == 0L) && (BOARD_FIELDS[0] & items[INDEX_ROOK]) != 0)
			if (!isKingUnderAtack() && !isKingUnderAtack(moveOf(kingIndex, kingIndex - 1)) && !isKingUnderAtack(moveOf(kingIndex, kingIndex - 2)))	
				movesBuilder.add(specialMoveOf(kingIndex, kingIndex - 2, getSpecialMoveForKingMove(true, true)));
		
		if (canCastleRght && (EMPTY_FOR_CASTELING_RGHT_SHORT & ~empty) == 0L && (getX(kingIndex) == 4 || (EMPTY_FOR_CASTELING_RGHT_LONG & ~empty) == 0L) && (BOARD_FIELDS[7] & items[INDEX_ROOK]) != 0)
			if (!isKingUnderAtack() && !isKingUnderAtack(moveOf(kingIndex, kingIndex + 1)) && !isKingUnderAtack(moveOf(kingIndex, kingIndex + 2)))	
				movesBuilder.add(specialMoveOf(kingIndex, kingIndex + 2, getSpecialMoveForKingMove(true, false)));
	}

	private void collectMoves(long field, long allItems, long allOponentItems, ItemGoesFunctionBit itemGoesFunction) {
		collectMoves(field, allItems, allOponentItems, itemGoesFunction, SPECIAL_MOVE_NO);
	}
	
	private void collectMoves(long field, long allItems, long allOponentItems, ItemGoesFunctionBit itemGoesFunction, int specialMove) {
		final LongUnaryOperator goFunction = itemGoesFunction.goFunction();
		final LongPredicate stopPredicate = itemGoesFunction.stopPredicate();
		final int startIndex = BOARD_INDEX_TO_LONG_INDEX[numberOfTrailingZeros(field)];
		while (true) {
			field = goFunction.applyAsLong(field);
			if (stopPredicate.test(field) || (allItems & field) != 0)
				return;

			final int move = specialMoveOf(startIndex, BOARD_INDEX_TO_LONG_INDEX[numberOfTrailingZeros(field)], specialMove);
			if (!isKingUnderAtack(move))
				movesBuilder.add(move);
		
			if ((allOponentItems & field) != 0)
				return;
		}
	}
	
	@Override
	protected void reverseBoardRepresentation() {
		long tmp;
		for (int i = 0; i < ITEMS_LENGTH; i ++) {
			tmp = reverse(items[i]);
			items[i] = reverse(oponentItems[i]);
			oponentItems[i] = tmp;
		}
		
		empty = reverse(empty);
	}

	private long itemPositions() {
		long result = 0;
		for (long itemPositions : items) {
			result |= itemPositions;
		}
		return result;
	}
	
	private long calculateEmpty() {
		long nonEmpty = 0;
		for (int i = 0; i < ITEMS_LENGTH; i ++) {
			nonEmpty |= items[i] | oponentItems[i];
		}
		return ~nonEmpty;
	}
	
	@Override
	protected BitBoardImpl cloneImpl() {
		return new BitBoardImpl(items, oponentItems, canCastleLeft, canCastleRght, canOponentCastleLeft, canOponentCastleRght);
	}
	
	public String toBinaryString() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("EMPTY\n").append(BitBoardUtils.toBinaryString(empty)).append("\n");
		
		for (int i = 0; i < ITEMS_LENGTH; i ++) {
			final long itemsPositions = items[i];
			if (itemsPositions != 0) {
				sb.append(Item.withValue((byte) (i + 1)).toString() + "S:\n" + BitBoardUtils.toBinaryString(itemsPositions) + "\n\n");
			}
		}
		
		for (int i = 0; i < ITEMS_LENGTH; i ++) {
			final long oponentItemsPositions = oponentItems[i];
			if (oponentItemsPositions != 0) {
				sb.append("OPONENT " + Item.withValue((byte) (i + 1)) + "S:\n" + BitBoardUtils.toBinaryString(oponentItemsPositions) + "\n\n");
			}
		}
		
		return sb.toString();
	}
	
	@Deprecated
	public void checkSum() {
		long allFields = 0;
		final long[] all = new long[2 * ITEMS_LENGTH + 1];
		System.arraycopy(items, 0, all, 0, ITEMS_LENGTH);
		System.arraycopy(oponentItems, 0, all, ITEMS_LENGTH, ITEMS_LENGTH);
		all[2 * ITEMS_LENGTH] = empty;
		
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
	public long items(byte itemType) {
		return items[itemType - 1];
	}

	@Override
	public long oponentItems(byte itemType) {
		return oponentItems[itemType - 1];
	}
}
