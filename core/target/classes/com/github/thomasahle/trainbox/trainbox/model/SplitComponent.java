package com.github.thomasahle.trainbox.trainbox.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SplitComponent implements Composite {
	
	private final static int SIZE = 2;
	private final static boolean PRI_UP = true;
	private final static boolean PRI_DOWN = false;
	
	private Component[] mComponents = new Component[2];
	// Is an array really needed here? I think just having topComponent and bottomComponent would be better - Matt
	
	private boolean outPriority;
	private boolean inPriority;
	
	private int mNextInput, mNextOutput;
	
	/**
	 * @param top The 'top' component
	 * @param bottom The 'bottom' component
	 * @param outPriority Which train should be output first initially?
	 * @param inPriority Which train should be input first initially?
	 */
	public SplitComponent(Component top, Component bottom, boolean inPriority, boolean outPriority) {
		mComponents[0] = top;
		mComponents[1] = bottom;
		this.inPriority = inPriority;
		this.outPriority = outPriority;
		mNextInput = inPriority ? 0 : 1;
		mNextOutput = outPriority ? 0 : 1;
	}
	
	public void flipInPriority(){
		inPriority = !inPriority;
		mNextInput = inPriority ? 0 : 1;
	}
	
	public void flipOutPriority(){
		outPriority = !outPriority;
		mNextOutput = outPriority ? 0 : 1;
	}
	
	@Override
	public boolean canEnter() {
		return mComponents[mNextInput].canEnter();
	}

	@Override
	public void enter(Train train) {
		mComponents[mNextInput].enter(train);
		flipInPriority();
	}

	@Override
	public boolean canLeave() {
		return mComponents[mNextOutput].canLeave();
	}

	@Override
	public Train leave() {
		Train train = mComponents[mNextOutput].leave();
		flipOutPriority();
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
