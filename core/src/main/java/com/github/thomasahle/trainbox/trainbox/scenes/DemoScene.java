package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;

import java.awt.Button;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.util.ArrayList;

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

import com.github.thomasahle.trainbox.trainbox.core.TrainBox;

/**
 * It might be cleaner to keep the demo showing off new components and stuff in a seperate scene. 
 */
public class DemoScene implements Scene, Keyboard.Listener, Pointer.Listener {
    int width = graphics().width();
	int height = graphics().height();
	CanvasImage bgImage = graphics().createImage(graphics().width(),graphics().height());
    ImageLayer bgLayer;
    GroupLayer demoLayer;
    TrainBox trainBox;
    ArrayList<ImageLayer> demoPages;  // this array contains the pages that the demo contains, allows easy iteration
    int currentDemoIndex;
    
    ImageLayer nextButtonImageLayer;
    ImageLayer backButtonImageLayer;
    ImageLayer doneButtonImageLayer;
    
	public DemoScene(final TrainBox trainBox) {
		this.trainBox = trainBox;
        bgLayer = graphics().createImageLayer(bgImage);
		Canvas canvas = bgImage.canvas();
        final Image backgroundImage = assets().getImage("images/pngs/startPage.png");
		canvas.drawImage(backgroundImage, 0, 0);
		
		// Create the demoLayer that contains the demo pages
        demoLayer = graphics().createGroupLayer();
        demoLayer.setTranslation(width/20+40, height/20);
        final Image demoPage1Image = assets().getImage("images/pngs/demoPage1.png");
        final Image demoPage2Image = assets().getImage("images/pngs/demoPage2.png");
        final Image demoPage3Image = assets().getImage("images/pngs/demoPage3.png");
        final Image demoPage4Image = assets().getImage("images/pngs/demoPage4.png");
        final Image demoPage5Image = assets().getImage("images/pngs/demoPage5.png");

        final ImageLayer demoPage1ImageLayer = graphics().createImageLayer(demoPage1Image);
        final ImageLayer demoPage2ImageLayer = graphics().createImageLayer(demoPage2Image);
        final ImageLayer demoPage3ImageLayer = graphics().createImageLayer(demoPage3Image);
        final ImageLayer demoPage4ImageLayer = graphics().createImageLayer(demoPage4Image);
        final ImageLayer demoPage5ImageLayer = graphics().createImageLayer(demoPage5Image);

        
        demoPages = new ArrayList();
        demoPages.add(demoPage1ImageLayer);
        demoPages.add(demoPage2ImageLayer);
        demoPages.add(demoPage3ImageLayer);
        demoPages.add(demoPage4ImageLayer);
        demoPages.add(demoPage5ImageLayer);



        for (int i =0; i<demoPages.size(); i++) {
        	demoLayer.add(demoPages.get(i));
        }
    
        
        demoLayer.setVisible(true);
        currentDemoIndex = 0;
        for (int i =1; i<demoPages.size(); i++) {
        	demoPages.get(i).setVisible(false);
        }  
        
        
        final Image backButtonImage = assets().getImage("images/pngs/backButton.png");
        backButtonImageLayer = graphics().createImageLayer(backButtonImage);
        demoLayer.add(backButtonImageLayer);
        backButtonImageLayer.setTranslation(20, 520);
        backButtonImageLayer.setVisible(false);
        backButtonImageLayer.addListener(new Mouse.Listener() {

			@Override
			public void onMouseDown(ButtonEvent event) {
				demoPages.get(currentDemoIndex).setVisible(false);
				demoPages.get(currentDemoIndex-1).setVisible(true);
				nextButtonImageLayer.setVisible(true);
				doneButtonImageLayer.setVisible(false);
				if ((currentDemoIndex-1) == 0) 
					backButtonImageLayer.setVisible(false);
				currentDemoIndex= currentDemoIndex-1;
			}

			@Override
			public void onMouseUp(ButtonEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMouseMove(MotionEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMouseWheelScroll(WheelEvent event) {
				// TODO Auto-generated method stub
				
			}});
        
        
        final Image nextButtonImage = assets().getImage("images/pngs/nextButton.png");
        nextButtonImageLayer = graphics().createImageLayer(nextButtonImage);
        demoLayer.add(nextButtonImageLayer);
        nextButtonImageLayer.setTranslation(680, 520);
        nextButtonImageLayer.addListener(new Mouse.Listener() {

			@Override
			public void onMouseDown(ButtonEvent event) {
				demoPages.get(currentDemoIndex).setVisible(false);
				demoPages.get(currentDemoIndex+1).setVisible(true);
				backButtonImageLayer.setVisible(true);
				if ((currentDemoIndex+1) == demoPages.size()-1) {
					nextButtonImageLayer.setVisible(false);
					doneButtonImageLayer.setVisible(true);
				}
				else {
					doneButtonImageLayer.setVisible(false);
				}

				currentDemoIndex= currentDemoIndex+1;

			}

			@Override
			public void onMouseUp(ButtonEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMouseMove(MotionEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMouseWheelScroll(WheelEvent event) {
				// TODO Auto-generated method stub
				
			}});
        
        

        final Image doneButtonImage = assets().getImage("images/pngs/doneButton.png");
        doneButtonImageLayer = graphics().createImageLayer(doneButtonImage);
        demoLayer.add(doneButtonImageLayer);
        doneButtonImageLayer.setTranslation(680, 520);
        doneButtonImageLayer.setVisible(false);
        doneButtonImageLayer.addListener(new Mouse.Listener() {

			@Override
			public void onMouseDown(ButtonEvent event) {
				demoPages.get(currentDemoIndex).setVisible(false);
				// initialize for the next run of the demo
				demoPages.get(0).setVisible(true);
				currentDemoIndex = 0;
				doneButtonImageLayer.setVisible(false);
				backButtonImageLayer.setVisible(false);
				nextButtonImageLayer.setVisible(true);


				trainBox.setScene(trainBox.getStartScene());


			}

			@Override
			public void onMouseUp(ButtonEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMouseMove(MotionEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMouseWheelScroll(WheelEvent event) {
				// TODO Auto-generated method stub
				
			}});

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
	
	@Override
	public void update(float delta) {

	}

	@Override
	public void onPointerStart(playn.core.Pointer.Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerEnd(playn.core.Pointer.Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerDrag(playn.core.Pointer.Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyDown(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyUp(Event event) {
		// TODO Auto-generated method stub
		
	}
	
	


}
