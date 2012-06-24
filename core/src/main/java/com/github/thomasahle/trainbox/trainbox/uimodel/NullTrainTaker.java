package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.log;

public class NullTrainTaker implements TrainTaker {
	private int maxExpectedLength = 0;
	@Override
	public void takeTrain(UITrain train) {
		log().debug("NullTaker took: "+train.train());
	}

	@Override
	public float leftBlock() {
		return Float.MAX_VALUE;
	}

	@Override
	public void updateMaxLengthTrainExpected(int compNum, int len) {
		this.maxExpectedLength = len;
		//propagation ends here

	}

}
