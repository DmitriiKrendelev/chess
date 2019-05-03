package org.dmkr.chess.engine.minimax.tree;


import org.dmkr.chess.api.BoardEngine;

public interface TreeBuildingStrategy {

	interface TreeContext {
		int getDeep();
		
		default int getLevel() {
			return getDeep() >> 1;
		}
		
		default boolean isOponentMove() {
			return (getDeep() & 1) == 1;
		}
	}
	
	boolean isLeaf(int move, TreeContext context);
	
	int[] getSubtreeMoves(BoardEngine board, TreeContext context);
	
}
