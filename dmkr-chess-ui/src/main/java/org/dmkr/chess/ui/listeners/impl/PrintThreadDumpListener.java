package org.dmkr.chess.ui.listeners.impl;

import com.google.inject.Inject;
import org.dmkr.chess.ui.listeners.AbstractPressAndTypedListener;

import java.awt.event.KeyEvent;

public class PrintThreadDumpListener extends AbstractPressAndTypedListener {

	@Inject
	private PrintThreadDumpListener() {
		super(CTRL, KeyEvent.VK_T, null, null);
	}

	@Override
	public void run() {
		Thread.getAllStackTraces().forEach((thread, trace) -> {
			System.out.println(thread.getName() + " " + thread.getId());
			for (StackTraceElement el : trace) {
				System.out.println("\t at " + el);
			}
			System.out.println();
		});
		
	}

}
