package org.dmkr.chess.engine.minimax;

import static org.dmkr.chess.api.model.Constants.VALUE_PAWN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.function.PieceValuesProvider;

public abstract class FindMoveAbstractTest<T extends BoardEngine> {
	protected static final int CHACKMATE_BARIER_VALUE = 100 * PieceValuesProvider.valueOf(VALUE_PAWN);
	protected static final Move ANY = new Move();

	protected abstract AsyncEngine<T> getEngine();
	
	protected void findMove(T board, Move ... expectedBestLine) {
		final AsyncEngine<T> engine = getEngine();
		
		engine.run(board);
		final Move bestMove = engine.getBestMove();
		final BestLine bestLine = engine.getBestLine();
		
		System.out.println();
		System.out.println(board);
		System.out.println(engine);
		System.out.println(bestLine);

		final Iterator<Move> bestLineMoves = bestLine.getMoves().iterator();
		for (Move expectedMove : expectedBestLine) {
			final Move bestLineMove = bestLineMoves.next();
			if (expectedMove == ANY) {
				assertNotNull(bestLineMove);
			} else {
				assertEquals(engine.getCurrentEvaluation() + "\n\n", expectedMove, bestLineMove);
			}
		}
		
		checkMove(board.clone(), bestMove, engine);
		checkBestLine(board.clone(), bestLine, engine);
	}
	
	protected void checkMove(BoardEngine board, Move move, AsyncEngine<T> engine) {
		
	}
	
	protected void checkBestLine(BoardEngine board, BestLine move, AsyncEngine<T> engine) {
		
	}
	
	protected void testCheckmateMove(T board, Move move, AsyncEngine<T> engine) {
		board.applyMove(move);
		assertTrue("Is not checkmate :\n" + board, board.isCheckmate());
		engine.run(board);
		assertNull(engine.getBestLine());
		assertNull(engine.getBestMove());
		assertTrue(engine.getCurrentEvaluation().isEmpty());
	}
	
	protected void testCheckmateBestLine(T board, BestLine bestLine, AsyncEngine<T> engine) {
		assertTrue(bestLine.getLineValue() > CHACKMATE_BARIER_VALUE);
		
		bestLine.getMoves().forEach(board::applyMove);
		
		assertTrue(board.isCheckmate());
	}	
}
