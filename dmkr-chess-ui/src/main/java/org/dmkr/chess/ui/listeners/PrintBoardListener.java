package org.dmkr.chess.ui.listeners;

import org.dmkr.chess.api.Board;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;

import com.google.inject.Inject;

import static org.dmkr.chess.engine.function.EvaluationFunctionUtils.getEvaluationDetails;

import java.util.Optional;

public class PrintBoardListener extends AbstractPressAndTypedListener {
	private static final int B = 66;
	private final Board board;
	private final AsyncEngine<BoardEngine> engine;
	
	@Inject
	private PrintBoardListener(BoardEngine board, AsyncEngine<BoardEngine> engine) {
		super(CTRL, B);
		this.board = board;
		this.engine = engine;
	}

	@Override
	public void run() {
		System.out.println(board);
		final BoardEngine boardClone = ((BoardEngine) board).clone();
		boardClone.invert();
		boardClone.rollbackMove();
		
		Optional.ofNullable(engine.getCurrentEvaluation()).ifPresent(
				eval -> eval.forEach(
						bestLine -> {
							System.out.println(bestLine);
							System.out.println(getEvaluationDetails(engine.getEvaluationFunction(), boardClone, bestLine));
							System.out.println();
						}
				)
		);
	}

}
