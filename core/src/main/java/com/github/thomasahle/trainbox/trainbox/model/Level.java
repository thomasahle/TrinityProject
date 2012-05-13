package com.github.thomasahle.trainbox.trainbox.model;

import java.util.Collections;
import java.util.List;

public class Level {
	public final List<Train> input, goal;
	public final int levelNumber;
	public Level(int levelNumber, List<Train> input, List<Train> goal) {
		this.levelNumber=levelNumber;
		this.input = Collections.unmodifiableList(input);
		this.goal = Collections.unmodifiableList(goal);
	}
	
}
