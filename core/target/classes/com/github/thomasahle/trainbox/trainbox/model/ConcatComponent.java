package com.github.thomasahle.trainbox.trainbox.model;

public class ConcatComponent implements Component {
	
	private final Component prev;
	
	public ConcatComponent(Component prev) {
		this.prev = prev;
	}
	
	private Train mTrain;
	
	@Override
	public Train pull() {
		if (mTrain.length() == 0)
			mTrain = prev.pull();
		Train t = mTrain.head();
		mTrain = mTrain.next();
		return t;
	}
}
