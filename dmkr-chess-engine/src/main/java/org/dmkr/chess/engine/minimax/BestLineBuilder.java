package org.dmkr.chess.engine.minimax;

import static org.dmkr.chess.common.primitives.Ints.reset;
import static org.dmkr.chess.engine.minimax.BestLine.newBestLine;

import java.util.List;
import java.util.stream.IntStream;

import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.common.collections.ValueAutoCreateList;
import org.dmkr.chess.common.primitives.IntStack;

import lombok.Setter;

@Setter
class BestLineBuilder {
	private final int[] nullBestLine;
	private final List<int[]> bestLine;
	private final IntStack currentPath;
	private boolean inverted;
	private int initialPositionValue;
	private int lineValue;
	private long duration;
	
	BestLineBuilder(int maxBestLineLenght, IntStack currentPath) {
		this.bestLine = new ValueAutoCreateList<>(() -> new int[maxBestLineLenght]);
		this.nullBestLine = new int[maxBestLineLenght];
		this.currentPath = currentPath;
	}
		
	void updateZeroDeep() {
		update(true, false, 1);
	}

	void update(boolean optimized, boolean isLeaf, int deep) {
		if (optimized || isLevelEmpty(deep - 1)) {
			if (isLeaf) { 
				currentLineToBestLine(deep - 1);
			} else {
				previousLevelLineToBestLine(deep);
			}
		}
	}
	
	private void currentLineToBestLine(int deep) {
		final int[] bestLineMoves = reset(bestLine.get(deep), nullBestLine);
		currentPath.copy(bestLineMoves);
	}
	
	private void previousLevelLineToBestLine(int deep) {
		reset(bestLine.get(deep - 1), bestLine.get(deep));
	}

	void clear() {
		for (int[] levelBestLine : bestLine) {
			reset(levelBestLine, nullBestLine);
		}
		
		lineValue = -1;
	}
	
	private boolean isLevelEmpty(int deep) {
		return bestLine.get(deep)[0] == 0;
	}
	
	BestLine build() {
		return newBestLine(bestLine.get(0), inverted, lineValue, lineValue - initialPositionValue, duration);
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("BestLine: " + lineValue);
		final boolean inverted = this.inverted;
		
		for (int i = 0; i < bestLine.size(); i ++) {
			sb.append("\n" + i + ". ");
			IntStream.of(bestLine.get(i)).filter(move -> move != 0).mapToObj(move -> Move.moveOf(move, inverted) + " ").forEach(sb::append);
		}
		
		return sb.toString();
	}
}
