package org.dmkr.chess.ui.saveandload;

import com.google.inject.Inject;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.ui.Player;
import org.dmkr.chess.ui.api.model.UIPoint;
import org.dmkr.chess.ui.api.model.UIRect;
import org.dmkr.chess.ui.config.UIBoardConfig;
import org.dmkr.chess.ui.helpers.UIBoardCoordsHelper;
import org.dmkr.chess.ui.helpers.UIBoardImagesHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Optional;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static org.dmkr.chess.api.model.Constants.SIZE;

public class LoadPositionPreviewPanel extends JPanel implements PropertyChangeListener {
    private static final int IMG_FIELD_SIZE = 25;
    private static final int IMG_SIZE = SIZE * IMG_FIELD_SIZE;

    private final Player player;
    private final SaveAndLoadPositionManager saveAndLoadPositionManager;
    private final UIBoardImagesHelper imagesHelper;
    private final UIBoardConfig uiBoardConfig;
    private final UIBoardCoordsHelper coordsHelper;
    private Optional<ImageIcon> icon = Optional.empty();

    @Inject
    private LoadPositionPreviewPanel(
            Player player,
            SaveAndLoadPositionManager saveAndLoadPositionManager,
            UIBoardImagesHelper imagesHelper,
            UIBoardConfig uiBoardConfig,
            UIBoardCoordsHelper coordsHelper) {
        this.player = player;
        this.saveAndLoadPositionManager = saveAndLoadPositionManager;
        this.imagesHelper = imagesHelper;
        this.uiBoardConfig = uiBoardConfig;
        this.coordsHelper = coordsHelper;
        setPreferredSize(new Dimension(IMG_SIZE, IMG_SIZE));
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        final String propertyName = e.getPropertyName();
        if (!propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            return;
        }

        final File file = (File) e.getNewValue();
        this.icon = createIcon(file);
        if (!icon.isPresent()) {
            return;
        }

        repaint();
    }

    public void paintComponent(Graphics g) {
        if (!icon.isPresent()) {
            return;
        }
        final ImageIcon icon = this.icon.get();

        g.fillRect(0, 0, IMG_SIZE, IMG_SIZE);
        g.drawImage(icon.getImage(), 0, 0, this);
    }

    private Optional<ImageIcon> createIcon(File file) {
        final SavedPosition savedPosition = saveAndLoadPositionManager.getPosition(file).orElse(null);
        if (savedPosition == null) {
            return Optional.empty();
        }

        final BoardEngine board = savedPosition.getBoard();
        final Player player = this.player.withColor(savedPosition.getColor());

        final BufferedImage boardImage = getBoardImage(player);
        final Graphics2D boardGraphics = boardImage.createGraphics();
        final int offsetX = uiBoardConfig.getBoardCoords().x();
        final int offsetY = uiBoardConfig.getBoardCoords().y();

        board.forEach(
                (field, coloredPiece) -> {
                    if (coloredPiece.isNull()) {
                        return;
                    }

                    final BufferedImage pieceImage = imagesHelper.getImage(coloredPiece);

                    final UIPoint fieldCenter = coordsHelper.getFieldCenter(field, player).minus(offsetX, offsetY);
                    final UIPoint imagePoint = coordsHelper.getImageCoords(pieceImage, fieldCenter.x(), fieldCenter.y());

                    boardGraphics.drawImage(pieceImage, imagePoint.x(), imagePoint.y(), this);
                }
        );

        boardGraphics.dispose();
        final BufferedImage scaledImage = scaleBoardTransformation(boardImage, IMG_SIZE, IMG_SIZE);
        // final BufferedImage rotatedImage = player.isWhite() ? scaledImage : invertBoardTransformation(scaledImage);

        return Optional.of(new ImageIcon(scaledImage));
    }

    private static BufferedImage scaleBoardTransformation(BufferedImage image, int newW, int newH) {
        final AffineTransform at = new AffineTransform();
        at.setToScale(((double) newW) / image.getWidth(), ((double) newH) / image.getHeight());
        final AffineTransformOp ato =  new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        final BufferedImage result = new BufferedImage(newW, newH, TYPE_4BYTE_ABGR);
        ato.filter(image, result);
        return result;
    }

    private static BufferedImage invertBoardTransformation(BufferedImage image) {
        final AffineTransform at = new AffineTransform();
        at.setToRotation(Math.PI, ((double) image.getWidth()) / 2, ((double) image.getHeight()) / 2);
        final AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

        final BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), TYPE_4BYTE_ABGR);
        ato.filter(image, result);
        return result;
    }

    private BufferedImage getBoardImage(Player player) {
        final UIRect uiRect = uiBoardConfig.getBoardCoords();
        final BufferedImage backgroundImage = imagesHelper.getBackgroundImage(player.getColor());
        final BufferedImage boardImage = new BufferedImage(uiRect.getWidth(), uiRect.getHight(), TYPE_4BYTE_ABGR);
        final BufferedImage backgroundSubImage = backgroundImage.getSubimage(uiRect.x(), uiRect.y(), uiRect.getWidth(), uiRect.getHight());
        backgroundSubImage.copyData(boardImage.getRaster());

        return boardImage;
    }
}
