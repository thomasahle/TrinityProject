package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;
import playn.core.CanvasImage;
import playn.core.Layer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;

import com.github.thomasahle.trainbox.trainbox.core.TrainBox;
import com.github.thomasahle.trainbox.trainbox.model.ComponentFactory;
import com.github.thomasahle.trainbox.trainbox.model.Level;
import com.github.thomasahle.trainbox.trainbox.uimodel.LevelFinishedListener;
import com.github.thomasahle.trainbox.trainbox.uimodel.UILevel;


/**
 * This should contain everything we need for a level:
 *  - The track
 *  - Components to add
 *  - The play button
 */
public class LevelScene implements Scene, LevelFinishedListener, Listener {
	
	private Layer mBgLayer;
	private Layer mPlayButton;
	private UILevel mLevel;
	TrainBox trainBox;
	


	public LevelScene(TrainBox trainBox) {
		this.trainBox = trainBox;
		// A background image. This should be really nice.
		//TODO set the graphics to stretch to the max available space.
		graphics().setSize(graphics().screenWidth(), graphics().screenHeight());
		final int HEIGHT = graphics().screenHeight();
		final int WIDTH = graphics().screenWidth();
		CanvasImage bgImage = graphics().createImage(WIDTH, HEIGHT);
		bgImage.canvas().setFillColor(0xffffffff).fillRect(0, 0, WIDTH, HEIGHT);
		mBgLayer = graphics().createImageLayer(bgImage);
		
		// Initialize the level we are going to try to solve
		mLevel = new UILevel(new Level(
				ComponentFactory.parseTrains("1-2-3 4-4"),
				ComponentFactory.parseTrains("1-2-3 4-4")));
		mLevel.setListener(this);
		
		// Connect the play button to the track
		mPlayButton = initPlayButton();
		mPlayButton.addListener(new Listener(){
			public void onPointerStart(Event event) {
				mLevel.paused(!mLevel.paused());
			}
			public void onPointerEnd(Event event) {}
			public void onPointerDrag(Event event) {}
		});
		mPlayButton.setTranslation(graphics().screenWidth()-100, graphics().screenHeight()-100);
		
		// Dragging of level
		mLevel.layer().addListener(this);
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
		mLevel.update(delta);
	}

	@Override
	public void onAttach() {
		graphics().rootLayer().add(mBgLayer);
		graphics().rootLayer().add(mLevel.layer());
		graphics().rootLayer().add(mPlayButton);
	}

	@Override
	public void onDetach() {
		// This helps us avoid a memory leak
		graphics().rootLayer().remove(mBgLayer);
		graphics().rootLayer().remove(mLevel.layer());
		graphics().rootLayer().remove(mPlayButton);
	}

	@Override
	public void levelCleared() {
		log().debug("Level Cleared!");
	}

	@Override
	public void levelFailed() {
		log().debug("Level Failed :(");
	}

	private float mDragStartXPos;
	@Override
	public void onPointerStart(Event event) {
		mDragStartXPos = event.localX();
	}
	@Override
	public void onPointerDrag(Event event) {
		mLevel.layer().setTranslation(event.x()-mDragStartXPos, 0);
	}
	@Override public void onPointerEnd(Event event) {}
}
