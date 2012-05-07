package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public abstract class BlackBoxComponent extends AbstractComponent {

	private UITrain mIncomming = null;
	private Queue<UITrain> mCurrent = new ArrayDeque<UITrain>();
	private UITrain mSent = null;
	private TrainTaker mTrainTaker;
	
	
	@Override
	public void setTrainTaker(TrainTaker listener) {
		mTrainTaker = listener;
	}
	
	@Override
	public List<UITrain> getCarriages() {
		return Collections.unmodifiableList(new ArrayList<UITrain>(mCurrent));
	}
	
	@Override
	public void update(float delta) {
		// TODO: We should indicate if the component is loaded or not.
		
		float compLeft = getDeepPosition().x;
		float compRight = compLeft + getSize().width;
	
		// Move the trains all the way into the garage
		if (mIncomming != null){
			float trainLeft = mIncomming.getPosition().x;
			
			if (trainLeft >= compLeft) {
				onTrainEntered(mIncomming, mCurrent); // Do component specific things
				mIncomming = null;
			}
			else {
				float newLeft = trainLeft + UITrain.SPEED*delta;
				float newRight = newLeft + mIncomming.getSize().width;
				mIncomming.setPosition(new Point(newLeft, mIncomming.getPosition().y));
				mIncomming.setCropRight(newRight - compRight);
			}
		}
		// Wait for the right moment to spit them out
		for (Iterator<UITrain> it = mCurrent.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			
			if (compRight < mTrainTaker.leftBlock()) {
				log().debug("Sending a cloned element to "+mTrainTaker);
				
				it.remove();
				train.getLayer().setVisible(true);
				train.setPosition(new Point(compRight-train.getSize().width, train.getPosition().y));
				train.setCropRight(0);
				mTrainTaker.takeTrain(train);
				
				mSent = train;
				
				log().debug("Train cloned. Sent to output queue.");
			}
		}
		// As they leave us, dragged by the next component, uncrop them gradually
		if (mSent != null) {
			float trainLeft = mSent.getPosition().x;
			
			mSent.setCropLeft(compLeft-trainLeft);
			
			if (trainLeft >= compRight) {
				mSent = null;
			}
		}
	}


	@Override
	public void takeTrain(UITrain train) {
		mIncomming = train;
		log().debug("Got a train to clone.");
	}

	@Override
	public float leftBlock() {
		// Nothing is supposed to stick through this component
		// However because of the hack used to 'truncate' trains, we can't
		// 'check' this condition.
		// assert mTrainTaker.leftBlock() > getPosition().x;
		
		// If something is being moved in, clearly don't overlap with it
		if (mIncomming != null)
			return mIncomming.getPosition().x - UITrain.PADDING;
		// If something is already being duped, don't send more stuff in
		// TODO: Should we also wait till the last dup is fully out?
		if (!mCurrent.isEmpty() || mSent != null)
			return getPosition().x;
		// If we don't have anything going on, we don't block
		return Float.MAX_VALUE;
	}
	public abstract void onTrainEntered(UITrain train, Queue<UITrain> currentTrains);


}
