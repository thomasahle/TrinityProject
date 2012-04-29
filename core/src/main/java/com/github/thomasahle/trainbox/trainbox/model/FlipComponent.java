package com.github.thomasahle.trainbox.trainbox.model;

public class FlipComponent implements Component {
	
	private final Component actual;
	
	public FlipComponent(Component prev) {
		SplitComponents prevs = new SplitComponents(prev);
		actual = new MergeComponent(prevs.snd, prevs.fst);
	}
	
	@Override
	public Train pull() {
		return actual.pull();
	}
}
