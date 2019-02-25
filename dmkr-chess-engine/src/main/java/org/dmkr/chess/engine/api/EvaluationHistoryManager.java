package org.dmkr.chess.engine.api;

import com.google.common.collect.ImmutableSortedSet;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.engine.minimax.BestLine;


public interface EvaluationHistoryManager<T extends BoardEngine> {

    static <T extends BoardEngine> EvaluationHistoryManager<T> newForgetHistoryManager() {
        return new EvaluationHistoryManager<T>() {
            @Override
            public void put(T board, ImmutableSortedSet<BestLine> evaluation) {
            }

            @Override
            public ImmutableSortedSet<BestLine> get(T board) {
                return null;
            }

            @Override
            public ImmutableSortedSet<BestLine> getAfterMove(T board, int move) {
                return null;
            }
        };
    }

    void put(T board, ImmutableSortedSet<BestLine> evaluation);

    ImmutableSortedSet<BestLine> get(T board);

    ImmutableSortedSet<BestLine> getAfterMove(T board, int move);

}
