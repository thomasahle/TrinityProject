package com.github.thomasahle.trainbox.trainbox.model;


public class MergeComponent implements Component {
	
	private Component fst, snd;
	
	/**
	 * @param fst The 'top' component
	 * @param snd The 'bottom' component
	 */
	public MergeComponent(Component top, Component bottom) {
		fst = top;
		snd = bottom;
	}
	
	@Override
	public Train pull() {
		Train t = fst.pull();
		if (t.length() != 0) {
			Component temp = fst;
			fst = snd;
			snd = temp;
		}
		return t;
	}
}
