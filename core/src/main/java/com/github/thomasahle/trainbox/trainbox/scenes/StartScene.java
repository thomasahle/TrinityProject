package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;

import playn.core.Canvas;
import playn.core.CanvasImage;
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
import com.github.thomasahle.trainbox.trainbox.core.TrainBox;

/**
 * It might be cleaner to keep the demo showing off new components and stuff in a seperate scene. 
 */
public class StartScene implements Scene, Keyboard.Listener, Pointer.Listener {
	
	static final float GRAVITY = 64;
	float px, py; // these are for the watermelon
	float x, y;
	float vyy;
	float ayy;
    float vxx = 80.0f;
    float axx = 0.00f;

	
	Image cloudImage = assets().getImage("images/pngs/trains.png");
    ImageLayer cloudLayer = graphics().createImageLayer(cloudImage);
    int width = graphics().width();
	int height = graphics().height();
	CanvasImage bgImage = graphics().createImage(graphics().width(),graphics().height());
    float a = 0.0f;
    float b = 0.0f;
    float vy = 0.00f;
    float vx = 0.08f;
    ImageLayer bgLayer;
    GroupLayer menuLayer;
    ImageLayer startButton; // contained in menulayer
    ImageLayer exitButton; // contained in menulayer
    ImageLayer watermelon;
    boolean menuVisible;   // whether the menu is visible
    TrainBox trainBox;
    
    
	public StartScene(final TrainBox trainBox) {
		this.trainBox = trainBox;
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
				trainBox.setScene(trainBox.getDemoScene());
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
				trainBox.setScene(trainBox.getLevelScene());
			}
			
			@Override
			public void onMouseMove(MotionEvent event) {
				exitButton.setRotation(-50);
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
        graphics().rootLayer().remove(menuLayer);
	    graphics().rootLayer().remove(watermelon);

	}
	
	@Override
	public void update(float delta) {
		//System.out.println("(" + x + "," + y + ")"+ vyy);
		menuLayer.setVisible(menuVisible);
		a+= vx*delta;
		//a+=0.08*delta;
		if(a>bgImage.width() + cloudImage.width()){
			a=-cloudImage.width();
		}
		b+= vy*delta;
		
		if(b>bgImage.height()+ cloudImage.height()){
			b=0;
		}
		if(b< -cloudImage.height()){
			b=bgImage.height()+cloudImage.height();
		}
		cloudLayer.setTranslation(a, b);
		

		if (((y >=bgLayer.height()-watermelon.height()*2) || (x>width-watermelon.width()) )) {
			resetWaterMelonPosition();
		}
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
		
/*		else {
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
		}*/
	}
	
	private void resetWaterMelonPosition() {
		if (y >=bgLayer.height()-watermelon.height()) {
			System.out.println("(" + x + "," + y + ")"+ vyy);
	    	vyy = -vyy; 
	    	vxx = vxx/2;
		}
		if (((x > bgLayer.width()) || (x < 0 )) & (0 < a) & (a < width-cloudLayer.width())) {
    		x = a+3*cloudLayer.width()/4;
    		y = b+1*cloudLayer.height()/6;
    		ayy = GRAVITY;
    		vyy= 0;
	    }
	 }

	
	@Override
	public void onKeyDown(Event event) {
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
