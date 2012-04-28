package com.github.thomasahle.trainbox.trainbox.model;

public class BoxComponent implements Component {
	
	private final Component prev;
	
	public BoxComponent(Component prev) {
		this.prev = prev;
	}
	
	@Override
	public Train pull() {
		Train t = prev.pull();
		t.addBehind(prev.pull());
		return t;
	}
}
