package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;

import java.awt.Button;
import java.awt.Component;

import javax.swing.plaf.metal.MetalButtonUI;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Events;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.Mouse;
import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.MotionEvent;
import playn.core.Mouse.WheelEvent;
import playn.core.Pointer;
import playn.core.Pointer.Listener;
import playn.core.SurfaceLayer;

/**
 * It might be cleaner to keep the demo showing off new components and stuff in a seperate scene. 
 */
public class StartScene implements Scene, Keyboard.Listener, Pointer.Listener {
	
	//static final float GRAVITY = 64;
	static final float GRAVITY = 128;
	float px, py; // these are for the watermelon
	float x, y;
	float vxx, vyy;
	float axx, ayy;
	float currTrPosX;
	float currTrPosY;
	
	
	Image cloudImage = assets().getImage("images/pngs/trains.png");
    ImageLayer cloudLayer = graphics().createImageLayer(cloudImage);
    int width = graphics().width();
	int height = graphics().height();
	CanvasImage bgImage = graphics().createImage(graphics().width(),graphics().height());
    float a = 0.0f;
    float b = 0.0f;
    float vy = 0.0f;
    ImageLayer bgLayer;
    GroupLayer menuLayer;
    ImageLayer startButton; // contained in menulayer
    ImageLayer exitButton; // contained in menulayer
    ImageLayer watermelon;
    boolean menuVisible;   // whether the menu is visible
    
	public StartScene() {
		currTrPosX = 0;
		currTrPosY = 0;
		menuVisible = true;
		CanvasImage bgImage = graphics().createImage(graphics().width(),graphics().height());
		Canvas canvas = bgImage.canvas();
		canvas.setFillColor(0xff87ceeb);
		canvas.fillRect(0, 0, width, height);
		canvas.setFillColor(0xff6495ed);
        canvas.fillRect(width/10, height/10, width*8/10, height*8/10);
		canvas.setFillColor(0xff000000);
        canvas.drawText("Welcome to the SellCS Game!", width/3,height/3);

        menuLayer = graphics().createGroupLayer();
        menuLayer.setTranslation(width/3, height/3);
        menuLayer.setScale((float) 0.3);
        final Image startButtonOnImage = assets().getImage("images/pngs/start1Tr.png");
        final ImageLayer startButton = graphics().createImageLayer(startButtonOnImage);
        menuLayer.add(startButton);
        startButton.addListener(new Mouse.Listener() {
            Image startButtonOffImage = assets().getImage("images/pngs/start2Tr.png");
            
			@Override
			public void onMouseWheelScroll(WheelEvent event) {	
			}

			
			@Override
			public void onMouseUp(ButtonEvent event) {
				startButton.setImage(startButtonOnImage);
				
			}
			
			@Override
			public void onMouseMove(MotionEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMouseDown(ButtonEvent event) {
				startButton.setImage(startButtonOffImage);
				
			}
		});
        
        final Image exitButtonImage = assets().getImage("images/pngs/exit2Tr.png");
        final ImageLayer exitButton = graphics().createImageLayer(exitButtonImage);
        menuLayer.add(exitButton);
        exitButton.setTranslation(startButton.scaledWidth()+50, 0);
        exitButton.addListener(new Mouse.Listener() {            
			@Override
			public void onMouseWheelScroll(WheelEvent event) {	
			}

			
			@Override
			public void onMouseUp(ButtonEvent event) {
				
			}
			
			@Override
			public void onMouseMove(MotionEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMouseDown(ButtonEvent event) {
				exitButton.setRotation(50);

			}
		});
        
        
        bgLayer = graphics().createImageLayer(bgImage);
	    cloudLayer.setTranslation(a, b);
	    
	    Image watermelonImg = assets().getImage("images/pngs/watermelonTr.png");
	    watermelon = graphics().createImageLayer(watermelonImg);

    	x = a+3*cloudLayer.width()/4;
    	y = b+1*cloudLayer.height()/6;
	    ayy = GRAVITY;
	    vxx = -vy; 
	}
	
	@Override
	public void onAttach() {
		graphics().rootLayer().add(bgLayer);
		graphics().rootLayer().add(cloudLayer);
        graphics().rootLayer().add(menuLayer);
	    graphics().rootLayer().add(watermelon);
	    pointer().setListener(this);
	    keyboard().setListener(this);	
	}

	@Override
	public void onDetach() {
		graphics().rootLayer().remove(bgLayer);
		graphics().rootLayer().remove(cloudLayer);
        graphics().rootLayer().add(menuLayer);
	}
	
	@Override
	public void update(float delta) {
		menuLayer.setVisible(menuVisible);
		a+=0.08*delta;
		if(a>bgImage.width() + cloudImage.width()){
			a=-cloudImage.width();
		}
		b+=0.01*vy*delta;
		
		if(b>bgImage.height()+ cloudImage.height()){
			b=0;
		}
		if(b< -cloudImage.height()){
			b=bgImage.height()+cloudImage.height();
		}
		
		cloudLayer.setTranslation(a, b);
		System.out.println("(" + currTrPosX + "," + currTrPosY + ")");
		if (y >bgLayer.height()) {
			resetWaterMelonPosition();
		}
		
		else {
		    px = x;
		    py = y;
	
		    // Update physics.
		    delta /= 1000;
		    vxx += axx * delta;
		    vyy += ayy * delta;
		    x += vxx * delta;
		    y += vyy * delta;
		    
		 // Interpolate current position.
		    float x = (this.x * delta) + (px * (1f - delta));
		    float y = (this.y * delta) + (py * (1f - delta));
	
		    // Update the layer.
		    //watermelon.resetTransform();
		    watermelon.setTranslation(
		      x - watermelon.image().width() / 2,
		      y - watermelon.image().height() / 2);
		}
	}
	
	private void resetWaterMelonPosition() {
		if ((a > 0) & (b < bgLayer.height()))
	    	x = a+3*cloudLayer.width()/4;
	    	y = b+1*cloudLayer.height()/6;
	    	ayy = GRAVITY;
	    	vyy= 0;
	    	vxx = -vy; 
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
		a = event.x();
		b = event.y();
		cloudLayer.setTranslation(a-(cloudImage.width()/2), b-(cloudImage.height()/2));
	}

	@Override
	public void onPointerDrag(playn.core.Pointer.Event event) {
		// TODO Auto-generated method stub
		
	}

}
