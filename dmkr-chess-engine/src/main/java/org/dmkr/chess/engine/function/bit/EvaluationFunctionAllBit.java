package org.dmkr.chess.engine.function.bit;

import org.apache.commons.lang3.ArrayUtils;
import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.utils.PieceGoesFunctionsBit;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.Functions;
import org.dmkr.chess.engine.function.common.EvaluationFunctionMovesAbstract;

import java.util.Map;
import java.util.function.Function;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;

import static java.lang.Long.bitCount;
import static java.lang.Long.numberOfTrailingZeros;
import static org.dmkr.chess.api.model.Constants.*;
import static org.dmkr.chess.api.model.Constants.VALUE_QUEEN;
import static org.dmkr.chess.api.model.Constants.VALUE_ROOK;
import static org.dmkr.chess.api.utils.BitBoardMasks.*;
import static org.dmkr.chess.api.utils.BitBoardMasks.BOARD_FIELDS_INVERTED;
import static org.dmkr.chess.api.utils.BitBoardUtils.bitCountOfZeroble;
import static org.dmkr.chess.api.utils.PieceGoesFunctionsBit.PieceGoesFunctionBit.*;
import static org.dmkr.chess.api.utils.PieceGoesFunctionsBit.PieceGoesFunctionBit.GO_RIGHT;
import static org.dmkr.chess.engine.function.PiecePositionValuesProvider.positionValues;
import static org.dmkr.chess.engine.function.PieceValuesProvider.valueOf;
import static org.dmkr.chess.engine.function.common.EvaluationFunctionPawnStructureAbstract.*;
import static org.dmkr.chess.engine.function.bit.EvaluationFunctionQueenInTheCenterBit.calculateQueenInTheCenterTooEarlyPenalty;
import static org.dmkr.chess.engine.function.bit.EvaluationFunctionRooksBit.calculateValuesOfRooksOnOpenFiles;

public class EvaluationFunctionAllBit extends EvaluationFunctionMovesAbstract<BitBoard> {
    public static final EvaluationFunction<BitBoard> INSTANCE = new EvaluationFunctionAllBit();

    private static final PieceGoesFunctionsBit.PieceGoesFunctionBit[] BISHOP_GOES_FUNCTIONS = new PieceGoesFunctionsBit.PieceGoesFunctionBit[]{GO_UP_LEFT, GO_UP_RIGHT, GO_DOWN_LEFT, GO_DOWN_RIGHT};
    private static final PieceGoesFunctionsBit.PieceGoesFunctionBit[] ROOK_GOES_FUNCTIONS = new PieceGoesFunctionsBit.PieceGoesFunctionBit[]{GO_UP, GO_DOWN, GO_LEFT, GO_RIGHT};
    private static final PieceGoesFunctionsBit.PieceGoesFunctionBit[] QUEEN_GOES_FUNCTIONS = ArrayUtils.addAll(BISHOP_GOES_FUNCTIONS, ROOK_GOES_FUNCTIONS);

    private static final Function<BitBoard, Map<String, Integer>> DETAILS_PROVIDER = Functions.getDefaultEvaluationFunction(BitBoard.class)::getEvaluationDetails;

    @Override
    public int calculateOneSidedValue(BitBoard board) {
        long valuableAtacksField = 0;
        int valueOfNumberOfMoves = 0;

        final long pawns = board.pieces(VALUE_PAWN);
        final long oponentPowns = board.oponentPieces(VALUE_PAWN);
        final long oponentKing = board.oponentPieces(VALUE_KING);
        final long oponentKingAndQueen = oponentKing | board.oponentPieces(VALUE_QUEEN);
        final long oponentHeavyPieces = oponentKingAndQueen | board.oponentPieces(VALUE_ROOK);
        final long oponentValuablePieces = oponentHeavyPieces | board.oponentPieces(VALUE_KNIGHT) | board.oponentPieces(VALUE_BISHOP);
        final long oponentPieces = oponentValuablePieces | oponentPowns;
        final long emptyAndOponentPositions = oponentPieces | board.emptyPositions();

        // pawns
        final long pawnAtacksLeft = (pawns & NOT_A) << (SIZE + 1);
        final long pawnAtacksRght = (pawns & NOT_H) << (SIZE - 1);

        final long pawnChainsLeft = pawnAtacksLeft & pawns;
        final long pawnChainsRght = pawnAtacksRght & pawns;
        int valueOfPawnStructure = 0;
        valueOfPawnStructure += valueOfPownChains(pawnChainsLeft, pawnChainsRght);

        final byte[] numPawnsOnFiles = new byte[SIZE];
        for (int i = 0; i < SIZE; i ++) {
            numPawnsOnFiles[i] = (byte) bitCountOfZeroble(pawns & FILES[i]);
        }

        valueOfPawnStructure += isolatedAndDoubledPawns(numPawnsOnFiles);
        valuableAtacksField |= (pawnAtacksLeft | pawnAtacksRght) & oponentValuablePieces;

        // knights
        long knights = board.pieces(VALUE_KNIGHT);
        while (knights != 0L) {
            final int knightBitIndex = numberOfTrailingZeros(knights);
            final int knightIndex = BOARD_INDEX_TO_LONG_INDEX[knightBitIndex];
            final long knightGoesPositions = KNIGHT_ATACKS[knightIndex] & emptyAndOponentPositions;

            valuableAtacksField |= knightGoesPositions & oponentHeavyPieces;
            valueOfNumberOfMoves += bitCount(knightGoesPositions & emptyAndOponentPositions) * LIGHT_PIECE_MOVE_VALUE;

            knights &= BOARD_FIELDS_INVERTED[knightIndex];
        }

        // bishops
        long bishops = board.pieces(VALUE_BISHOP);
        while (bishops != 0L) {
            final int bishopBitIndex = numberOfTrailingZeros(bishops);
            final int bishopIndex = BOARD_INDEX_TO_LONG_INDEX[bishopBitIndex];
            final long bishopField = BOARD_FIELDS[bishopIndex];

            for (PieceGoesFunctionsBit.PieceGoesFunctionBit bishopGoesFunction : BISHOP_GOES_FUNCTIONS) {
                final LongUnaryOperator goFunction = bishopGoesFunction.goFunction();
                final LongPredicate stopPredicate = bishopGoesFunction.stopPredicate();

                long currentBishopField = bishopField;
                while (true) {
                    currentBishopField = goFunction.applyAsLong(currentBishopField);
                    if (stopPredicate.test(currentBishopField)) {
                        // go out of board
                        break;
                    } else if ((emptyAndOponentPositions & currentBishopField) == 0) {
                        // my piece
                        break;
                    } else if ((oponentPieces & currentBishopField) != 0) {
                        // oponent piece
                        valueOfNumberOfMoves += LIGHT_PIECE_MOVE_VALUE;
                        if ((currentBishopField & oponentHeavyPieces) != 0) {
                            valuableAtacksField |= currentBishopField;
                        }
                        break;
                    } else {
                        // empty
                        valueOfNumberOfMoves += LIGHT_PIECE_MOVE_VALUE;
                    }
                }
            }

            bishops &= BOARD_FIELDS_INVERTED[bishopIndex];
        }

        // rooks
        long rooks = board.pieces(VALUE_ROOK);
        while (rooks != 0L) {
            final int rookBitIndex = numberOfTrailingZeros(rooks);
            final int rookIndex = BOARD_INDEX_TO_LONG_INDEX[rookBitIndex];
            final long rookField = BOARD_FIELDS[rookIndex];

            for (PieceGoesFunctionsBit.PieceGoesFunctionBit rookGoesFunction : ROOK_GOES_FUNCTIONS) {
                final LongUnaryOperator goFunction = rookGoesFunction.goFunction();
                final LongPredicate stopPredicate = rookGoesFunction.stopPredicate();

                long currentRookPosition = rookField;
                while (true) {
                    currentRookPosition = goFunction.applyAsLong(currentRookPosition);
                    if (stopPredicate.test(currentRookPosition)) {
                        // go out of board
                        break;
                    } else if ((emptyAndOponentPositions & currentRookPosition) == 0) {
                        // my piece
                        break;
                    } else if ((oponentPieces & currentRookPosition) != 0) {
                        // oponent piece
                        valueOfNumberOfMoves += ROOK_MOVE_VALUE;
                        if ((currentRookPosition & oponentKingAndQueen) != 0) {
                            valuableAtacksField |= currentRookPosition;
                        }
                        break;
                    } else {
                        // empty
                        valueOfNumberOfMoves += ROOK_MOVE_VALUE;
                    }
                }
            }
            rooks &= BOARD_FIELDS_INVERTED[rookIndex];
        }


        // queens
        long queens = board.pieces(VALUE_QUEEN);
        while (queens != 0L) {
            final int queenBitIndex = numberOfTrailingZeros(queens);
            final int queenIndex = BOARD_INDEX_TO_LONG_INDEX[queenBitIndex];
            final long queenField = BOARD_FIELDS[queenIndex];

            for (PieceGoesFunctionsBit.PieceGoesFunctionBit queenGoesFunction : QUEEN_GOES_FUNCTIONS) {
                final LongUnaryOperator goFunction = queenGoesFunction.goFunction();
                final LongPredicate stopPredicate = queenGoesFunction.stopPredicate();

                long currentQueenPosition = queenField;
                while (true) {
                    currentQueenPosition = goFunction.applyAsLong(currentQueenPosition);
                    if (stopPredicate.test(currentQueenPosition)) {
                        // go out of board
                        break;
                    } else if ((emptyAndOponentPositions & currentQueenPosition) == 0) {
                        // my piece
                        break;
                    } else if ((oponentPieces & currentQueenPosition) != 0) {
                        // oponent piece
                        valueOfNumberOfMoves += QUEEN_MOVE_VALUE;
                        if ((currentQueenPosition & oponentKing) != 0) {
                            valuableAtacksField |= currentQueenPosition;
                        }
                        break;
                    } else {
                        valueOfNumberOfMoves += QUEEN_MOVE_VALUE;
                    }
                }
            }
            queens &= BOARD_FIELDS_INVERTED[queenIndex];
        }

        // value of item positions
        int valueOfItemPositions = 0;
        for (int i = 0; i < NUMBER_OF_PIECES; i ++) {
            final byte pieceType = (byte) (i + 1);
            final int valueOfPieceType = valueOf(pieceType);

            long pieces = board.pieces(pieceType);
            while (pieces != 0L) {
                final int piecesLastBit = numberOfTrailingZeros(pieces);
                final int piecesBoardIndex = BOARD_INDEX_TO_LONG_INDEX[piecesLastBit];
                valueOfItemPositions += positionValues(pieceType)[piecesBoardIndex];
                valueOfItemPositions += valueOfPieceType;

                pieces &= BOARD_FIELDS_INVERTED[piecesBoardIndex];
            }
        }

        return
                valueOfPawnStructure
                + valueOfNumberAtacks(valuableAtacksField)
                + valueOfNumberOfMoves
                + valueOfItemPositions
                + calculateQueenInTheCenterTooEarlyPenalty(board)
                + calculateValuesOfRooksOnOpenFiles(board);
    }

    @Override
    public Map<String, Integer> getEvaluationDetails(BitBoard board) {
        return DETAILS_PROVIDER.apply(board);
    }

    @Override
    public String toString() {
        return "All Bit";
    }
}