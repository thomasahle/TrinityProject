package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import playn.core.CanvasImage;
import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class UIIdentityComponent extends AbstractComponent implements UIComponent, TrainTaker {

	private final static int HEIGHT = 100;
	private int mWidth;
	
	private Layer mBackLayer, mFrontLayer;
	private Deque<UITrain> mTrains = new ArrayDeque<UITrain>();
	
	public UIIdentityComponent(int width) {
		mWidth = width;
		
		mFrontLayer = graphics().createImageLayer(graphics().createImage(1,1));
		
		CanvasImage image = graphics().createImage(width, HEIGHT);
		image.canvas().setFillColor(0xaaaa0000);
		image.canvas().fillCircle(width/2.f, HEIGHT/2.f, width/2.f);
		mBackLayer = graphics().createImageLayer(image);
	}

	@Override
	public List<UITrain> getCarriages() {
		return Collections.unmodifiableList(new ArrayList<UITrain>(mTrains));
	}

	@Override
	public Dimension getSize() {
		return new Dimension(mWidth, HEIGHT);
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
		
		float rightBorder = getTrainTaker().leftBlock();
		for (Iterator<UITrain> it = mTrains.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			float trainLeft = train.getPosition().x;
			float compLeft = getDeepPosition().x;
			float trainRight = trainLeft + train.getSize().width;
			float compRight = compLeft + getSize().width;
			
			// FIXME: There is a problem with trains that are given to us, but already have past by.
			// This can happen when a dup component (or another one) spits out a very long train.
			// It is not clear if giving a train with right side out of bounds should be allowed.
			
			// If the train is now entirely gone from us.
			if (trainLeft >= compRight) {
				it.remove();
				continue;
			}
			// If the train is no longer controlled by us, but still 'on us'.
			if (trainRight > compRight) {
				continue;
			}
			// See how far we can move it
			float newRight = Math.min(rightBorder, trainRight + UITrain.SPEED*delta);
			float newLeft = newRight-train.getSize().width;
			train.setPosition(new Point(newLeft, train.getPosition().y));
			// If it is now out in the right side, give it away
			if (newRight > compRight) {
				log().debug("Giving a train to "+getTrainTaker());
				getTrainTaker().takeTrain(train);
			}
			// Update our working right border
			assert rightBorder >= newLeft - UITrain.PADDING;
			rightBorder = newLeft - UITrain.PADDING;
		}
	}

	@Override
	public void takeTrain(UITrain train) {
		mTrains.add(train);
	}

	@Override
	public float leftBlock() {
		if (mTrains.isEmpty())
			return Integer.MAX_VALUE;
		return mTrains.peekLast().getPosition().x - UITrain.PADDING;
	}
}
