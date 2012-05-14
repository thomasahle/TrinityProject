package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import playn.core.CanvasImage;
import playn.core.ImageLayer;
import playn.core.Layer;
import pythagoras.f.Dimension;

import com.github.thomasahle.trainbox.trainbox.util.CanvasHelper;

public class UIIdentityComponent extends AbstractComponent implements UIComponent, TrainTaker {

	private final static int HEIGHT = 100;
	private int mWidth;
	
	private Layer mFrontLayer;
	private ImageLayer mBackLayer;
	private LinkedList<UITrain> mTrains = new LinkedList<UITrain>();
	
	@Override
	public List<UITrain> getTrains() {
	    return Collections.unmodifiableList(new ArrayList<UITrain>(mTrains));
	}
	
	public UIIdentityComponent(int width) {
		mWidth = width;
		
		mFrontLayer = CanvasHelper.newEmptyLayer();
		
		mBackLayer = graphics().createImageLayer();
		updateTracks();
		xpadding(ComponentHelper.RAIL_EXTRA);
	}

	private void updateTracks() {
		int imageWidth = mWidth+(int)Math.ceil(2*ComponentHelper.RAIL_EXTRA);
		CanvasImage image = graphics().createImage(imageWidth, HEIGHT);
		ComponentHelper.drawTracks(image.canvas(), mWidth);
		mBackLayer.setImage(image);
	}
	
	public void setWidth(int width) {
		Dimension oldSize = getSize();
		mWidth = width;
		updateTracks();
		fireSizeChanged(oldSize);
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
