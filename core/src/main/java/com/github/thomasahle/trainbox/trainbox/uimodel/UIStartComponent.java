package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.List;

import com.github.thomasahle.trainbox.trainbox.model.Train;

import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class UIStartComponent implements UIComponent {

	public UIStartComponent(List<Train> input) {
		// TODO Auto-generated constructor stub
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
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTrainTaker(TrainTaker listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public TrainTaker getTrainTaker() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPosition(Point position) {
		// TODO Auto-generated method stub

	}

	@Override
	public Point getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAdded(UIComposite parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRemoved(UIComposite parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public UIComposite getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTrainsChangedListener(TrainsChangedListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paused(boolean paused) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean paused() {
		// TODO Auto-generated method stub
		return false;
	}

}
