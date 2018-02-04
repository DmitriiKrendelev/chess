package org.dmkr.chess.ui.helpers;

import java.awt.Color;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import static com.google.common.base.Preconditions.checkArgument;

@UtilityClass
public class UIColorsHelper {
	
	public static Color getAlphaColor(@NonNull Color rgb, double alpha) {
		return getAlphaColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), alpha);
	}
	
	public static Color getAlphaColor(int r, int g, int b, double alpha) {
		checkArgument(alpha >= 0.0d && alpha <= 1.0d, "Illegal alpha value: " + alpha);
		return new Color(r, g, b, (int) (alpha * 255));
	}
	
	public static Color getPositionChangedColor(int positionValueChange) {
		final double maxPositionValueChange = 100D;
		final int maxColorComponent = 255;
		final int alpha = 200;
		
		final double relation = Math.min(1.0D, Math.abs(positionValueChange) / maxPositionValueChange);

		final int r = positionValueChange > 0 ? 0 : (int) (relation * maxColorComponent);
		final int g = positionValueChange < 0 ? 0 : (int) (relation * maxColorComponent);
		final int b = (int) ((1 - relation) * maxColorComponent);

		return new Color(r, g, b, alpha);
	}
}
