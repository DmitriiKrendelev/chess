package org.dmkr.chess.engine.minimax;

import static java.lang.System.currentTimeMillis;

import static org.dmkr.chess.api.utils.MoveUtils.valueOf;
import static java.util.Comparator.comparing;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.api.ProgressProvider;

import com.google.common.util.concurrent.AtomicDouble;

import static com.google.common.collect.ImmutableSortedSet.copyOf;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Preconditions.checkNotNull;

public class ProgressProviderImpl implements ProgressProvider {
	private final AtomicDouble currentProgressPercent = new AtomicDouble();
	private final AtomicBoolean inProgress = new AtomicBoolean();
	private final AtomicLong start = new AtomicLong();
	private final AtomicLong time = new AtomicLong();
	private final AtomicLong finish = new AtomicLong();
	private final AtomicLong currentCount = new AtomicLong();
	private final AtomicLong speed = new AtomicLong();
	private final AtomicReference<Move> currentMove = new AtomicReference<>();
	private final List<BestLine> currentEvalution = new CopyOnWriteArrayList<>();
	private SortedSet<BestLine> finalEvalution = null;
	
	private static final Comparator<BestLine> linesComparator = comparing(BestLine::getLineValue).thenComparing(bestLine -> valueOf(bestLine.getMoves().get(0))).reversed();
	
	private static final Move NOT_IN_PROGRESS_MOVE = new Move() {
		@Override 
		public String toString() {
			return "Not in progress";
		}
	};
	
	void start() {
		checkState(!isInProgress());
		cleanup();
		start.set(currentTimeMillis());
		finish.set(0);
		inProgress.set(true);
	}
	
	void finish() {
		checkState(isInProgress());
		time.set(getCurrentTimeInProgress());
		finish.set(currentTimeMillis());
		currentProgressPercent.set(1d);
		currentMove.set(NOT_IN_PROGRESS_MOVE);
		finalEvalution = checkNotNull(getCurrentEvaluation());
		inProgress.set(false);
		currentEvalution.clear();
	}
	
	void cleanup() {
		currentProgressPercent.set(0d);
		inProgress.set(false);
		time.set(0);
		start.set(0);
		currentCount.set(0);
		finalEvalution = null;
		currentEvalution.clear();
	}
	
	void update(BestLine bestLine, double progressStep) {
		currentEvalution.add(bestLine);
		currentProgressPercent.addAndGet(progressStep);
		
		final double secs = getCurrentTimeInProgress() / 1000d;
		speed.set((long) (currentCount.get() / secs));
	}
	
	void update(Move nextMove) {
		this.currentMove.set(nextMove);
	}
	
	void addCount(int count) {
		currentCount.addAndGet(count);
	}
	
	@Override
	public int getCurrentProgressPercents() {
		return (int) (currentProgressPercent.get() * 100);
	}

	@Override
	public int getCurrentTimeInProgress() {
		return (int) (isInProgress() ? (currentTimeMillis() - start.get()) : time.get());
	}
	
	@Override
	public long getEvaluationFinishedTime() {
		return finish.get();
	}

	@Override
	public long getCurrentCount() {
		return currentCount.get();
	}

	@Override
	public SortedSet<BestLine> getCurrentEvaluation() {
		return isInProgress() ? copyOf(linesComparator, this.currentEvalution) : finalEvalution;
	}

	@Override
	public Move getCurrentMove() {
		return currentMove.get();
	}

	@Override
	public boolean isInProgress() {
		return inProgress.get();
	}

	@Override
	public long getSpeed() {
		return speed.get();
	}
}
