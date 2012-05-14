package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.ImageLayer;

import com.github.thomasahle.trainbox.trainbox.core.TrainBox;

public class LoadingScene implements Scene {

	private ImageLayer textLayer;

	public LoadingScene(TrainBox trainBox) {

	    CanvasImage textImage = graphics().createImage(640, 50);
	    textLayer = graphics().createImageLayer(textImage);
	    textImage.canvas().setFillColor(0x88888888);
	    textImage.canvas().drawText("Loading assets...", 20, 30);
	    textLayer.setScale(4);
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void onAttach() {
		graphics().rootLayer().add(textLayer);
	}

	@Override
	public void onDetach() {
		graphics().rootLayer().remove(textLayer);
	}

}
