package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

/* Once we have Images for the cargo, maybe have this component 'drop' the
 * ground where the alien avatar for the level (planet) can be waiting, and
 * react as each piece of cargo hits the ground?
 */



public class UIEndComponent extends BlackBoxComponent {

	private final static int HEIGHT = 100;
	private int mWidth;
	
	private CanvasImage textImage;
	private Layer mBackLayer, mImageLayer, textLayer;
	private GroupLayer mFrontLayer;
	private String cargosDelivered = "";
	private int deliveredCount =0;
	

	
	public UIEndComponent(int width) {
		mWidth = width;
		
		mBackLayer = graphics().createGroupLayer();
		
		CanvasImage image = graphics().createImage(width, HEIGHT);
		image.canvas().setFillColor(0xff775577);
		image.canvas().fillCircle(width/2.f, HEIGHT/2.f, width/2.f);
		mImageLayer = graphics().createImageLayer(image);
		mFrontLayer = graphics().createGroupLayer();
		mFrontLayer.add(mImageLayer);
		
		textImage = graphics().createImage(width *10, HEIGHT); //TODO do something better than an arbitrary constant here.
		//When we have Images as cargo rather than integers we can use the width of those.
	    textLayer = graphics().createImageLayer(textImage);
	    textImage.canvas().setFillColor(0xff000000);
	    mFrontLayer.add(textLayer);
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
	public void onTrainEntered(UITrain train, Queue<UITrain> currentTrains) {
		List<UICarriage> carriages = train.getCarriages();
		// Unload the cargo from each carriage
		for(UICarriage c:carriages){
			int cargo = c.getCargo();
			deliveredCount ++;
			cargosDelivered = cargo+" | " + cargosDelivered;	
			log().debug("Cargo: "+cargo+" delivered sucessfully!");
		}
		//Display the cargos delivered
		textImage.canvas().clear();
		textImage.canvas().drawText(cargosDelivered, 10, HEIGHT/2);
		//Destroy the train.
		fireTrainDestroyed(train);
	}
}