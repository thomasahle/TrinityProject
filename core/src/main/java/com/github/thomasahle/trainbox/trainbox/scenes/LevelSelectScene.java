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

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Keyboard;
import playn.core.Keyboard.TypedEvent;
import playn.core.Pointer;
import playn.core.Pointer.Event;

public class LevelSelectScene implements Scene, Keyboard.Listener, Pointer.Listener{
	
	TrainBox trainBox;
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


	private void initializeLevelButtons() {
		List<Level> levels =  getLevels();
		float x = 90;
		float y = 200;
		int j =0;
	 	final Image levelButtonOk = assets().getImage("images/pngs/levelButton.png");
		final Image levelButtonNotOk = assets().getImage("images/pngs/inaccessibleLevelButton.png");

		for (int i=0; i< levels.size(); i++) {
			 final ImageLayer levelButtonImageLayer = graphics().createImageLayer();
			 if (levels.get(i).isAccessible())  {
				 levelButtonImageLayer.setImage(levelButtonOk);
				 
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


	public void startLevel(Level l){
		// Each level will have a button that when clicked will call this method with the level to be launched.
		UILevel level = new UILevel(l);
		trainBox.setScene(trainBox.getLevelScene());
	}
	
	@Override
	public void onPointerStart(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerEnd(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerDrag(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyDown(playn.core.Keyboard.Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyUp(playn.core.Keyboard.Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

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
	}

	
	/* LEVEL LIST */
	
	public List<Level> getLevels(){
		Level l1 = new Level(
				ComponentFactory.parseTrains("1-5-9-13 2-6-10-14 3-7-11-15 4-8-12-16"),
				ComponentFactory.parseTrains("1-2-3-4 5-6-7-8 9-10-11-12 13-14-15-16")
				);
		Level l2 = new Level(
				ComponentFactory.parseTrains("1-5-9-13 2-6-10-14 3-7-11-15 4-8-12-16"),
				ComponentFactory.parseTrains("1-2-3-4 5-6-7-8 9-10-11-12 13-14-15-16")
				);
		Level l3 = new Level(
				ComponentFactory.parseTrains("1-5-9-13 2-6-10-14 3-7-11-15 4-8-12-16"),
				ComponentFactory.parseTrains("1-2-3-4 5-6-7-8 9-10-11-12 13-14-15-16")
				);
		Level l4 = new Level(
				ComponentFactory.parseTrains("1-5-9-13 2-6-10-14 3-7-11-15 4-8-12-16"),
				ComponentFactory.parseTrains("1-2-3-4 5-6-7-8 9-10-11-12 13-14-15-16")
				);
		Level l5 = new Level(
				ComponentFactory.parseTrains("1-5-9-13 2-6-10-14 3-7-11-15 4-8-12-16"),
				ComponentFactory.parseTrains("1-2-3-4 5-6-7-8 9-10-11-12 13-14-15-16")
				);
		Level l6 = new Level(
				ComponentFactory.parseTrains("1-5-9-13 2-6-10-14 3-7-11-15 4-8-12-16"),
				ComponentFactory.parseTrains("1-2-3-4 5-6-7-8 9-10-11-12 13-14-15-16")
				);
		Level l7 = new Level(
				ComponentFactory.parseTrains("1-5-9-13 2-6-10-14 3-7-11-15 4-8-12-16"),
				ComponentFactory.parseTrains("1-2-3-4 5-6-7-8 9-10-11-12 13-14-15-16")
				);
		Level l8 = new Level(
				ComponentFactory.parseTrains("1-5-9-13 2-6-10-14 3-7-11-15 4-8-12-16"),
				ComponentFactory.parseTrains("1-2-3-4 5-6-7-8 9-10-11-12 13-14-15-16")
				);
		Level l9 = new Level(
				ComponentFactory.parseTrains("1-5-9-13 2-6-10-14 3-7-11-15 4-8-12-16"),
				ComponentFactory.parseTrains("1-2-3-4 5-6-7-8 9-10-11-12 13-14-15-16")
				);
		Level l10 = new Level(
				ComponentFactory.parseTrains("1-5-9-13 2-6-10-14 3-7-11-15 4-8-12-16"),
				ComponentFactory.parseTrains("1-2-3-4 5-6-7-8 9-10-11-12 13-14-15-16")
				);
		
		List<Level> levels = new ArrayList<Level>();
		levels.add(l1);
		levels.add(l2);
		levels.add(l3);
		levels.add(l4);
		levels.add(l5);
		levels.add(l6);
		levels.add(l7);
		levels.add(l8);
		levels.add(l9);
		levels.add(l10);
	return levels;
	}

}
