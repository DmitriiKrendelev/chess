package org.dmkr.chess.ui.listeners;

public class PrintThreadDumpListener extends AbstractPressAndTypedListener {
	private static final int CTRL = 17;
	private static final int P = 80;
	
	public PrintThreadDumpListener() {
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
