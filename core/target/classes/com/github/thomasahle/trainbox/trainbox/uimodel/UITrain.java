package com.github.thomasahle.trainbox.trainbox.uimodel;

import playn.core.Layer;

import com.github.thomasahle.trainbox.trainbox.model.Train;

public class UITrain {
	private Layer mLayer;
	private Train mTrain;
	private UITrain mNext;
	private float mLastUpdate;
	
	public final static float SPEED = 1.f; // pixels/s
	
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
