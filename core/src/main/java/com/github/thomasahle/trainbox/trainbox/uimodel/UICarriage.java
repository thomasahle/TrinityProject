package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.Layer;
import pythagoras.f.FloatMath;
import pythagoras.f.Point;
import pythagoras.f.Transform;
import pythagoras.i.Dimension;

public class UICarriage {
	
	public static final int HEIGHT = 64;
	public static final int WIDTH = 64;
	
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
		Image emptyTrain = assets().getImage("images/red_train.png");
		image.canvas().drawImage(emptyTrain, 0, 0, WIDTH, HEIGHT);
		image.canvas().setFillColor(0xffffffff);
		image.canvas().drawText(""+cargo, 9, 25);
		mLayer = graphics().createImageLayer(image);
	}
	
	public Point getPosition() {
		return mPosition;
	}
	public void setPosition(Point position) {
		getLayer().setOrigin(-position.x, -position.y);
		mPosition = position;
	}
	public void setRotation(float dx, float dy) {
		float theta = FloatMath.atan2(dy, dx);
		Transform tr = getLayer().transform();
		tr.translate(WIDTH/2.f+mPosition.x, HEIGHT/2.f+mPosition.y);
		tr.setRotation(theta);
		tr.translate(-WIDTH/2.f-mPosition.x, -HEIGHT/2.f-mPosition.y);
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
