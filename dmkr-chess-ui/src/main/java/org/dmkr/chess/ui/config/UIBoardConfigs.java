package org.dmkr.chess.ui.config;

import static java.awt.Font.BOLD;
import static org.dmkr.chess.api.model.Constants.ENGINE_NAME_AND_VERSION;
import static org.dmkr.chess.ui.api.model.UIRect.newRect;
import static org.dmkr.chess.ui.helpers.UIColorsHelper.getAlphaColor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import lombok.experimental.UtilityClass;
import org.dmkr.chess.ui.api.model.UIPoint;
import org.dmkr.chess.ui.api.model.UIRect;

@UtilityClass
public class UIBoardConfigs {

	public static final UIBoardConfig DEFAULT_UI_CONFIG = UIBoardConfig.builder()
			.title(ENGINE_NAME_AND_VERSION)
			.size(new Dimension(1000, 700))
			.backGroundPathForWhite("ui/style1/board/empty_board_white.png")
			.backGroundPathForBlack("ui/style1/board/empty_board_black.png")
			.piecesFolderPath("ui/style1/pieces/")
			.cursorsFolderPath("ui/style1/cursors/")
			
			// Coords
			.boardCoords(new UIRect(41, 34, 580, 582))
			.pieceMovingSpeed(100)
			
			// Text
			.textPosition(new UIPoint(641, 54))
			.textStyle(new Font("Chess UI Text Style", BOLD, 12))
			.textColor(getAlphaColor(0, 0, 200, 1.00d))
			.focusedTextColor(getAlphaColor(0, 0, 50, 1.00d))
			.focusedAreaColor(getAlphaColor(200, 200, 255, 0.5d))
			
			// colors
			.backgroundColor(Color.WHITE)
			.pressedFieldColor(getAlphaColor(Color.BLUE, 0.30d))
			.allowedMovesColor(getAlphaColor(Color.BLUE, 0.10d))
			
			// timeouts
			.repaintTimeoutMillis(20)
			.arrowDrawDelayMillis(3000)
			
			// progress bar
			.progressBar(newRect(641, 34, 120, 15))
			.progressBarColor(getAlphaColor(Color.BLUE, 0.1d))
			.progressBarBorderColor(getAlphaColor(Color.BLUE, 1.00d))
			
			.build();
			
}
