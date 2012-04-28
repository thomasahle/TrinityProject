package com.github.thomasahle.trainbox.trainbox.model;

import playn.core.Image;

public interface Component {
	public boolean canEnter();
	public void enter(Train train);
	public boolean canLeave();
	public Train leave();
	public boolean isEmpty();
}
