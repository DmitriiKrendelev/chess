package org.dmkr.chess.engine.board;

import java.util.Optional;
import java.util.function.Supplier;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.board.bit.BitBoardBuilder;
import org.dmkr.chess.engine.board.impl.BoardBuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BoardFactory {
	
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	@Getter
	private static enum BoardImplType {
		BIT(BitBoard.class, BitBoardBuilder::board),
		IMPL(BoardEngine.class, BoardBuilder::board);

		private final Class<? extends BoardEngine> boardType;
		private final Supplier<AbstractBoardBuilder> boardCreator;

	}
	
	private static final String BOARD_IMPL_TYPE_PROPERTY = "dmkr-chess.board-type";
	private static final BoardImplType DEFAULT_BOARD_IMPL_TYPE = BoardImplType.BIT;
	
	private static BoardImplType getBoardImplType() {
		return Optional.ofNullable(System.getProperty(BOARD_IMPL_TYPE_PROPERTY))
				.map(String::toUpperCase)
				.map(BoardImplType::valueOf)
				.orElse(DEFAULT_BOARD_IMPL_TYPE);
	}
	
	public static Supplier<? extends AbstractBoardBuilder> getBoardBuilder() {
		return getBoardImplType().boardCreator();
	}
	
	public static Class<? extends BoardEngine> getBoardType() {
		return getBoardImplType().boardType();
	}
	
	public static AbstractBoardBuilder of(@NonNull String ... yLines) {
		return AbstractBoardBuilder.of(getBoardBuilder(), yLines);
	}
	
	public static BoardEngine newInitialPositionBoard() {
		return AbstractBoardBuilder.newInitialPositionBoard(getBoardBuilder());
	}

	static {
		System.out.println("Initialized BoardType: " + getBoardImplType());
	}
	
}
