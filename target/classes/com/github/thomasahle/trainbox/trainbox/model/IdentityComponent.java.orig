package com.github.thomasahle.trainbox.trainbox.model;

public class IdentityComponent implements Component {
<<<<<<< HEAD
	private final Component prev;
	
	public IdentityComponent(Component prev) {
		this.prev = prev;
=======

	private Train mTrain = Train.NULL;
	
	@Override
	/* Each ID component can hold only one train and outputs the same train, unchanged. */
	public boolean canEnter() {
		return this.isEmpty();
>>>>>>> 8eb2317ac6722739b98acb780ef6f6fdd86b5060
	}
	
	@Override
<<<<<<< HEAD
	public Train pull() {
		return prev.pull();
	}
=======
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

>>>>>>> 8eb2317ac6722739b98acb780ef6f6fdd86b5060
}
