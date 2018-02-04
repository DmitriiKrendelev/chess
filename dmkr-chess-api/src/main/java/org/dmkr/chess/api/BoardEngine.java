package org.dmkr.chess.api;

import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.utils.BoardUtils.index;
import static org.dmkr.chess.api.utils.BoardUtils.invertCoord;
import static org.dmkr.chess.api.utils.BoardUtils.invertIndex;
import static org.dmkr.chess.api.utils.MoveUtils.toMovesHistory;
import static org.dmkr.chess.api.utils.MoveUtils.toSet;
import static org.dmkr.chess.common.primitives.Bytes.byte1;
import static org.dmkr.chess.common.primitives.Bytes.byte2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.IntPredicate;

import org.dmkr.chess.api.model.Color;
import org.dmkr.chess.api.model.ColoredItem;
import org.dmkr.chess.api.model.Field;
import org.dmkr.chess.api.model.Item;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.api.utils.MoveUtils;
import org.dmkr.chess.common.cache.CacheReset;
import org.dmkr.chess.common.cache.CacheValue;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import static org.dmkr.chess.common.cache.CachableCreator.*;


public interface BoardEngine extends Board {
	byte at(int index);
	
	int[] calculateAllowedMoves(MovesSelector movesSelector);
	
	int[] calculateAllowedMovesOponent(MovesSelector movesSelector);
	
	int[] allowedMoves();
	
	int[] allowedMoves(IntPredicate moveSelector);

	int[] allowedMovesOponent();
	
	int[] movesHistory();
	
	int moveNumber();
	
	boolean isInverted();
	
	BoardEngine clone();
	
	boolean isKingUnderAtack();
	
	boolean isKingUnderAtackAfterMove(int move);
	
	boolean calculateIsKingUnderAtack();
	
	void applyMove(int move);
	
	void rollbackMove();
	
	BoardEngine invert();
	
	default BoardEngine useCache() {
		return createCachableProxy(this);
	}
	
	@Override
	@CacheReset
	default void applyMove(Move move) {
		applyMove(valueOf(move));
		invert();
	}
		  
	@Override
	@CacheReset
	default void rollback() {
		invert();
		rollbackMove();
	}					  
 
	default byte at(int x, int y) {
		return at(index(x, y));
	}

	@Override
	default ColoredItem at(Field field) {
		final byte item = at(invertIndex(field.index(), isInverted()));
		return ColoredItem.of(Color.ofItem(item, isInverted()), Item.withValue(item));
	}
	
	@Override
	default void forEach(BiPredicate<Field, ColoredItem> filter, BiConsumer<Field, ColoredItem> consumer) {
		final boolean isInverted = isInverted();
		
		for (int x = 0; x < SIZE; x ++) {
			for (int y = 0; y < SIZE; y ++) {
				final Field field = Field.resolve(invertCoord(x, isInverted), invertCoord(y, isInverted));
				final ColoredItem coloredItem = at(field);
				
				if (filter == null || filter.test(field, coloredItem)) {
					consumer.accept(field, at(field));
				}
			}
		}
	}
	
	@Override
	default void forEach(BiConsumer<Field, ColoredItem> consumer) {
		forEach(null, consumer);
	}
	
	@CacheValue	
	@Override
	default Set<Move> getAllowedMoves() {
		return toSet(allowedMoves(), isInverted());
	}
	
	@CacheValue	
	@Override
	default List<Move> getMovesHistory() {
		return toMovesHistory(movesHistory(), isInverted());
	}
	
	@CacheValue			
	@Override
	default Map<Field, Set<Field>> getAllowedMovesFields() {
		final int[] allowedMoves = allowedMoves();
		final Map<Field, Set<Field>> allowedMoveFields = new HashMap<>(); 
		
		final boolean isInverted = isInverted();
		for (int move : allowedMoves) {
			final Field from = Field.resolve(byte1(move), isInverted);
			final Field to = Field.resolve(byte2(move), isInverted);
			
			allowedMoveFields.computeIfAbsent(from, fromKey -> new HashSet<>()).add(to);
		}
		
		final ImmutableMap.Builder<Field, Set<Field>> resultBuilder = ImmutableMap.builder();
		allowedMoveFields.forEach((from, to) -> resultBuilder.put(from, ImmutableSet.copyOf(to))); 
		return resultBuilder.build();
	}
	
	default boolean isCheckmate() {
		return isKingUnderAtack() && allowedMoves().length == 0;
	}
	
	default boolean isStalemate() {
		return !isKingUnderAtack() && allowedMoves().length == 0;
	}

	default int valueOf(Move move) {
		return MoveUtils.valueOf(move, this);
	}
}
