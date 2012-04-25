package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import playn.core.*;

import com.github.thomasahle.trainbox.trainbox.model.Train;

public class UITrain {
	
	public final static float SPEED = 1.f; // pixels/s
	
	private Layer mLayer;
	private Train mTrain;
	private UITrain mNext;
	private float mLastUpdate;
	
	public UITrain(Train train) {
		mTrain = train;
		mLayer = graphics().createImageLayer(graphics().createImage(50, 30));
	}
	
	public float getLastUpdate() {
		return mLastUpdate;
	}
	public void setLastUpdate(float lastUpdate) {
		mLastUpdate = lastUpdate;
	}
	public Layer getLayer() {
		return mLayer;
	}
	public Train getTrain() {
		return mTrain;
	}
	public UITrain getNext() {
		return mNext;
	}
}
