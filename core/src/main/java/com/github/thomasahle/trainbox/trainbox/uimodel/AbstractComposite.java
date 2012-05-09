package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.List;

/**
 * This is just a helper abstract.
 */
public abstract class AbstractComposite extends AbstractComponent implements UIComposite, TrainsChangedListener, SizeChangedListener {
	@Override
	public void update(float delta) {
		List<UIComponent> list = getChildren();
		for (int i = list.size()-1; i >= 0; i--)
			list.get(i).update(delta);
	}
	
	protected void install(UIComponent child) {
		child.setTrainsChangedListener(this);
		child.setSizeChangedListener(this);
		child.paused(paused());
		child.onAdded(this);
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
}
