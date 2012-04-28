package com.github.thomasahle.trainbox.trainbox.model;

public class IdentityComponent implements Component {

	private Train mTrain = Train.NULL;
	
	@Override
	/* Each ID component can hold only one train and outputs the same train, unchanged. */
	public boolean canEnter() {
		return this.isEmpty();
	}

	@Override
	public void enter(Train train) {
		mTrain = train;
	}

	@Override
	public boolean canLeave() {
		return mTrain != Train.NULL;
	}

	@Override
	public Train leave() {
		Train outTrain = mTrain;
		mTrain = Train.NULL;
		return outTrain;
	}

	@Override
	public boolean isEmpty() {
		return mTrain == Train.NULL;
	}

}
