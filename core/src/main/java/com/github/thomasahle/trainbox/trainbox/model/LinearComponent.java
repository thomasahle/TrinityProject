package com.github.thomasahle.trainbox.trainbox.model;

import java.util.Collections;
import java.util.List;

public class LinearComponent implements Composite {
	
	private List<Component> mComponents;
	
	public LinearComponent (List<Component> components) {
		mComponents = components;
	}
	
	@Override
	public boolean canEnter() {
		return !mComponents.isEmpty()
				&& mComponents.get(0).canEnter();
	}

	@Override
	public void enter(Train train) {
		if (!canEnter())
			throw new IllegalStateException();
		mComponents.get(0).enter(train);
	}

	@Override
	public boolean canLeave() {
		return !mComponents.isEmpty()
				&& mComponents.get(mComponents.size()-1).canLeave();
	}

	@Override
	public Train leave() {
		if (!canLeave())
			throw new IllegalStateException();
		return mComponents.get(mComponents.size()-1).leave();
	}

	@Override
	public boolean isEmpty() {
		for (Component comp : mComponents)
			if (!comp.isEmpty())
				return false;
		return true;
	}

	@Override
	public List<Component> getChildren() {
		return Collections.unmodifiableList(mComponents);
	}
}
