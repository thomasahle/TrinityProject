package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import playn.core.GroupLayer;
import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

// A tricky thing here is to avoid deadlocks.
// For a start we can fix them using buffer component

// Even simpler start: Use teleportation instead of lead from/to

public class UICrazyComponent extends AbstractComposite implements SizeChangedListener {
	
	private Deque<UITrain> mTopBuffer = new ArrayDeque<UITrain>();
	private Deque<UITrain> mBotBuffer = new ArrayDeque<UITrain>();
	private Deque<UITrain> mNextInbuf;
	
	private Dimension mSize;
	private UIComponent mTopComp, mBotComp;
	private TrainTaker mNextOut;
	
	private GroupLayer mBackLayer = graphics().createGroupLayer();
	private GroupLayer mFrontLayer = graphics().createGroupLayer();
	
	public UICrazyComponent(UIComponent top, UIComponent bot) {
		mNextInbuf = mTopBuffer;
		
		mTopComp = top;
		mBotComp = bot;
		add(top);
		add(bot);
		onSizeChanged(top, new Dimension(0,0));
		
		mNextOut = top;
		top.setTrainTaker(new TrainTaker() {
			@Override
			public void takeTrain(UITrain train) {
				getTrainTaker().takeTrain(train);
				mNextOut = mBotComp;
			}
			@Override
			public float leftBlock() {
				if (mNextOut == this)
					return Float.MAX_VALUE;
				return Float.MIN_VALUE;
			}
		});
		bot.setTrainTaker(new TrainTaker() {
			@Override
			public void takeTrain(UITrain train) {
				getTrainTaker().takeTrain(train);
				mNextOut = mTopComp;
			}
			@Override
			public float leftBlock() {
				if (mNextOut == this)
					return Float.MAX_VALUE;
				return Float.MIN_VALUE;
			}
		});
	}
	
	private void add(UIComponent comp) {
		comp.setSizeChangedListener(this);
		mBackLayer.add(comp.getBackLayer());
		mFrontLayer.add(comp.getFrontLayer());
		super.install(comp);
	}
	
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
		return mSize;
	}

	@Override
	public Layer getBackLayer() {
		return mBackLayer;
	}

	@Override
	public Layer getFrontLayer() {
		return mFrontLayer;
	}
	
	@Override
	public void update(float delta) {
		if (!mTopBuffer.isEmpty() && mTopComp.leftBlock() >= 0) {
			log().debug("Giving waiting split train to "+mTopComp);
			UITrain ttrain = mTopBuffer.poll();
			ttrain.getLayer().setVisible(true);
			mTopComp.takeTrain(ttrain);
		}
		if (!mBotBuffer.isEmpty() && mBotComp.leftBlock() >= 0) {
			log().debug("Giving waiting split train to "+mBotComp);
			UITrain ttrain = mBotBuffer.poll();
			ttrain.getLayer().setVisible(true);
			mBotComp.takeTrain(ttrain);
		}
		
		// Update children
		super.update(delta);
	}
	
	@Override
	public void takeTrain(UITrain train) {
		log().debug("Got train to splitter, sending it "+(mNextInbuf==mTopBuffer?"up":"down"));
		train.getLayer().setVisible(false);
		mNextInbuf.add(train);
		mNextInbuf = (mNextInbuf==mTopBuffer) ? mBotBuffer : mTopBuffer;
	}

	@Override
	public float leftBlock() {
		return Float.MAX_VALUE;
	}
	
	@Override
	public void onSizeChanged(UIComponent source, Dimension oldSize) {
		//mTopComp.setPosition(new Point(0, 0));
		mBotComp.setPosition(new Point(0, mTopComp.getSize().height));
		
		Dimension newSize = new Dimension(
				Math.max(mTopComp.getSize().width, mBotComp.getSize().width),
				mTopComp.getSize().height + mBotComp.getSize().height);
		
		if (!newSize.equals(mSize)) {
			Dimension ourOldSize = mSize;
			mSize = newSize;
			fireSizeChanged(ourOldSize);
		}
	}
}
