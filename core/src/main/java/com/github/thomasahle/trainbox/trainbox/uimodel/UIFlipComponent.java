package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import playn.core.CanvasImage;
import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class UIFlipComponent extends BlackBoxComponent {

	
	private final static int HEIGHT = 100;
	private int mWidth;
	
	private Layer mBackLayer, mFrontLayer;
	private UITrain unpairedTrain;	
	
	public UIFlipComponent(int width) {
		mWidth = width;
		
		mBackLayer = graphics().createImageLayer(graphics().createImage(1,1));
		
		CanvasImage image = graphics().createImage(width, HEIGHT);
		image.canvas().setFillColor(0xff0000aa);
		image.canvas().fillCircle(width/2.f, HEIGHT/2.f, width/2.f);
		mFrontLayer = graphics().createImageLayer(image);
	}
	
	public Dimension getSize(){
		return new Dimension(mWidth,HEIGHT);
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
	public void onTrainEntered(UITrain train, Queue<UITrain> currentTrains) {
		if(unpairedTrain != null){
			currentTrains.add(train);
			currentTrains.add(unpairedTrain);
			unpairedTrain = null;
		}else{
			unpairedTrain = train;
		}
	}

}
