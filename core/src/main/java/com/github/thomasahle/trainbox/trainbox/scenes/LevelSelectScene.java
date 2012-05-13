package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;

import java.util.ArrayList;
import java.util.List;

import com.github.thomasahle.trainbox.trainbox.core.TrainBox;
import com.github.thomasahle.trainbox.trainbox.model.ComponentFactory;
import com.github.thomasahle.trainbox.trainbox.model.Level;
import com.github.thomasahle.trainbox.trainbox.uimodel.UILevel;
import com.github.thomasahle.trainbox.trainbox.util.LevelTracker;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Keyboard;
import playn.core.Keyboard.TypedEvent;
import playn.core.PlayN;
import playn.core.Pointer;
import playn.core.Pointer.Event;

public class LevelSelectScene implements Scene, Keyboard.Listener, Pointer.Listener{
	
	private final TrainBox trainBox;
	int width = graphics().width();
	int height = graphics().height();
	CanvasImage bgImage = graphics().createImage(graphics().width(),graphics().height());
    ImageLayer bgLayer;
    GroupLayer demoLayer;
    final ImageLayer demoPageImageLayer;
/*    Image levelButtonImage;

	final Image LevelButtonOkImage = assets().getImage("images/pngs/inaccessibleLevelButton.png");
	final Image LevelButtonNotOkImage = assets().getImage("images/pngs/levelButton.png");

*/
	public LevelSelectScene(final TrainBox trainBox ){
		this.trainBox = trainBox;
		
		bgLayer = graphics().createImageLayer(bgImage);
		Canvas canvas = bgImage.canvas();
        final Image backgroundImage = assets().getImage("images/pngs/standardBackground.png");
		canvas.drawImage(backgroundImage, 0, 0);
		
		
		// Create the demoLayer that contains the level select pages
        demoLayer = graphics().createGroupLayer();
        demoLayer.setTranslation(width/20+40, height/20);
		
        final Image demoPageImage = assets().getImage("images/pngs/chooseLevelBlurb.png");
        demoPageImageLayer = graphics().createImageLayer(demoPageImage);
    	demoLayer.add(demoPageImageLayer);
    	initializeLevelButtons();
	}
/*	
	private void initializeLevelButtons() {
		List<Level> levels =  getLevels();
		x = 0;
		y = 50;
		newX =0;
		int j = 0;
		for(int i =0; i <levels.size(); i++) {
			final ImageLayer levelButtonImageLayer = graphics().createImageLayer();
			if (levels.get(i).isAccessible()) {
				levelButtonImageLayer.setImage(LevelButtonNotOkImage);
			}
			else {
				levelButtonImageLayer.setImage(LevelButtonOkImage);
			}
		    demoLayer.add(levelButtonImageLayer);
		    levelButtonImageLayer.setTranslation(x, y);
		    j+=1;
				
		    // initialize for the position of the next button
		    newX=((x+LevelButtonOkImage.width()+10)%demoPageImageLayer.width());
		    if (j ==6) {
		        y+=LevelButtonOkImage.height()*5/4;
		        j = 0;
		     }
		    x = (newX+LevelButtonOkImage.width()*1/4);
	    }   
	        
	        
		}*/

	private final List<ImageLayer> mButtons = new ArrayList<ImageLayer>();

	private void initializeLevelButtons() {
		int numberOfLevels = Level.levels.size();
		int currentProgress = LevelTracker.getCurrentProgress();
		mButtons.clear();
		
		float x = 90;
		float y = 200;
		int j =0;
	 	final Image levelButtonOk = assets().getImage("images/pngs/levelButton.png");
		final Image levelButtonNotOk = assets().getImage("images/pngs/inaccessibleLevelButton.png");

		for (int i=0; i<numberOfLevels; i++) {
			 final ImageLayer levelButtonImageLayer = graphics().createImageLayer();
			 mButtons.add(levelButtonImageLayer);
			 if (i<=currentProgress) {
				 final int level = i;
				 levelButtonImageLayer.setImage(levelButtonOk);
	/*			 levelButtonImageLayer.addListener(new Pointer.Listener() {
					
					@Override
					public void onPointerStart(Event event) {
						trainBox.setLevel(level);
					}
					
					@Override
					public void onPointerEnd(Event event) {}
					
					@Override
					public void onPointerDrag(Event event) {}
				});                                                               */
			 }
			 else {
				 levelButtonImageLayer.setImage(levelButtonNotOk);
			 }
		     demoLayer.add(levelButtonImageLayer);
		     levelButtonImageLayer.setTranslation(x, y);
		     j+=1;
		     x+= levelButtonImageLayer.width()+10;
		     if (j == 6) {
		    	 x = 90;
		    	 y+= levelButtonOk.height()+20;
		    	 j = 0;
		     }
		}
		
	}


	
	@Override
	public void onPointerStart(Event event) {
		
		
		
		int numberOfLevels = Level.levels.size();
		int currentProgress = LevelTracker.getCurrentProgress(); //The level you're currently doing
		
		//TODO - if user clicks on button for level i, call trainBox.setLevel(i)
		//TODO Delete the following code
		
		trainBox.setLevel(0);
		//PlayN.log().debug("Seting level 1 - (choosing other levels not implemented yet)");
	}

	@Override
	public void onPointerEnd(Event event) {
	}

	@Override
	public void onPointerDrag(Event event) {}

	@Override
	public void onKeyDown(playn.core.Keyboard.Event event) {}

	@Override
	public void onKeyTyped(TypedEvent event) {}

	@Override
	public void onKeyUp(playn.core.Keyboard.Event event) {}

	@Override
	public void update(float delta) {}

	@Override
	public void onAttach() {
		graphics().rootLayer().add(bgLayer);
	    graphics().rootLayer().add(demoLayer);
	    pointer().setListener(this);
	    keyboard().setListener(this);
	}

	@Override
	public void onDetach() {
		graphics().rootLayer().remove(bgLayer);
	    graphics().rootLayer().remove(demoLayer);
	    for(ImageLayer l:mButtons){
	    	//graphics().rootLayer().remove(l);
	    }
	    graphics().rootLayer().remove(bgLayer);
	    graphics().rootLayer().remove(demoLayer);
<<<<<<< HEAD
=======
	    graphics().rootLayer().remove(demoLayer);	
<<<<<<< HEAD
=======
>>>>>>> 4ee43ee1c3af9e7241d094a52c03d92e38ba141b
>>>>>>> add8809f754f5b63f8c416a1bfffc9d1faabc494
>>>>>>> 881d8d4423c5143acfa9964eb9e56ca0ac7ef39a
	    pointer().setListener(null);
	    keyboard().setListener(null);
	}

}
