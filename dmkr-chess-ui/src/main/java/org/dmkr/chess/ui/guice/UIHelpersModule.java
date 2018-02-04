package org.dmkr.chess.ui.guice;

import org.dmkr.chess.ui.helpers.UIBoardCoordsHelper;
import org.dmkr.chess.ui.helpers.UIBoardImagesHelper;
import org.dmkr.chess.ui.helpers.UIBoardTextHelper;
import org.dmkr.chess.ui.helpers.UIMousePositionHelper;

import com.google.inject.AbstractModule;

public class UIHelpersModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UIMousePositionHelper.class).asEagerSingleton();
		bind(UIBoardImagesHelper.class).asEagerSingleton();
		bind(UIBoardCoordsHelper.class).asEagerSingleton();
		bind(UIBoardTextHelper.class).asEagerSingleton();
	}
	
	
}
