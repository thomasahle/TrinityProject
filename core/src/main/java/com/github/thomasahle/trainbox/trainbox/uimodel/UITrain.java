package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.thomasahle.trainbox.trainbox.model.Train;

import playn.core.GroupLayer;
import playn.core.Layer;
import pythagoras.f.Point;
import pythagoras.i.Dimension;

public class UITrain {
	
	public final static float SPEED = 0.064f; // pixels/s
	public final static float PADDING = 10.f;
	
	private List<UICarriage> mCarriages;
	private GroupLayer mLayer;
	private Point mPosition;
	private Dimension mSize;
	
	public UITrain(Train train) {
		this(fromTrain(train));
	}
	
	public UITrain(int... cargos) {
		this(fromCargos(cargos));
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
	
	private static List<UICarriage> fromCargos (int[] cargos) {
		List<UICarriage> carriages = new ArrayList<UICarriage>();
		for (int cargo : cargos)
			carriages.add(new UICarriage(cargo));
		return carriages;
	}
	
	private static List<UICarriage> fromTrain (Train train) {
		List<UICarriage> carriages = new ArrayList<UICarriage>();
		while (train.length() > 0) {
			carriages.add(new UICarriage(train.cargo()));
			train = train.tail();
		}
		return carriages;
	}
	
	
	private void install(List<UICarriage> carriages) {
		mCarriages = carriages;
		int x = 0;
		int y = 0;
		for (int i = carriages.size()-1; i >= 0; i--) {
			UICarriage car = carriages.get(i);
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
	
	// TODO: The current cropping is a hack, which might not work well in all
	// situations. Better would be to use this approach:
	// https://groups.google.com/forum/?fromgroups#!topic/playn/9iEnc5HiceI
	public void setCropRight(float width) {
		for (UICarriage car : mCarriages) {
			if (width >= car.getSize().width) {
				car.getLayer().setVisible(true);
				width -= car.getSize().width;
			}
			else {
				car.getLayer().setVisible(false);
			}
		}
	}
	
	public void setCropLeft(float width) {
		for (int i = mCarriages.size()-1; i >= 0; i--) {
			UICarriage car = mCarriages.get(i);
			if (width >= car.getSize().width) {
				car.getLayer().setVisible(true);
				width -= car.getSize().width;
			}
			else {
				car.getLayer().setVisible(false);
			}
		}
	}
}
