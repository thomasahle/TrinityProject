package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Layer;
import pythagoras.f.Point;
import pythagoras.i.Dimension;

import com.github.thomasahle.trainbox.trainbox.model.Carriage;
import com.github.thomasahle.trainbox.trainbox.model.NullTrain;
import com.github.thomasahle.trainbox.trainbox.model.Train;

public class UITrain {
	
	public final static float PADDING = 20.f;
	public final float defaultSpeed = 0.1f;
	public float speed = 0.1f; // pixels/s
	private List<UICarriage> mCarriages;
	private GroupLayer mLayer;
	private Point mPosition;
	private Dimension mSize;
	private float mRightCrop;
	private float mLeftCrop;
	
	private ImageLayer mDebugLayer;
	private final static boolean DEBUG = false;
	
	public UITrain(Train train) {
		this(fromTrain(train));
	}
	
	public void setSpeed(float newSpeed){
		this.speed = newSpeed;
	}
	public float getSpeed(){
		return speed;
	}
	
	public UITrain(int... cargos) {
		this(fromCargos(cargos));
	}
	
	public UITrain(List<UICarriage> carriages) {
		mPosition = new Point(0,0);
		mCarriages = carriages;
		mLayer = graphics().createGroupLayer();
		
		install(carriages);
	}

	// Copy constructor
	public UITrain(UITrain old) {
		mPosition = old.getPosition();
		mCarriages = new ArrayList<UICarriage>();
		for (UICarriage car : old.getCarriages())
			mCarriages.add(new UICarriage(car));
		mLayer = graphics().createGroupLayer();
		
		install(mCarriages);
		setSpeed(old.getSpeed());
		setCropLeft(old.getCropLeft());
		setCropRight(old.getCropRight());
	}

	private static List<UICarriage> fromCargos (int[] cargos) {
		List<UICarriage> carriages = new ArrayList<UICarriage>();
		for (int cargo : cargos)
			carriages.add(new UICarriage(cargo));
		return carriages;
	}
	
	private static List<UICarriage> fromTrain (Train train) {
		List<UICarriage> carriages = new ArrayList<UICarriage>();
		while (train.length() > 0) {
			carriages.add(new UICarriage(train.cargo()));
			train = train.tail();
		}
		return carriages;
	}
	
	
	private void install(List<UICarriage> carriages) {
		mCarriages = carriages;
		int width = 0;
		int height = 0;
		for (int i = carriages.size()-1; i >= 0; i--) {
			UICarriage car = carriages.get(i);
			car.setPosition(new Point(width, 0));
			width += car.getSize().width;
			height = Math.max(height, car.getSize().height);
			mLayer.add(car.getLayer());
		}
		mSize = new Dimension(width, height);
		
		if (DEBUG) {
			mDebugLayer = graphics().createImageLayer();
			mLayer.add(mDebugLayer);
			updateDebugLayer();
		}
	}
	
	private void updateDebugLayer() {
		if (DEBUG) {
			CanvasImage image = graphics().createImage(mSize.width, mSize.height);
			image.canvas().setStrokeColor(0xffff0000).strokeRect(0, 0, mSize.width-1, mSize.height-1);
			mDebugLayer.setImage(image);
		}
	}

	public UITrain cutOffHead() {
		UICarriage hcar = mCarriages.remove(0);
		mLayer.remove(hcar.getLayer());
		Dimension newSize = new Dimension((int)hcar.getPosition().x, mSize.height);
		mRightCrop -= mSize.width - newSize.width;
		mSize = newSize;
		updateDebugLayer();
		
		Point position = new Point(getPosition().x+hcar.getPosition().x,getPosition().y);
		UITrain head = new UITrain(Arrays.asList(hcar));
		head.setPosition(position);
		head.setSpeed(getSpeed());
		//head.setCropRight(width)
		//head.setCropLeft(width)
		return head;
	}
	
	public Point getPosition() {
		return mPosition;
	}
	int times = 0;
	public UITrain setPosition(Point position) {
		if (position.x < getPosition().x)
			times += 1;
		else times = 0;
		assert times < 5;
		assert position.x > 0 || position.x >= getPosition().x;
			
		getLayer().setTranslation(position.x, position.y);
		mPosition = position;
		return this;
	}
	public UITrain vertCenterOn(UIComponent comp) {
		float y = comp.getDeepPosition().y + comp.getSize().height/2 - getSize().height/2;
		setPosition(new Point(getPosition().x, y));
		return this;
	}
	public Dimension getSize() {
		return mSize;
	}
	public Layer getLayer() {
		return mLayer;
	}
	public List<UICarriage> getCarriages() {
		return Collections.unmodifiableList(mCarriages);
	}
	
	/**
	 * Returns a Train object you can use for `toString` and things.
	 * @return The `Train` representing this `UITrain`. 
	 */
	public Train train() {
		Train train = new NullTrain();
		for (int i = mCarriages.size()-1; i >= 0; i--)
			train = new Carriage(mCarriages.get(i).getCargo()).addLast(train);
		return train;
	}
	
	// TODO: The current cropping is a hack, which might not work well in all
	// situations. Better would be to use this approach:
	// https://groups.google.com/forum/?fromgroups#!topic/playn/9iEnc5HiceI
	/**
	 * How much we should crop off the right side.
	 */
	public void setCropRight(float width) {
		mRightCrop = width;
		applyCrop();
	}
	
	/**
	 * How much we should crop of the left side.
	 */
	public void setCropLeft(float width) {
		mLeftCrop = width;
		applyCrop();
	}
	
	private float getCropRight() {
		return mLeftCrop;
	}

	private float getCropLeft() {
		return mRightCrop;
	}
	
	private void applyCrop() {
		// Resets the carriages to visible.
		// This is (hopefully) done off the buffer, so no worries about glitches.
		for (UICarriage car : mCarriages) {
			car.getLayer().setVisible(true);
			car.getLayer().setAlpha(1);
		}
		// Crop from right
		float rightCrop = mRightCrop;
		for (UICarriage car : mCarriages) {
			if (rightCrop >= car.getSize().width) {
				car.getLayer().setVisible(false);
			} else if (rightCrop > 0) {
				car.getLayer().setAlpha(1-rightCrop/car.getSize().width);
			}
			rightCrop -= car.getSize().width;
		}
		// Crop from left
		float leftCrop = mLeftCrop;
		for (int i = mCarriages.size()-1; i >= 0; i--) {
			UICarriage car = mCarriages.get(i);
			if (leftCrop >= car.getSize().width) {
				car.getLayer().setVisible(false);
			} else if (leftCrop > 0) {
				car.getLayer().setAlpha(1-leftCrop/car.getSize().width);
			}
			leftCrop -= car.getSize().width;
		}
	}
}
