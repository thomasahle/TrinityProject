package com.github.thomasahle.trainbox.trainbox.model;

import java.util.Collections;
import java.util.List;

public class Level {
	public final List<Train> input, goal;
	public Level(List<Train> input, List<Train> goal) {
		this.input = Collections.unmodifiableList(input);
		this.goal = Collections.unmodifiableList(goal);
	}
	public boolean isAccessible() {
		// TODO Auto-generated method stub
		return false;
	}
}
