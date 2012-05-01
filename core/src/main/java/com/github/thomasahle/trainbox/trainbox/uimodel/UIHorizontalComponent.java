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
	
	public final int padding;
	
	// Invariant: |mComponents| > 0
	private List<UIComponent> mComponents = new ArrayList<UIComponent>();
	private GroupLayer mBackLayer = graphics().createGroupLayer();
	private GroupLayer mFrontLayer = graphics().createGroupLayer();
	private ImageLayer bg;
	
	public UIHorizontalComponent(int padding) {
		this.padding = padding;
		
		CanvasImage bgImage = graphics().createImage(1000, 1000);
		bgImage.canvas().setFillColor(0xaa00ff00)
						.fillRect(0, 0, getSize().width, getSize().height)
						.fillRect(0, 0, 50, 50);
		bg = graphics().createImageLayer(bgImage);
		mBackLayer.add(bg);
		
		addReal(new UIIdentityComponent(padding));
	}
	
	public void add(UIComponent comp) {
		addReal(comp);
		addReal(new UIIdentityComponent(padding));
	}
	
	private void addReal(UIComponent comp) {
		if (mComponents.size() > 0) {
			mComponents.get(mComponents.size()-1).setTrainTaker(comp);
		}
		// The new component is the last, so it should have the HorizontalComponent's tracker
		comp.setTrainTaker(getTrainTaker());
		
		// Add the component and its layer, setting its position at the end of our current width
		Dimension oldSize = getSize();
		mBackLayer.add(comp.getBackLayer());
		mFrontLayer.add(comp.getFrontLayer());
		comp.setPosition(new Point(oldSize.width, 0));
		mComponents.add(comp);
		comp.onAdded(this);
		super.install(comp);
		
		// We have now resized, so we need to redraw
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
	public Layer getBackLayer() {
		return mBackLayer;
	}
	
	@Override
	public Layer getFrontLayer() {
		return mFrontLayer;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
	}

	@Override
	public void setTrainTaker(TrainTaker listener) {
		super.setTrainTaker(listener);
		mComponents.get(mComponents.size()-1).setTrainTaker(listener);
	}

	@Override
	public void takeTrain(UITrain train) {
		System.out.println("Passing train down from "+this+" to "+mComponents.get(0));
		mComponents.get(0).takeTrain(train);
	}

	@Override
	public float leftBlock() {
		return mComponents.get(0).leftBlock();
	}
}
