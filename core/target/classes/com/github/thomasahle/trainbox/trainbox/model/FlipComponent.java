package com.github.thomasahle.trainbox.trainbox.model;

public class FlipComponent implements Component {
	
	private final Component actual;
	
	public FlipComponent(Component prev) {
		actual = new SplitComponent(
				new IdentityComponent(prev),
				new IdentityComponent(prev),
				true);
	}
	
	@Override
	public Train pull() {
		return actual.pull();
	}
}
