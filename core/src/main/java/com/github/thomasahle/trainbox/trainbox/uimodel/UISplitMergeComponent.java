package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

import com.github.thomasahle.trainbox.trainbox.model.Train;
import com.github.thomasahle.trainbox.trainbox.util.QuadPath;

// It's expected that this deadlocks when the output is waiting for the input.
// This is not a task for the splitmerge to solve, but rather for the player.

public class UISplitMergeComponent extends AbstractComposite {
	
	private static final float BOTTOM_ALPHA = 0.5f;

	private final static float SIDES_WIDTH = 150;
	
	private LinkedList<UITrain> mIngoing = new LinkedList<UITrain>();
	private LinkedList<UITrain> mOutgoing= new LinkedList<UITrain>();
	
	private Set<UITrain> mUpgoing = new HashSet<UITrain>();
	private Set<UITrain> mDowngoing = new HashSet<UITrain>();
	private Set<UITrain> mNextDirection;
	
	
	private Dimension mSize;
	private UIComponent mTopComp, mBotComp;
	
	private ImageLayer mLeftLayer = graphics().createImageLayer();
	private ImageLayer mRightLayer = graphics().createImageLayer();
	private GroupLayer mBackLayer = graphics().createGroupLayer();
	private GroupLayer mFrontLayer = graphics().createGroupLayer();
	
	private QuadPath mUpPathIn, mDownPathIn;
	private QuadPath mUpPathOut, mDownPathOut;
	
	public UISplitMergeComponent(UIComponent top, UIComponent bot) {
		mNextDirection = mUpgoing;
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
		assert top instanceof UIHorizontalComponent;
		assert bot instanceof UIHorizontalComponent;
		
		mBackLayer.add(mLeftLayer);
		mBackLayer.add(mRightLayer);
	}
	
	private QuadPath createPath(float x0, float y0, float x2, float y2) {
		QuadPath path = new QuadPath();
		float x1 = (x0+x2)/2;
		float y1 = (y0+y2)/2;
		path.moveTo(x0, y0);
		path.quadraticCurveTo(x1, y0, x1, y1);
		path.quadraticCurveTo(x1, y2, x2, y2);
		return path;
	}
	
	private void updatePaths() {
		mUpPathIn = createPath (
				0, mSize.height/2,
				SIDES_WIDTH, mTopComp.getSize().height/2);
		mDownPathIn = createPath (
				0, mSize.height/2,
				SIDES_WIDTH, mBotComp.getPosition().y + mBotComp.getSize().height/2);
		mUpPathOut = createPath (
				0, mTopComp.getSize().height/2,
				SIDES_WIDTH, mSize.height/2);
		mDownPathOut = createPath (
				0, mBotComp.getPosition().y + mBotComp.getSize().height/2,
				SIDES_WIDTH, mSize.height/2);
	}
	
	private void updateImages() {
		QuadPath first, second;
		
		CanvasImage imageLeft = graphics().createImage((int)SIDES_WIDTH, (int)mSize.height);
		if ((!mIngoing.isEmpty() && mUpgoing.contains(mIngoing.get(mIngoing.size()-1))) || (mIngoing.isEmpty() && mNextDirection == mUpgoing)) {
			first = mDownPathIn;
			second = mUpPathIn;
		} else {
			first = mUpPathIn;
			second = mDownPathIn;
		}
		drawTwoTracks(imageLeft, first, second);
		mLeftLayer.setImage(imageLeft);
		
		CanvasImage imageRight = graphics().createImage((int)SIDES_WIDTH, (int)mSize.height);
		if ((!mOutgoing.isEmpty() && mUpgoing.contains(mOutgoing.peek())) || (mOutgoing.isEmpty() && mNextTaker == mTopTaker)) {
			first = mDownPathOut;
			second = mUpPathOut;
		} else {
			first = mUpPathOut;
			second = mDownPathOut;
		}
		drawTwoTracks(imageRight, first, second);
		mRightLayer.setImage(imageRight);
	}

	private void drawTwoTracks(CanvasImage image, QuadPath bottom, QuadPath top) {
		image.canvas().setAlpha(BOTTOM_ALPHA);
		ComponentHelper.drawBendTrack(image.canvas(), bottom);
		image.canvas().setAlpha(1f);
		ComponentHelper.drawBendTrack(image.canvas(), top);
	}
	
	private void add(UIComponent comp) {
		mBackLayer.add(comp.getBackLayer());
		mFrontLayer.add(comp.getFrontLayer());
		super.install(comp);
	}
	
	@Override
	public void onSizeChanged(UIComponent source, Dimension oldSize) {
		
		Dimension newSize = new Dimension(
				Math.max(mTopComp.getSize().width, mBotComp.getSize().width) + 2*SIDES_WIDTH,
				mTopComp.getSize().height + mBotComp.getSize().height);
		
		if (!newSize.equals(mSize)) {
			mTopComp.setPosition(new Point(SIDES_WIDTH, 0));
			mBotComp.setPosition(new Point(SIDES_WIDTH, mTopComp.getSize().height));
			mRightLayer.setTranslation(newSize.width-SIDES_WIDTH, 0);
			
			Dimension ourOldSize = mSize;
			mSize = newSize;
			fireSizeChanged(ourOldSize);
			
			// Scale
			int diff = (int)(mTopComp.getSize().width - mBotComp.getSize().width);
			if (Math.abs(diff) > 1) {
				UIComposite biggest = (UIComposite)(diff > 0 ? mTopComp : mBotComp);
				UIComposite smallest = (UIComposite)(diff < 0 ? mTopComp : mBotComp);
				diff = Math.abs(diff);
				// See how small we can make the biggest
				for (UIComponent comp : biggest.getChildren())
					if (comp instanceof UIIdentityComponent) {
						UIIdentityComponent icomp = (UIIdentityComponent)comp;
						int take = Math.max((int)icomp.getSize().width-100, 0);
						take = Math.min(take, diff);
						icomp.setWidth((int)icomp.getSize().width - take);
						diff -= take;
					}
				// Scale the smallest one up
				for (UIComponent comp : smallest.getChildren())
					if (comp instanceof UIIdentityComponent) {
						UIIdentityComponent icomp = (UIIdentityComponent)comp;
						icomp.setWidth((int)icomp.getSize().width + diff);
						break;
					}
			}
			
			updatePaths();
			updateImages();
		}
	}
	
	@Override
	public List<UIComponent> getChildren() {
		return Arrays.asList(mTopComp, mBotComp);
	}
	
	@Override
	public boolean shouldBeDeleted() {
		return ((UIComposite)mTopComp).getChildren().size() == 0
				|| ((UIComposite)mBotComp).getChildren().size() == 0;
	}
	
	@Override
	public boolean insertChildAt(UIComponent child, Point position) {
		if (position.y < mTopComp.getSize().height
				&& SIDES_WIDTH < position.x
				&& position.x <= SIDES_WIDTH + mTopComp.getSize().width
				&& mTopComp instanceof UIComposite) {
			Point newPoint = new Point(position.x-SIDES_WIDTH, position.y);
			return ((UIComposite)mTopComp).insertChildAt(child, newPoint);
		}
		else if (position.y >= mTopComp.getSize().height
				&& SIDES_WIDTH < position.x
				&& position.x <= SIDES_WIDTH + mBotComp.getSize().width
				&& mBotComp instanceof UIComposite) {
			Point newPoint = new Point(position.x-SIDES_WIDTH, position.y - mTopComp.getSize().height);
			return ((UIComposite)mBotComp).insertChildAt(child, newPoint);
		}
		// We could also check if a identity component has been clicked, which
		// is a direct child of ours, but let's just assume that we only have
		// horizontal components as children
		return false;
	}

	@Override
	public boolean deleteChildAt(Point position) {
		// TODO Auto-generated method stub
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
		if (paused())
			return;
		
		float tBorder = Math.min(mUpPathIn.calculateT(mTopComp.leftBlock()), mDownPathIn.calculateT(mBotComp.leftBlock()));
		moveAmazingTrains(mIngoing, delta, getDeepPosition().x, mUpPathIn, mDownPathIn, tBorder, mTopComp, mBotComp);
		
		// Update children
		super.update(delta);
		
		//tBorder = mUpPathOut.calculateT(getTrainTaker().leftBlock());
		tBorder = Float.MAX_VALUE;
		moveAmazingTrains(mOutgoing, delta, getDeepPosition().x + getSize().width - SIDES_WIDTH, mUpPathOut, mDownPathOut, tBorder, getTrainTaker(), getTrainTaker());
		
	}
	
	@Override
	public List<UITrain> getTrains() {
		List<UITrain> trains = new ArrayList<UITrain>(super.getTrains());
		trains.addAll(mIngoing);
		trains.addAll(mOutgoing);
		return trains;
	}
	
	private void moveAmazingTrains(List<UITrain> trains, float delta, float compLeft, QuadPath uppath, QuadPath downpath, float tBorder, TrainTaker top, TrainTaker bot) {
		for (Iterator<UITrain> it = trains.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			float trainLeft = train.getPosition().x;
			float trainRight = trainLeft + train.getSize().width;
			float compRight = compLeft + SIDES_WIDTH;
			
			// If the train is now entirely gone from us.
			if (trainLeft >= compRight) {
				it.remove();
				updateImages();
				continue;
			}
			
			QuadPath path = mUpgoing.contains(train) ? uppath : downpath;
			UIComponent taker = (UIComponent)(mUpgoing.contains(train) ? top : bot);
			
			// If the train is no longer controlled by us, but still 'on us'.
			if (trainRight > compRight) {
				for (UICarriage car : train.getCarriages()) {
					float carLeft = car.getPosition().x + trainLeft - compLeft;
					float carRight = carLeft + car.WIDTH;
					float tCenter = path.calculateT((carRight+carLeft)/2.f);
					tCenter = Math.min(tCenter, tBorder - car.WIDTH/2.f);
					if (carLeft < compRight) {
						float[] slope = path.evaluateSlope(tCenter);
						float[] pos = path.evaluate(tCenter);
						pos[0] += compLeft - trainLeft - car.WIDTH/2.f;
						pos[1] += getDeepPosition().y - (train.getPosition().y + train.getSize().height/2.f);
						//car.setRotation(1,0);
						//pos[0] = Math.max(pos[0], carLeft);
						car.setPosition(new Point(pos[0], pos[1]));
						car.setRotation(slope[0], slope[1]);
					}
					tBorder = tCenter - car.WIDTH/2.f;
				}
				continue;
			}
			
			// Main move carriages part
			for (UICarriage car : train.getCarriages()) {
				float carLeft = car.getPosition().x + trainLeft - compLeft;
				float carRight = carLeft + car.WIDTH;
				float tCenter = path.calculateT((carRight+carLeft)/2.f);
				tCenter = Math.min(tBorder - car.WIDTH/2.f, tCenter + train.getSpeed()*delta);
				
				if (carRight >= 0 || !car.touched()) {
					float[] slope = path.evaluateSlope(tCenter);
					float[] pos = path.evaluate(tCenter);
					pos[0] += compLeft - trainLeft - car.WIDTH/2.f;
					pos[1] += getDeepPosition().y - (train.getPosition().y + train.getSize().height/2.f);
					car.setPosition(new Point(pos[0], pos[1]));
					car.setRotation(slope[0], slope[1]);
				}
				tBorder = tCenter - car.WIDTH/2.f;
			}
			
			// Updating the position of the train
			UICarriage first = train.getCarriages().get(0);
			float newRight = trainLeft + first.getPosition().x + first.WIDTH;
			float newLeft = newRight - train.getSize().width;
			train.setPosition(new Point(newLeft, train.getPosition().y));
			for (UICarriage car : train.getCarriages()) {
				float carLeft = car.getPosition().x + trainLeft - compLeft;
				float carRight = carLeft + car.WIDTH;
				if (carRight >= 0 || !car.touched()) {
					car.setPosition(new Point(car.getPosition().x+trainLeft-newLeft, car.getPosition().y));
				}
			}
			
			if (newRight > compRight) {
				Point oldPos = train.getPosition();
				taker.takeTrain(train);
				// Hack to avoid flicker
				if (trains == mOutgoing) {
					float shifty = oldPos.y - train.getPosition().y;
					for (UICarriage car : train.getCarriages()) {
						car.setPosition(new Point(
								car.getPosition().x,
								car.getPosition().y+shifty));
					}
				}
			}
			
			tBorder -= UITrain.PADDING;
		}
	}
	
	@Override
	public void takeTrain(UITrain train) {
		log().debug("Got train to splitter, sending it "+(mNextDirection==mUpgoing?"up":"down"));
		log().debug("tblock was reported: "+mUpPathIn.calculateT(leftBlock()-getDeepPosition().x));
		assert train.getPosition().x+train.getSize().width <= leftBlock();
		
		mIngoing.add(train);
		mNextDirection.add(train);
		//for (UICarriage car : train.getCarriages())
		//	mTrainT.put(car, train.getPosition().x + car.getPosition().x - getDeepPosition().x + car.getSize().width);
		
		// Hack to avoid flicker
		Point oldPos = train.getPosition();
		if (mNextDirection == mUpgoing) {
			train.vertCenterOn(mTopComp);
		} else {
			train.vertCenterOn(mBotComp);
		}
		float shifty = oldPos.y - train.getPosition().y;
		for (UICarriage car : train.getCarriages()) {
			car.setPosition(new Point(
					car.getPosition().x,
					car.getPosition().y+shifty));
		}
		
		mNextDirection = (mNextDirection==mUpgoing) ? mDowngoing : mUpgoing;
		updateImages();
	}

	@Override
	public float leftBlock() {
		// Channel leftBlock from next component
		float res = getTrainTaker().leftBlock();
		// Don't allow trains to jump over us
		res = Math.min(res, getDeepPosition().x+SIDES_WIDTH-0.1f);
		// Don't overlap trains we currently manage
		if (!mIngoing.isEmpty())
			// FIXME: This is a necessary evil because the real, nice interleaving isn't working.
			res = Math.min(res, getDeepPosition().x);
			//res = Math.min(res, mIngoing.getLast().getPosition().x - UITrain.PADDING);
		return res;
	}
	
	private TrainTaker mTopTaker = new TrainTaker() {
		@Override
		public void takeTrain(UITrain train) {
			assert mOutgoing.isEmpty();
			mUpgoing.add(train);
			mOutgoing.add(train);
			mNextTaker = mBotTaker;
			updateImages();
		}
		@Override
		public float leftBlock() {
			float end = mTopComp.getDeepPosition().x + mTopComp.getSize().width;
			if (mNextTaker == this && mOutgoing.isEmpty())
				return Math.max(getTrainTaker().leftBlock(), end);
			return end;
		}
	};
	
	private TrainTaker mBotTaker = new TrainTaker() {
		@Override
		public void takeTrain(UITrain train) {
			assert mOutgoing.isEmpty();
			mDowngoing.add(train);
			mOutgoing.add(train);
			mNextTaker = mTopTaker;
			updateImages();
		}
		@Override
		public float leftBlock() {
			float end = mBotComp.getDeepPosition().x + mBotComp.getSize().width;
			if (mNextTaker == this && mOutgoing.isEmpty())
				return Math.max(getTrainTaker().leftBlock(), end);
			return end;
		}
	};
	private TrainTaker mNextTaker;
}
