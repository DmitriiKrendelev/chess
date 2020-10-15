package org.dmkr.chess.engine.minimax.tree;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.function.EvaluationFunction;

import java.util.List;

import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.Lists.*;
import static org.dmkr.chess.api.utils.MoveUtils.*;

@RequiredArgsConstructor(staticName = "treeBuildingStrategyWithLevelMoveProviders")
public class TreeBuildingStrategyImpl implements TreeBuildingStrategy {
	
	private final TreeLevelMovesProvider[] levelMovesProviders;

	@Builder(builderMethodName = "treeBuildingStrategy", builderClassName = "TreeBuildingStrategyBuilder")
	public static TreeBuildingStrategy buildTreeBuildingStrategy(
			@NonNull EvaluationFunction<? extends BoardEngine> evaluationFunction,
			TreeLevelMovesProvider onLevel1,
			TreeLevelMovesProvider onLevel2,
			TreeLevelMovesProvider onLevel3,
			TreeLevelMovesProvider onLevel4,
			TreeLevelMovesProvider onLevel5,
			TreeLevelMovesProvider onLevel6,
			List<TreeLevelMovesProvider> onSeventhLevelAndDeeper) {
		
		final List<TreeLevelMovesProvider> levelMovesProvidersWithNulls = newArrayList(onLevel1, onLevel2, onLevel3, onLevel4, onLevel5, onLevel6);

		if (onSeventhLevelAndDeeper != null) {
			levelMovesProvidersWithNulls.addAll(onSeventhLevelAndDeeper);
		}
		
		boolean previousIsNull = false;
		final List<TreeLevelMovesProvider> levelMovesProviders = newArrayList();
		for (TreeLevelMovesProvider provider : levelMovesProvidersWithNulls) {
			if (provider != null) {
				checkArgument(!previousIsNull);
				levelMovesProviders.add(provider);
			} else {
				previousIsNull = true;
			}
		}

		levelMovesProviders.forEach(p -> p.setEvaluationFunction(evaluationFunction));

		return treeBuildingStrategyWithLevelMoveProviders(levelMovesProviders.stream().toArray(TreeLevelMovesProvider[]::new));
	}
	
	@Override
	public boolean isLeaf(int move, TreeContext context) {
		if (!context.isOponentMove()) {
		    return false;
		}

		final int currentLevel = context.getLevel();
		if (currentLevel == levelMovesProviders.length - 1) {
			return true;
		}
		if (levelMovesProviders[currentLevel + 1].onCaptureMoveOnly() && !isCapturedMove(move)) {
			return true;
		}
		
		return false;
	}

	@Override
	public int[] getSubtreeMoves(BoardEngine board, TreeContext context) {
		return levelMovesProviders[context.getLevel()].getMoves(board);
	}
}
