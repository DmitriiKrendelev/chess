package org.dmkr.chess.ui.api.model;

import static com.google.common.base.Preconditions.checkState;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class UIText {
	private int x;
	private int y;
	private int width;
	private int hight;
	private final String text;
	
	static final UIText EMPTY = new UIText("null");
	
	public UIText(String text) {
		checkState(isNotBlank(text));
		this.text = text;
	}

	int x() {
		return x;
	}

	int y() {
		return y;
	}

	int getWidth() {
		return width;
	}

	int getHight() {
		return hight;
	}

	String getText() {
		return text;
	}
	
	void updatePosition(UIPoint uiPoint) {
		this.x = uiPoint.x();
		this.y = uiPoint.y();
	}
	
	public void draw(Graphics2D g, Font textStyle) {
		if (this == EMPTY) {
			return;
		}
		final TextLayout layout = new TextLayout(text, textStyle, g.getFontRenderContext());
		final Rectangle2D rect = layout.getBounds();
		
		width = (int) rect.getWidth();
		hight = (int) rect.getHeight();
		
		layout.draw(g, x, y);
	}
	
	public void draw(Graphics2D g, Font textStyle, int xCenter, int yCenter) {
		if (this == EMPTY) {
			return;
		}
		final TextLayout layout = new TextLayout(text, textStyle, g.getFontRenderContext());
		final Rectangle2D rect = layout.getBounds();
		
		width = (int) rect.getWidth();
		hight = (int) rect.getHeight();
		x = xCenter + 2 - (int) (width / 2);
		y = yCenter + 2 + (int) (hight / 2);
		
		layout.draw(g, x, y);
	}

	@Override
	public String toString() {
		return "x = " + x + " y = " + y + " w = " + width + " h = " + hight + " : " + text;
	}
}
