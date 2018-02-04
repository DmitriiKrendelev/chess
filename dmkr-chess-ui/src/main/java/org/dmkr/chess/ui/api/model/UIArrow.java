package org.dmkr.chess.ui.api.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(of = {"from", "to"})
@RequiredArgsConstructor
public class UIArrow {
	private static final int[] ARROW_HEAD_X_COORDS = {0, -5, 5};
	private static final int[] ARROW_HEAD_Y_COORDS = {8, -5, -5};
	
	private final Stroke arrowStyle = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private final UIPoint from;
	private final UIPoint to;
	private final Polygon arrowHead = new Polygon(ARROW_HEAD_X_COORDS, ARROW_HEAD_Y_COORDS, ARROW_HEAD_X_COORDS.length); 
	private final AffineTransform tx = new AffineTransform();
	
	public void draw(Color color, Graphics2D g) {
		g.setColor(color);
		g.setStroke(arrowStyle);
		g.drawLine(from.x(), from.y(), to.x(), to.y());
		
		tx.setToIdentity();
		final double angle = Math.atan2(to.y() - from.y(), to.x() - from.x());
		tx.translate(to.x(), to.y());
		tx.rotate(angle - Math.PI / 2.0);  
		g.setTransform(tx);   
		g.fill(arrowHead);
		tx.setToIdentity();
	}
	
}
