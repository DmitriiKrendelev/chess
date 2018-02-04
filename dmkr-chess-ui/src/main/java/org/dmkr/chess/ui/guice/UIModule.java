package org.dmkr.chess.ui.guice;

import static org.dmkr.chess.engine.board.BoardFactory.newInitialPositionBoard;
import static org.dmkr.chess.engine.function.Functions.getDefaultEvaluationFunctionAware;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.treeBuildingStrategy;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.allMovesProvider;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.bestNMovesProvider;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.capturedMovesProvider;
import static org.dmkr.chess.ui.config.UIBoardConfigs.DEFAULT_UI_CONFIG;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.ui.UIBoard;
import org.dmkr.chess.ui.UIBoardJComponent;
import org.dmkr.chess.ui.config.UIBoardConfig;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class UIModule extends AbstractModule {

	@Override
	protected void configure() {
		
		final EvaluationFunctionAware<BoardEngine> evaluationFunctionAware = getDefaultEvaluationFunctionAware();
    	
    	final AsyncEngine<BoardEngine> engine = minimax()
        	.treeStrategyCreator(() -> 
        		treeBuildingStrategy()
        			.onFirstLevel(allMovesProvider())
					.onSecondLevel(bestNMovesProvider(12, evaluationFunctionAware))
					.onThirdLevel(bestNMovesProvider(12, evaluationFunctionAware))
					.onFourthLevel(capturedMovesProvider())
					.build())
    		.evaluationFunctionAware(evaluationFunctionAware)
			.isAsynchronous(true)
			.parallelLevel(4)
			.build();

    	bind(new TypeLiteral<AsyncEngine<BoardEngine>>() {}).toInstance(engine);
    	bind(BoardEngine.class).toInstance(newInitialPositionBoard());
		bind(UIBoardConfig.class).toInstance(DEFAULT_UI_CONFIG);
		bind(UIBoardJComponent.class).asEagerSingleton();
		bind(UIBoard.class).asEagerSingleton();
	}

}
