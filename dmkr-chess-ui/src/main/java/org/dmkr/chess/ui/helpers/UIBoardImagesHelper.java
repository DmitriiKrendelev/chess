package org.dmkr.chess.ui.helpers;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.imageio.ImageIO;

import org.dmkr.chess.api.model.ColoredItem;
import org.dmkr.chess.ui.config.UIBoardConfig;

import com.google.inject.Inject;

import lombok.Getter;

import static com.google.common.collect.ImmutableMap.copyOf;
import static java.util.stream.Collectors.toMap;
import static java.awt.Toolkit.getDefaultToolkit;


public class UIBoardImagesHelper {
	private static final String FILE_EXT_PNG = ".png";
	private static final String OPEN_HAND_CURSOR = "open_hand";
	private static final String CLOSED_HAND_CURSOR = "closed_hand";
	
	private final String itemsFolderPath;

	@Getter
	private final BufferedImage backgroundImage;
	private final Map<ColoredItem, BufferedImage> itemsCache;
	
	@Getter
	private final Cursor openHandCursor, closedHandCursor, defaultCursor;
	
	@Inject
	public UIBoardImagesHelper(UIBoardConfig config) {
		this.itemsFolderPath = config.getItemsFolderPath();
		this.backgroundImage = load(config.getBackGroundPath());
		
		this.itemsCache = copyOf(ColoredItem.stream().collect(toMap(item -> item, this::load)));
		final BufferedImage openHandImage = load(config.getCursorsFolderPath() + OPEN_HAND_CURSOR + FILE_EXT_PNG);
		final BufferedImage closedHandImage = load(config.getCursorsFolderPath() + CLOSED_HAND_CURSOR + FILE_EXT_PNG);
		
		openHandCursor = getDefaultToolkit().createCustomCursor(openHandImage, center(openHandImage), OPEN_HAND_CURSOR);
		closedHandCursor = getDefaultToolkit().createCustomCursor(closedHandImage, center(closedHandImage), CLOSED_HAND_CURSOR);
		defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	}
	
	public BufferedImage getImage(ColoredItem coloredItem) {
		return itemsCache.get(coloredItem);
		
	}
	
	private BufferedImage load(ColoredItem coloredItem) {
		return load(itemsFolderPath + getImageName(coloredItem));
	}
	
	private BufferedImage load(String path) {
		try {
			return ImageIO.read(getClass().getClassLoader().getResource(path));
		} catch (Exception e) {
			throw new RuntimeException("Cannot load image: " + path, e);
		}
	}
	
	private Point center(BufferedImage image) {
		return new Point(image.getWidth() / 2 , image.getWidth() / 2);
	}
	
	private String getImageName(ColoredItem coloredItem) {
		return coloredItem.color().name().toLowerCase() + "_" + coloredItem.item().name().toLowerCase() + FILE_EXT_PNG;
	}
}
