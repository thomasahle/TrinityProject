package com.github.thomasahle.trainbox.trainbox.core;

import static playn.core.PlayN.*;

import playn.core.CanvasImage;
import playn.core.Game;
import playn.core.Canvas;
import playn.core.Image;
import playn.core.ImageLayer;


public class TrainBox implements Game {
	
	Image cloudImage = assets().getImage("images/cloud.png");
    ImageLayer cloudLayer = graphics().createImageLayer(cloudImage);
    int width = 640;
	int height = 480;
	CanvasImage bgImage = graphics().createImage(width, height);
    float x = 0.0f;
    float y = 0.0f;
    
	@Override
	public void init() {
		
		CanvasImage bgImage = graphics().createImage(width, height);
		Canvas canvas = bgImage.canvas();
		canvas.setFillColor(0xff87ceeb);
		canvas.fillRect(0, 0, width, height);
		ImageLayer bgLayer = graphics().createImageLayer(bgImage);
		graphics().rootLayer().add(bgLayer);
		graphics().rootLayer().add(cloudLayer);
	    cloudLayer.setTranslation(x, y);
	    
	    CanvasImage textImage = graphics().createImage(500, 100);
	    textImage.canvas().setFillColor(0xff000000);
	    textImage.canvas().drawText("Thomas was here!", 20, 30);
	    ImageLayer textLayer = graphics().createImageLayer(textImage);
	    graphics().rootLayer().add(textLayer);
	
	}

	@Override
	public void update(float delta) {
		x+=0.08*delta;
		if(x>bgImage.width() + cloudImage.width()){
			x=-cloudImage.width();
		}
		cloudLayer.setTranslation(x, y);
	}

	@Override
	public void paint(float alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int updateRate() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
