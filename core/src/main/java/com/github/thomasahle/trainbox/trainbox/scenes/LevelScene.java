package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;
import static playn.core.PlayN.pointer;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Mouse;
import playn.core.Keyboard.TypedEvent;
import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.MotionEvent;
import playn.core.Mouse.WheelEvent;
import playn.core.Layer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Dimension;

import com.github.thomasahle.trainbox.trainbox.core.TrainBox;
import com.github.thomasahle.trainbox.trainbox.model.ComponentFactory;
import com.github.thomasahle.trainbox.trainbox.model.Level;
import com.github.thomasahle.trainbox.trainbox.uimodel.LevelFinishedListener;
import com.github.thomasahle.trainbox.trainbox.uimodel.ToolManager;
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
public class LevelScene implements Scene, LevelFinishedListener, Listener, Keyboard.Listener {
	TrainBox trainBox;
	final int HEIGHT = graphics().screenHeight();
	final int WIDTH = graphics().screenWidth();
	private Layer mBgLayer;
	private Layer mPlayButton;
	private UILevel mLevel;
	private final int mLevelNumber;
	private UIPallet mPallet;
	int currPauseGoButtonImageIndex = 0;

	
	
	
	GroupLayer goalBarLayer;

	
	GroupLayer levelStatusLayer;
	ImageLayer levelFailedBlurbImageLayer;
	ImageLayer levelCompletedBlurbImageLayer;
	
	GroupLayer levelControlLayer;

	ImageLayer pauseButtonImageLayer;
	private ToolManager toolMan;
	
	GroupLayer levelPopupLayer;
	
	public LevelScene(TrainBox trainBox, Level l) {
		this.trainBox = trainBox;
		toolMan = new ToolManager();

		// A background image. This should be really nice.
		CanvasImage bgImage = graphics().createImage(WIDTH, HEIGHT);
		Image backgroundImage = assets().getImage("images/pngs/standardBackground.png");
		bgImage.canvas().drawImage(backgroundImage, 0, 0);
		mBgLayer = graphics().createImageLayer(bgImage);
		
		// Initialize the level we are going to try to solve
		mLevel = new UILevel(toolMan, l);
		toolMan.add(mLevel);
		mLevel.setListener(this);
		mLevelNumber=l.levelNumber;
		
		
		// initalize the level controller buttons
		initLevelController();
		
		// Dragging of level
		mLevel.layer().addListener(this);
		
		
		// adding a goal bar
		goalBarLayer = graphics().createGroupLayer();
		goalBarLayer.setTranslation(10, HEIGHT*2/3+20);
		initGoalBar();
		initLevelStatus();
		initLevelPopup();
		
		Image pauseButtonImage = assets().getImage("images/pngs/pauseButton.png");	
		pauseButtonImageLayer = graphics().createImageLayer(pauseButtonImage);
		pauseButtonImageLayer.setTranslation(graphics().width()-146, graphics().height()-168);
		pauseButtonImageLayer.setVisible(false);
		
		setView(0, 0);

	}
	
	
	private void initLevelPopup() {
		levelPopupLayer = graphics().createGroupLayer();
		levelPopupLayer.setVisible(false);
		levelPopupLayer.setTranslation(WIDTH/5, HEIGHT/5);

		
		// set background
        final Image backgroundImage = assets().getImage("images/pngs/menuBackground.png");
        ImageLayer bgImageLayer = graphics().createImageLayer(backgroundImage);
        levelPopupLayer.add(bgImageLayer);

		// button to enter a new level
		Image changeLevelButtonImage = assets().getImage("images/pngs/changeLevelButton.png");
		ImageLayer changeLevelButtonImageLayer = graphics().createImageLayer(changeLevelButtonImage);
		levelPopupLayer.add(changeLevelButtonImageLayer);
		changeLevelButtonImageLayer.setTranslation(350, 100);
		changeLevelButtonImageLayer.addListener(new Listener() {

			@Override
			public void onPointerStart(Event event) {
				mLevel.paused(!mLevel.paused());
				trainBox.setScene(trainBox.getLevelSelectScene());

			}

			@Override
			public void onPointerEnd(Event event) {
			}

			@Override
			public void onPointerDrag(Event event) {
			}});
		
		Image levelPopulBackButtonImage = assets().getImage("images/pngs/backButton.png");
		ImageLayer levelPopulBackButtonImageLayer = graphics().createImageLayer(levelPopulBackButtonImage);
		levelPopupLayer.add(levelPopulBackButtonImageLayer);
		levelPopulBackButtonImageLayer.setTranslation(370, 230);
		levelPopulBackButtonImageLayer.addListener(new Listener() {

			@Override
			public void onPointerStart(Event event) {
				levelPopupLayer.setVisible(false);
			}

			@Override
			public void onPointerEnd(Event event) {
			}

			@Override
			public void onPointerDrag(Event event) {
			}});
		
		
		
		Image levelPopulHomeButtonImage = assets().getImage("images/pngs/homeButton.png");
		ImageLayer levelPopulHomeButtonImageLayer = graphics().createImageLayer(levelPopulHomeButtonImage);
		levelPopupLayer.add(levelPopulHomeButtonImageLayer);
		levelPopulHomeButtonImageLayer.setTranslation(150, 250);;
		levelPopulHomeButtonImageLayer.addListener(new Listener() {

			@Override
			public void onPointerStart(Event event) {
				trainBox.setScene(trainBox.getStartScene());
			}

			@Override
			public void onPointerEnd(Event event) {
			}

			@Override
			public void onPointerDrag(Event event) {
			}});
		
		
		final Image demoButtonImage = assets().getImage("images/pngs/demoButton.png");
        final ImageLayer demoButtonImageLayer = graphics().createImageLayer(demoButtonImage);
        levelPopupLayer.add(demoButtonImageLayer);
        demoButtonImageLayer.setTranslation(100,40);
        demoButtonImageLayer.addListener(new Mouse.Listener() {
           Image demoButtonPressedImage = assets().getImage("images/pngs/demoButtonPressed.png");
            
			@Override
			public void onMouseWheelScroll(WheelEvent event) {	
			}

			
			@Override
			public void onMouseUp(ButtonEvent event) {
		        demoButtonImageLayer.setImage(demoButtonPressedImage);

				
			}
			
			@Override
			public void onMouseMove(MotionEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMouseDown(ButtonEvent event) {
		        demoButtonImageLayer.setImage(demoButtonPressedImage);
				trainBox.setScene(trainBox.getDemoScene());
			}
		});
		
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
			}

			@Override
			public void onPointerDrag(Event event) {
			}});
		
		
		mPlayButton.setTranslation(graphics().width()-145, graphics().height()-150);
				
		
		Image menuButtonImage = assets().getImage("images/pngs/menuButton.png");
		ImageLayer menuButtonImageImageLayer = graphics().createImageLayer(menuButtonImage);
		menuButtonImageImageLayer.setTranslation(graphics().width()*3/4, graphics().height()-150);
		menuButtonImageImageLayer.addListener(new Listener() {

			@Override
			public void onPointerStart(Event event) {
				mLevel.paused(true);
				levelPopupLayer.setVisible(true);

			}

			@Override
			public void onPointerEnd(Event event) {
			}

			@Override
			public void onPointerDrag(Event event) {
			}});
		
		
		
		// add the component pallet.
		mPallet = new UIPallet(this);
		
		UIComponentButton dupBut = new UIComponentButton(toolMan, UIToken.DUP);
		UIComponentButton boxBut = new UIComponentButton(toolMan, UIToken.BOX);
		UIComponentButton flipBut = new UIComponentButton(toolMan, UIToken.FLIP);
		UIComponentButton catBut = new UIComponentButton(toolMan, UIToken.CAT);
		UIComponentButton mergBut = new UIComponentButton(toolMan, UIToken.MERG);

		mPallet.add(dupBut);
		mPallet.add(boxBut);
		mPallet.add(flipBut);
		mPallet.add(catBut);
		mPallet.add(mergBut);
		
		mPallet.getLayer().setTranslation(20, graphics().height() - 180);
		
		levelControlLayer.add(mPallet.getLayer());
		levelControlLayer.add(mPlayButton);
		levelControlLayer.add(menuButtonImageImageLayer);
			
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
				trainBox.setLevel(mLevelNumber+1);
			}
			
			@Override
			public void onPointerEnd(Event event) {}

			@Override
			public void onPointerDrag(Event event) {}
			
		});

	}

	private void initGoalBar() {
		Image goalBarImage = assets().getImage("images/pngs/clickNDropBar.png");	
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
		graphics().rootLayer().add(levelPopupLayer);
		keyboard().setListener(this);
	  	pointer().setListener(this);
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
		graphics().rootLayer().remove(levelPopupLayer);
		keyboard().setListener(null);
	    pointer().setListener(null);
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
	private float mDragStartYPos;
	@Override
	public void onPointerStart(Event event) {
		mDragStartXPos = event.localX();
		mDragStartYPos = event.localY();

	}
	@Override
	public void onPointerDrag(Event event) {
		Dimension size = mLevel.getSize();
		float x = event.x()-mDragStartXPos;
		float y = event.y()-mDragStartYPos;
		
		setView(x, y);
	}


	private void setView(float x, float y) {
		float ybot = -mLevel.getSize().height + graphics().height() - 200;
		float ytop = 0;
		if (y > ytop) y = ytop;
		if (y < ybot) y = ybot;
		
		float xbot = -mLevel.getSize().width + graphics().width();
		float xtop = 0;
		if (x > xtop) x = xtop;
		if (x < xbot) x = xbot;
		
		mLevel.layer().setTranslation(x,y);
	}
	@Override public void onPointerEnd(Event event) {}


	@Override
	public void onKeyDown(playn.core.Keyboard.Event event) {
		if(event.key() == Key.UP){
			mLevel.increaseTrainSpeed(0.005f);
			log().debug("INCREASING SPEED");
		}
		if(event.key() == Key.DOWN){
			mLevel.decreaseTrainSpeed(0.005f);
			log().debug("DECREASING SPEED");

		}
		
		if(event.key() == Key.ESCAPE){
			toolMan.unselect();
		}
	}
	


	@Override
	public void onKeyTyped(TypedEvent event) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onKeyUp(playn.core.Keyboard.Event event) {
		// TODO Auto-generated method stub
		
	}
}
