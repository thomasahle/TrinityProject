package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.Iterator;
import java.util.Queue;

import pythagoras.f.Dimension;
import pythagoras.f.Point;

public abstract class AbstractComponent implements UIComponent {
	private UIComposite mParent;
	private Point mPosition = new Point();
	private TrainTaker mTrainTaker = new NullTrainTaker();
	private TrainsChangedListener mTrainsChangedListener;
	private boolean mPaused;
	private SizeChangedListener mSizeChangedListener;
	
	@Override
	public void onAdded(UIComposite parent) {
		mParent = parent;
	}
	@Override
	public void onRemoved(UIComposite parent) {
		mParent = null;
	}

	@Override
	public UIComposite getParent() {
		return mParent;
	}
	
	
	@Override
	public void setPosition(Point position) {
		// Move layer
		getBackLayer().setTranslation(position.x, position.y);
		getFrontLayer().setTranslation(position.x, position.y);
		// Move trains
		float diffx = position.x - getPosition().x;
		float diffy = position.y - getPosition().y;
		for (UITrain train : getTrains()) {
			train.setPosition(new Point(train.getPosition().x + diffx, train.getPosition().y + diffy));
		}
		// Save
		mPosition = position;
	}

	@Override
	public Point getPosition() {
		return mPosition;
	}
	
	@Override
	public Point getDeepPosition() {
		Point p = getPosition();
		UIComposite par = getParent();
		while (par != null) {
			p = p.add(par.getPosition().x, par.getPosition().y);
			par = par.getParent();
		}
		return p;
	}
	
	@Override
	public void setTrainTaker(TrainTaker listener) {
		this.mTrainTaker = listener;
	}

	@Override
	public TrainTaker getTrainTaker() {
		return mTrainTaker;
	}
	
	
	@Override
	public void setTrainsChangedListener(TrainsChangedListener listener) {
		mTrainsChangedListener = listener;
	}
	protected void fireTrainCreated(UITrain train) {
		if (mTrainsChangedListener != null)
			mTrainsChangedListener.onTrainCreated(train);
	}
	protected void fireTrainDestroyed(UITrain train) {
		if (mTrainsChangedListener != null){
			//train.getLayer().setVisible(false);
			train.setPosition(new Point(Float.MAX_VALUE, train.getPosition().y));
			mTrainsChangedListener.onTrainDestroyed(train);
			//train.getLayer().destroy();
		}
	}
	
	
	@Override
	public void paused(boolean paused) {
		mPaused = paused;
	}
	@Override
	public boolean paused() {
		return mPaused;
	}
	
	
	@Override
	public void setSizeChangedListener(SizeChangedListener listener) {
		mSizeChangedListener = listener;
	}
	protected void fireSizeChanged(Dimension oldSize) {
		if (mSizeChangedListener != null)
			mSizeChangedListener.onSizeChanged(this, oldSize);
	}
	
	/**
	 * Helper method for moving trains, when it just has to be done the obvious way
	 * @param trains The trains to be moved, in order
	 * @param delta The amount to move them
	 */
	public float moveTrains(Queue<UITrain> trains, float delta) {
		float rightBorder = getTrainTaker().leftBlock();
		for (Iterator<UITrain> it = trains.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			float trainLeft = train.getPosition().x;
			float compLeft = getDeepPosition().x;
			float trainRight = trainLeft + train.getSize().width;
			float compRight = compLeft + getSize().width;
			
			// If the train is now entirely gone from us.
			if (trainLeft >= compRight) {
				it.remove();
				continue;
			}
			// If the train is no longer controlled by us, but still 'on us'.
			if (trainRight > compRight) {
				continue;
			}
			// See how far we can move it
			float newRight = Math.min(rightBorder, trainRight + UITrain.SPEED*delta);
			float newLeft = newRight-train.getSize().width;
			train.setPosition(new Point(newLeft, train.getPosition().y));
			// If it is now out in the right side, give it away
			if (newRight > compRight) {
				getTrainTaker().takeTrain(train);
			}
			// Update our working right border
			rightBorder = newLeft - UITrain.PADDING;
		}
		return rightBorder;
	}
}
