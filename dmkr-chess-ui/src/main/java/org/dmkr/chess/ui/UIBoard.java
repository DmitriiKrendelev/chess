package org.dmkr.chess.ui;

import javax.swing.JFrame;

import org.dmkr.chess.ui.config.UIBoardConfig;
import org.dmkr.chess.ui.listeners.BestLineVisualizerListener;
import org.dmkr.chess.ui.listeners.ItemsDragAndDropListener;
import org.dmkr.chess.ui.listeners.MovesRollbackListener;
import org.dmkr.chess.ui.listeners.PrintBoardListener;
import org.dmkr.chess.ui.listeners.PrintThreadDumpListener;

import com.google.inject.Inject;

@SuppressWarnings("serial")
public class UIBoard extends JFrame {
	@Inject private UIBoardConfig config;
	@Inject private UIBoardJComponent jComponent;
	@Inject private MovesRollbackListener movesRollbackListener;
	@Inject private PrintBoardListener printBoardListener;
	@Inject private PrintThreadDumpListener printThreadDumpListener;
	@Inject private ItemsDragAndDropListener dragAndDropListener;
	@Inject private BestLineVisualizerListener bestLineVisualizerListener;
	
	public void run() throws Exception {
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setTitle(config.getTitle());
		setSize(config.getSize());
		setBackground(config.getBackgroundColor());
		
		setContentPane(jComponent);
		addKeyListener(movesRollbackListener);
		addKeyListener(printBoardListener);
		addKeyListener(printThreadDumpListener);
		
		addMouseListener(dragAndDropListener);
		addMouseMotionListener(dragAndDropListener);
		addMouseListener(bestLineVisualizerListener);
		
		jComponent.run();
	}
}
