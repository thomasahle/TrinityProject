package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;

import java.util.Queue;

import playn.core.CanvasImage;
import playn.core.Layer;
import pythagoras.f.Dimension;

public class UIDupComponent extends BlackBoxComponent{

	private final static int HEIGHT = 100;
	private int mWidth;
	
	private Layer mBackLayer, mFrontLayer;
	

	
	public UIDupComponent(int width) {
		mWidth = width;
		
		mBackLayer = graphics().createGroupLayer();
		
		CanvasImage image = graphics().createImage(width, HEIGHT);
		image.canvas().setFillColor(0xffaa00aa);
		image.canvas().fillCircle(width/2.f, HEIGHT/2.f, width/2.f);
		mFrontLayer = graphics().createImageLayer(image);
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
	public void onTrainEntered(UITrain train, Queue<UITrain> currentTrains) {
		currentTrains.add(train);
		train.getLayer().setVisible(false);
		UITrain clone = new UITrain(train);
		currentTrains.add(clone);
		fireTrainCreated(clone);
		clone.getLayer().setVisible(false);
	}
}
