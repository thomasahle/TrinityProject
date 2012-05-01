package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import playn.core.GroupLayer;
import playn.core.Layer;
import pythagoras.f.Point;
import pythagoras.i.Dimension;

public class UITrain {
	
	public final static float SPEED = 0.034f; // pixels/s
	public final static float PADDING = 10.f;
	
	private List<UICarriage> mCarriages;
	private GroupLayer mLayer;
	private Point mPosition;
	private Dimension mSize;
	
	public UITrain(int... cargos) {
		mPosition = new Point(0,0);
		mCarriages = new ArrayList<UICarriage>();
		for (int cargo : cargos)
			mCarriages.add(new UICarriage(cargo));
		
		mLayer = graphics().createGroupLayer();
		
		install(mCarriages);
	}
	
	public UITrain(List<UICarriage> carriages) {
		mPosition = new Point(0,0);
		mCarriages = carriages;
		mLayer = graphics().createGroupLayer();
		
		install(carriages);
	}

	// Copy constructor
	public UITrain(UITrain old) {
		mPosition = old.getPosition();
		mCarriages = new ArrayList<UICarriage>();
		for (UICarriage car : old.getCarriages())
			mCarriages.add(new UICarriage(car));
		mLayer = graphics().createGroupLayer();
		
		install(mCarriages);
	}
	
	private void install(List<UICarriage> carriages) {
		mCarriages = carriages;
		int x = 0;
		int y = 0;
		for (UICarriage car : carriages) {
			car.setPosition(new Point(x, 0));
			x += car.getSize().width;
			y = Math.max(y, car.getSize().height);
			mLayer.add(car.getLayer());
		}
		mSize = new Dimension(x, y);
	}
	
	public Point getPosition() {
		return mPosition;
	}
	public UITrain setPosition(Point position) {
		getLayer().setTranslation(position.x, position.y);
		mPosition = position;
		return this;
	}
	public Dimension getSize() {
		return mSize;
	}
	public Layer getLayer() {
		return mLayer;
	}
	public List<UICarriage> getCarriages() {
		return Collections.unmodifiableList(mCarriages);
	}
}
