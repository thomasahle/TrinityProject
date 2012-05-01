package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.log;
import static playn.core.PlayN.pointer;
import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.Pointer;

/**
 * It might be cleaner to keep the demo showing off new components and stuff in a seperate scene. 
 */
public class MoveScene implements Scene, Keyboard.Listener, Pointer.Listener {

	
    int width = 640;
	int height = 480;
	CanvasImage bgImage = graphics().createImage(width, height);
    float x = 0.0f;
    float y = 0.0f;
    float vy = 0.0f;
    
	
	ImageLayer trainLayer = createTrain();
	ImageLayer doubleWall = createComponent();
    
    CanvasImage statsImage = graphics().createImage(640, 50);
    ImageLayer statsLayer = graphics().createImageLayer(statsImage);
    ImageLayer compMenu;
    boolean menuVisible = false;
    ImageLayer bgLayer;
    ImageLayer textLayer;
    
	public MoveScene() {
		CanvasImage bgImage = graphics().createImage(width, height);
		Canvas canvas = bgImage.canvas();
		canvas.setFillColor(0xff87ceeb);
		canvas.fillRect(0, 0, width, height);
		bgLayer = graphics().createImageLayer(bgImage);

	    
	    CanvasImage textImage = graphics().createImage(640, 50);
	    textLayer = graphics().createImageLayer(textImage);
	    textImage.canvas().setFillColor(0xff000000);
	    textImage.canvas().drawText("Try clicking and using the UP and DOWN arrows, press C to toggle component menu.", 20, 30);
	    
	    
	    statsLayer.setTranslation(0, 50);

	    statsImage.canvas().setStrokeColor(0x000000);
	    statsImage.canvas().drawLine(0, statsImage.height(), statsImage.width(), statsImage.height());
	    textImage.canvas().drawLine(0, textImage.height(), textImage.width(), textImage.height());
	    
	    /*ComponentMenu cMenu = new ComponentMenu();
	    compMenu = cMenu.getLayer();
	    graphics().rootLayer().add(compMenu);
	    compMenu.setVisible(false);*/
	}
	
	
	public ImageLayer createTrain() {
		Image trainImage = assets().getImage("images/emptyTrain.png");
		CanvasImage cImage = graphics().createImage(trainImage.width()/2, trainImage.height()/2);
		Canvas c = cImage.canvas();
		c.drawImage(trainImage, 0, 0, trainImage.width()/2, trainImage.height()/2);
	    ImageLayer trainLayer = graphics().createImageLayer(cImage);
	    trainLayer.setTranslation(x, y);
		return trainLayer;
	}
	
	
	public ImageLayer createComponent() {
		CanvasImage textImage = graphics().createImage(10, 100);
	    textLayer = graphics().createImageLayer(textImage);
	    textImage.canvas().setFillColor(0xff000000);
	    textImage.canvas().fillRect(0, 0, width, height);
	    textImage.canvas().drawText("Fill me", 20, 30);
	    
	    	
	    textLayer.setTranslation(200, 200);
		return textLayer;
	}
	
	
	
	@Override
	public void onAttach() {
		graphics().rootLayer().add(bgLayer);
		graphics().rootLayer().add(trainLayer);
		graphics().rootLayer().add(doubleWall);
		graphics().rootLayer().add(textLayer);
	    graphics().rootLayer().add(statsLayer);
	    pointer().setListener(this);
	    keyboard().setListener(this);	
	}

	@Override
	public void onDetach() {
		graphics().rootLayer().remove(bgLayer);
		graphics().rootLayer().remove(trainLayer);
		graphics().rootLayer().remove(textLayer);
		graphics().rootLayer().remove(statsLayer);
	}
	
	@Override
	public void update(float delta) {
		//x+=0.08*delta;
		if(x>bgImage.width() + trainLayer.image().width()){
			x=-trainLayer.image().width();
		}
		y+=0.01*vy*delta;
		
		if(y>bgImage.height()+ trainLayer.image().height()){
			y=0;
		}
		if(y< -trainLayer.image().height()){
			y=bgImage.height()+trainLayer.image().height();
		}
		
		trainLayer.setTranslation(x, y);
		statsImage.canvas().clear();
		statsImage.canvas().drawText("VEL: "+vy, 20, 30);
	}
	
	// checks whether the new position is still within the box of the canvas
	public boolean checkGoodPosition(float x, float y) {
		if ((0 <=x & x< bgImage.width()) & ((0 <=y & y< bgImage.height()))) 
			return true;
		else
			return false;
	}
	
	
	@Override
	public void onKeyDown(Event event) {
		log().debug(event.key() + " Typed");
		if(event.key() == Key.DOWN){
			if (checkGoodPosition(x, y+trainLayer.image().height()+10))
				y= y+10;
		}
		if(event.key() == Key.UP){
			if (checkGoodPosition(x, y-10))
				y= y-10;
		}
		if(event.key() == Key.LEFT){
			if (checkGoodPosition(x-10, y))
				x = x-10;
		}
		if(event.key() == Key.RIGHT){
			if (checkGoodPosition(x+trainLayer.image().width()+10, y))
				x= x+10;
		}
		if(event.key() == Key.C){
			menuVisible = !menuVisible;
			compMenu.setVisible(menuVisible);
		}
		
	}

	@Override
	public void onKeyUp(Event event) {
	
	}

	@Override
	public void onPointerStart(playn.core.Pointer.Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerEnd(playn.core.Pointer.Event event) {
		x = event.x();
		y = event.y();
		trainLayer.setTranslation(x-(trainLayer.image().width()/2), y-(trainLayer.image().height()/2));
	}

	@Override
	public void onPointerDrag(playn.core.Pointer.Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		// TODO Auto-generated method stub
		
	}

}
