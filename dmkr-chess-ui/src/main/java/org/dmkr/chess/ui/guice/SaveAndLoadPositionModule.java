package org.dmkr.chess.ui.guice;

import com.google.inject.AbstractModule;
import org.dmkr.chess.ui.saveandload.LoadPositionPreviewPanel;
import org.dmkr.chess.ui.saveandload.LoadFileFilter;
import org.dmkr.chess.ui.saveandload.SaveAndLoadPositionManager;


public class SaveAndLoadPositionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LoadPositionPreviewPanel.class).asEagerSingleton();
        bind(SaveAndLoadPositionManager.class).asEagerSingleton();
        bind(LoadFileFilter.class).asEagerSingleton();
    }
}
