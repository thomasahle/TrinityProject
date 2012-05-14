package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.mouse;
import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.log;
import static playn.core.PlayN.pointer;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.TypedEvent;
import playn.core.Layer;
import playn.core.Mouse;
import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.MotionEvent;
import playn.core.Mouse.WheelEvent;
import playn.core.Pointer;
import playn.core.Pointer.Event;
import pythagoras.f.Point;

import com.github.thomasahle.trainbox.trainbox.core.TrainBox;
import com.github.thomasahle.trainbox.trainbox.model.Level;
import com.github.thomasahle.trainbox.trainbox.uimodel.LevelFinishedListener;
import com.github.thomasahle.trainbox.trainbox.uimodel.ToolManager;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory.UIToken;
import com.github.thomasahle.trainbox.trainbox.uimodel.UILevel;


/**
 * This should contain everything we need for a level:
 *  - The track
 *  - Components to add
 *  - The play button
 */
public class LevelScene implements Scene, Mouse.Listener, Pointer.Listener, Keyboard.Listener {
	private static final float SPEED_INCREASE = 0.005f;
	private static final int MENU_HEIGHT = 200;
	private final static int HEIGHT = graphics().screenHeight();
	private final static int WIDTH = graphics().screenWidth();
	
	TrainBox trainBox;
	
	private Layer mBgLayer;
	private Layer mPlayButton;
	private UILevel mLevel;
	private UIPallet mPallet;
	private ToolManager toolMan;
	int currPauseGoButtonImageIndex = 0;

	GroupLayer levelStatusLayer;
	ImageLayer levelFailedBlurbImageLayer;
	ImageLayer levelCompletedBlurbImageLayer;
	ImageLayer retryButtonLeveLStatusImageLayer;
	ImageLayer nextButtonLeveLStatusImageLayer;
	GroupLayer levelControlLayer;
	ImageLayer pauseButtonImageLayer;
	GroupLayer levelPopupLayer;
	
	public LevelScene(TrainBox trainBox, Level level) {
		this.trainBox = trainBox;
		mLevel = new UILevel(level);
		
		// A background image. This should be really nice.
		CanvasImage bgImage = graphics().createImage(WIDTH, HEIGHT);
		Image backgroundImage = assets().getImage("images/pngs/standardBackground.png");
		bgImage.canvas().drawImage(backgroundImage, 0, 0);
		mBgLayer = graphics().createImageLayer(bgImage);
		
		// initialise the level controller buttons
		initToolsAndDragging();
		initLevelController();
		initLevelStatus();
		initLevelPopup();
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
		graphics().rootLayer().add(levelStatusLayer);
		graphics().rootLayer().add(levelPopupLayer);
		keyboard().setListener(this);
		mouse().setListener(this);
	}

	@Override
	public void onDetach() {
		// This helps us avoid a memory leak
		graphics().rootLayer().remove(mBgLayer);
		graphics().rootLayer().remove(mLevel.layer());
		graphics().rootLayer().remove(levelControlLayer);
		graphics().rootLayer().remove(pauseButtonImageLayer);
		graphics().rootLayer().remove(levelStatusLayer);
		graphics().rootLayer().remove(levelPopupLayer);
		keyboard().setListener(null);
		mouse().setListener(null);
	}
	
	private void setPaused(boolean paused) {
		mLevel.paused(paused);
		if (!paused) {
			currPauseGoButtonImageIndex = 1;
			pauseButtonImageLayer.setVisible(true);
		}
		else {
			currPauseGoButtonImageIndex = 0;
			pauseButtonImageLayer.setVisible(false);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	// UI Initialization
	///////////////////////////////////////////////////////////////////////////
	
	private void initLevelPopup() {
		levelPopupLayer = graphics().createGroupLayer();
		levelPopupLayer.setVisible(false);
		levelPopupLayer.setTranslation(WIDTH / 5, HEIGHT / 5);

		// set background
		final Image backgroundImage = assets().getImage("images/pngs/menuBackground.png");
		ImageLayer bgImageLayer = graphics().createImageLayer(backgroundImage);
		levelPopupLayer.add(bgImageLayer);

		// button to enter a new level
		Image changeLevelButtonImage = assets().getImage("images/pngs/changeLevelButton.png");
		ImageLayer changeLevelButtonImageLayer = graphics().createImageLayer(changeLevelButtonImage);
		levelPopupLayer.add(changeLevelButtonImageLayer);
		changeLevelButtonImageLayer.setTranslation(350, 100);
		
		changeLevelButtonImageLayer.addListener(new Pointer.Adapter() {
			@Override public void onPointerStart(Event event) {
				setPaused(!mLevel.paused());
				trainBox.setScene(trainBox.getLevelSelectScene());
			}
		});

		Image levelPopulBackButtonImage = assets().getImage("images/pngs/backButton.png");
		ImageLayer levelPopulBackButtonImageLayer = graphics().createImageLayer(levelPopulBackButtonImage);
		levelPopupLayer.add(levelPopulBackButtonImageLayer);
		levelPopulBackButtonImageLayer.setTranslation(370, 230);
		levelPopulBackButtonImageLayer.addListener(new Pointer.Adapter() {
			@Override public void onPointerStart(Event event) {
				levelPopupLayer.setVisible(false);
			}
		});

		Image levelPopulHomeButtonImage = assets().getImage("images/pngs/homeButton.png");
		ImageLayer levelPopulHomeButtonImageLayer = graphics().createImageLayer(levelPopulHomeButtonImage);
		levelPopupLayer.add(levelPopulHomeButtonImageLayer);
		levelPopulHomeButtonImageLayer.setTranslation(150, 250);
		levelPopulHomeButtonImageLayer.addListener(new Pointer.Adapter() {
			@Override public void onPointerStart(Event event) {
				trainBox.setScene(trainBox.getStartScene());
			}
		});

		final Image demoButtonImage = assets().getImage("images/pngs/demoButton.png");
		final ImageLayer demoButtonImageLayer = graphics().createImageLayer(demoButtonImage);
		levelPopupLayer.add(demoButtonImageLayer);
		demoButtonImageLayer.setTranslation(100, 40);
		levelPopulHomeButtonImageLayer.addListener(new Pointer.Adapter() {
			@Override public void onPointerStart(Event event) {
				trainBox.setScene(trainBox.getDemoScene());
			}
		});
	}

	private void initLevelController() {
		Image goButtonImage = assets().getImage("images/pngs/goButton.png");
		mPlayButton = graphics().createImageLayer(goButtonImage);
		
		levelControlLayer = graphics().createGroupLayer();
		
		// Connect the play button to the track
		mPlayButton.addListener(new Pointer.Adapter() {
			@Override public void onPointerStart(Event event) {
				setPaused(!mLevel.paused());
			}
		});
		
		mPlayButton.setTranslation(graphics().width()-130, graphics().height()-125);
				
		Image pauseButtonImage = assets().getImage("images/pngs/pauseButton.png");	
		pauseButtonImageLayer = graphics().createImageLayer(pauseButtonImage);
		pauseButtonImageLayer.setTranslation(graphics().width()-130, graphics().height()-143);
		pauseButtonImageLayer.setVisible(false);
		
		Image menuButtonImage = assets().getImage("images/pngs/menuButton.png");
		ImageLayer menuButtonImageImageLayer = graphics().createImageLayer(menuButtonImage);
		menuButtonImageImageLayer.setTranslation(graphics().width()*3/4, graphics().height()-125);
		menuButtonImageImageLayer.addListener(new Pointer.Adapter() {
			@Override public void onPointerStart(Event event) {
				setPaused(true);
				levelPopupLayer.setVisible(true);
			}
		});
		
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
		
		mPallet.getLayer().setTranslation(20, graphics().height() - 100 - 30);
		
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
		
		//initialise the next button image layer
		Image nextButtonImage = assets().getImage("images/pngs/nextButton.png");
		nextButtonLeveLStatusImageLayer = graphics().createImageLayer(nextButtonImage);
		levelStatusLayer.add(nextButtonLeveLStatusImageLayer);
		nextButtonLeveLStatusImageLayer.setTranslation(680, 520);
		nextButtonLeveLStatusImageLayer.addListener(new Pointer.Adapter() {
			@Override public void onPointerStart(Event event) {
				levelFailedBlurbImageLayer.setVisible(false);
				levelCompletedBlurbImageLayer.setVisible(false);
				levelStatusLayer.setVisible(false);
				trainBox.setLevel(mLevel.getLevel().levelNumber);
			}
		});
		
		//initialise the retry button image layer
		//TODO create an image called retryButton and replace the text below VVVV
			Image retryButtonImage = assets().getImage("images/pngs/nextButton.png");
			retryButtonLeveLStatusImageLayer = graphics().createImageLayer(retryButtonImage);
			levelStatusLayer.add(retryButtonLeveLStatusImageLayer);
			retryButtonLeveLStatusImageLayer.setTranslation(200, 520);
			retryButtonLeveLStatusImageLayer.addListener(new Pointer.Adapter() {
				@Override public void onPointerStart(Event event) {
					levelFailedBlurbImageLayer.setVisible(false);
					levelCompletedBlurbImageLayer.setVisible(false);
					levelStatusLayer.setVisible(false);
					trainBox.setLevel(mLevel.getLevel().levelNumber);
				}
			});
	
		mLevel.setListener(new LevelFinishedListener() {
			@Override public void levelCleared() {
				nextButtonLeveLStatusImageLayer.setVisible(true);
				log().debug("Level Cleared!");
				levelStatusLayer.setVisible(true);
				levelCompletedBlurbImageLayer.setVisible(true);
			}
			@Override public void levelFailed(String message) {
				nextButtonLeveLStatusImageLayer.setVisible(false);
				log().debug("Level Failed :(");
				levelStatusLayer.setVisible(true);
				levelFailedBlurbImageLayer.setVisible(true);
			}
		});
	}
	
	private void initToolsAndDragging() {
		toolMan = new ToolManager();
		// If the click has propagated to the root layer, we have unchecked our tool
		mBgLayer.addListener(new Pointer.Adapter() {
			@Override public void onPointerStart(Event event) {
				log().debug("Fell through");
				toolMan.unselect();
			}
		});
		// A listener on the level
		mLevel.layer().addListener((Pointer.Listener)this);
		setLevelTranslation(
				graphics().width()/2.f - mLevel.getSize().width/2.f,
				(graphics().height()-MENU_HEIGHT)/2.f - mLevel.getSize().height/2.f);
	}

	///////////////////////////////////////////////////////////////////////////
	// Input/Output
	///////////////////////////////////////////////////////////////////////////
	
	private boolean mIsDragging = false;
	private float mDragStartXPos;
	private float mDragStartYPos;
	@Override
	public void onPointerStart(Event event) {
		boolean didInsertSomething = false;
		if (toolMan.isSelected() && mLevel.paused()) {
			Point p = new Point(event.localX(), event.localY());
			didInsertSomething = mLevel.insertChildAt(UIComponentFactory.fromTok(toolMan.getCurrentTool()), p);
		}
		if (didInsertSomething) {
			mIsDragging = false;
			toolMan.unselect();
			setLevelTranslation(
					mLevel.layer().transform().tx(),
					mLevel.layer().transform().ty());
		}
		else {
			toolMan.unselect();
			mIsDragging = true;
			mDragStartXPos = event.localX();
			mDragStartYPos = event.localY();
		}
	}
	@Override
	public void onPointerDrag(Event event) {
		if (mIsDragging) {
			float x = event.x()-mDragStartXPos;
			float y = event.y()-mDragStartYPos;
			setLevelTranslation(x, y);
		}
	}
	@Override
	public void onPointerEnd(Event event) {
		mIsDragging = false;
	}

	private void setLevelTranslation(float x, float y) {
		
		float y0 = 0;
		float y1 = graphics().height() - MENU_HEIGHT;
		float x0 = 0;
		float x1 = graphics().width();
		
		if (mLevel.getSize().width < x1-x0) {
			x = Math.max(x, x0);
			x = Math.min(x, x1 - mLevel.getSize().width);
		}
		else {
			x = Math.max(x, x1 - mLevel.getSize().width);
			x = Math.min(x, x0);
		}
		if (mLevel.getSize().height < y1-y0) {
			y = y0 + (y1-y0)/2 - mLevel.getSize().height/2;
		}
		else {
			y = Math.max(y, y1 - mLevel.getSize().height);
			y = Math.min(y, y0);
		}
		
		mLevel.layer().setTranslation(x,y);
	}

	@Override
	public void onKeyDown(playn.core.Keyboard.Event event) {
		if(event.key() == Key.UP){
			mLevel.increaseTrainSpeed(SPEED_INCREASE);
			log().debug("INCREASING SPEED");
		}
		if(event.key() == Key.DOWN){
			mLevel.decreaseTrainSpeed(SPEED_INCREASE);
			log().debug("DECREASING SPEED");

		}
		
		if(event.key() == Key.ESCAPE){
			toolMan.unselect();
		}
	}
	@Override public void onKeyTyped(TypedEvent event) {}
	@Override public void onKeyUp(playn.core.Keyboard.Event event) {}

	@Override public void onMouseDown(ButtonEvent event) {
		// Unfortunately this doesn't quite work.
		// The pointer listener will still register a click. Whatever.
		if (event.button() != Mouse.BUTTON_LEFT) {
			toolMan.unselect();
			event.setPreventDefault(true);
		}
	}
	@Override public void onMouseUp(ButtonEvent event) {}
	@Override public void onMouseMove(MotionEvent event) {}
	@Override public void onMouseWheelScroll(WheelEvent event) {}
}
