package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.ArrayList;
import java.util.List;

/**
 * This is just a helper abstract.
 */
public abstract class AbstractComposite extends AbstractComponent implements UIComposite {
	@Override
	public List<UITrain> getCarriages() {
		List<UITrain> carriages = new ArrayList<UITrain>();
		for (UIComponent comp : getChildren())
			carriages.addAll(comp.getCarriages());
		return carriages;
	}
	
	@Override
	public void update(float delta) {
		List<UIComponent> list = getChildren();
		for (int i = list.size()-1; i >= 0; i--)
			list.get(i).update(delta);
	}
}
