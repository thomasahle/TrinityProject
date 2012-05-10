package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.ArrayList;
import java.util.List;

import pythagoras.f.Point;

public class UIStartComponent extends UIIdentityComponent {

	public UIStartComponent() {
		this(new ArrayList<UITrain>());
	}

	public UIStartComponent(List<UITrain> trains) {
		super(calcWidth(trains));
		
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
		for (UITrain train : input)
			width += train.getSize().width;
		return width;
	}
	
	@Override
	public void setPosition(Point position) {
		super.setPosition(position);
		
	}
}
