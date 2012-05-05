package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.List;

import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

// A tricky thing here is to avoid deadlocks.
// For a start we can fix them using buffer component

public class UICrazyComponent extends AbstractComposite {

	@Override
	public List<UIComponent> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertChildAt(UIComponent child, Point position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Dimension getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Layer getBackLayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Layer getFrontLayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void takeTrain(UITrain train) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float leftBlock() {
		// TODO Auto-generated method stub
		return 0;
	}

}
