package org.dmkr.chess.ui;

import lombok.Builder;
import lombok.Getter;
import org.dmkr.chess.api.model.Color;

@Builder(builderMethodName = "player")
@Getter
public class Player {
    private final String name;
    private final Color color;

    public static Player.PlayerBuilder player() {
        return new Player.PlayerBuilder();
    }

    public boolean isWhite() {
        return color == Color.WHITE;
    }
}
