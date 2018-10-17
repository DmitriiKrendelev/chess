package org.dmkr.chess.ui.guice;

import com.google.common.collect.ImmutableList;
import org.dmkr.chess.ui.listeners.*;

import com.google.inject.AbstractModule;

import java.util.EventListener;
import java.util.List;

public class UIListenersModule extends AbstractModule {
	private static final List<Class<? extends EventListener>> UI_LISTENERS =
			ImmutableList.of(
					MovesRollbackListener.class,
					PrintBoardListener.class,
					PrintThreadDumpListener.class,
					BestLineVisualizerListener.class,
					PiecesDragAndDropListener.class,
					SavePositionListener.class,
					LoadPositionListener.class
			);


	@Override
	protected void configure() {
		UI_LISTENERS.forEach(listener -> bind(listener).asEagerSingleton());
	}

}
