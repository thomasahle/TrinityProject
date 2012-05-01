package com.github.thomasahle.trainbox.trainbox.uimodel;

public interface TrainsChangedListener {
	public void onTrainCreated(UITrain train);
	public void onTrainDestroyed(UITrain train);
}
