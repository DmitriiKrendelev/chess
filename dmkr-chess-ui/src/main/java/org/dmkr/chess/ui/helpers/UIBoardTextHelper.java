package org.dmkr.chess.ui.helpers;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.api.AsyncEngine;
import org.dmkr.chess.engine.minimax.BestLine;
import org.dmkr.chess.ui.Player;
import org.dmkr.chess.ui.api.model.UIPoint;
import org.dmkr.chess.ui.api.model.UIPointMutable;
import org.dmkr.chess.ui.api.model.UIText;
import org.dmkr.chess.ui.api.model.UITextBlock;
import org.dmkr.chess.ui.api.model.UITextBlock.UITextBlockBuilder;
import org.dmkr.chess.ui.config.UIBoardConfig;
import org.dmkr.chess.ui.listeners.impl.BestLineVisualizerListener;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Double.*;
import static java.util.Collections.*;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.time.DateUtils.*;
import static org.apache.commons.lang3.time.DurationFormatUtils.*;
import static org.apache.commons.lang3.tuple.ImmutablePair.*;
import static org.dmkr.chess.engine.function.EvaluationFunctionUtils.*;

public class UIBoardTextHelper {
	private static final int MAX_BEST_LINES = 3;
	private final Player player;
	private final UIMousePositionHelper mousePositionHelper;
	private final BestLineVisualizerListener bestLineVisualizerListener;
	private final BoardEngine board;


	private final Color textColor;
	private final Color focusedTextColor;
	private final Color focusedAreaColor;
	private final Font textStyle;
	
	private final DecimalFormat longFormat = new DecimalFormat("###,###,###,###");
	private final DecimalFormat valueFormat = new DecimalFormat("###.##");
	private final DecimalFormat parallelLevelFormat = new DecimalFormat("##.##");
	private static final String durationFormatHMS = "H'h' mm'min' ss'sec' SSS'ms'";
	private static final String durationFormatMS = "mm'min' ss'sec' SSS'ms'";
	private static final String durationFormatS = "ss'sec' SSS'ms'";
	
	@Inject
	public UIBoardTextHelper(
	        Player player,
			UIBoardConfig config,
			UIMousePositionHelper mousePositionHelper,
			BestLineVisualizerListener bestLineVisualizerListener,
			BoardEngine board) {

		this.player = player;
	    this.mousePositionHelper = mousePositionHelper;
		this.bestLineVisualizerListener = bestLineVisualizerListener;
		this.board = board;

		this.textColor = config.getTextColor();
		this.focusedTextColor = config.getFocusedTextColor();
		this.focusedAreaColor = config.getFocusedAreaColor();
		this.textStyle = config.getTextStyle();
	}
	
	private final AtomicReference<Pair<Move, List<UITextBlock>>> progressTextCache = new AtomicReference<>(of(null, emptyList()));
	private final AtomicReference<EvaluationTextCacheKey> evaluationTextCache = new AtomicReference<>(new EvaluationTextCacheKey(-1, null, null));

	@RequiredArgsConstructor
	private static class EvaluationTextCacheKey {
		private final int halfMoves;
		private final AsyncEngine<BoardEngine> engine;
		private final List<UITextBlock> evaluationText;
	}

	public void drawSingleRow(String text, int x, int y, Graphics2D g) {
		g.setColor(textColor);
		new UIText(text).draw(g, textStyle, x, y);
	}

	public void drawText(Graphics2D g, AsyncEngine<BoardEngine> engine, UIPoint textPosition) {
		final ArrayList<UITextBlock> result = new ArrayList<>();
		
		result.addAll(getEvaluationText(engine));
		result.addAll(getProgressText(engine));

		final UIPointMutable mutableTextPosition = new UIPointMutable(textPosition, x -> x, y -> y + textStyle.getSize());
		result.forEach(uiTextBlock -> uiTextBlock.draw(g, mutableTextPosition));
	}
	
	private List<UITextBlock> getEvaluationText(AsyncEngine<BoardEngine> engine) {
		return evaluationTextCache.updateAndGet(cached -> {
			final int halfMoves = board.movesHistorySize();
			return cached.halfMoves == halfMoves && cached.engine == engine ? cached : new EvaluationTextCacheKey(halfMoves, engine, singletonList(calculateEvaluationText(engine)));
		}).evaluationText;
	}
	
	private UITextBlock calculateEvaluationText(AsyncEngine<BoardEngine> engine) {
		final UITextBlockBuilder builder = uiTextBlockBuilder();
		builder.textLine("Game :");
		builder.textLine("     " + board.moveNumber() + "-th move : " + (board.isInverted() ? "Black" : "White"));
		builder.textLine(EMPTY);


		builder.textLine("Static Position Evaluation :");
		final BoardEngine boardCopy = board.clone();
		if (player.isBoardInvertedForPlayer(boardCopy)) {
			boardCopy.invert();
		}

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
	
	private List<UITextBlock> getProgressText(AsyncEngine<BoardEngine> engine) {
		final List<UITextBlock> currentProgressText = progressTextCache.updateAndGet((Pair<Move, List<UITextBlock>> cache) -> {
			final Move cacheKey = cache.getKey();
			final Move currentMove = engine.getCurrentMove();

			if (currentMove == null) {
				return of(null, Collections.emptyList());
			}

			return cacheKey == currentMove ? cache : of(currentMove, calculateProgressText(engine));
		}).getValue();

		final UITextBlock timeProgress = uiTextBlockBuilder()
				.textLine("Time : " + formatDurationMillis(engine.getCurrentTimeInProgress()))
				.isFocusable(false)
				.build();
				
		final List<UITextBlock> result = new ArrayList<>(currentProgressText);
		result.add(timeProgress);
		return result;
	}
	
	private List<UITextBlock> calculateProgressText(AsyncEngine<BoardEngine> engine) {
		final List<UITextBlock> result = new ArrayList<>();
		final UITextBlockBuilder builder = uiTextBlockBuilder();

		builder.textLine("Engine Progress :");
		builder.textLine(EMPTY);
		builder.textLine("Count : " + longFormat.format(engine.getCurrentCount()));
		builder.textLine("Speed : " + longFormat.format(engine.getSpeed()) + " per sec");
		builder.textLine("Parallel Level : " + formatParallelLevel(engine.getParallelLevel()));
		builder.textLine(EMPTY);
		builder.textLine("Full Count : " + longFormat.format(engine.getFullCount()));
		builder.textLine("Full Speed : " + longFormat.format(engine.getFullSpeed()) + " per sec");
		builder.textLine("Full Parallel Level : " + formatParallelLevel(engine.getFullParallelLevel()));
		builder.textLine("Full Time : " + formatDurationMillis(engine.getFullTime()));
		builder.textLine(EMPTY);
		builder.textLine("Evaluation : ");
		builder.isFocusable(false);
		result.add(builder.build());
		
		int index = 0;
		final SortedSet<BestLine> currentEvaluation = engine.getCurrentEvaluation();
		if (currentEvaluation == null) {
			return result;
		}

		final boolean isInProgress = engine.isInProgress();
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

			builder.textLine("     time: " + (bestLine.isCached() ? "cached" : formatDurationMillis(bestLine.getDuration())));
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
