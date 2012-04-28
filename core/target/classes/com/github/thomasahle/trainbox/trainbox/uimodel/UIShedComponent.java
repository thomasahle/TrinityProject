package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.awt.Dimension;
import java.util.List;

import playn.core.Layer;

public class UIShedComponent implements UIComponent {
	
	private List<Integer> mGoal;

	public UIShedComponent (List<Integer> goal) {
		mGoal = goal;
	}
	
	@Override
	public List<UITrain> getCarriages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Layer getLayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void enterTrain(UITrain train) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

}
