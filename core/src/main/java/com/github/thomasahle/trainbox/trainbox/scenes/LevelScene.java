package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.pointer;
import static playn.core.PlayN.log;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Layer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Point;

import com.github.thomasahle.trainbox.trainbox.uimodel.TrainsChangedListener;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComposite;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIDupComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIHorizontalComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UITrain;


/**
 * This should contain everything we need for a level:
 *  - The track
 *  - Components to add
 *  - The play button
 */
public class LevelScene implements Scene, Listener {
	
	final static int WIDTH = 1000;
	final static int HEIGHT = 1000;
	
	private Layer mBgLayer;
	private UIComposite mTrack;
	private GroupLayer mTrainLayer;
	
	private Layer mPlayButton;

	public LevelScene() {
		// A background image. This should be really nice.
		CanvasImage bgImage = graphics().createImage(WIDTH, HEIGHT);
		bgImage.canvas().setFillColor(0xff111111).fillRect(0, 0, WIDTH, HEIGHT);
		mBgLayer = graphics().createImageLayer(bgImage);
		
		// Create a recursive track
		UIHorizontalComponent track = new UIHorizontalComponent(100);
		track.add(new UIDupComponent(100));
			UIHorizontalComponent nested = new UIHorizontalComponent(100);
			nested.add(new UIDupComponent(100));
		track.add(nested);
		
		// And add some trains. These will just have position 0,0 from start,
		// but the identity component will push them to the left until they fit.
		track.takeTrain(new UITrain(1));
		track.takeTrain(new UITrain(2));
		
		// Some components can create new trains. We want to know about this, since
		// the trains won't be visible unless we add them to our train layer.
		mTrainLayer = graphics().createGroupLayer();
		track.setTrainsChangedListener(new TrainsChangedListener() {
			public void onTrainCreated(UITrain train) {
				mTrainLayer.add(train.getLayer());
			}
			public void onTrainDestroyed(UITrain train) {
				mTrainLayer.remove(train.getLayer());
			}
		});
		
		// Connect the play button to the track
		mPlayButton = initPlayButton();
		mPlayButton.addListener(new Listener(){
			public void onPointerStart(Event event) {
				log().debug("Start clicked");
				// TODO: track.togglePause()
			}
			public void onPointerEnd(Event event) {}
			public void onPointerDrag(Event event) {}
		});
		
		mTrack = track;
	}
	
	private Layer initPlayButton() {
		CanvasImage img = graphics().createImage(100, 100);
		img.canvas().setFillColor(0xffffffff).fillRect(0, 0, 100, 100);
		playn.core.Path triangle = img.canvas().createPath();
		triangle.moveTo(0, 0);
		triangle.lineTo(87, 50);
		triangle.lineTo(0,100);
		triangle.lineTo(0,0);
		img.canvas().setFillColor(0xff339900).fillPath(triangle);
		return graphics().createImageLayer(img);
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
		
		pointer().setListener(this);
		
		graphics().rootLayer().add(mPlayButton);
		mPlayButton.setTranslation(graphics().screenWidth()-100, graphics().screenHeight()-100);
	}

	@Override
	public void onDetach() {
		graphics().rootLayer().remove(mBgLayer);
		graphics().rootLayer().remove(mTrack.getBackLayer());
		graphics().rootLayer().remove(mTrainLayer);
		graphics().rootLayer().remove(mTrack.getFrontLayer());
		
		pointer().setListener(null);
		
		graphics().rootLayer().remove(mPlayButton);
	}

	@Override
	public void onPointerStart(Event event) {
		// FIXME: Is this the right way to get the point, or do we need something localX?
		// Perhaps we should use track.backLayer().setListener or something?
		Point p = new Point(event.x(), event.y());
		if (event.y() < mTrack.getSize().height && event.x() < mTrack.getSize().width)
			mTrack.insertChildAt(new UIDupComponent(80), p);
	}
	@Override
	public void onPointerEnd(Event event) {}
	@Override
	public void onPointerDrag(Event event) {}
}
