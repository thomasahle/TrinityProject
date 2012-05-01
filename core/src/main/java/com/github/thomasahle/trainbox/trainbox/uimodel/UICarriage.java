package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.Layer;
import pythagoras.f.Point;
import pythagoras.i.Dimension;

public class UICarriage {
	
	private static final int HEIGHT = 20;
	private static final int WIDTH = 36;
	
	private Layer mLayer;
	private Point mPosition;
	private int mCargo;
	
	public UICarriage(int cargo) {
		mPosition = new Point(0,0);
		mCargo = cargo;
		drawLayer(cargo);
	}

	// Copy constructor
	public UICarriage(UICarriage car) {
		car.setPosition(car.getPosition());
		mCargo = car.getCargo();
		drawLayer(mCargo);
	}

	private void drawLayer(int cargo) {
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
	public Layer getLayer() {
		return mLayer;
	}
	public int getCargo() {
		return mCargo;
	}
}
