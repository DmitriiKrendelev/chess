package org.dmkr.chess.ui.api.model;

import static com.google.common.base.Preconditions.checkState;

import java.awt.Color;
import java.awt.Graphics2D;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UIRect {
	private final int x;
	private final int y;
	private final int width;
	private final int hight;
	
	public static final UIRect ZERO_SIZE_RECT = new UIRect(0, 0, 0, 0) {
		@Override
		public boolean isInside(UIPoint uiPoint) {
			return false;
		}
	};

	public static UIRect newRect(double x, double y, double width, double hight) {
		checkState(x >= 0, "x = %s", x);
		checkState(y >= 0, "y = %s", y);
		checkState(width >= 0, "width = %s", width);
		checkState(hight >= 0, "hight = %s", hight);
		
		return hight == 0 || width == 0 ? ZERO_SIZE_RECT : new UIRect(x, y, width, hight);
	}
	
	private UIRect(double x, double y, double width, double hight) {
		this((int) x, (int) y, (int) width, (int) hight);
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHight() {
		return hight;
	}
	
	public boolean isInside(UIPoint uiPoint) {
		return isInside(uiPoint.x(), uiPoint.y());
	}
	
	public boolean isInside(int pointX, int pointY) {
		return 
				pointX > x && 
				pointY > y && 
				pointX < x + width &&
				pointY < y + hight;
	}
	
	public void draw(Graphics2D g, Color color) {
		g.setColor(color);
		g.fillRect(x, y, width, hight);
	}
	
	@Override
	public String toString() {
		return "x = " + x + " y = " + y + " w = " + width + " h = " + hight;
	}
}
