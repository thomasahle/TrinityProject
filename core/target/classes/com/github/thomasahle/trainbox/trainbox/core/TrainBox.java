package com.github.thomasahle.trainbox.trainbox.core;

import static playn.core.PlayN.*;

import playn.core.CanvasImage;
import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;

public class TrainBox implements Game {
  @Override
  public void init() {
    // create and add background image layer
    Image bgImage = assets().getImage("images/bg.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    graphics().rootLayer().add(bgLayer);
    
    CanvasImage textImage = graphics().createImage(500, 100);
    textImage.canvas().setFillColor(0xff000000);
    textImage.canvas().drawText("Thomas was here!", 20, 30);
    ImageLayer textLayer = graphics().createImageLayer(textImage);
    graphics().rootLayer().add(textLayer);
  }

  @Override
  public void paint(float alpha) {
    // the background automatically paints itself, so no need to do anything here!
	
  }

  @Override
  public void update(float delta) {
  }

  @Override
  public int updateRate() {
    return 25;
  }
}
