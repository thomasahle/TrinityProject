package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.Layer;
import pythagoras.f.Point;
import pythagoras.i.Dimension;

import com.github.thomasahle.trainbox.trainbox.model.Train;

public class UITrain {
	
	public final static float SPEED = 0.05f; // pixels/s
	public final static float PADDING = 5.f;
	
	private Layer mLayer;
	private UITrain mNext;
	private float mLastUpdate;
	private Dimension mSize;
	private Point mPosition;
	
	public UITrain() {
		mPosition = new Point(0,0);
		int width = 50;
		int height = 30;
		mSize = new Dimension(width, height);
		CanvasImage image = graphics().createImage(width, height);
		image.canvas().setFillColor(0xff0000ff);
		image.canvas().fillRect(0, 0, width, height);
		mLayer = graphics().createImageLayer(image);
	}
	
	public Point getPosition() {
		return mPosition;
	}
	public void setPosition(Point position) {
		mPosition = position;
	}
	public Dimension getSize() {
		return mSize;
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
	public UITrain getNext() {
		return mNext;
	}
}
