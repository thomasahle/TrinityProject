package com.github.thomasahle.trainbox.trainbox.uimodel;

import pythagoras.f.Point;

public abstract class AbstractComponent implements UIComponent {
	private UIComposite mParent;
	private Point mPosition = new Point();
	private TrainTaker mTrainTaker = new NullTrainTaker();
	private TrainsChangedListener mTrainsChangedListener;

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
		getBackLayer().setTranslation(position.x, position.y);
		getFrontLayer().setTranslation(position.x, position.y);
		mPosition = position;
	}

	@Override
	public Point getPosition() {
		return mPosition;
	}
	
	protected Point getDeepPosition() {
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
	protected void fireTrainCreatedEvent(UITrain train) {
		mTrainsChangedListener.onTrainCreated(train);
	}
	protected void fireTrainDestroyedEvent(UITrain train) {
		mTrainsChangedListener.onTrainDestroyed(train);
	}
}
