package org.dmkr.chess.ui.helpers;

import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.SwingUtilities;

import org.dmkr.chess.ui.UIBoardJComponent;
import org.dmkr.chess.ui.api.model.UIPoint;

import com.google.inject.Inject;


public class UIMousePositionHelper {
	@Inject private UIBoardJComponent component;
	
	public UIPoint getMouseLocation() {
		final Point mousePoint = MouseInfo.getPointerInfo().getLocation();
		if (component != null) {
			SwingUtilities.convertPointFromScreen(mousePoint, component); 
		}
		return new UIPoint(mousePoint.getX(), mousePoint.getY());
	}
}
