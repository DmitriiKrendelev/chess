package org.dmkr.chess.api.utils;

import java.util.function.Function;

public interface MovesFilter extends Function<int[], int[]> {

    void addMove(int move);

    int[] build();

    @Override
    default int[] apply(int[] moves) {
        for (int move : moves) {
            addMove(move);
        }
        return build();
    }

}
