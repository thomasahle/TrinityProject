package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import pythagoras.f.Point;

public abstract class BlackBoxComponent extends AbstractComponent {

	private UITrain mIncomming = null;
	private Queue<UITrain> mCurrent = new LinkedList<UITrain>();
	private UITrain mSent = null;
	
	@Override
	public List<UITrain> getTrains() {
		List<UITrain> carriages = new ArrayList<UITrain>(mCurrent);
		if (mIncomming != null) carriages.add(mIncomming);
		if (mSent != null) carriages.add(mSent);
		// We don't need to use 'unmodifiable' here, as the list is spec
		// created in this method call anyway.
		return carriages;
	}
	
	@Override
	public void update(float delta) {
		// TODO: We should indicate if the component is loaded or not.
		
		if (paused())
			return;
		
		float compLeft = getDeepPosition().x;
		float compRight = compLeft + getSize().width;
		
		// We need to handle the outgoing train first, in order to make space for the next one.
		// As the train leaves us, dragged by the next component, uncrop it gradually
		if (mSent != null) {
			float trainLeft = mSent.getPosition().x;
			
			mSent.setCropLeft(compLeft-trainLeft);
			
			if (trainLeft >= compRight) {
				mSent = null;
			}
		}
		// Move the trains all the way into the garage
		if (mIncomming != null){
			float trainLeft = mIncomming.getPosition().x;
			
			if (trainLeft >= compLeft) {
				onTrainEntered(mIncomming, mCurrent); // Do component specific things
				mIncomming = null;
			}
			else {
				float newLeft = trainLeft + mIncomming.getSpeed()*delta;
				newLeft = Math.min(newLeft, compLeft+0.1f); // Don't move it too far
				float newRight = newLeft + mIncomming.getSize().width;
				mIncomming.setPosition(new Point(newLeft, mIncomming.getPosition().y));
				mIncomming.setCropRight(newRight - compRight);
			}
		}
		// Wait for the right moment to spit them out
		for (Iterator<UITrain> it = mCurrent.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			
			if (compRight < getTrainTaker().leftBlock()) {
				log().debug("Sending a processed element to "+getTrainTaker());
				
				it.remove();
				train.getLayer().setVisible(true);
				train.setPosition(new Point(compRight-train.getSize().width, train.getPosition().y));
				train.setCropRight(0);
				log().debug("compRight: "+compRight);
				log().debug("Before it was: "+getTrainTaker().leftBlock());
				getTrainTaker().takeTrain(train);
				log().debug("Now it is: "+getTrainTaker().leftBlock());
				
				assert mSent == null;
				mSent = train;
			}
		}
	}


	@Override
	public void takeTrain(UITrain train) {
		// The train can't have passed us already. This makes things a lot simpler. 
		assert train.getPosition().x < getDeepPosition().x+getSize().width;
		// We can only drag in one train at time.
		assert mIncomming == null;
		mIncomming = train;
		log().debug("Got a train to move inside.");
	}

	@Override
	public float leftBlock() {
		// Nothing is supposed to stick through this component
		// However because of the hack used to 'truncate' trains, we can't
		// 'check' this assertion.
		//assert mTrainTaker.leftBlock() > getPosition().x;
		
		// If we don't have anything going on, we don't block
		float res = Float.MAX_VALUE;
		
		// Don't allow trains to jump over us
		res = Math.min(res, getDeepPosition().x+getSize().width-0.1f);
		
		// If something is being moved in, clearly don't overlap with it
		if (mIncomming != null)
			res = Math.min(res, mIncomming.getPosition().x - UITrain.PADDING);
		
		// If something is already being processed, don't send more stuff in.
		// Notice that the implementations of components may 'hide' trains from
		// us, that is they don't add them to mCurrent in order to have more trains
		// moved in.
		if (!mCurrent.isEmpty() || mSent != null)
			res = Math.min(res, getDeepPosition().x);
		
		return res;
	}
	
	/**
	 * Normally components override takeTrain to take control over a train that
	 * now has it right most side in the component.
	 * BlackBoxComponents on the other hand, only have to manage trains that are
	 * 'all the way inside the component'. That is fully hidden from the user.
	 * This method allows components to do their work, and when they are ready,
	 * output one or more trains to 'currentTrains' which will be driven out of the
	 * black box at the first given opportunity.
	 * @param train A new train that is now hidden inside the component
	 * @param currentTrains A queue of the next trains the component will output
	 */
	public abstract void onTrainEntered(UITrain train, Queue<UITrain> currentTrains);
}
