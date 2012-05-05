package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import playn.core.CanvasImage;
import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class UIFlipComponent extends AbstractComponent implements UIComponent, TrainTaker {

	
	private final static int HEIGHT = 100;
	private int mWidth;
	
	private Layer mBackLayer, mFrontLayer;
	private Deque<UITrain> mTrains = new ArrayDeque<UITrain>();
	private TrainTaker mTrainTaker;
	private UITrain unpairedTrain;
	private boolean isUnpairedTrain = false;
	
	
	public void setTrainTaker(TrainTaker listener){
		mTrainTaker = listener;
	}
	
	public UIFlipComponent(int width) {
		mWidth = width;
		
		mBackLayer = graphics().createImageLayer(graphics().createImage(1,1));
		
		CanvasImage image = graphics().createImage(width, HEIGHT);
		image.canvas().setFillColor(0xdd0000aa);
		image.canvas().fillCircle(width/2.f, HEIGHT/2.f, width/2.f);
		mFrontLayer = graphics().createImageLayer(image);
	}
	
	public Dimension getSize(){
		return new Dimension(mWidth,HEIGHT);
	}
	
	@Override
	public void takeTrain(UITrain train) {
		if(isUnpairedTrain){
			mTrains.add(train);
			mTrains.add(unpairedTrain);
			isUnpairedTrain = false;
			log().debug("Got a train and matched it with unpaired train. Queue length is now "+mTrains.size());
		}else{
			unpairedTrain = train;
			isUnpairedTrain = true;
			log().debug("Got a train, it is currently unpaired. Queue length is now "+mTrains.size());
		}
		train.getLayer().setVisible(false);
		
	}

	@Override
	public float leftBlock() {
		// We never block
		return Float.MAX_VALUE;
	}

	@Override
	public List<UITrain> getCarriages() {
		return Collections.unmodifiableList(new ArrayList<UITrain>(mTrains));
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
		for (Iterator<UITrain> it = mTrains.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			float compLeft = getDeepPosition().x;
			float compRight = compLeft + getSize().width;
			
			if (compRight < mTrainTaker.leftBlock()) {
				log().debug("Sending a cloned element to "+mTrainTaker);
				
				it.remove();
				train.setPosition(new Point(compRight-train.getSize().width, train.getPosition().y));
				mTrainTaker.takeTrain(train);
				train.getLayer().setVisible(true);
			}
		}
	}

}
