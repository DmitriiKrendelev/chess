package org.dmkr.chess.ui.helpers;

import static java.lang.Math.sqrt;
import static java.lang.System.currentTimeMillis;
import static org.dmkr.chess.api.model.Constants.SIZE;
import static org.dmkr.chess.ui.api.model.UIRect.newRect;

import java.awt.image.BufferedImage;
import java.util.Map;

import org.dmkr.chess.api.Board;
import org.dmkr.chess.api.model.ColoredPiece;
import org.dmkr.chess.api.model.Field;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.common.lang.Procedure;
import org.dmkr.chess.ui.Player;
import org.dmkr.chess.ui.api.model.UIArrow;
import org.dmkr.chess.ui.api.model.UIPoint;
import org.dmkr.chess.ui.api.model.UIRect;
import org.dmkr.chess.ui.config.UIBoardConfig;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import lombok.Getter;

public class UIBoardCoordsHelper {

	private final int lowX;
	private final int lowY;
	private final int highX;
	private final int highY;
	private final int pieceMovingSpeed;
	
	private final double xSize;
	private final double ySize;

	// white player vision
	private final Map<Field, UIPoint> whiteFieldCenters;
	private final Map<Field, UIRect> whiteFields;

	// black player vision
	private final Map<Field, UIPoint> blackFieldCenters;
	private final Map<Field, UIRect> blackFields;

	public class MovingPiece {
		@Getter
		private final Field from, to;
		@Getter
		private final ColoredPiece coloredPiece;
		
		private final UIPoint fromPoint;
		private final UIPoint toPoint;
		private final Procedure onFinish;

		// times in milliseconds
		private final long startTime;
		private final long duration;
		// speeds in pixels per milliseconds 
		private final double xSpeed;
		private final double ySpeed;
		private boolean isFinished = false;
		
		public MovingPiece(Move move, Board board, Player player, Procedure onFinish) {
			this(move, board.at(move.from()), player, onFinish);
		}
		
		public MovingPiece(Move move, ColoredPiece coloredPiece, Player player, Procedure onFinish) {
			this.from = move.from();
			this.to = move.to();
			this.fromPoint = getFieldCenters(player).get(from);
			this.toPoint = getFieldCenters(player).get(to);
			this.coloredPiece = coloredPiece;
			this.onFinish = onFinish;
		
			this.startTime = currentTimeMillis();
			
			final int deltaX = toPoint.x() - fromPoint.x();
			final int deltaY = toPoint.y() - fromPoint.y();
			final double movement = sqrt(deltaX * deltaX + deltaY * deltaY);
			final double pieceMovingSpeedMillis = pieceMovingSpeed / 1000D;
			this.xSpeed = pieceMovingSpeedMillis * deltaX / movement;
			this.ySpeed = pieceMovingSpeedMillis * deltaY / movement;
			this.duration = (long) (movement / pieceMovingSpeedMillis);
		}

		public UIPoint getLocation() {
			final long time = currentTimeMillis() - startTime;
			if (time > duration) {
				if (!isFinished) {
					onFinish.invoke();
					isFinished = true;
				}
			}
			
			final double x = fromPoint.x() + xSpeed * time;
			final double y = fromPoint.y() + ySpeed * time;
			
			return new UIPoint(x, y);
		}

	}
	
	@Inject
	public UIBoardCoordsHelper(UIBoardConfig config) {
		final UIRect boardCoords = config.getBoardCoords();
		
		this.lowX = boardCoords.x();
		this.lowY = boardCoords.y();
		this.highX = boardCoords.x() + boardCoords.getWidth();
		this.highY = boardCoords.y() + boardCoords.getHight();
		this.pieceMovingSpeed = config.getPieceMovingSpeed();
		
		this.xSize = (double) (highX - lowX) / SIZE;
		this.ySize = (double) (highY - lowY) / SIZE;

		final ImmutableMap.Builder<Field, UIPoint> whiteFieldCentersBuilder = ImmutableMap.builder();
		final ImmutableMap.Builder<Field, UIPoint> blackFieldCentersBuilder = ImmutableMap.builder();
		final ImmutableMap.Builder<Field, UIRect> whiteFieldsBuilder = ImmutableMap.builder();
		final ImmutableMap.Builder<Field, UIRect> blackFieldsBuilder = ImmutableMap.builder();
		for (int x = 0; x < SIZE; x ++) {
			for (int y = 0; y < SIZE; y ++) {
				final double leftUpperX = lowX + xSize * x;
				final double leftUpperY = lowY + ySize * y;
				final double centerX = leftUpperX + xSize * 0.5d;
				final double centerY = leftUpperY + ySize * 0.5d;
				
				final UIPoint fieldCenter = new UIPoint(centerX, centerY);
				final UIRect fieldRect = newRect(leftUpperX, leftUpperY, xSize, ySize);
				
				final Field whiteField = Field.resolve(x, SIZE - 1 - y);
				final Field blackField = Field.resolve(SIZE - 1 - x, y);
				whiteFieldCentersBuilder.put(whiteField, fieldCenter);
				blackFieldCentersBuilder.put(blackField, fieldCenter);
				whiteFieldsBuilder.put(whiteField, fieldRect);
				blackFieldsBuilder.put(blackField, fieldRect);
			}
		}
		this.whiteFieldCenters = whiteFieldCentersBuilder.build();
		this.blackFieldCenters = blackFieldCentersBuilder.build();
		this.whiteFields = whiteFieldsBuilder.build();
		this.blackFields = blackFieldsBuilder.build();
	}
	
	public UIPoint getImageCoords(BufferedImage image, int x, int y) {
		return new UIPoint(x - image.getWidth() / 2, y - image.getHeight() / 2);
	}
	
	public UIRect getFieldRect(Field field, Player player) {
		return getFields(player).get(field);
	}
	
	public boolean isOnBoard(int x, int y) {
		return x > lowX && x < highX && y > lowY && y < highY;
	}
	
	public Field resolveField(int x, int y, Player player) {
		final int boardX = getBoardX(x);
		final int boardY = getBoardY(y);
		return player.isWhite() ? Field.resolve(boardX, boardY) : Field.resolve(SIZE - 1 - boardX, SIZE - 1 - boardY);
	}
	
	public int getBoardX(int x) {
		return (int) ((x - lowX) / xSize);
	}
	
	public int getBoardY(int y) {
		return SIZE - 1 - (int) ((y - lowY) / ySize);
	}
	
	public UIPoint getFieldCenter(Field field, Player player) {
		return getFieldCenters(player).get(field);
	}
	
	public UIArrow arrow(Move move, Player player) {
		final UIPoint from = getFieldCenter(move.from(), player);
		final UIPoint to = getFieldCenter(move.to(), player);
		
		return new UIArrow(from, to);
	}

	private Map<Field, UIPoint> getFieldCenters(Player player) {
		return player.isWhite() ? whiteFieldCenters : blackFieldCenters;
	}

	private Map<Field, UIRect> getFields(Player player) {
		return player.isWhite() ? whiteFields : blackFields;
	}
}
