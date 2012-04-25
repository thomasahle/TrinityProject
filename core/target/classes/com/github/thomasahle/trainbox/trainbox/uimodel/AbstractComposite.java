package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.ArrayList;
import java.util.List;

/**
 * This is just a helper abstract.
 */
public abstract class AbstractComposite implements UIComposite {
	@Override
	public List<UITrain> getCarriages() {
		List<UITrain> carriages = new ArrayList<UITrain>();
		for (UIComponent comp : getChildren())
			carriages.addAll(comp.getCarriages());
		return carriages;
	}
}
