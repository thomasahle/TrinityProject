package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;
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
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentButton;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory.UIToken;
import com.github.thomasahle.trainbox.trainbox.uimodel.UILevel;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIPallet;


/**
 * This should contain everything we need for a level:
 *  - The track
 *  - Components to add
 *  - The play button
 */
public class LevelScene implements Scene, LevelFinishedListener, Listener {
	TrainBox trainBox;
	final int HEIGHT = graphics().screenHeight();
	final int WIDTH = graphics().screenWidth();
	private Layer mBgLayer;
	private Layer mPlayButton;
	private UILevel mLevel;
	private UIPallet mPallet;
	int currPauseGoButtonImageIndex = 0;

	
	GroupLayer goalBarLayer;

	
	GroupLayer levelStatusLayer;
	ImageLayer levelFailedBlurbImageLayer;
	ImageLayer levelCompletedBlurbImageLayer;
	
	GroupLayer levelControlLayer;

	ImageLayer pauseButtonImageLayer;
	
	public LevelScene(TrainBox trainBox, Level l) {
		this.trainBox = trainBox;
		// A background image. This should be really nice.

		CanvasImage bgImage = graphics().createImage(WIDTH, HEIGHT);
		Image backgroundImage = assets().getImage("images/pngs/standardBackground.png");
		bgImage.canvas().drawImage(backgroundImage, 0, 0);
		mBgLayer = graphics().createImageLayer(bgImage);
		
		// Initialize the level we are going to try to solve
		mLevel = new UILevel(l);
		mLevel.setListener(this);
		
		
		// initalize the level controller buttons
		initLevelController();
		
		// Dragging of level
		mLevel.layer().addListener(this);
		
		
		// adding a goal bar
		goalBarLayer = graphics().createGroupLayer();
		goalBarLayer.setTranslation(10, HEIGHT*5/6);
		initGoalBar();
		initLevelStatus();
		
		Image pauseButtonImage = assets().getImage("images/pngs/pauseButton.png");	
		pauseButtonImageLayer = graphics().createImageLayer(pauseButtonImage);
		pauseButtonImageLayer.setTranslation(graphics().width()-146, graphics().height()-168);
		pauseButtonImageLayer.setVisible(false);

	}
	
	
	private void initLevelController() {
		Image goButtonImage = assets().getImage("images/pngs/goButton.png");
		mPlayButton = graphics().createImageLayer(goButtonImage);
		
		
		levelControlLayer = graphics().createGroupLayer();
		
		// Connect the play button to the track
		mPlayButton.addListener(new Listener() {

			@Override
			public void onPointerStart(Event event) {
				mLevel.paused(!mLevel.paused());
				currPauseGoButtonImageIndex=(1+currPauseGoButtonImageIndex)%2;
				updateGoPauseButtonImage();	
			}

			@Override
			public void onPointerEnd(Event event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPointerDrag(Event event) {
				// TODO Auto-generated method stub
				
			}});
		
		
		mPlayButton.setTranslation(graphics().width()-145, graphics().height()-150);
				
		
		Image changeLevelButtonImage = assets().getImage("images/pngs/changeLevelButton.png");
		ImageLayer changeLevelButtonImageLayer = graphics().createImageLayer(changeLevelButtonImage);
		changeLevelButtonImageLayer.setTranslation(graphics().width()*3/4, graphics().height()-150);
		changeLevelButtonImageLayer.addListener(new Listener() {

			@Override
			public void onPointerStart(Event event) {
				trainBox.setScene(trainBox.getLevelSelectScene());


				
			}

			@Override
			public void onPointerEnd(Event event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPointerDrag(Event event) {
				// TODO Auto-generated method stub
				
			}});
		
		
		// add the component pallet.
		mPallet = new UIPallet();
		
		UIComponentButton dupBut = new UIComponentButton(mLevel, UIToken.DUP);
		UIComponentButton boxBut = new UIComponentButton(mLevel, UIToken.BOX);
		UIComponentButton flipBut = new UIComponentButton(mLevel, UIToken.FLIP);
		UIComponentButton catBut = new UIComponentButton(mLevel, UIToken.CAT);
		UIComponentButton mergBut = new UIComponentButton(mLevel, UIToken.MERG);

		mPallet.add(dupBut);
		mPallet.add(boxBut);
		mPallet.add(flipBut);
		mPallet.add(catBut);
		mPallet.add(mergBut);
		
		mPallet.getLayer().setTranslation(20, graphics().height() - 180);
		
		levelControlLayer.add(mPallet.getLayer());
		levelControlLayer.add(mPlayButton);
		
		levelControlLayer.add(changeLevelButtonImageLayer);
	
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
		levelCompletedBlurbImageLayer.setVisible(false);
		levelStatusLayer.setVisible(false);
		
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



	
	
	
	@Override
	public void update(float delta) {
		mLevel.update(delta);
	}

	@Override
	public void onAttach() {
		graphics().rootLayer().add(mBgLayer);
		graphics().rootLayer().add(mLevel.layer());
		graphics().rootLayer().add(levelControlLayer);
		graphics().rootLayer().add(pauseButtonImageLayer);
		graphics().rootLayer().add(goalBarLayer);
		graphics().rootLayer().add(levelStatusLayer);
	}

	@Override
	public void onDetach() {
		// This helps us avoid a memory leak
		graphics().rootLayer().remove(mBgLayer);
		graphics().rootLayer().remove(mLevel.layer());
		graphics().rootLayer().remove(levelControlLayer);
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
