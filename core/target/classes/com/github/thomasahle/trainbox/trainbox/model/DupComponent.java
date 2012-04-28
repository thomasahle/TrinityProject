package com.github.thomasahle.trainbox.trainbox.model;

public class DupComponent implements Component {
	private final static int MAKE_DUPS = 2;
	
	private Train mTrain = Train.EMPTY;
	private int mDupsMade = MAKE_DUPS;
	// INV: mTrain != NULL  =>  1 <= mDupsMade < MAKE_DUPS
	
	private final Component prev;
	
	public DupComponent(Component prev) {
		this.prev = prev;
	}
	
	@Override
	public Train pull() {
		if (mDupsMade == MAKE_DUPS || mTrain == Train.EMPTY) {
			mTrain = prev.pull();
			mDupsMade = 1;
			return mTrain;
		}	
		mDupsMade += 1;
		return mTrain;
	}
}
