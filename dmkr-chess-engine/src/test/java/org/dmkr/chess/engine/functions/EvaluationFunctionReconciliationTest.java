package org.dmkr.chess.engine.functions;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;
import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.Board;
import org.dmkr.chess.api.BoardEngine;
import static org.dmkr.chess.api.model.Constants.*;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.board.bit.BitBoardBuilder;
import org.dmkr.chess.engine.board.bit.BitBoardImpl;
import org.dmkr.chess.engine.board.impl.BoardBuilder;
import org.dmkr.chess.engine.function.EvaluationFunction;
import org.dmkr.chess.engine.function.Functions;
import org.dmkr.chess.engine.function.bit.EvaluationFunctionAllBit;
import org.junit.Assert;
import org.junit.Test;

import static org.dmkr.chess.engine.function.Functions.*;
import static org.junit.Assert.assertEquals;

public class EvaluationFunctionReconciliationTest {

	private final Random random = new Random(getTodayLong());

	private static long getTodayLong() {
		final long todayLong = LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.UTC);
		System.out.println("Today's Long = " + todayLong);
		return todayLong;
	}

	@Test
	public void test() throws Exception {
		for (int gameNum = 0; gameNum < 100; gameNum ++) {
			
			final BoardEngine board = BoardBuilder.newInitialPositionBoard();
			final BitBoard bitBoard = BitBoardBuilder.newInitialPositionBoard();
			
			for (int i = 0; i < 100; i ++) {
				final Set<Move> boardMoves = board.getAllowedMoves();
				final Set<Move> bitBoardMoves = bitBoard.getAllowedMoves();
				final Set<Move> diff1 = Sets.difference(bitBoardMoves, boardMoves);
				if (!diff1.isEmpty()) {
					System.out.println("BitMoves - Moves = " + diff1);
				}

				final Set<Move> diff2 = Sets.difference(boardMoves, bitBoardMoves);
				if (!diff2.isEmpty()) {
					System.out.println("Moves - BitMoves = " + diff2);
				}

				assertEquals("Moves are not equal for:\n" + board + "\n" + bitBoard, boardMoves, bitBoardMoves);
				if (boardMoves.isEmpty()) {
					continue;
				}

				final int randomNum = random.nextInt(boardMoves.size());
				final Move randomMove = boardMoves.stream().skip(randomNum).findFirst().get();
				
				board.applyMove(randomMove);
				bitBoard.applyMove(randomMove);

				checkSerialization(board);
				checkSerialization(bitBoard);

				checkEquals(board, bitBoard);
				((BitBoardImpl) bitBoard).checkSum();

	//			System.out.println("\n### " + (i + 1) + ".");
	//			System.out.println("Board:\n" + board);
	//			System.out.println("BitBoard:\n" + bitBoard);
				
				for (Functions func : enabledValues()) {
					final int bitValue = func.getFunction(BitBoard.class).value(bitBoard);
					final int value = func.getFunction(BoardEngine.class).value(board);

	//				System.out.println(func + " Bit  : " + bitValue);
	//				System.out.println(func + " Impl : " + value);

					assertEquals("Evaluation is not equal for:\n" + func +
							"\n" + board +
							"\nBit: " + bitValue +
							"\nImpl:" + value +
							"\n",
							value, bitValue);
				}

				final EvaluationFunction<BitBoard> compositeFunctionBit = getDefaultEvaluationFunction(BitBoard.class);
				final EvaluationFunction<BitBoard> functionAllBit = EvaluationFunctionAllBit.INSTANCE;
				final int bitValue = compositeFunctionBit.value(bitBoard);
				final int bitAllValue = functionAllBit.value(bitBoard);


				assertEquals("Evaluation is not equal for Bit and BitAll:\n" +
								"\n" + board +
								"\nBit: " + bitValue +
								"\nBitAll:" + bitAllValue +
								"\n",
						bitValue, bitAllValue);
			}
		}
	}

	private static void checkEquals(BoardEngine board1, BoardEngine board2) {
		final String message = board1 + "\n" + board2 + "\n";
		for (int i = 0; i < SIZE * SIZE; i ++) {
			Assert.assertEquals(message + " at index " + i, board1.at(i), board2.at(i));
			Assert.assertEquals(message + " at index " + i, board1.isEmpty(i), board2.isEmpty(i));
		}

		Assert.assertEquals(message, board1.canCastleLeft(), board2.canCastleLeft());
		Assert.assertEquals(message, board1.canCastleRght(), board2.canCastleRght());
		Assert.assertEquals(message, board1.canOponentCastleLeft(), board2.canOponentCastleLeft());
		Assert.assertEquals(message, board1.canOponentCastleRght(), board2.canOponentCastleRght());
	}

	private static void checkSerialization(Board board) throws IOException, ClassNotFoundException {
		final byte[] serialized;
		try (
				final ByteArrayOutputStream baos = new ByteArrayOutputStream();
				final ObjectOutputStream oos = new ObjectOutputStream(baos)
		) {
			oos.writeObject(board);
			serialized = baos.toByteArray();
		}

		final Board deserialized;
		try (
				final ByteArrayInputStream bios = new ByteArrayInputStream(serialized);
				final ObjectInputStream ois = new ObjectInputStream(bios)
		) {
			deserialized = (Board) ois.readObject();
		}

		if (!board.equals(deserialized)) {
			Assert.fail("Before serialization:\n" + board + "\n\nAfter deserialization:\n" + deserialized + "\n");
		}
	}
	
}
