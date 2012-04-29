package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
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
public class DemoScene implements Scene, Keyboard.Listener, Pointer.Listener {

	Image cloudImage = assets().getImage("images/cloud.png");
    ImageLayer cloudLayer = graphics().createImageLayer(cloudImage);
    int width = 640;
	int height = 480;
	CanvasImage bgImage = graphics().createImage(width, height);
    float x = 0.0f;
    float y = 0.0f;
    float vy = 0.0f;
    CanvasImage statsImage = graphics().createImage(640, 50);
    ImageLayer statsLayer = graphics().createImageLayer(statsImage);
    ImageLayer compMenu;
    boolean menuVisible = false;
    ImageLayer bgLayer;
    ImageLayer textLayer;
    
	public DemoScene() {
		CanvasImage bgImage = graphics().createImage(width, height);
		Canvas canvas = bgImage.canvas();
		canvas.setFillColor(0xff87ceeb);
		canvas.fillRect(0, 0, width, height);
		bgLayer = graphics().createImageLayer(bgImage);
		
	    cloudLayer.setTranslation(x, y);
	    
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
	
	@Override
	public void onAttach() {
		graphics().rootLayer().add(bgLayer);
		graphics().rootLayer().add(cloudLayer);
		graphics().rootLayer().add(textLayer);
	    graphics().rootLayer().add(statsLayer);
	    pointer().setListener(this);
	    keyboard().setListener(this);	
	}

	@Override
	public void onDetach() {
		graphics().rootLayer().remove(bgLayer);
		graphics().rootLayer().remove(cloudLayer);
		graphics().rootLayer().remove(textLayer);
		graphics().rootLayer().remove(statsLayer);
	}
	
	@Override
	public void update(float delta) {
		x+=0.08*delta;
		if(x>bgImage.width() + cloudImage.width()){
			x=-cloudImage.width();
		}
		y+=0.01*vy*delta;
		
		if(y>bgImage.height()+ cloudImage.height()){
			y=0;
		}
		if(y< -cloudImage.height()){
			y=bgImage.height()+cloudImage.height();
		}
		
		cloudLayer.setTranslation(x, y);
		statsImage.canvas().clear();
		statsImage.canvas().drawText("VEL: "+vy, 20, 30);
	}
	
	@Override
	public void onKeyDown(Event event) {
		System.out.println(event.key() + " Pressed");
		if(event.key() == Key.DOWN){
			vy += 1;
		}
		if(event.key() == Key.UP){
			vy -= 1;
		}
		if(event.key() == Key.C){
			menuVisible = !menuVisible;
			compMenu.setVisible(menuVisible);
		}
		
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		// TODO Auto-generated method stub
		
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
		cloudLayer.setTranslation(x-(cloudImage.width()/2), y-(cloudImage.height()/2));
	}

	@Override
	public void onPointerDrag(playn.core.Pointer.Event event) {
		// TODO Auto-generated method stub
		
	}

}
