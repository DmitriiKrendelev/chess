package org.dmkr.chess.engine.benchmarks.data;

import lombok.experimental.UtilityClass;
import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.board.bit.BitBoardBuilder;
import org.dmkr.chess.engine.board.impl.BoardBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@UtilityClass
public class PositionsProvider {
    private static final int NUMBER_OF_POSITIONS = 64;
    private static final int MASK = NUMBER_OF_POSITIONS - 1;

    private static final Random RANDOM = new Random(0L);

    private static final List<BoardEngine> GENERATED_BOARD_POSITIONS;
    private static final List<BitBoard> GENERATED_BIDBOARD_POSITIONS;

    private static final BoardEngine A_BOARD;
    private static final BitBoard A_BIT_BOARD;

    private long l = 0L;

    static {
        final List<BoardEngine> generatedBoardList = new ArrayList<>();
        final List<BitBoard> generatedBitBoardList = new ArrayList<>();

        final BoardEngine boardClone = BoardBuilder.newInitialPositionBoard();
        final BitBoard bitBoardClone = BitBoardBuilder.newInitialPositionBoard();

        for (int i = 0; i < NUMBER_OF_POSITIONS; i ++) {
            final Set<Move> boardMoves = boardClone.getAllowedMoves();

            if (boardMoves.isEmpty()) {
                throw new IllegalStateException("No moves for:\n" + boardClone);
            }

            final int randomNum = RANDOM.nextInt(boardMoves.size());
            final Move randomMove = boardMoves.stream().skip(randomNum).findFirst().get();

            boardClone.applyMove(randomMove);
            bitBoardClone.applyMove(randomMove);

            generatedBoardList.add(boardClone.clone());
            generatedBitBoardList.add((BitBoard) bitBoardClone.clone());
        }

        GENERATED_BOARD_POSITIONS = generatedBoardList;
        GENERATED_BIDBOARD_POSITIONS = generatedBitBoardList;

        final int anInt = RANDOM.nextInt();
        A_BOARD = GENERATED_BOARD_POSITIONS.get(anInt & MASK);
        A_BIT_BOARD = GENERATED_BIDBOARD_POSITIONS.get(anInt & MASK);
    }

    public static BoardEngine getNextBoardPosition()  {
        return GENERATED_BOARD_POSITIONS.get((int) ((++ l) & MASK));
    }

    public static BitBoard getNextBitBoardPosition()  {
        return GENERATED_BIDBOARD_POSITIONS.get((int) ((++ l) & MASK));
    }

    public static BoardEngine aBoard()  {
        return A_BOARD.clone();
    }

    public static BitBoard aBitBoard()  {
        return (BitBoard) A_BIT_BOARD.clone();
    }

}
