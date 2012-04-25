package com.github.thomasahle.trainbox.trainbox.model;

public class DupComponent implements Component {
	private final static int MAKE_DUPS = 2;
	private Train mTrain = Train.NULL;
	private int mDupsMade;
	
	@Override
	public boolean canEnter() {
		return mTrain == Train.NULL;
	}

	@Override
	public void enter(Train train) {
		if (mTrain != Train.NULL)
			throw new IllegalStateException("The dup already has a train");
		mTrain = train;
		mDupsMade = 0;
	}

	@Override
	public boolean canLeave() {
		return mTrain != Train.NULL;
	}

	@Override
	public Train leave() {
		Train train = mTrain;
		mDupsMade += 1;
		if (mDupsMade == MAKE_DUPS)
			mTrain = Train.NULL;
		return train;
	}

	@Override
	public boolean isEmpty() {
		return mTrain == Train.NULL;
	}
}
