package com.github.thomasahle.trainbox.trainbox.model;

public class NullTrain implements Train {
	public Train head() {
		return Train.EMPTY;
	}
	
	public int length() {
		return 0;
	}
	
	public Train addLast(Train behind) {
		return behind;
	}

	@Override
	public int cargo() {
		throw new UnsupportedOperationException("Can't query cargo from a length 0 train");
	}

	@Override
	public Train tail() {
		return this;
	}
	
	@Override
	public String toString() {
		return "[Null>";
	}
}