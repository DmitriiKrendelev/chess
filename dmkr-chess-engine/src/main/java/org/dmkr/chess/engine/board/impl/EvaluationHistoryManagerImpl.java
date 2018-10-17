package org.dmkr.chess.engine.board.impl;

import com.google.common.collect.ImmutableSortedSet;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.api.EvaluationHistoryManager;
import org.dmkr.chess.engine.minimax.BestLine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class EvaluationHistoryManagerImpl<T extends BoardEngine> implements EvaluationHistoryManager<T> {
    private final Map<T, ImmutableSortedSet<BestLine>> cache = new ConcurrentHashMap<>();
    private final Map<T, ImmutableSortedSet<BestLine>> afterMoveCache = new ConcurrentHashMap<>();
    private final AtomicLong cacheAsk = new AtomicLong();
    private final AtomicLong cacheGotResult = new AtomicLong();


    @Override
    public void put(T board, ImmutableSortedSet<BestLine> evaluation) {
        cache.put((T) board.cloneDummy(), evaluation);

        final int move = board.valueOf(evaluation.first().getMoves().get(0));
        board.applyMove(move);
        afterMoveCache.put((T) board.cloneDummy(), evaluation);
        board.rollbackMove();
    }

    @Override
    public ImmutableSortedSet<BestLine> get(T board) {
        final ImmutableSortedSet<BestLine> cached = cache.get(board);
        cacheAsk.incrementAndGet();

        if (cached != null) {
            cacheGotResult.incrementAndGet();
        }

        return cached;
    }

    @Override
    public ImmutableSortedSet<BestLine> getAfterMove(T board, int move) {
        board.applyMove(move);
        cacheAsk.incrementAndGet();
        final ImmutableSortedSet<BestLine> cached = afterMoveCache.get(board);
        board.rollbackMove();

        if (cached != null) {
            cacheGotResult.incrementAndGet();
        }

        return cached;
    }

    @Override
    public String toString() {
        return "Cache [Ask: " + cacheAsk + " Got: " + cacheGotResult + "]";
    }
}
