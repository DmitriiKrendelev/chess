package org.dmkr.chess.ui.api.model;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class UIPoint {
	private final int x;
	private final int y;
	
	public UIPoint(double x, double y) {
		this((int) x, (int) y);
	}
	
	public UIPoint minus(int deltaX, int deltaY) {
		return new UIPoint(x - deltaX, y - deltaY);
	}
	
	public UIPoint plus(int deltaX, int deltaY) {
		return new UIPoint(x + deltaX, y + deltaY);
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}
}