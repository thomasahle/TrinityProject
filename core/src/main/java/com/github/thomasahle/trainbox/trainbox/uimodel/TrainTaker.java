package com.github.thomasahle.trainbox.trainbox.uimodel;

/**
 * Something that will take over a train form someone else.
 * A bit like a listener that waits for someone to be finished with a train.
 */
public interface TrainTaker {
	/**
	 * Assumes that train.position.x + train.size.width <= leftBock()
	 * @param train
	 */
	public void takeTrain(UITrain train);
	/**
	 * See `takeTrain`
	 * @return
	 */
	public float leftBlock();
	
	public void updateMaxLengthTrainExpected(int compNum, int len);
}
