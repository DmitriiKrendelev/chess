package org.dmkr.chess.engine.minimax.tree;

import static org.dmkr.chess.api.utils.MoveUtils.isCapturedMove;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.allMoves;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.bestNMoves;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.stream.IntStream;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "treeBuildingStrategyWithLevelMoveProviders")
public class TreeBuildingStrategyImpl implements TreeBuildingStrategy {
	
	private final TreeLevelMovesProvider[] levelMovesProviders;

	@Builder(builderMethodName = "treeBuildingStrategy", builderClassName = "TreeBuildingStrategyBuilder")
	public static TreeBuildingStrategy buildTreeBuildingStrategy(
			TreeLevelMovesProvider onFirstLevel,
			TreeLevelMovesProvider onSecondLevel,
			TreeLevelMovesProvider onThirdLevel,
			TreeLevelMovesProvider onFourthLevel,
			TreeLevelMovesProvider onFifthLevel,
			TreeLevelMovesProvider onSixthLevel,
			List<TreeLevelMovesProvider> onSeventhLevelAndDeeper) {
		
		final List<TreeLevelMovesProvider> levelMovesProvidersWithNulls =
				newArrayList(onFirstLevel, onSecondLevel, onThirdLevel, onFourthLevel, onFifthLevel, onSixthLevel);

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
		
		return treeBuildingStrategyWithLevelMoveProviders(levelMovesProviders.stream().toArray(TreeLevelMovesProvider[]::new));
	}
	
	@Builder(builderMethodName = "treeBuildingStrategyWithParams", builderClassName = "TreeBuildingStrategyBuilderWithParams")
	public static TreeBuildingStrategy buildTreeBuildingStrategyWithParams(
			@NonNull Integer fullScanLevel,
			@NonNull Integer cutOffLevel,
			@NonNull Integer cutOffNumberOfMoves,
			@NonNull Integer captureMovesLevel,
			@NonNull EvaluationFunctionAware<? extends BoardEngine> evaluationFunctionAware) {
		
		final List<TreeLevelMovesProvider> levelMovesProviders = newArrayList();
		IntStream.range(0, fullScanLevel)
			.forEach(level -> levelMovesProviders.add(allMoves()));
		
		IntStream.range(0, cutOffLevel)
			.forEach(level -> levelMovesProviders.add(bestNMoves(cutOffNumberOfMoves, evaluationFunctionAware)));
		
		IntStream.range(0, captureMovesLevel)
			.forEach(level -> levelMovesProviders.add(TreeLevelMovesProvider.capturedMoves()));
		
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
