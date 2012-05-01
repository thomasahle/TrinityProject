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

public class UIDupComponent extends AbstractComponent implements UIComponent, TrainTaker {

	private final static int HEIGHT = 100;
	private int mWidth;
	
	private Layer mBackLayer, mFrontLayer;
	private Deque<UITrain> mTrains = new ArrayDeque<UITrain>();
	private TrainTaker mTrainTaker;
	
	@Override
	public void setTrainTaker(TrainTaker listener) {
		mTrainTaker = listener;
	}
	
	public UIDupComponent(int width) {
		mWidth = width;
		
		mBackLayer = graphics().createImageLayer(graphics().createImage(1,1));
		
		CanvasImage image = graphics().createImage(width, HEIGHT);
		image.canvas().setFillColor(0xaaaa00aa);
		image.canvas().fillCircle(width/2.f, HEIGHT/2.f, width/2.f);
		mFrontLayer = graphics().createImageLayer(image);
	}

	@Override
	public List<UITrain> getCarriages() {
		return Collections.unmodifiableList(new ArrayList<UITrain>(mTrains));
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

	@Override
	public void takeTrain(UITrain train) {
		mTrains.add(train);
		train.getLayer().setVisible(false);
		
		UITrain clone = new UITrain(train.getCargo());
		mTrains.add(clone);
		fireTrainCreatedEvent(clone);
		clone.getLayer().setVisible(false);
		log().debug("Got a train. Queue length is now "+mTrains.size());
	}

	@Override
	public float leftBlock() {
		// We never block
		return Float.MAX_VALUE;
	}
}
