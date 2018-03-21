package org.dmkr.chess.engine.board.impl;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.board.AbstractBoardBuilder;

import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(staticName = "board")
public class BoardBuilder extends AbstractBoardBuilder {

	@Override
	public BoardEngine build() {
		return new BoardImpl(board.clone(),
				canCastleLeft,
				canCastleRght,
				canOponentCastleLeft,
				canOponentCastleRght,
				inverted);
	}
	
	public static AbstractBoardBuilder of(@NonNull String ... yLines) {
		return of(BoardBuilder::new, yLines);
	}
	
	public static BoardEngine newInitialPositionBoard() {
		return newInitialPositionBoard(BoardBuilder::new);
	}
}
