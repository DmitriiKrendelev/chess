package org.dmkr.chess.ui.helpers;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_HOUR;
import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_MINUTE;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDuration;
import static org.dmkr.chess.engine.function.EvaluationFunctionUtils.getEvaluationDetails;
import static org.apache.commons.lang3.tuple.ImmutablePair.of;
import static java.util.Collections.emptyList;
import static java.lang.Double.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.api.ProgressProvider;
import org.dmkr.chess.engine.minimax.BestLine;
import org.dmkr.chess.ui.api.model.UIPoint;
import org.dmkr.chess.ui.api.model.UIPointMutable;
import org.dmkr.chess.ui.api.model.UIText;
import org.dmkr.chess.ui.api.model.UITextBlock;
import org.dmkr.chess.ui.api.model.UITextBlock.UITextBlockBuilder;
import org.dmkr.chess.ui.config.UIBoardConfig;
import org.dmkr.chess.ui.listeners.BestLineVisualizerListener;

import com.google.inject.Inject;

public class UIBoardTextHelper {
	private static final int MAX_BEST_LINES = 3;
	private final UIMousePositionHelper mousePositionHelper;
	private final BestLineVisualizerListener bestLineVisualizerListener;
	private final Color textColor;
	private final Color focusedTextColor;
	private final Color focusedAreaColor;
	private final UIPoint textPosition;
	private final Font textStyle;
	
	private final DecimalFormat longFormat = new DecimalFormat("###,###,###,###");
	private final DecimalFormat valueFormat = new DecimalFormat("###.##");
	private final DecimalFormat parallelLevelFormat = new DecimalFormat("##.##");
	private static final String durationFormatHMS = "H'h' mm'min' ss'sec' SSS'ms'";
	private static final String durationFormatMS = "mm'min' ss'sec' SSS'ms'";
	private static final String durationFormatS = "ss'sec' SSS'ms'";
	
	@Inject
	public UIBoardTextHelper(UIBoardConfig config, UIMousePositionHelper mousePositionHelper, BestLineVisualizerListener bestLineVisualizerListener) {
		this.mousePositionHelper = mousePositionHelper;
		this.bestLineVisualizerListener = bestLineVisualizerListener;
		this.textColor = config.getTextColor();
		this.focusedTextColor = config.getFocusedTextColor();
		this.focusedAreaColor = config.getFocusedAreaColor();
		this.textPosition = config.getTextPosition();
		this.textStyle = config.getTextStyle();
	}
	
	private final AtomicReference<Pair<Move, List<UITextBlock>>> progressTextCache = new AtomicReference<>(of(null, emptyList()));
	private final AtomicReference<Pair<Integer, List<UITextBlock>>> evaluationTextCache = new AtomicReference<>(of(-1, emptyList()));
	
	public void drawSingleRow(String text, int x, int y, Graphics2D g) {
		g.setColor(textColor);
		new UIText(text).draw(g, textStyle, x, y);
	}

	public void drawText(AsyncEngine<BoardEngine> engine, BoardEngine board, Graphics2D g) {
		final ArrayList<UITextBlock> result = new ArrayList<>();
		
		result.addAll(getEvaluationText(engine, board));
		result.addAll(getProgressText(engine, board, g));

		final UIPointMutable mutableTextPosition = new UIPointMutable(textPosition, x -> x, y -> y + textStyle.getSize());
		result.forEach(uiTextBlock -> uiTextBlock.draw(g, mutableTextPosition));
	}
	
	private List<UITextBlock> getEvaluationText(AsyncEngine<BoardEngine> engine, BoardEngine board) {
		return evaluationTextCache.updateAndGet(cached -> {
			final int boardMoveNumber = board.moveNumber();
			return cached.getKey() == boardMoveNumber ? cached : of(boardMoveNumber, singletonList(getEvaluationTextImpl(engine, board)));
		}).getValue();
	}
	
	private UITextBlock getEvaluationTextImpl(AsyncEngine<BoardEngine> engine, BoardEngine board) {
		final UITextBlockBuilder builder = uiTextBlockBuilder();
		builder.textLine("Position Evaluation :");
		final BoardEngine boardCopy = board.clone();
		
		boardCopy.invert();
		getEvaluationDetails(engine.getEvaluationFunction(), boardCopy).forEach((name, value) -> {
			if (StringUtils.isNotBlank(name)) {
				builder.textLine("     " + name + " : " + formatValue(value));
			}
		});
		
		builder.textLine(EMPTY);
		builder.textLine(EMPTY);
		
		builder.isFocusable(false);
		
		return builder.build(); 
	}
	
	private List<UITextBlock> getProgressText(AsyncEngine<BoardEngine> engine, BoardEngine board, Graphics2D g) {
		final List<UITextBlock> currentProgressText = progressTextCache.updateAndGet((Pair<Move, List<UITextBlock>> cache) -> {
			final Move cacheKey = cache.getKey();
			final Move currentMove = engine.getCurrentMove();

			if (currentMove == null) {
				return of(null, Collections.emptyList());
			}

			return cacheKey == currentMove ? cache : of(currentMove, getProgressTextImpl(engine));
		}).getValue();

		final UITextBlock timeProgress = uiTextBlockBuilder()
				.textLine("Time : " + formatDurationMillis(engine.getCurrentTimeInProgress()))
				.isFocusable(false)
				.build();
				
		final List<UITextBlock> result = new ArrayList<>(currentProgressText);
		result.add(timeProgress);
		return result;
	}
	
	private List<UITextBlock> getProgressTextImpl(ProgressProvider progress) {
		final List<UITextBlock> result = new ArrayList<>();
		final UITextBlockBuilder builder = uiTextBlockBuilder();

		builder.textLine("Engine Progress :");
		builder.textLine(EMPTY);
		builder.textLine("Count : " + longFormat.format(progress.getCurrentCount()));
		builder.textLine("Speed : " + longFormat.format(progress.getSpeed()) + " per sec");
		builder.textLine("Parallel Level : " + formatParallelLevel(progress.getParallelLevel()));
		builder.textLine(EMPTY);
		builder.textLine("Evaluation : ");
		builder.isFocusable(false);
		result.add(builder.build());
		
		int index = 0;
		final SortedSet<BestLine> currentEvaluation = progress.getCurrentEvaluation();
		if (currentEvaluation == null) {
			return result;
		}

		final boolean isInProgress = progress.isInProgress();
		if (!isInProgress) {
			builder.isFocusable(true);
		}
		for (BestLine bestLine : currentEvaluation) {
			if (index ++ == MAX_BEST_LINES) {
				break;
			}
			
			builder.textLine(index + ". Moves Line : " + formatValue(bestLine.getLineValue()) + " [" + formatValue(bestLine.getLineValueChange()) + "]");
			final Iterator<Move> moves = bestLine.getMoves().iterator();
			int i = 0;
			while (moves.hasNext()) {
				builder.textLine("     " + (++ i) + ". " + moves.next() + " : " + (moves.hasNext() ? moves.next() : "..."));
			}
			
			builder.textLine(EMPTY);
			
			final UITextBlock uiTextBlock = builder.build();

			if (!isInProgress) {
				bestLineVisualizerListener.registerBestLine(bestLine, uiTextBlock);
			}

			result.add(uiTextBlock);
		}
		
		return result;
	}
	
	private String formatDurationMillis(long durtion) {
		if (durtion > MILLIS_PER_HOUR) {
			return formatDuration(durtion, durationFormatHMS);
		} else if (durtion > MILLIS_PER_MINUTE) {
			return formatDuration(durtion, durationFormatMS);
		} else {
			return formatDuration(durtion, durationFormatS);
		}
	}

	private String formatParallelLevel(double parallelLevel) {
		return isNaN(parallelLevel) || isInfinite(parallelLevel) ? "" : parallelLevelFormat.format(parallelLevel);
	}
	
	private String formatValue(int value) {
		return (value > 0 ? "+" : "") + valueFormat.format(value / 100d);
	}
	
	private UITextBlockBuilder uiTextBlockBuilder() {
		return UITextBlock.builder()
				.mousePositionHelper(mousePositionHelper)
				.textColor(textColor)
				.focusedTextColor(focusedTextColor)
				.focusedAreaColor(focusedAreaColor)
				.textStyle(textStyle);
	}
}
