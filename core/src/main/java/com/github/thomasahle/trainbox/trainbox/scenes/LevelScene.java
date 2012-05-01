package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.graphics;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Layer;

import com.github.thomasahle.trainbox.trainbox.uimodel.TrainsChangedListener;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIDupComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIHorizontalComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UITrain;


/**
 * This should contain everything we need for a level:
 *  - The track
 *  - Components to add
 *  - The play button
 */
public class LevelScene implements Scene {
	
	final static int WIDTH = 1000;
	final static int HEIGHT = 1000;
	
	private Layer mBgLayer;
	private UIComponent mTrack;
	private GroupLayer mTrainLayer;
	
	public LevelScene() {
		CanvasImage bgImage = graphics().createImage(WIDTH, HEIGHT);
		bgImage.canvas().setFillColor(0xff111111).fillRect(0, 0, WIDTH, HEIGHT);
		mBgLayer = graphics().createImageLayer(bgImage);
		
		UIHorizontalComponent track = new UIHorizontalComponent(100);
		track.add(new UIDupComponent(100));
			UIHorizontalComponent nested = new UIHorizontalComponent(100);
			nested.add(new UIDupComponent(100));
		track.add(nested);
		
		track.takeTrain(new UITrain(1));
		track.takeTrain(new UITrain(2));
		
		mTrainLayer = graphics().createGroupLayer();
		track.setTrainsChangedListener(new TrainsChangedListener() {
			public void onTrainCreated(UITrain train) {
				mTrainLayer.add(train.getLayer());
			}
			public void onTrainDestroyed(UITrain train) {
				mTrainLayer.remove(train.getLayer());
			}
		});
		
		mTrack = track;
	}
	
	@Override
	public void update(float delta) {
		mTrack.update(delta);
	}

	@Override
	public void onAttach() {
		graphics().rootLayer().add(mBgLayer);
		graphics().rootLayer().add(mTrack.getBackLayer());
		// FIXME: This add should really be in the constructor, but that hangs
		for (UITrain train : mTrack.getCarriages())
			mTrainLayer.add(train.getLayer());
		graphics().rootLayer().add(mTrainLayer);
		graphics().rootLayer().add(mTrack.getFrontLayer());
	}

	@Override
	public void onDetach() {
		graphics().rootLayer().remove(mBgLayer);
		graphics().rootLayer().remove(mTrack.getBackLayer());
		graphics().rootLayer().remove(mTrainLayer);
		graphics().rootLayer().remove(mTrack.getFrontLayer());
	}
}
