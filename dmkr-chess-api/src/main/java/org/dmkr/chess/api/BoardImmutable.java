package org.dmkr.chess.api;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import org.dmkr.chess.api.model.ColoredItem;
import org.dmkr.chess.api.model.Field;
import org.dmkr.chess.api.model.Move;

public interface BoardImmutable {
	
	ColoredItem at(Field field);
	
	boolean canCastleLeft();

	boolean canCastleRght();
	
	boolean canOponentCastleLeft();

	boolean canOponentCastleRght();
	
	void forEach(BiPredicate<Field, ColoredItem> filter, BiConsumer<Field, ColoredItem> consumer);
	
	void forEach(BiConsumer<Field, ColoredItem> consumer);
	
	Set<Move> getAllowedMoves();
	
	Map<Field, Set<Field>> getAllowedMovesFields();
	
	List<Move> getMovesHistory();
	
	boolean isInverted();
}
