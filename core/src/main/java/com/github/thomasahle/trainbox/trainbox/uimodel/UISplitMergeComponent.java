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

public class UISplitMergeComponent extends AbstractComposite {
	
	private Deque<UITrain> mTopBuffer = new ArrayDeque<UITrain>();
	private Deque<UITrain> mBotBuffer = new ArrayDeque<UITrain>();
	private Deque<UITrain> mNextInbuf;
	
	private Dimension mSize;
	private UIComponent mTopComp, mBotComp;
	
	private GroupLayer mBackLayer = graphics().createGroupLayer();
	private GroupLayer mFrontLayer = graphics().createGroupLayer();
	
	public UISplitMergeComponent(UIComponent top, UIComponent bot) {
		mNextInbuf = mTopBuffer;
		mNextTaker = mTopTaker;
		//mNextInbuf = mBotBuffer;
		//mNextTaker = mBotTaker;
		
		mTopComp = top;
		mBotComp = bot;
		add(top);
		add(bot);
		onSizeChanged(top, new Dimension(0,0));
		
		top.setTrainTaker(mTopTaker);
		bot.setTrainTaker(mBotTaker);
	}
	
	private void add(UIComponent comp) {
		mBackLayer.add(comp.getBackLayer());
		mFrontLayer.add(comp.getFrontLayer());
		super.install(comp);
	}
	
	@Override
	public void onSizeChanged(UIComponent source, Dimension oldSize) {
		// TODO: Should those be centered horizontally?
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
	
	@Override
	public List<UIComponent> getChildren() {
		return Arrays.asList(mTopComp, mBotComp);
	}

	@Override
	public boolean insertChildAt(UIComponent child, Point position) {
		if (position.y < mTopComp.getSize().height
				&& position.x < mTopComp.getSize().width
				&& mTopComp instanceof UIComposite) {
			return ((UIComposite)mTopComp).insertChildAt(child, position);
		}
		else if (position.y >= mTopComp.getSize().height
				&& position.x < mBotComp.getSize().width
				&& mBotComp instanceof UIComposite) {
			Point newPoint = new Point(position.x, position.y - mTopComp.getSize().height);
			return ((UIComposite)mBotComp).insertChildAt(child, newPoint);
		}
		// We could also check if a identity component has been clicked, which
		// is a direct child of ours, but let's just assume that we only have
		// horizontal components as children
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
		if (!mTopBuffer.isEmpty() && mTopComp.leftBlock() >= mTopComp.getPosition().x) {
			log().debug("Giving waiting split train up to "+mTopComp);
			
			UITrain ttrain = mTopBuffer.poll();
			ttrain.getLayer().setVisible(true);
			mTopComp.takeTrain(ttrain);
		}
		
		if (!mBotBuffer.isEmpty() && mBotComp.leftBlock() >= mBotComp.getPosition().x) {
			log().debug("Giving waiting split train down to "+mBotComp);
			
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
	
	private TrainTaker mTopTaker = new TrainTaker() {
		@Override
		public void takeTrain(UITrain train) {
			getTrainTaker().takeTrain(train);
			mNextTaker = mBotTaker;
		}
		@Override
		public float leftBlock() {
			if (mNextTaker == this)
				return getTrainTaker().leftBlock();
			return getDeepPosition().x + getSize().width;
		}
	};
	
	private TrainTaker mBotTaker = new TrainTaker() {
		@Override
		public void takeTrain(UITrain train) {
			getTrainTaker().takeTrain(train);
			mNextTaker = mTopTaker;
		}
		@Override
		public float leftBlock() {
			if (mNextTaker == this)
				return getTrainTaker().leftBlock();
			return getDeepPosition().x + getSize().width;
		}
	};
	private TrainTaker mNextTaker;
}
