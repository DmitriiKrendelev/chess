package org.dmkr.chess.ui.listeners.impl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dmkr.chess.engine.minimax.BestLine;
import org.dmkr.chess.ui.UIBoardJComponent;
import org.dmkr.chess.ui.api.model.UITextBlock;

import com.google.inject.Inject;
import org.dmkr.chess.ui.listeners.UIListener;

public class BestLineVisualizerListener extends MouseAdapter implements UIListener {
	private final Map<BestLine, UITextBlock> bestLines = new ConcurrentHashMap<>(); 
	@Inject private UIBoardJComponent jComponent;
	
	public void mouseClicked(MouseEvent event) {
		if (!jComponent.isBestLineVisualisationEnabled()) {
			return;
		}
		
		final int x = event.getX();
		final int y = event.getY();

		bestLines.entrySet().stream()
				.filter(e -> e.getValue().getRect().isInside(x, y))
				.map(Map.Entry::getKey)
				.findAny()
				.ifPresent(jComponent::startBestLineVisualisation);
	}
	
	public void registerBestLine(BestLine bestLine, UITextBlock textBlock) {
		bestLines.put(bestLine, textBlock);
	}
	
	public void clear() {
		bestLines.clear();
	}
	
}
