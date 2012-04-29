package com.github.thomasahle.trainbox.trainbox.uimodel;

public class NullTrainTaker implements TrainTaker {

	@Override
	public void takeTrain(UITrain train) {
		
	}

	@Override
	public float leftBlock() {
		return Float.MAX_VALUE;
	}

}
