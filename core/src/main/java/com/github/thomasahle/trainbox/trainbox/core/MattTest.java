package com.github.thomasahle.trainbox.trainbox.core;

import static playn.core.PlayN.*;

import playn.core.CanvasImage;
import playn.core.Game;
import playn.core.Canvas;
import playn.core.ImageLayer;


public class MattTest implements Game {

	@Override
	public void init() {
		int width = 640;
		int height = 480;
		CanvasImage bgImage = graphics().createImage(width, height);
		Canvas canvas = bgImage.canvas();
		canvas.setFillColor(0xff87ceeb);
		canvas.fillRect(0, 0, width, height);
		ImageLayer bgLayer = graphics().createImageLayer(bgImage);
		graphics().rootLayer().add(bgLayer);
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
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
