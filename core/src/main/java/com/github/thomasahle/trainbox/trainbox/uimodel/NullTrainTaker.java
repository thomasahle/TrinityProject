package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.log;

public class NullTrainTaker implements TrainTaker {

	@Override
	public void takeTrain(UITrain train) {
		log().debug("NullTaker took: "+train.train());
	}

	@Override
	public float leftBlock() {
		return Float.MAX_VALUE;
	}

}
