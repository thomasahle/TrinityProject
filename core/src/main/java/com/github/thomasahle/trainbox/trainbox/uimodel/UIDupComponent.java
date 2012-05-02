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
	private Deque<UITrain> mIncomming = new ArrayDeque<UITrain>();
	private Deque<UITrain> mOutgoing = new ArrayDeque<UITrain>();
	private Deque<UITrain> mSent = new ArrayDeque<UITrain>();
	private TrainTaker mTrainTaker;
	
	@Override
	public void setTrainTaker(TrainTaker listener) {
		mTrainTaker = listener;
	}
	
	public UIDupComponent(int width) {
		mWidth = width;
		
		mBackLayer = graphics().createGroupLayer();
		
		CanvasImage image = graphics().createImage(width, HEIGHT);
		image.canvas().setFillColor(0xffaa00aa);
		image.canvas().fillCircle(width/2.f, HEIGHT/2.f, width/2.f);
		mFrontLayer = graphics().createImageLayer(image);
	}

	@Override
	public List<UITrain> getCarriages() {
		return Collections.unmodifiableList(new ArrayList<UITrain>(mOutgoing));
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
		// TODO: This works ok now, but we still need some more animation graphics,
		//		 As a minimum we should indicate if the component is loaded or not.
		
		// 		 Perhaps we want to blockLeft if there is already a train here being duped? 

		float compLeft = getDeepPosition().x;
		float compRight = compLeft + getSize().width;
		
		// WARNING: Don't get confused by the sillyness that is,
		// 			that mIncomming and mSent uses queues. They will never contain more than one item.
		
		// Move the trains all the way into the garrage
		for (Iterator<UITrain> it = mIncomming.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			float trainLeft = train.getPosition().x;
			
			if (trainLeft >= compLeft) {
				it.remove();
				
				mOutgoing.add(train);
				train.getLayer().setVisible(false);
				UITrain clone = new UITrain(train);
				mOutgoing.add(clone);
				fireTrainCreated(clone);
				clone.getLayer().setVisible(false);
				
				log().debug("Train captured, moved to middle queue. Length is: "+mOutgoing.size());
			}
			else {
				float newLeft = trainLeft + UITrain.SPEED*delta;
				train.setPosition(new Point(newLeft, train.getPosition().y));
				train.setCropLeft(compRight - newLeft);
			}
		}
		// Wait for the right moment to spit them out
		for (Iterator<UITrain> it = mOutgoing.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			
			if (compRight < mTrainTaker.leftBlock()) {
				log().debug("Sending a cloned element to "+mTrainTaker);
				
				it.remove();
				train.setPosition(new Point(compRight-train.getSize().width, train.getPosition().y));
				mTrainTaker.takeTrain(train);
				train.getLayer().setVisible(true);
				
				mSent.add(train);
				
				log().debug("Train cloned. Sent to output queue.");
			}
		}
		// As they leave us, dragged by the next component, uncrop them gradually
		for (Iterator<UITrain> it = mSent.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			float trainLeft = train.getPosition().x;
			float trainRight = trainLeft + train.getSize().width;
			
			train.setCropRight(trainRight-compLeft);
			
			if (trainLeft >= compRight) {
				it.remove();
			}
		}
	}

	@Override
	public void takeTrain(UITrain train) {
		mIncomming.add(train);
		log().debug("Got a train to clone.");
	}

	@Override
	public float leftBlock() {
		// We never block
		return Float.MAX_VALUE;
	}
}
