package org.dmkr.chess.ui;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Wither;
import org.dmkr.chess.api.Board;
import org.dmkr.chess.api.model.Color;

@Builder(builderMethodName = "player")
@Wither
@Getter
public class Player {
    private final String name;
    private final Color color;
    private final boolean isReadOnly;

    public static Player.PlayerBuilder player() {
        return new Player.PlayerBuilder();
    }

    public boolean isWhite() {
        return color == Color.WHITE;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public boolean isBoardInvertedForPlayer(Board board) {
        return board.isInverted() == isWhite();
    }
}
