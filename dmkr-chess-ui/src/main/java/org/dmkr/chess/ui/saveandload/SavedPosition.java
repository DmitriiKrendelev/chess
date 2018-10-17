package org.dmkr.chess.ui.saveandload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.dmkr.chess.api.BoardEngine;
import org.dmkr.chess.api.model.Color;

import java.io.*;

@AllArgsConstructor
@ToString
@Getter
public class SavedPosition implements Serializable {
    private Color color;
    private BoardEngine board;

}
