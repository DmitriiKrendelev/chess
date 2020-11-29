package org.dmkr.chess.ui;

import com.google.inject.Guice;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.EvaluationFunctionAware;
import org.dmkr.chess.engine.board.impl.EvaluationHistoryManagerImpl;
import org.dmkr.chess.engine.function.bit.EvaluationFunctionAllBit;
import org.dmkr.chess.ui.guice.SaveAndLoadPositionModule;
import org.dmkr.chess.ui.guice.UIHelpersModule;
import org.dmkr.chess.ui.guice.UIListenersModule;
import org.dmkr.chess.ui.guice.UIModule;

import java.awt.*;

import static org.dmkr.chess.api.model.Color.*;
import static org.dmkr.chess.engine.board.BoardFactory.*;
import static org.dmkr.chess.engine.minimax.MiniMax.*;
import static org.dmkr.chess.engine.minimax.tree.TreeBuildingStrategyImpl.*;
import static org.dmkr.chess.engine.minimax.tree.TreeLevelMovesProviders.*;
import static org.dmkr.chess.ui.Player.*;

public class Runner {

    public static void main(String[] args) {
        final Player player = player()
                .name("Test Player")
                .color(BLACK)
                .isReadOnly(true)
                .build();

        final BoardEngine board = newInitialPositionBoard();

        run(player, board);
    }

    public static void run(Player player, BoardEngine board) {
        EventQueue.invokeLater(() -> {

            try {
                Guice.createInjector(
                        UIModule.builder()
                                .player(player)
                                .engine1(engine())
                                .engine2(engine())
                                .board(board)
                                .build(),
                        new UIHelpersModule(),
                        new UIListenersModule(),
                        new SaveAndLoadPositionModule()
                ).getInstance(UIBoardJFrame.class).run();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static AsyncEngine<BoardEngine> engine() {
        return minimax()
                .treeStrategyCreator(() ->
                                treeBuildingStrategy()
                                        .onLevel1(nBestMoves(5))
                                        .onLevel2(nBestMoves(5))
                                        .onLevel3(nBestMoves(5))
                                        .onLevel4(nBestMoves(5))
                                        .onLevel5(nBestMoves(5))
//                                    .onLevel1(allMoves())
//                                    .onLevel2(nBestMoves(16))
//                                    .onLevel3(nBestMoves(12))
//                                    .onLevel4(capturedMoves(2, 4))
//                                    .onLevel5(capturedMoves(2, 4))
                )
                .evaluationFunctionAware((EvaluationFunctionAware) EvaluationFunctionAware.of(EvaluationFunctionAllBit.INSTANCE))
                .evaluationHistoryManager(new EvaluationHistoryManagerImpl())
                .isAsynchronous(true)
                .parallelLevel(4)
                .build();
    }

}
