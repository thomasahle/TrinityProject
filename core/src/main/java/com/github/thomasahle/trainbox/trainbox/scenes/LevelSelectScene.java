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
    Image levelButtonImage;
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
        final ImageLayer demoPageImageLayer = graphics().createImageLayer(demoPageImage);
    	demoLayer.add(demoPageImageLayer);
    	initializeLevelButtons();


	}
	
	private void initializeLevelButtons() {
		List<Level> levels =  getLevels();
		int x = 0;
		int y = 0;
		Image levelButtonImage = assets().getImage("images/pngs/inaccessibleLevelButton.png");
		for(int i =0; i <levels.size(); i++) {
			if (levels.get(i).isAccessible()) {
				levelButtonImage = assets().getImage("images/pngs/levelButton.png");
			}
				
			
	        final ImageLayer levelButtonImageLayer = graphics().createImageLayer(levelButtonImage);
	        levelButtonImageLayer.setTranslation(x, y);
	    	demoLayer.add(levelButtonImageLayer);

	    	// initialize for the position of the next button
	        int newX=(x+levelButtonImage.width())%graphics().width();
	        if (newX < x) {
	        	y+=levelButtonImage.height();
	        }
	        x = newX;
	        
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
