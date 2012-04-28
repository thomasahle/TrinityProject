package com.github.thomasahle.trainbox.trainbox.model;

public class IdentityComponent implements Component {
	private final Component prev;
	
	public IdentityComponent(Component prev) {
		this.prev = prev;
	}
	
	@Override
	public Train pull() {
		return prev.pull();
	}
}
