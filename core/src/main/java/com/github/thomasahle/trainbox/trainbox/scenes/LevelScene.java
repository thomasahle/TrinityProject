package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayList;

import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
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
	int currPauseGoButtonImageIndex = 0;
	Image goButtonImage = assets().getImage("images/pngs/goButton.png");
	Image pauseButtonImage = assets().getImage("images/pngs/pauseButton.png");	
	ImageLayer pauseButtonImageLayer = graphics().createImageLayer(pauseButtonImage);
	
	GroupLayer goalBarLayer;
	ImageLayer levelFailedBlurbLayer;
	ImageLayer levelCompletedBlurbLayer;


	
	public LevelScene(TrainBox trainBox) {
		// A background image. This should be really nice.
		final int HEIGHT = graphics().screenHeight();
		final int WIDTH = graphics().screenWidth();
		CanvasImage bgImage = graphics().createImage(WIDTH, HEIGHT);
		Image backgroundImage = assets().getImage("images/pngs/standardBackground.png");
		bgImage.canvas().drawImage(backgroundImage, 0, 0);
		mBgLayer = graphics().createImageLayer(bgImage);
		
		// Initialize the level we are going to try to solve
		mLevel = new UILevel(new Level(
				ComponentFactory.parseTrains("1-5-9-13 2-6-10-14 3-7-11-15 4-8-12-16"),
				ComponentFactory.parseTrains("1-2-3-4 5-6-7-8 9-10-11-12 13-14-15-16")));
		mLevel.setListener(this);
		
		// Connect the play button to the track
		mPlayButton = initPlayButton();
		mPlayButton.addListener(new Listener(){
			public void onPointerStart(Event event) {
				mLevel.paused(!mLevel.paused());
				currPauseGoButtonImageIndex=(1+currPauseGoButtonImageIndex)%2;
				updateGoPauseButtonImage();
			}
			public void onPointerEnd(Event event) {}
			public void onPointerDrag(Event event) {}
		});
		mPlayButton.setTranslation(graphics().width()-150, graphics().height()-150);
		pauseButtonImageLayer.setTranslation(graphics().width()-150, graphics().height()-168);
		
		// Dragging of level
		mLevel.layer().addListener(this);
		
		
		// adding a goal bar
		goalBarLayer = graphics().createGroupLayer();
		goalBarLayer.setTranslation(10, HEIGHT*5/6);
		initGoalBar();
	}
	
	private void initGoalBar() {
		Image goalBarImage = assets().getImage("images/pngs/goalBar.png");	
		ImageLayer goalBarImageLayer = graphics().createImageLayer(goalBarImage);
		goalBarLayer.add(goalBarImageLayer);
	}

	protected void updateGoPauseButtonImage() {
			pauseButtonImageLayer.setVisible(!(currPauseGoButtonImageIndex ==0));	
	}



	private Layer initPlayButton() {
		CanvasImage img = graphics().createImage(150, 150);
/*		CanvasImage img = graphics().createImage(150, 150);

		ImageLayer pauseButtonImageLayer = graphics().createImageLayer(goButtonImage);
		ArrayList<Image> goPauseButtonRotationList = new ArrayList<Image>();
		goPauseButtonRotationList.add(goButtonImage);
		goPauseButtonRotationList.add(pauseButtonImage);

		*/
		pauseButtonImageLayer.setVisible(false);
		return graphics().createImageLayer(goButtonImage);
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
		graphics().rootLayer().add(pauseButtonImageLayer);
		graphics().rootLayer().add(goalBarLayer);
	}

	@Override
	public void onDetach() {
		// This helps us avoid a memory leak
		graphics().rootLayer().remove(mBgLayer);
		graphics().rootLayer().remove(mLevel.layer());
		graphics().rootLayer().remove(mPlayButton);
		graphics().rootLayer().remove(pauseButtonImageLayer);
		graphics().rootLayer().remove(goalBarLayer);


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
