package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.Layer;
import pythagoras.f.Point;
import pythagoras.i.Dimension;

import com.github.thomasahle.trainbox.trainbox.model.Train;

public class UITrain {
	
	private static final int HEIGHT = 30;
	private static final int WIDTH = 50;
	public final static float SPEED = 0.05f; // pixels/s
	public final static float PADDING = 5.f;
	
	private Layer mLayer;
	private UITrain mNext;
	private float mLastUpdate;
	private Point mPosition;
	
	public UITrain(int cargo) {
		mPosition = new Point(0,0);
		
		CanvasImage image = graphics().createImage(WIDTH, HEIGHT);
		image.canvas().setFillColor(0xff0000ff);
		image.canvas().fillRect(0, 0, WIDTH, HEIGHT);
		image.canvas().setFillColor(0xffffffff);
		image.canvas().drawText(""+cargo, 2, HEIGHT-2);
		mLayer = graphics().createImageLayer(image);
	}
	
	public Point getPosition() {
		return mPosition;
	}
	public void setPosition(Point position) {
		getLayer().setTranslation(position.x, position.y);
		mPosition = position;
	}
	public Dimension getSize() {
		return new Dimension(WIDTH, HEIGHT);
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
