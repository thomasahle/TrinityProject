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

public class UINoGoComponent extends AbstractComponent implements UIComponent, TrainTaker {

	private final static int HEIGHT = 100;
	private int mWidth;
	
	private Layer mBackLayer, mFrontLayer;
	
	@Override
	public List<UITrain> getTrains() {
	    return Collections.emptyList();
	}
	
	public UINoGoComponent(int width) {
		mWidth = width;
		
		mFrontLayer = CanvasHelper.newEmptyLayer();
		
		CanvasImage image = graphics().createImage(width, HEIGHT);
		image.canvas().setFillColor(0xff000000).fillRect(0, 0, width, HEIGHT);
		mBackLayer = graphics().createImageLayer(image);
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
	}	
	
	@Override
	public void takeTrain(UITrain train) {
		throw new UnsupportedOperationException("Hey! It's 'No Go'!");
	}

	@Override
	public float leftBlock() {
		return getDeepPosition().x;
	}
	public void reset(){
		//has no trains, so nothing to reset.
	}
}
