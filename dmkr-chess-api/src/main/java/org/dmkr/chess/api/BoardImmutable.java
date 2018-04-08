package org.dmkr.chess.api;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import org.dmkr.chess.api.model.ColoredPiece;
import org.dmkr.chess.api.model.Field;
import org.dmkr.chess.api.model.Move;

public interface BoardImmutable {
	
	ColoredPiece at(Field field);
	
	boolean canCastleLeft();

	boolean canCastleRght();
	
	boolean canOponentCastleLeft();

	boolean canOponentCastleRght();
	
	void forEach(BiPredicate<Field, ColoredPiece> filter, BiConsumer<Field, ColoredPiece> consumer);
	
	void forEach(BiConsumer<Field, ColoredPiece> consumer);
	
	Set<Move> getAllowedMoves();
	
	Map<Field, Set<Field>> getAllowedMovesFields();
	
	List<Move> getMovesHistory();
	
	boolean isInverted();
}
