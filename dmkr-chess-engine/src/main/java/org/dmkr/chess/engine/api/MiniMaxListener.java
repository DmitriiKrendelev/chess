package org.dmkr.chess.engine.api;

import org.dmkr.chess.api.model.Move;

public interface MiniMaxListener {

	void onMove(Move move);
	
	void onEvaluation(int moveValue);
	
	
	public static final MiniMaxListener NOPE = new MiniMaxListener() {
		@Override
		public void onMove(Move move) {
			
		}
		
		@Override
		public void onEvaluation(int moveValue) {
			
		}
	};
}
