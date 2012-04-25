package com.github.thomasahle.trainbox.trainbox.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SplitComponent implements Composite {
	
	private final static int SIZE = 2;
	private Component[] mComponents = new Component[2];
	private boolean mTwist;
	
	private int mNextInput, mNextOutput;
	
	/**
	 * @param fst The 'top' component
	 * @param snd The 'bottom' component
	 * @param twist If `twist` is true, the bottom component will be made to
	 *  	  output before the top one.
	 */
	public SplitComponent(Component fst, Component snd, boolean twist) {
		mComponents[0] = fst;
		mComponents[1] = snd;
		mNextInput = 0;
		mNextOutput = mTwist ? 1 : 0;
	}
	
	@Override
	public boolean canEnter() {
		return mComponents[mNextInput].canEnter();
	}

	@Override
	public void enter(Train train) {
		mComponents[mNextInput].enter(train);
		mNextInput = (mNextInput + 1) % SIZE;
	}

	@Override
	public boolean canLeave() {
		return mComponents[mNextOutput].canLeave();
	}

	@Override
	public Train leave() {
		Train train = mComponents[mNextOutput].leave();
		mNextOutput = (mNextOutput + 1) % SIZE;
		return train;
	}

	@Override
	public List<Component> getChildren() {
		return Collections.unmodifiableList(Arrays.asList(mComponents));
	}

	@Override
	public boolean isEmpty() {
		for (Component comp : mComponents)
			if (!comp.isEmpty())
				return false;
		return true;
	}
}
