package org.dmkr.chess.engine.minimax;

import static java.lang.Integer.MAX_VALUE;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class MoveValue {
	private int move;
	private int value;
	
	MoveValue resetToMax() {
		this.move = 0;
		this.value = MAX_VALUE;
		return this;
	}
	
	boolean minimize(int move, int value) {
		if (this.value > value) {
			this.value = value;
			this.move = move;

			return true;
		}
		
		return false;
	}
}
