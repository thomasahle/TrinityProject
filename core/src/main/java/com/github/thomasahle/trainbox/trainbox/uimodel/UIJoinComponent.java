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

public class UIJoinComponent extends BlackBoxComponent{

	
	private int mWidth, mHeight;
	
	private Layer mBackLayer, mFrontLayer;
	private UITrain frontTrain;	
	
	@Override
	public List<UITrain> getTrains() {
		List<UITrain> trains = new ArrayList<UITrain>(super.getTrains());
		if (frontTrain != null) trains.add(frontTrain);
		return trains;
	}
	
	public UIJoinComponent() {
		mBackLayer = CanvasHelper.newEmptyLayer();
		
		Image joinComponentImage = assets().getImage("images/pngs/boxComponent.png");
		mFrontLayer = graphics().createImageLayer(joinComponentImage);
		
		mWidth = joinComponentImage.width();
		mHeight = joinComponentImage.height();
	}
	
	@Override
	public Dimension getSize() {
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
		if (frontTrain != null) {
			List<UICarriage> newTrainCarriages = new ArrayList<UICarriage>(
					frontTrain.getCarriages());
			newTrainCarriages.addAll(train.getCarriages());
			UITrain newTrain = new UITrain(newTrainCarriages);
			float oldSpeed = train.getSpeed();
			fireTrainDestroyed(train);
			fireTrainDestroyed(frontTrain);
			frontTrain = null;

			newTrain.getLayer().setVisible(false);
			currentTrains.add(newTrain);
			fireTrainCreated(newTrain);
			newTrain.setSpeed(oldSpeed);
			
		}
		else {
			frontTrain = train;
		}
	}
	
	/*@Override
	public float leftBlock() {
		return getDeepPosition().x;
	}*/
}
