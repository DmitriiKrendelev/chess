package org.dmkr.chess.engine.functions;

import java.util.Set;

import org.dmkr.chess.api.BitBoard;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.board.bit.BitBoardBuilder;
import org.dmkr.chess.engine.board.impl.BoardBuilder;
import org.dmkr.chess.engine.function.Functions;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EvaluationFunctionReconciliationTest {
	
	@Test
	public void test() {
		for (int gameNum = 0; gameNum < 100; gameNum ++) {
			
			final BoardEngine board = BoardBuilder.newInitialPositionBoard();
			final BitBoard bitBoard = BitBoardBuilder.newInitialPositionBoard();
			
			for (int i = 0; i < 100; i ++) {
				final Set<Move> boardMoves = board.getAllowedMoves();
				final Set<Move> bitBoardMoves = bitBoard.getAllowedMoves();
				
				assertEquals("Moves are not equal for:\n" + board, boardMoves, bitBoardMoves);
				if (boardMoves.isEmpty()) {
					continue;
				}
				 
				final int randomNum = (int) (Math.random() * (boardMoves.size() - 1)); 
				final Move randomMove = boardMoves.stream().skip(randomNum).findFirst().get();
				
				board.applyMove(randomMove);
				bitBoard.applyMove(randomMove);
		
	//			System.out.println("\n### " + (i + 1) + ".");
	//			System.out.println("Board:\n" + board);
	//			System.out.println("BitBoard:\n" + bitBoard);
				
				for (Functions func : Functions.values()) {
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
				
			}
		
		}
	}
	
}
