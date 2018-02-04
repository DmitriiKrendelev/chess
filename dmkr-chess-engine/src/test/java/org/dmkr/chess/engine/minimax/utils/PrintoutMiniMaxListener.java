package org.dmkr.chess.engine.minimax.utils;

import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.minimax.MiniMaxFilteringListener;
import static org.apache.commons.lang3.StringUtils.repeat;

import java.util.stream.Stream;

public class PrintoutMiniMaxListener extends MiniMaxFilteringListener {

	public PrintoutMiniMaxListener(Move... filterMoves) {
		super(filterMoves);
	}
	
	public PrintoutMiniMaxListener(String... filterMoves) {
		super(Stream.of(filterMoves).map(Move::moveOf).toArray(Move[]::new));
	}

	@Override
	protected void onMoveFiltered(Move move) {
		final String shift = shift();
		
		if (down) {
			System.out.println("{");
		} 
		
		System.out.print(shift + "\"" + move + "\" : ");
	}

	@Override
	protected void onEvaluationFiltered(int moveValue) {
		final String shift = shift();
		
		if (!down) {
			System.out.println(shift + "}\n" + shift + "\"value\" : " + (-moveValue) + "\n");
		} else {
			System.out.println(-moveValue);
		}
	}
	
	private String shift() {
		return repeat("\t", path.size() - filterMoves.length);
	}
}
