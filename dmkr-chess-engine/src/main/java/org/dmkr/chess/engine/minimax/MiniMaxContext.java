package org.dmkr.chess.engine.minimax;
import java.util.List;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.common.primitives.IntStack;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.minimax.MoveValue;
import org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategy;

public interface MiniMaxContext<T extends BoardEngine> extends EvaluationFunctionAware<T> {
	
	void reset(boolean isInverted, int positionInitial);
	
	IntStack getCurrentPath();

	List<MoveValue> getMinimaxValuesStack();

	BestLineBuilder getBestLineBuilder();
	
	void applyMove(T board, int move);
	
	void rollbackMove(T board, int moveValue);
	
	int deep();
	
	boolean isOponentMove();
	
	TreeBuildingStrategy getTreeBuildingStrategy();
}
