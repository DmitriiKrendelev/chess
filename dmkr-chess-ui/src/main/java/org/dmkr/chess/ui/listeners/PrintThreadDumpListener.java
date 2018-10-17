package org.dmkr.chess.ui.listeners;

import com.google.inject.Inject;

public class PrintThreadDumpListener extends AbstractPressAndTypedListener {
	private static final int P = 80;

	@Inject
	private PrintThreadDumpListener() {
		super(CTRL, P);
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
