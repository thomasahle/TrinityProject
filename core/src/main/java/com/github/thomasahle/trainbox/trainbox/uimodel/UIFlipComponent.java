package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.github.thomasahle.trainbox.trainbox.util.CanvasHelper;

import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.Layer;
import pythagoras.f.Dimension;

public class UIFlipComponent extends BlackBoxComponent {

	
	private int mWidth, mHeight;
	
	private Layer mBackLayer, mFrontLayer;
	private UITrain unpairedTrain;	
	
	@Override
	public List<UITrain> getTrains() {
		List<UITrain> trains = new ArrayList<UITrain>(super.getTrains());
		if (unpairedTrain != null) trains.add(unpairedTrain);
		return trains;
	}
	
	public UIFlipComponent() {
		mBackLayer = CanvasHelper.newEmptyLayer();

		Image flipComponentImage = assets().getImage("images/pngs/flipComponent.png");
		mFrontLayer = graphics().createImageLayer(flipComponentImage);
		
		mWidth = flipComponentImage.width();
		mHeight = flipComponentImage.height();
	}
	
	public Dimension getSize(){
		return new Dimension(mWidth,mHeight);
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
		if(unpairedTrain != null){
			currentTrains.add(train);
			currentTrains.add(unpairedTrain);
			unpairedTrain = null;
		}else{
			unpairedTrain = train;
		}
	}

}
