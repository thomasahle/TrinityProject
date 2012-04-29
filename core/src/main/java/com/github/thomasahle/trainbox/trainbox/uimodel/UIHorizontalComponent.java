package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class UIHorizontalComponent extends AbstractComposite {
	
	private List<UIComponent> mComponents = new ArrayList<UIComponent>();
	private GroupLayer mLayer = graphics().createGroupLayer();
	private ImageLayer bg;
	private TrainTaker mTrainTaker = new NullTrainTaker();
	private Point mPosition;
	
	public UIHorizontalComponent() {
		CanvasImage bgImage = graphics().createImage(1000, 1000);
		bgImage.canvas().setFillColor(0xaa00ff00);
		bgImage.canvas().fillRect(0, 0, getSize().width, getSize().height);
		bgImage.canvas().fillRect(0, 0, 50, 50);
		bg = graphics().createImageLayer(bgImage);
		mLayer.add(bg);
		System.out.println("Ba");
		add(new UIIdentityComponent(100));
		add(new UIIdentityComponent(100));
		System.out.println("Bc");
	}
	
	public void add(UIComponent comp) {
		if (mComponents.size() > 0) {
			mComponents.get(mComponents.size()-1).setTrainTaker(comp);
		}
		comp.setTrainTaker(mTrainTaker);
		
		Dimension oldSize = getSize();
		mLayer.add(comp.getLayer());
		comp.setPosition(new Point(oldSize.width, 0));
		mComponents.add(comp);
		
		CanvasImage bgImage = graphics().createImage(1000, 1000);
		bgImage.canvas().setFillColor(0xaa00ff00);
		bgImage.canvas().fillRect(0, 0, getSize().width, getSize().height);
		bg.setImage(bgImage);
	}
	
	@Override
	public List<UIComponent> getChildren() {
		return Collections.unmodifiableList(mComponents);
	}

	@Override
	public Dimension getSize() {
		int width = 0;
		float height = Float.MIN_VALUE;
		for (UIComponent child : getChildren()) {
			width += child.getSize().width;
			height = Math.max(height, child.getSize().height);
		}
		return new Dimension(width, height);
	}

	@Override
	public Layer getLayer() {
		return mLayer;
	}

	@Override
	public void enterTrain(UITrain train) {
		mComponents.get(0).enterTrain(train);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
	}

	@Override
	public void setTrainTaker(TrainTaker listener) {
		mTrainTaker = listener;
		mComponents.get(mComponents.size()-1).setTrainTaker(listener);
	}

	@Override
	public void takeTrain(UITrain train) {
		mComponents.get(0).takeTrain(train);
	}

	@Override
	public float leftBlock() {
		return mComponents.get(0).leftBlock();
	}

	@Override
	public void setPosition(Point position) {
		getLayer().setTranslation(position.x, position.y);
		mPosition = position;
		/*float x = mPosition.x;
		for (UIComponent comp : mComponents) {
			comp.setPosition(new Point(x, position.y));
			x += comp.getSize().width;
		}*/
	}

	@Override
	public Point getPosition() {
		return mPosition;
	}
}
