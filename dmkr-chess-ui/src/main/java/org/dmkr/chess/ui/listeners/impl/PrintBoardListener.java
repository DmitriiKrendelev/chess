package org.dmkr.chess.ui.listeners.impl;

import com.google.inject.name.Named;
import org.dmkr.chess.api.Board;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;

import com.google.inject.Inject;
import org.dmkr.chess.ui.listeners.AbstractPressAndTypedListener;

import static java.awt.event.KeyEvent.VK_P;
import static org.dmkr.chess.engine.function.EvaluationFunctionUtils.getEvaluationDetails;

import java.util.Optional;

public class PrintBoardListener extends AbstractPressAndTypedListener {
	private final Board board;
	private final AsyncEngine<BoardEngine> engine;
	
	@Inject
	private PrintBoardListener(BoardEngine board, @Named("engine1") AsyncEngine<BoardEngine> engine) {
		super(CTRL, VK_P, null, null);
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
