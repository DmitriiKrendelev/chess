package org.dmkr.chess.ui;

import java.awt.EventQueue;

import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.api.EvaluationHistoryManager;
import org.dmkr.chess.engine.board.impl.EvaluationHistoryManagerImpl;
import org.dmkr.chess.engine.function.bit.EvaluationFunctionAllBit;
import org.dmkr.chess.ui.guice.SaveAndLoadPositionModule;
import org.dmkr.chess.ui.guice.UIHelpersModule;
import org.dmkr.chess.ui.guice.UIListenersModule;
import org.dmkr.chess.ui.guice.UIModule;

import com.google.inject.Guice;

import static org.dmkr.chess.api.model.Color.BLACK;
import static org.dmkr.chess.api.model.Color.WHITE;
import static org.dmkr.chess.engine.board.BoardFactory.newInitialPositionBoard;
import static org.dmkr.chess.engine.minimax.MiniMax.minimax;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.treeBuildingStrategy;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.allMovesProvider;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.bestNMovesProvider;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProvider.capturedMovesProvider;
import static org.dmkr.chess.ui.Player.player;

public class Runner {

    public static void main(String[] args) {
        final Player player = player()
                .name("Test Player")
                .color(BLACK)
                .build();

        final BoardEngine board = newInitialPositionBoard();

        run(player, board);
    }

	public static void run(Player player, BoardEngine board) {
		EventQueue.invokeLater(() -> {

            final EvaluationFunctionAware<BoardEngine> evaluationFunction = (EvaluationFunctionAware) EvaluationFunctionAware.of(EvaluationFunctionAllBit.INSTANCE);

            final EvaluationHistoryManager evaluationHistoryManager = new EvaluationHistoryManagerImpl();

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
                    .evaluationHistoryManager(evaluationHistoryManager)
                    .isAsynchronous(true)
                    .parallelLevel(4)
                    .build();


            try {
                Guice.createInjector(
                        new UIModule(player, engine, board, evaluationHistoryManager),
                        new UIHelpersModule(),
                        new UIListenersModule(),
                        new SaveAndLoadPositionModule()
                ).getInstance(UIBoard.class).run();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}
