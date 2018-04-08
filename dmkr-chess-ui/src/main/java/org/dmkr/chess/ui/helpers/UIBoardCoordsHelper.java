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
	private final Map<Field, UIPoint> fieldCenters;
	private final Map<Field, UIRect> fields;
	private final boolean isWhite;

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
		
		public MovingPiece(Move move, Board board, Procedure onFinish) {
			this(move, board.at(move.from()), onFinish);
		}
		
		public MovingPiece(Move move, ColoredPiece coloredPiece, Procedure onFinish) {
			this.from = move.from();
			this.to = move.to();
			this.fromPoint = fieldCenters.get(from);
			this.toPoint = fieldCenters.get(to);
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
	public UIBoardCoordsHelper(UIBoardConfig config, Player player) {
		final UIRect boardCoords = config.getBoardCoords();
		
		this.lowX = boardCoords.x();
		this.lowY = boardCoords.y();
		this.highX = boardCoords.x() + boardCoords.getWidth();
		this.highY = boardCoords.y() + boardCoords.getHight();
		this.pieceMovingSpeed = config.getPieceMovingSpeed();
		
		this.xSize = (double) (highX - lowX) / SIZE;
		this.ySize = (double) (highY - lowY) / SIZE;
		this.isWhite = player.isWhite();

		final ImmutableMap.Builder<Field, UIPoint> fieldCentersBuilder = ImmutableMap.builder();
		final ImmutableMap.Builder<Field, UIRect> fieldsBuilder = ImmutableMap.builder();
		for (int x = 0; x < SIZE; x ++) {
			for (int y = 0; y < SIZE; y ++) {
				final double leftUpperX = lowX + xSize * x;
				final double leftUpperY = lowY + ySize * y;
				final double centerX = leftUpperX + xSize * 0.5d;
				final double centerY = leftUpperY + ySize * 0.5d;
				
				final UIPoint fieldCenter = new UIPoint(centerX, centerY);
				final UIRect fieldRect = newRect(leftUpperX, leftUpperY, xSize, ySize);
				
				final Field field = isWhite ? Field.resolve(x, SIZE - 1 - y) : Field.resolve(SIZE - 1 - x, y);
				fieldCentersBuilder.put(field, fieldCenter);
				fieldsBuilder.put(field, fieldRect);
			}
		}
		this.fieldCenters = fieldCentersBuilder.build();
		this.fields = fieldsBuilder.build();
	}
	
	public UIPoint getImageCoords(BufferedImage image, int x, int y) {
		return new UIPoint(x - image.getWidth() / 2, y - image.getHeight() / 2);
	}
	
	public UIRect getFieldRect(Field field) {
		return fields.get(field);
	}
	
	public boolean isOnBoard(int x, int y) {
		return x > lowX && x < highX && y > lowY && y < highY;
	}
	
	public Field resolveField(int x, int y) {
		final int boardX = getBoardX(x);
		final int boardY = getBoardY(y);
		return isWhite ? Field.resolve(boardX, boardY) : Field.resolve(SIZE - 1 - boardX, SIZE - 1 - boardY);
	}
	
	public int getBoardX(int x) {
		return (int) ((x - lowX) / xSize);
	}
	
	public int getBoardY(int y) {
		return SIZE - 1 - (int) ((y - lowY) / ySize);
	}
	
	public UIPoint getFieldCenter(Field field) {
		return fieldCenters.get(field);
	}
	
	public UIArrow arrow(Move move) {
		final UIPoint from = getFieldCenter(move.from());
		final UIPoint to = getFieldCenter(move.to());
		
		return new UIArrow(from, to);
	}
}
