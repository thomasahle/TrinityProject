package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;

import java.awt.Point;


import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
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

    
    Image oxfordLogoImage = assets().getImage("images/CompSci_logo_portraitR_RGB.jpg");
    ImageLayer oxfordLogoImageLayer = graphics().createImageLayer(oxfordLogoImage);    
    
    Image cloudImage = assets().getImage("images/pngs/trains.png");
    ImageLayer cloudLayer = graphics().createImageLayer(cloudImage);
    int width = graphics().width();
	int height = graphics().height();
	CanvasImage bgImage = graphics().createImage(graphics().width(),graphics().height());
	//CanvasImage bgImage = graphics().createImage(graphics().width(),graphics().height());
    float a = 0.0f;
    float b = 0.0f;
    float vy = 0.00f;
    float vx = 0.08f;
    ImageLayer bgLayer;
    GroupLayer menuLayer;
    ImageLayer startButton; // contained in menulayer
    ImageLayer exitButton; // contained in menulayer
    ImageLayer watermelon;
    GroupLayer aboutLayer;
    boolean menuVisible;   // whether the menu is visible
    TrainBox trainBox;
    
    
	public StartScene(final TrainBox trainBox) {		
	    oxfordLogoImageLayer.setTranslation(width-oxfordLogoImageLayer.width()-10,height-oxfordLogoImageLayer.height()-38);

		this.trainBox = trainBox;
		menuVisible = true;
		Canvas canvas = bgImage.canvas();
		canvas.setFillColor(0xffe9b96e);
		canvas.fillRect(0, 0, width, height);
        final Image backgroundImage = assets().getImage("images/pngs/startPage.png");
		canvas.drawImage(backgroundImage, 0, 0);
		
        aboutLayer = graphics().createGroupLayer();
        aboutLayer.setTranslation(width/20+40, height/20);
        final Image aboutBlurbImage = assets().getImage("images/pngs/aboutPage.png");
        final ImageLayer aboutBlurbImageLayer = graphics().createImageLayer(aboutBlurbImage);
        aboutLayer.add(aboutBlurbImageLayer);
        aboutLayer.setVisible(false);
        
        final Image aboutBlurBackButtonImage = assets().getImage("images/pngs/backButton.png");
        final ImageLayer aboutBlurBackButtonImageLayer = graphics().createImageLayer(aboutBlurBackButtonImage);
        aboutLayer.add(aboutBlurBackButtonImageLayer);
        aboutBlurBackButtonImageLayer.setTranslation(680, 520);
        aboutBlurBackButtonImageLayer.addListener(new Pointer.Listener() {
			@Override
			public void onPointerStart(playn.core.Pointer.Event event) {
				aboutLayer.setVisible(false);
			}

			@Override
			public void onPointerEnd(playn.core.Pointer.Event event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPointerDrag(playn.core.Pointer.Event event) {
				// TODO Auto-generated method stub
				
			}
		});
        		
        menuLayer = graphics().createGroupLayer();
        menuLayer.setTranslation(width/5, height/4+40);
        final Image menuBackgoundImage = assets().getImage("images/pngs/menuBackground.png");
        final ImageLayer menuBackgoundImageLayer = graphics().createImageLayer(menuBackgoundImage);
        menuLayer.add(menuBackgoundImageLayer);
        
        final Image aboutButtonImage = assets().getImage("images/pngs/aboutButton.png");
        final ImageLayer aboutButtonImageLayer = graphics().createImageLayer(aboutButtonImage);
        menuLayer.add(aboutButtonImageLayer);
        aboutButtonImageLayer.setTranslation(205,240);
        aboutButtonImageLayer.addListener(new Pointer.Listener() {
			@Override
			public void onPointerStart(playn.core.Pointer.Event event) {
		        aboutLayer.setVisible(true);
			}

			@Override
			public void onPointerEnd(playn.core.Pointer.Event event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPointerDrag(playn.core.Pointer.Event event) {
				// TODO Auto-generated method stub
				
			}
		});
        		
        final Image demoButtonImage = assets().getImage("images/pngs/demoButton.png");
        final ImageLayer demoButtonImageLayer = graphics().createImageLayer(demoButtonImage);
        menuLayer.add(demoButtonImageLayer);
        demoButtonImageLayer.setTranslation(110,80);
        demoButtonImageLayer.addListener(new Pointer.Listener() {
        	
          Image demoButtonPressedImage = assets().getImage("images/pngs/demoButtonPressed.png");

			@Override
			public void onPointerStart(playn.core.Pointer.Event event) {
//		        demoButtonImageLayer.setImage(demoButtonPressedImage);
				trainBox.setScene(trainBox.getDemoScene());
			}

			@Override
			public void onPointerEnd(playn.core.Pointer.Event event) {
//		        demoButtonImageLayer.setImage(demoButtonImage);
			}

			@Override
			public void onPointerDrag(playn.core.Pointer.Event event) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        		        
        final Image playButtonImage = assets().getImage("images/pngs/playButton.png");
        final ImageLayer playButtonImageLayer = graphics().createImageLayer(playButtonImage);
        menuLayer.add(playButtonImageLayer);
        playButtonImageLayer.setTranslation(280,50);
        playButtonImageLayer.addListener(new Pointer.Listener() {
        	
        	Image playButtonPressedImage = assets().getImage("images/pngs/playButtonPressed.png");

			@Override
			public void onPointerStart(playn.core.Pointer.Event event) {
		        playButtonImageLayer.setImage(playButtonPressedImage);
				trainBox.setScene(trainBox.getLevelSelectScene());
			}

			@Override
			public void onPointerEnd(playn.core.Pointer.Event event) {
		        playButtonImageLayer.setImage(playButtonPressedImage);
			}

			@Override
			public void onPointerDrag(playn.core.Pointer.Event event) {
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
		graphics().rootLayer().setScale(1, 1);
		graphics().rootLayer().add(bgLayer);
		graphics().rootLayer().add(cloudLayer);
        graphics().rootLayer().add(menuLayer);
	    graphics().rootLayer().add(watermelon);
	    graphics().rootLayer().add(aboutLayer);
	    graphics().rootLayer().add(oxfordLogoImageLayer);
	    pointer().setListener(this);
	    keyboard().setListener(this);	
	}

	@Override
	public void onDetach() {
		graphics().rootLayer().remove(bgLayer);
		graphics().rootLayer().remove(cloudLayer);
        graphics().rootLayer().remove(menuLayer);
	    graphics().rootLayer().remove(watermelon);
	    graphics().rootLayer().remove(aboutLayer);
	    graphics().rootLayer().remove(oxfordLogoImageLayer);

	    pointer().setListener(null);
	    keyboard().setListener(null);	
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
		float x = event.x();
		float y = event.y();
		float h = watermelon.height()/2;
		float w = watermelon.width()/2;
		if(x>px-w && x< px+w && y>py-h && y<py+h){
			graphics().rootLayer().setScale(-1, 1);
			graphics().rootLayer().setTranslation(graphics().width(), 0);
		}
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
