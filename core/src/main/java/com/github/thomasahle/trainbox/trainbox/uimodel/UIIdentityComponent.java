package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;

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

public class UIIdentityComponent implements UIComponent, TrainTaker {

	private final static int HEIGHT = 100;
	private int mWidth;
	
	private Layer mLayer;
	private Deque<UITrain> mTrains = new ArrayDeque<UITrain>();
	private TrainTaker mTrainTaker;
	private Point mPosition;
	
	@Override
	public void setTrainTaker(TrainTaker listener) {
		mTrainTaker = listener;
	}
	
	public UIIdentityComponent(int width) {
		mWidth = width;
		CanvasImage image = graphics().createImage(width, HEIGHT);
		image.canvas().setFillColor(0xaaaa0000);
		image.canvas().fillCircle(width/2.f, HEIGHT/2.f, width/2.f);
		System.out.println("X"+width+" "+HEIGHT);
		System.out.println("Ca");
		mLayer = graphics().createImageLayer(image);
		System.out.println("Cb");
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
	public Layer getLayer() {
		return mLayer;
	}

	@Override
	public void enterTrain(UITrain train) {
		mTrains.add(train);
	}

	@Override
	public void update(float delta) {
		System.out.println("Taker: "+mTrainTaker);
		float rightBorder = mTrainTaker.leftBlock();
		for (Iterator<UITrain> it = mTrains.descendingIterator(); it.hasNext(); ) {
			UITrain train = it.next();
			float trainLeft = train.getPosition().x;
			float compLeft = mLayer.originX();
			float trainRight = trainLeft + train.getSize().width;
			float compRight = compLeft + getSize().width;
			
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
			//System.out.println(rightBorder+" "+UITrain.SPEED*delta);
			float newRight = Math.min(rightBorder, trainRight + UITrain.SPEED*delta);
			float newLeft = newRight-train.getSize().width;
			train.getLayer().setTranslation(newLeft, train.getPosition().y);
			train.setPosition(new Point(newLeft, train.getPosition().y));
			//System.out.println("Was "+trainLeft+" "+trainRight+"; "+compLeft+" "+compRight);
			//System.out.println("Moving train "+train+" to "+newLeft);
			// If it is now out in the right side, give it away
			if (newRight > compRight) {
				mTrainTaker.takeTrain(train);
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
		return mTrains.peekLast().getLayer().originX() - UITrain.PADDING;
	}

	@Override
	public void setPosition(Point position) {
		mPosition = position;
	}

	@Override
	public Point getPosition() {
		return mPosition;
	}
	
}
