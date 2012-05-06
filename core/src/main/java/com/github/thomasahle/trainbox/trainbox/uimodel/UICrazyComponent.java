package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

// A tricky thing here is to avoid deadlocks.
// For a start we can fix them using buffer component

// Even simpler start: Use teleportation instead of lead from/to

public class UICrazyComponent extends AbstractComposite {
	
	private Deque<UITrain> mTopBuffer = new ArrayDeque<UITrain>();
	private Deque<UITrain> mBotBuffer = new ArrayDeque<UITrain>();
	
	
	private UIComponent mTopComp, mBotComp;
	
	@Override
	public List<UIComponent> getChildren() {
		return Arrays.asList(mTopComp, mBotComp);
	}

	@Override
	public boolean insertChildAt(UIComponent child, Point position) {
		// We don't accept inserts
		return false;
	}

	@Override
	public Dimension getSize() {
		return new Dimension(
				Math.max(mTopComp.getSize().width, mBotComp.getSize().width),
				mTopComp.getSize().height + mBotComp.getSize().height);
	}

	@Override
	public Layer getBackLayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Layer getFrontLayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void takeTrain(UITrain train) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float leftBlock() {
		// TODO Auto-generated method stub
		return 0;
	}

}
