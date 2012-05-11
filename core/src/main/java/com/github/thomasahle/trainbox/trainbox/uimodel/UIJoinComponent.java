package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import playn.core.CanvasImage;
import playn.core.Layer;
import pythagoras.f.Dimension;

public class UIJoinComponent extends BlackBoxComponent{

	
	private final static int HEIGHT = 100;
	private int mWidth;
	
	private Layer mBackLayer, mFrontLayer;
	private UITrain frontTrain;	
	
	@Override
	public List<UITrain> getTrains() {
		List<UITrain> trains = new ArrayList<UITrain>(super.getTrains());
		if (frontTrain != null) trains.add(frontTrain);
		return trains;
	}
	
	public UIJoinComponent(int width) {
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
		if (frontTrain != null) {
			List<UICarriage> newTrainCarriages = new ArrayList<UICarriage>(
					frontTrain.getCarriages());
			newTrainCarriages.addAll(train.getCarriages());
			UITrain newTrain = new UITrain(newTrainCarriages);
			
			fireTrainDestroyed(train);
			fireTrainDestroyed(frontTrain);
			frontTrain = null;

			newTrain.getLayer().setVisible(false);
			newTrain.setCropLeft(newTrain.getSize().width - getSize().width);
			currentTrains.add(newTrain);
			fireTrainCreated(newTrain);
		}
		else {
			frontTrain = train;
		}
	}
}
