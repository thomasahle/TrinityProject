package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.github.thomasahle.trainbox.trainbox.util.CanvasHelper;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Layer;
import playn.core.Path;
import pythagoras.f.Dimension;

public class UIIdentityComponent extends AbstractComponent implements UIComponent, TrainTaker {

	private final static int HEIGHT = 100;
	private int mWidth;
	
	private Layer mBackLayer, mFrontLayer;
	private LinkedList<UITrain> mTrains = new LinkedList<UITrain>();
	
	@Override
	public List<UITrain> getTrains() {
	    return Collections.unmodifiableList(new ArrayList<UITrain>(mTrains));
	}
	
	public UIIdentityComponent(int width) {
		mWidth = width;
		
		mFrontLayer = CanvasHelper.newEmptyLayer();
		
		int imageWidth = width+(int)Math.ceil(2*ComponentHelper.RAIL_EXTRA);
		CanvasImage image = graphics().createImage(imageWidth, HEIGHT);
		ComponentHelper.drawTracks(image.canvas(), width);
		mBackLayer = graphics().createImageLayer(image);
		xpadding(ComponentHelper.RAIL_EXTRA);
	}
	
	@Override
	public Dimension getSize() {
		return new Dimension(mWidth, HEIGHT);
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
		
		if (paused())
			return;
		
		moveTrains(mTrains, delta);
	}	
	
	@Override
	public void takeTrain(UITrain train) {
		// The train can't have passed us already. This makes things a lot simpler. 
		assert train.getPosition().x < getDeepPosition().x+getSize().width;
		mTrains.add(train);
		train.vertCenterOn(this);
	}

	@Override
	public float leftBlock() {
		// Channel leftBlock from previous component
		float res = getTrainTaker().leftBlock();
		// Don't allow trains to jump over us
		res = Math.min(res, getDeepPosition().x+getSize().width-0.1f);
		// Don't overlap trains we currently manage
		if (!mTrains.isEmpty())
			res = Math.min(res, mTrains.getLast().getPosition().x - UITrain.PADDING);
		return res;
	}
}
