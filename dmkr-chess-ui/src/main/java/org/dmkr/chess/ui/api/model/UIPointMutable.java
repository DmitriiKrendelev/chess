package org.dmkr.chess.ui.api.model;

import java.util.function.IntUnaryOperator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UIPointMutable {
	private UIPoint uiPoint;
	private final IntUnaryOperator xMutator;
	private final IntUnaryOperator yMutator;
	
	public UIPoint moveAndGet() {
		return uiPoint = new UIPoint(xMutator.applyAsInt(uiPoint.x()), yMutator.applyAsInt(uiPoint.y()));
	}
	
	
}
