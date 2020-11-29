package org.dmkr.chess.engine.minimax;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.RequiredArgsConstructor;
import org.dmkr.chess.api.model.Move;
import org.dmkr.chess.engine.api.ProgressProvider;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.ImmutableSortedSet.*;
import static java.lang.System.*;
import static java.util.Comparator.*;
import static org.dmkr.chess.api.utils.MoveUtils.*;

@RequiredArgsConstructor
public class ProgressProviderImpl implements ProgressProvider {
	private final AtomicDouble currentProgressPercent = new AtomicDouble();
	private final AtomicBoolean inProgress = new AtomicBoolean();
	private final AtomicLong start = new AtomicLong();
	private final AtomicLong time = new AtomicLong();
	private final AtomicLong finish = new AtomicLong();
	private final AtomicLong currentCount = new AtomicLong();
	private final AtomicLong speed = new AtomicLong();
	private final AtomicLong currentTotalCalculationTime = new AtomicLong();
	private final AtomicReference<Move> currentMove = new AtomicReference<>();
	private final List<BestLine> currentEvalution = new CopyOnWriteArrayList<>();
	private SortedSet<BestLine> finalEvalution = null;

	private final AtomicLong fullCount = new AtomicLong();
	private final AtomicLong fullTime = new AtomicLong();
	private final AtomicLong fullTotalTime = new AtomicLong();

	private final AtomicBoolean inPaused = new AtomicBoolean();
	private final AtomicLong pausedTime = new AtomicLong();
	private final AtomicLong pauseStart = new AtomicLong();

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
		currentTotalCalculationTime.set(0);
		inProgress.set(true);
	}
	
	void finish() {
		checkState(isInProgress());
		time.set(getCurrentTimeInProgress());
		finish.set(currentTimeMillis());
		currentProgressPercent.set(1d);
		currentMove.set(NOT_IN_PROGRESS_MOVE);
		finalEvalution = Optional.ofNullable(finalEvalution).orElseGet(() -> checkNotNull(getCurrentEvaluation()));
		currentEvalution.clear();
		inProgress.set(false);
		inPaused.set(false);

		fullTime.addAndGet(time.get());
		fullCount.addAndGet(currentCount.get());
		fullTotalTime.addAndGet(currentTotalCalculationTime.get());
	}
	
	void cleanup() {
		currentProgressPercent.set(0d);
		inProgress.set(false);
		time.set(0);
		start.set(0);
		currentCount.set(0);
		finalEvalution = null;
		currentEvalution.clear();
		currentTotalCalculationTime.set(0);

		inPaused.set(false);
		pausedTime.set(0);
		pauseStart.set(0);
	}

	void setFinalEvalution(SortedSet<BestLine> finalEvalution) {
		checkState(this.finalEvalution == null);
		this.finalEvalution = finalEvalution;
	}
	
	void update(BestLine bestLine, double progressStep) {
		currentEvalution.add(bestLine);
		currentProgressPercent.addAndGet(progressStep);
		
		final double secs = getCurrentTimeInProgress() / 1000d;
		speed.set((long) (currentCount.get() / secs));
		currentTotalCalculationTime.addAndGet(bestLine.getDuration());
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
		if (inPaused.get()) {
			return (int) (pauseStart.get() - start.get() - pausedTime.get());
		}

		return (int) (isInProgress() ? (currentTimeMillis() - start.get() - pausedTime.get()) : time.get());
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

	@Override
	public double getParallelLevel() {
		return ((double) currentTotalCalculationTime.get()) / ((double) getCurrentTimeInProgress());
	}

	@Override
	public long getFullCount() {
		return fullCount.get();
	}

	@Override
	public long getFullTime() {
		return fullTime.get();
	}

	@Override
	public long getFullTotalTime() {
		return fullTotalTime.get();
	}

	@Override
	public long getFullSpeed() {
		final double secs = getFullTime() / 1000d;
		return (long) (fullCount.get() / secs);
	}

	@Override
	public double getFullParallelLevel() {
		return ((double) getFullTotalTime()) / ((double) getFullTime());
	}

	@Override
	public void pause() {
		inPaused.set(true);
		pauseStart.set(currentTimeMillis());
	}

	@Override
	public void resume() {
		inPaused.set(false);
		pausedTime.addAndGet(currentTimeMillis() - pauseStart.get());
	}

	@Override
	public boolean isPaused() {
		return inPaused.get();
	}
}
