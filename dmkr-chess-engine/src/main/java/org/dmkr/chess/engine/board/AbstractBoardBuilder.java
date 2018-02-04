package org.dmkr.chess.engine.board;

import static com.google.common.base.Preconditions.checkArgument;
import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.api.model.Item.NO_ITEM;

import java.util.function.Supplier;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Color;
import org.dmkr.chess.api.model.Field;
import org.dmkr.chess.api.model.Item;

import lombok.NonNull;

public abstract class AbstractBoardBuilder {
	protected final byte[] board = new byte[SIZE * SIZE];
	protected boolean canCastleLeft;
	protected boolean canCastleRght;
	protected boolean canOponentCastleLeft;
	protected boolean canOponentCastleRght;
	
	protected static <T extends AbstractBoardBuilder> T of(@NonNull Supplier<T> creator, @NonNull String ... yLines) {
		checkArgument(yLines.length == SIZE);
		
		final T builder = creator.get();
		for (int y = 0; y < SIZE; y ++) {
			final char[] yLineChars = yLines[y].toCharArray();
			
			int x = 0;
			for (int i = 0; i < yLineChars.length; i ++) {
				final char c = yLineChars[i];
				if (c == ' ' )
					continue;
				
				if (c != NO_ITEM) {
					final Item item = Item.withShortName(c);
					final Color color = Color.ofItem(c);
					final Field field = Field.resolve(x, SIZE - 1 - y);
					
					((AbstractBoardBuilder) builder).board[field.index()] = color.item(item);
				}
				
				x ++;
			}
		}
		
		return builder;
	}
	
	public AbstractBoardBuilder canCastleLeft() {
		canCastleLeft = true;
		return this;
	}

	public AbstractBoardBuilder canCastleRght() {
		canCastleRght = true;
		return this;
	}
	
	public AbstractBoardBuilder canOponentCastleLeft() {
		canOponentCastleLeft = true;
		return this;
	}
	
	public AbstractBoardBuilder canOponentCastleRght() {
		canOponentCastleRght = true;
		return this;
	}
	
	public abstract BoardEngine build();
	
	protected static <T extends AbstractBoardBuilder> BoardEngine newInitialPositionBoard(Supplier<T> builderCreator) {
		return AbstractBoardBuilder.of(builderCreator,
					"r n b q k b n r", 
					"p p p p p p p p", 		
					"- - - - - - - -", 		
					"- - - - - - - -", 		
					"- - - - - - - -", 		
					"- - - - - - - -",
					"P P P P P P P P", 		
					"R N B Q K B N R")
					.canCastleLeft()
					.canCastleRght()
					.canOponentCastleLeft()
					.canOponentCastleRght()
					.build();
	}
}