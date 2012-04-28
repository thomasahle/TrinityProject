package com.github.thomasahle.trainbox.trainbox.model;


public class SplitComponent implements Component {
	
	private Component fst, snd, nextPull;
	
	/**
	 * @param fst The 'top' component
	 * @param snd The 'bottom' component
	 * @param twist If `twist` is true, the bottom component will be made to
	 *  	  output before the top one.
	 */
	public SplitComponent(Component top, Component bottom, boolean twist) {
		// StartingComponent c = new StartingComponent();
		// 	new SplitComponent(
		// 		new DupComponent(c),
		// 		new DupComponent(c))
		fst = top;
		snd = bottom;
		nextPull = twist ? fst : snd;
	}
	
	@Override
	public Train pull() {
		Train t = nextPull.pull();
		if (t.length() != 0) {
			nextPull = nextPull==fst ? snd : fst;
		}
		return t;
	}
}
