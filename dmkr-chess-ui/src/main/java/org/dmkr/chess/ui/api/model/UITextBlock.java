package org.dmkr.chess.ui.api.model;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.dmkr.chess.common.collections.StreamUtils.toImmutableList;
import static org.dmkr.chess.ui.api.model.UIRect.newRect;
import static org.dmkr.chess.ui.api.model.UIText.EMPTY;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dmkr.chess.ui.helpers.UIMousePositionHelper;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

public class UITextBlock {
	private final UIMousePositionHelper mousePositionHelper;
	private final List<UIText> textLines;
	private final Optional<Color> textColor;
	private final Optional<Color> focusedTextColor;
	private final Optional<Color> focusedAreaColor;
	private final Font textStyle;
	private final boolean isFocusable;

	@Builder(buildMethodName = "buildInternal")
	private UITextBlock(
			@NonNull UIMousePositionHelper mousePositionHelper, 
			@Singular List<String> textLines, 
			Color textColor, 
			Color focusedTextColor, 
			Color focusedAreaColor, 
			Font textStyle, 
			boolean isFocusable) {
		
		checkState(textLines.stream().filter(StringUtils::isNotBlank).findAny().isPresent(), "Text block is empty: %s", textLines);
		
		this.mousePositionHelper = mousePositionHelper;
		this.textLines = textLines.stream().map(text -> isBlank(text) ? EMPTY : new UIText(text)).collect(toImmutableList());
		this.textColor = Optional.ofNullable(textColor);
		this.focusedTextColor = Optional.ofNullable(focusedTextColor);
		this.focusedAreaColor = Optional.ofNullable(focusedAreaColor);
		this.textStyle = textStyle;
		this.isFocusable = isFocusable;
	}
	
	public void draw(Graphics2D g, UIPointMutable point) {
		if (isFocusable && isFocused()) {
			focusedAreaColor.ifPresent(color -> getRect().draw(g, color));
			focusedTextColor.ifPresent(g::setColor);
		} else {
			textColor.ifPresent(g::setColor);
		}
		
		textLines.forEach(uiText -> {
				uiText.updatePosition(point.moveAndGet());
				uiText.draw(g, textStyle);
			});
	}

	public UIRect getRect() {
		int xL = MAX_VALUE;
		int yL = MAX_VALUE;
		int xR = MIN_VALUE;
		int yR = MIN_VALUE;
		
		for (UIText uiText : textLines) {
			if (uiText == EMPTY) {
				continue;
			}
			
			xL = min(xL, uiText.x());
			yL = min(yL, max(0, uiText.y() - textStyle.getSize()));
			xR = max(xR, uiText.x() + uiText.getWidth());
			yR = max(yR, uiText.y() + uiText.getHight());
		}
		
		return newRect(xL, yL, xR - xL, yR - yL);		
	}
	
	public boolean isFocused() {
		final UIPoint mouseLocation = mousePositionHelper.getMouseLocation();
		return getRect().isInside(mouseLocation.x(), mouseLocation.y());
	}
	
	@Override
	public String toString() {
		return textLines.stream().map(Object::toString).collect(Collectors.joining("\n"));
	}
	
	public static class UITextBlockBuilder {
		public UITextBlock build() {
			final UITextBlock result = buildInternal();
			clearTextLines();
			return result;
		}
	}
}
