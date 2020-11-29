package org.dmkr.chess.ui.guice;

import com.google.common.collect.ImmutableList;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.dmkr.chess.ui.listeners.UIListener;
import org.dmkr.chess.ui.listeners.impl.*;

import java.util.List;

public class UIListenersModule extends AbstractModule {
    private static final List<Class<? extends UIListener>> UI_LISTENERS =
            ImmutableList.of(
                    NewGameWhiteListener.class,
                    NewGameBlackListener.class,
                    PrintBoardListener.class,
                    PrintThreadDumpListener.class,
                    BestLineVisualizerListener.class,
                    PiecesDragAndDropListener.class,
                    SavePositionListener.class,
                    LoadPositionListener.class,
                    MovesRollbackListener.class,
                    PauseListener.class,
                    ExitListener.class
            );

    @Override
    protected void configure() {
        UI_LISTENERS.forEach(listener -> bind(listener).asEagerSingleton());

        bind(new TypeLiteral<List<Class<? extends UIListener>>>() {}).toInstance(UI_LISTENERS);
    }


}
