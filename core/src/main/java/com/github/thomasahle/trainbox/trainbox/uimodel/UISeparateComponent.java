package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import playn.core.CanvasImage;
import playn.core.Layer;
import pythagoras.f.Dimension;

public class UISeparateComponent extends BlackBoxComponent{

	
	private final static int HEIGHT = 100;
	private int mWidth;
	
	private Layer mBackLayer, mFrontLayer;
	
	public UISeparateComponent(int width) {
		mWidth = width;
		
		mBackLayer = graphics().createImageLayer(graphics().createImage(1,1));
		
		CanvasImage image = graphics().createImage(width, HEIGHT);
		image.canvas().setFillColor(0xff009999);
		image.canvas().fillCircle(width/2.f, HEIGHT/2.f, width/2.f);
		mFrontLayer = graphics().createImageLayer(image);
	}
	
	@Override
	public Dimension getSize() {
		return new Dimension(mWidth,HEIGHT);
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
		List<UICarriage> carriages = train.getCarriages();
		for(UICarriage c: carriages){
			UICarriage tmpCarriage = new UICarriage(c);
			UITrain tmpTrain = new UITrain(Arrays.asList(tmpCarriage));
			fireTrainCreated(tmpTrain);
			currentTrains.add(tmpTrain);
			tmpTrain.getLayer().setVisible(false);
		}
		fireTrainDestroyed(train);
		
	}
}
