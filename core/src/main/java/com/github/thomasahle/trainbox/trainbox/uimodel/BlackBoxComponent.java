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

	private Deque<UITrain> mIncomming = new ArrayDeque<UITrain>();
	private Deque<UITrain> mOutgoing = new ArrayDeque<UITrain>();
	private Deque<UITrain> mSent = new ArrayDeque<UITrain>();
	private TrainTaker mTrainTaker;
	
	
	@Override
	public void setTrainTaker(TrainTaker listener) {
		mTrainTaker = listener;
	}
	
	@Override
	public List<UITrain> getCarriages() {
		return Collections.unmodifiableList(new ArrayList<UITrain>(mOutgoing));
	}
	
	@Override
	public void update(float delta) {
		// TODO: We should indicate if the component is loaded or not.
		
		float compLeft = getDeepPosition().x;
		float compRight = compLeft + getSize().width;
	
		// Move the trains all the way into the garage
		for (Iterator<UITrain> it = mIncomming.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			float trainLeft = train.getPosition().x;
			
			if (trainLeft >= compLeft) {
				it.remove();
				onTrainEntered(train, mOutgoing); // Do component specific things
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
		// Nothing is supposed to stick through this component
		// However because of the hack used to 'truncate' trains, we can't
		// 'check' this condition.
		// assert mTrainTaker.leftBlock() > getPosition().x;
		
		// If something is being moved in, clearly don't overlap with it
		if (!mIncomming.isEmpty())
			return mIncomming.getLast().getPosition().x - UITrain.PADDING;
		// If something is already being duped, don't send more stuff in
		// TODO: Should we also wait till the last dup is fully out?
		if (!mOutgoing.isEmpty() || !mSent.isEmpty())
			return getPosition().x;
		// If we don't have anything going on, we don't block
		return Float.MAX_VALUE;
	}
	public abstract void onTrainEntered(UITrain train, Queue<UITrain> currentTrains);


}
