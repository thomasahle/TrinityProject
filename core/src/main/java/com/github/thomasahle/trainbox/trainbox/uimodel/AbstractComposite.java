package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.ArrayList;
import java.util.List;

/**
 * This is just a helper abstract.
 */
public abstract class AbstractComposite extends AbstractComponent implements
		UIComposite, TrainsChangedListener, SizeChangedListener {
	public List<UITrain> getTrains() {
		List<UITrain> mTrains = new ArrayList<UITrain>();
		for (UIComponent comp : getChildren())
			mTrains.addAll(comp.getTrains());
		return mTrains;
	}

	@Override
	public void update(float delta) {
		List<UIComponent> list = getChildren();
		for (int i = list.size() - 1; i >= 0; i--)
			list.get(i).update(delta);
	}

	protected void install(UIComponent child) {
		child.setTrainsChangedListener(this);
		child.setSizeChangedListener(this);
		child.paused(paused());
		child.onAdded(this);
	}
	
	protected void uninstall(UIComponent child) {
		child.onRemoved(this);
	}

	public void onTrainCreated(UITrain train) {
		fireTrainCreated(train);
	}

	public void onTrainDestroyed(UITrain train) {
		fireTrainDestroyed(train);
	}

	public void paused(boolean paused) {
		super.paused(paused);
		for (UIComponent comp : getChildren())
			comp.paused(paused);
	}
	
	public void reset(){
		for(UIComponent child : getChildren()){
			child.reset();
		}
	}
	public boolean locked(){ //returns true if the composite has children or is manually locked via the locked variable.
		Boolean res = false;
		if(getChildren().size()>0) res = true;
		res = res || locked;
		return res;
	}
	
}
