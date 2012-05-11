package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pythagoras.f.Point;

public class UIStartComponent extends UIIdentityComponent {

	private List<UITrain> mTrains;

	public UIStartComponent() {
		this(new ArrayList<UITrain>());
	}
	
	@Override
	public List<UITrain> getTrains() {
	    return Collections.unmodifiableList(new ArrayList<UITrain>(mTrains));
	}
	
	public UIStartComponent(List<UITrain> trains) {
		super(calcWidth(trains));
		mTrains = trains;
		
		float width = getSize().width;
		float right = width;
		for (UITrain train : trains) {
			takeTrain(train);
			float left = right - train.getSize().width;
			train.setPosition(new Point(left, train.getPosition().y));
			right = left - UITrain.PADDING;
		}
	}

	private static int calcWidth(List<UITrain> input) {
		int width = 0;
		for (UITrain train : input) {
			width += train.getSize().width;
			width += UITrain.PADDING;
		}
		return width;
	}
}
