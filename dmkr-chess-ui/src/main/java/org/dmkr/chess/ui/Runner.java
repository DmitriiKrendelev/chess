package org.dmkr.chess.ui;

import java.awt.EventQueue;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.function.bit.EvaluationFunctionAllBit;
import org.dmkr.chess.ui.guice.UIHelpersModule;
import org.dmkr.chess.ui.guice.UIListenersModule;
import org.dmkr.chess.ui.guice.UIModule;

import com.google.inject.Guice;

import static org.dmkr.chess.api.model.Color.BLACK;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.treeBuildingStrategy;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.allMovesProvider;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.bestNMovesProvider;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.capturedMovesProvider;
import static org.dmkr.chess.ui.Player.player;

public class Runner {
	
	public static void main(String args[]) {
		EventQueue.invokeLater(() -> {
            final Player player = player()
                    .name("Player")
                    .color(BLACK)
                    .build();

            final EvaluationFunctionAware<BoardEngine> evaluationFunction = (EvaluationFunctionAware) EvaluationFunctionAware.of(EvaluationFunctionAllBit.INSTANCE);

            final AsyncEngine<BoardEngine> engine = minimax()
                    .treeStrategyCreator(() ->
                            treeBuildingStrategy()
                                    .onFirstLevel(allMovesProvider())
                                    .onSecondLevel(bestNMovesProvider(16, evaluationFunction))
                                    .onThirdLevel(bestNMovesProvider(12, evaluationFunction))
                                    .onFourthLevel(capturedMovesProvider(2, 4))
                                    .onFifthLevel(capturedMovesProvider(2, 4))
                                    .build())
                    .evaluationFunctionAware(evaluationFunction)
                    .isAsynchronous(true)
                    .parallelLevel(4)
                    .build();


            try {
                Guice.createInjector(
                        new UIModule(player, engine),
                        new UIHelpersModule(),
                        new UIListenersModule()
                ).getInstance(UIBoard.class).run();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}
