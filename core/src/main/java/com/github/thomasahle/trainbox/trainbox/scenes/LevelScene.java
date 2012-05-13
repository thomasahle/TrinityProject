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
	final int HEIGHT = graphics().screenHeight();
	final int WIDTH = graphics().screenWidth();
	private Layer mBgLayer;
	private Layer mPlayButton;
	private UILevel mLevel;
	int currPauseGoButtonImageIndex = 0;
	Image goButtonImage = assets().getImage("images/pngs/goButton.png");
	Image pauseButtonImage = assets().getImage("images/pngs/pauseButton.png");	
	ImageLayer pauseButtonImageLayer = graphics().createImageLayer(pauseButtonImage);
	
	GroupLayer goalBarLayer;

	
	GroupLayer levelStatusLayer;
	ImageLayer levelFailedBlurbImageLayer;
	ImageLayer levelCompletedBlurbImageLayer;

	
	public LevelScene(TrainBox trainBox) {
		// A background image. This should be really nice.

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
		mPlayButton.setTranslation(graphics().screenWidth()-150, graphics().screenHeight()-150);
		pauseButtonImageLayer.setTranslation(graphics().screenWidth()-150, graphics().screenHeight()-168);
		
		// Dragging of level
		mLevel.layer().addListener(this);
		
		
		// adding a goal bar
		goalBarLayer = graphics().createGroupLayer();
		goalBarLayer.setTranslation(10, HEIGHT*5/6);
		initGoalBar();
		initLevelStatus();
	}
	
	private void initLevelStatus() {
		levelStatusLayer = graphics().createGroupLayer();
		levelStatusLayer.setTranslation(WIDTH/20+40, HEIGHT/20);

		Image levelFailedBlurbImage = assets().getImage("images/pngs/levelFailedBlurb.png");
		Image levelCompletedBlurb = assets().getImage("images/pngs/levelCompleteBlurb.png");
		levelFailedBlurbImageLayer = graphics().createImageLayer(levelFailedBlurbImage);
		levelCompletedBlurbImageLayer = graphics().createImageLayer(levelCompletedBlurb);
		levelStatusLayer.add(levelCompletedBlurbImageLayer);
		levelStatusLayer.add(levelFailedBlurbImageLayer);
		levelFailedBlurbImageLayer.setVisible(false);
		levelCompletedBlurbImageLayer.setVisible(true);
		
		//initialize the next button image layer
		Image nextButtonImage = assets().getImage("images/pngs/nextButton.png");
		ImageLayer nextButtonLeveLStatusImageLayer = graphics().createImageLayer(nextButtonImage);
		levelStatusLayer.add(nextButtonLeveLStatusImageLayer);
		nextButtonLeveLStatusImageLayer.setTranslation(680, 520);
		nextButtonLeveLStatusImageLayer.addListener(new Listener() {

			@Override
			public void onPointerStart(Event event) {
				levelFailedBlurbImageLayer.setVisible(false);
				levelCompletedBlurbImageLayer.setVisible(false);
				levelStatusLayer.setVisible(false);
			}
			

			@Override
			public void onPointerEnd(Event event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPointerDrag(Event event) {
				// TODO Auto-generated method stub
				
			}
			
		});

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
		graphics().rootLayer().add(levelStatusLayer);
	}

	@Override
	public void onDetach() {
		// This helps us avoid a memory leak
		graphics().rootLayer().remove(mBgLayer);
		graphics().rootLayer().remove(mLevel.layer());
		graphics().rootLayer().remove(mPlayButton);
		graphics().rootLayer().remove(pauseButtonImageLayer);
		graphics().rootLayer().remove(goalBarLayer);
		graphics().rootLayer().remove(levelStatusLayer);
	}

	@Override
	public void levelCleared() {
		log().debug("Level Cleared!");
		levelStatusLayer.setVisible(true);
		levelCompletedBlurbImageLayer.setVisible(true);
	}

	@Override
	public void levelFailed() {
		log().debug("Level Failed :(");
		levelStatusLayer.setVisible(true);
		levelFailedBlurbImageLayer.setVisible(true);
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
