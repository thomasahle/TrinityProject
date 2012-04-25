package com.github.thomasahle.trainbox.trainbox.model;

public interface Component {
	public boolean canEnter();
	public void enter(Train train);
	public boolean canLeave();
	public Train leave();
	public boolean isEmpty();
}
