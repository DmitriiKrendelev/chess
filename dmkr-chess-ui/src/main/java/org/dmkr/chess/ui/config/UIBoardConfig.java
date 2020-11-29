package org.dmkr.chess.ui.config;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import org.dmkr.chess.ui.api.model.UIPoint;
import org.dmkr.chess.ui.api.model.UIRect;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UIBoardConfig {
	private final String title;
	private final Dimension size;
	private final Color backgroundColor;
	private final Color allowedMovesColor;
	private final Color pressedFieldColor;
	private final Color arrowColor;
	private final UIRect boardCoords;
	private final int pieceMovingSpeed;
	private final String backGroundPathForWhite;
	private final String backGroundPathForBlack;
	private final String piecesFolderPath;
	private final String cursorsFolderPath;
	private final UIPoint textPosition1;
	private final UIPoint textPosition2;
	private final Font textStyle;
	private final Color textColor;
	private final Color focusedTextColor;
	private final Color focusedAreaColor;
	private final UIRect progressBar;
	private final Color progressBarColor;
	private final Color progressBarBorderColor;
	private final int repaintTimeoutMillis;
	private final int arrowDrawDelayMillis;
	
}
