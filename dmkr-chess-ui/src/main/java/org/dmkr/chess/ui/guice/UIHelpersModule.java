package org.dmkr.chess.ui.guice;

import org.dmkr.chess.ui.helpers.*;

import com.google.inject.AbstractModule;
import org.dmkr.chess.ui.saveandload.SaveAndLoadPositionManager;

public class UIHelpersModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UIMousePositionHelper.class).asEagerSingleton();
		bind(UIBoardImagesHelper.class).asEagerSingleton();
		bind(UIBoardCoordsHelper.class).asEagerSingleton();
		bind(UIBoardTextHelper.class).asEagerSingleton();
		bind(SaveAndLoadPositionManager.class).asEagerSingleton();
	}
	
	
}
