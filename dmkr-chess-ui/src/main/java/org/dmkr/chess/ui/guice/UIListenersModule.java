package org.dmkr.chess.ui.guice;

import org.dmkr.chess.ui.listeners.BestLineVisualizerListener;
import org.dmkr.chess.ui.listeners.ItemsDragAndDropListener;
import org.dmkr.chess.ui.listeners.MovesRollbackListener;
import org.dmkr.chess.ui.listeners.PrintBoardListener;
import org.dmkr.chess.ui.listeners.PrintThreadDumpListener;

import com.google.inject.AbstractModule;

public class UIListenersModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MovesRollbackListener.class).asEagerSingleton();
		bind(PrintBoardListener.class).asEagerSingleton();
		bind(PrintThreadDumpListener.class).asEagerSingleton();
		bind(BestLineVisualizerListener.class).asEagerSingleton();
		bind(ItemsDragAndDropListener.class).asEagerSingleton();
	}

}
