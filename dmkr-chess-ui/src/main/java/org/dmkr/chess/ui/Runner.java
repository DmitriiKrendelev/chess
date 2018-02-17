package org.dmkr.chess.ui;

import java.awt.EventQueue;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.ui.guice.UIHelpersModule;
import org.dmkr.chess.ui.guice.UIListenersModule;
import org.dmkr.chess.ui.guice.UIModule;

import com.google.inject.Guice;

import static org.dmkr.chess.engine.function.Functions.getDefaultEvaluationFunctionAware;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.treeBuildingStrategy;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.allMovesProvider;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.bestNMovesProvider;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.capturedMovesProvider;

public class Runner {
	
	public static void main(String args[]) {
		EventQueue.invokeLater(() -> {
            final EvaluationFunctionAware<BoardEngine> evaluationFunctionAware = getDefaultEvaluationFunctionAware();

            final AsyncEngine<BoardEngine> engine = minimax()
                    .treeStrategyCreator(() ->
                            treeBuildingStrategy()
                                    .onFirstLevel(allMovesProvider())
                                    .onSecondLevel(bestNMovesProvider(16, evaluationFunctionAware))
                                    .onThirdLevel(bestNMovesProvider(12, evaluationFunctionAware))
                                    .onFourthLevel(capturedMovesProvider())
                                    .onFifthLevel(capturedMovesProvider())
                                    .build())
                    .evaluationFunctionAware(evaluationFunctionAware)
                    .isAsynchronous(true)
                    .parallelLevel(4)
                    .build();


            try {
                Guice.createInjector(
                        new UIModule(engine),
                        new UIHelpersModule(),
                        new UIListenersModule()
                ).getInstance(UIBoard.class).run();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
	
}
