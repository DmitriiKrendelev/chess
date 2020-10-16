package org.dmkr.chess.ui.guice;

import static org.dmkr.chess.ui.config.UIBoardConfigs.DEFAULT_UI_CONFIG;

import lombok.Builder;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.ui.Player;
import org.dmkr.chess.ui.UIBoardJFrame;
import org.dmkr.chess.ui.UIBoardJComponent;
import org.dmkr.chess.ui.UIBoardJMenuBar;
import org.dmkr.chess.ui.config.UIBoardConfig;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

@Builder
public class UIModule extends AbstractModule {
    private final Player player;
    private final AsyncEngine<BoardEngine> engine;
    private final BoardEngine board;

    @Override
    protected void configure() {
        bind(new TypeLiteral<AsyncEngine<BoardEngine>>() {}).toInstance(engine);
        bind(BoardEngine.class).toInstance(board);
        bind(UIBoardConfig.class).toInstance(DEFAULT_UI_CONFIG);
        bind(UIBoardJComponent.class).asEagerSingleton();
        bind(UIBoardJFrame.class).asEagerSingleton();
        bind(Player.class).toInstance(player);
        bind(UIBoardJMenuBar.class).asEagerSingleton();
    }

}
