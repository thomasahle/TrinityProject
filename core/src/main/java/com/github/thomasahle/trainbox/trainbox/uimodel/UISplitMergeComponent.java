package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.Path;
import pythagoras.f.Dimension;
import pythagoras.f.FloatMath;
import pythagoras.f.Point;

import com.github.thomasahle.trainbox.trainbox.util.QuadPath;

// It's expected that this deadlocks when the output is waiting for the input.
// This is not a task for the splitmerge to solve, but rather for the player.

public class UISplitMergeComponent extends AbstractComposite {
	
	private final static float SIDES_WIDTH = 150;
	
	private LinkedList<UITrain> mTopBuffer = new LinkedList<UITrain>();
	private LinkedList<UITrain> mBotBuffer = new LinkedList<UITrain>();
	private LinkedList<UITrain> mNextInbuf;
	
	private Dimension mSize;
	private UIComponent mTopComp, mBotComp;
	
	private ImageLayer mLeftLayer = graphics().createImageLayer();
	private ImageLayer mRightLayer = graphics().createImageLayer();
	private GroupLayer mBackLayer = graphics().createGroupLayer();
	private GroupLayer mFrontLayer = graphics().createGroupLayer();
	
	private QuadPath mUpPathIn, mDownPathIn;
	private QuadPath mUpPathOut, mDownPathOut;
	
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
		
		mBackLayer.add(mLeftLayer);
		mBackLayer.add(mRightLayer);
		mRightLayer.setImage(graphics().createImage(1, 1));
		mLeftLayer.setImage(graphics().createImage(1, 1));
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
	
	private void drawBendTrack(CanvasImage image, QuadPath path) {
		Path playnPath = path.paintPath(image.canvas().createPath());
		image.canvas().setStrokeColor(0xff000000);
		image.canvas().setStrokeWidth(30);
		image.canvas().strokePath(playnPath);
		image.canvas().setStrokeColor(0xffffffff);
		image.canvas().setStrokeWidth(24);
		image.canvas().strokePath(playnPath);
	}
	private void updateLeftSideLayer() {
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
		
		CanvasImage imageLeft = graphics().createImage((int)SIDES_WIDTH, (int)mSize.height);
		drawBendTrack(imageLeft, mUpPathIn);
		drawBendTrack(imageLeft, mDownPathIn);
		mLeftLayer.setImage(imageLeft);
		
		CanvasImage imageRight = graphics().createImage((int)SIDES_WIDTH, (int)mSize.height);
		drawBendTrack(imageRight, mUpPathOut);
		drawBendTrack(imageRight, mDownPathOut);
		mRightLayer.setImage(imageRight);
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
			
			updateLeftSideLayer();
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
		if (paused())
			return;
		float tBorder = mUpPathIn.calculateT(getTrainTaker().leftBlock());
		//if (mTopBuffer.size() > 0)
		//	log().debug(tBorder+" "+getTrainTaker().leftBlock());
		//float rightBorder = mTopComp.leftBlock();
		for (Iterator<UITrain> it = mTopBuffer.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			float trainLeft = train.getPosition().x;
			float compLeft = getDeepPosition().x;
			float trainRight = trainLeft + train.getSize().width;
			float compRight = compLeft + SIDES_WIDTH;
			//log().debug(Arrays.toString(new float[] {trainLeftT, compLeftT, trainRightT, compRightT}));
			
			// If the train is now entirely gone from us.
			if (trainLeft >= compRight) {
				it.remove();
				continue;
			}
			// If the train is no longer controlled by us, but still 'on us'.
			if (trainRight > compRight) {
				for (UICarriage car : train.getCarriages()) {
					float carLeft = car.getPosition().x + trainLeft - compLeft;
					float carRight = carLeft + car.WIDTH;
					float tCenter = mUpPathIn.calculateT((carRight+carLeft)/2.f);
					tCenter = Math.min(tCenter, tBorder - car.WIDTH/2.f);
					if (carLeft < compRight) {
						float[] slope = mUpPathIn.evaluateSlope(tCenter);
						float[] pos = mUpPathIn.evaluate(tCenter);
						pos[0] += (compLeft - trainLeft)-car.WIDTH/2.f;
						pos[1] -= mTopComp.getSize().height/2.f;
						car.setRotation(1,0);
						car.setPosition(new Point(pos[0], pos[1]));
						car.setRotation(slope[0], slope[1]);
					}
					tBorder = tCenter - car.WIDTH/2.f;
				}
				continue;
			}
			
			train.vertCenterOn(mTopComp);
			for (UICarriage car : train.getCarriages()) {
				float carLeft = car.getPosition().x + trainLeft - compLeft;
				float carRight = carLeft + car.WIDTH;
				//if (carRight >= 0) {
					float tCenter = mUpPathIn.calculateT((carRight+carLeft)/2.f);
					tCenter = Math.min(tBorder - car.WIDTH/2.f, tCenter + UITrain.SPEED*delta);
					float[] slope = mUpPathIn.evaluateSlope(tCenter);
					float[] pos = mUpPathIn.evaluate(tCenter);
					pos[0] += (compLeft - trainLeft)-car.WIDTH/2.f;
					pos[1] -= mTopComp.getSize().height/2.f;
					car.setRotation(1,0);
					car.setPosition(new Point(pos[0], pos[1]));
					car.setRotation(slope[0], slope[1]);
					tBorder = tCenter - car.WIDTH/2.f;
				//}
			}
			
			UICarriage first = train.getCarriages().get(0);
			float newRight = trainLeft + first.getPosition().x + first.WIDTH;
			float newLeft = newRight - train.getSize().width;
			train.setPosition(new Point(newLeft, train.getPosition().y));
			for (UICarriage car : train.getCarriages()) {
				float[] rot = car.getRotation();
				car.setRotation(1, 0);
				car.setPosition(new Point(car.getPosition().x+trainLeft-newLeft, car.getPosition().y));
				car.setRotation(rot[0], rot[1]);
			}
			
			if (newRight > compRight) {
				mTopComp.takeTrain(train);
			}
			
			tBorder -= UITrain.PADDING;
		}
		
		
		/*if (!mTopBuffer.isEmpty() && mTopComp.leftBlock() >= mTopComp.getPosition().x) {
			
			
			log().debug("Giving waiting split train up to "+mTopComp);
			
			UITrain ttrain = mTopBuffer.poll();
			for (UICarriage car : ttrain.getCarriages())
				car.setRotation(0.5f, 0.5f);
			ttrain.getLayer().setVisible(true);
			mTopComp.takeTrain(ttrain);
		}*/
		
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
			float end = mTopComp.getDeepPosition().x + mTopComp.getSize().width;
			if (mNextTaker == this)
				return Math.max(getTrainTaker().leftBlock(), end);
			return end;
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
			float end = mTopComp.getDeepPosition().x + mTopComp.getSize().width;
			if (mNextTaker == this)
				return Math.max(getTrainTaker().leftBlock(), end);
			return end;
		}
	};
	private TrainTaker mNextTaker;
}
