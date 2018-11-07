package org.dmkr.chess.ui.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dmkr.chess.api.model.Field;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.ui.Player;
import org.dmkr.chess.ui.UIBoardJComponent;
import org.dmkr.chess.ui.api.model.UIPoint;
import org.dmkr.chess.ui.helpers.UIBoardCoordsHelper;
import org.dmkr.chess.ui.helpers.UIMousePositionHelper;

@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @_(@Inject))
public class PiecesDragAndDropListener extends MouseAdapter {
	private final AtomicBoolean mousePressed = new AtomicBoolean();
	private final AtomicReference<Field> pressedField = new AtomicReference<>();
	
	@Inject private UIBoardCoordsHelper coordsHelper;
	@Inject private UIMousePositionHelper mousePositionHelper;
	@Inject private UIBoardJComponent uiComponent;
	@Inject private Player player;

	@Override
	public void mousePressed(MouseEvent e) {
		final UIPoint uiPoint = mousePositionHelper.getMouseLocation();

		final int x = uiPoint.x();
		final int y = uiPoint.y();

		if (!coordsHelper.isOnBoard(x, y)) {
			return;
		}
		final Field field = coordsHelper.resolveField(x, y, player);

		mousePressed.set(true);
		pressedField.set(field);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		final UIPoint uiPoint = mousePositionHelper.getMouseLocation();

		final int x = uiPoint.x();
		final int y = uiPoint.y();
		
		final Field fromField = this.pressedField.get();
		mousePressed.set(false);
		pressedField.set(null);
		if (!coordsHelper.isOnBoard(x, y)) {
			return;
		}
		
		final Field toField = coordsHelper.resolveField(x, y, player);
		uiComponent.onMove(Move.moveOf(fromField, toField));
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		uiComponent.repaint();
	}
	
	public boolean isPressed() {
		return mousePressed.get();
	}
	
	public Field getPressedField() {
		return pressedField.get();
	}
	
	public Field getMouseAtField() {
		final UIPoint mousePosition = mousePositionHelper.getMouseLocation();
		final int x = mousePosition.x();
		final int y = mousePosition.y();
		
		return coordsHelper.isOnBoard(x, y) ? coordsHelper.resolveField(x, y, player) : null;
	}
}
