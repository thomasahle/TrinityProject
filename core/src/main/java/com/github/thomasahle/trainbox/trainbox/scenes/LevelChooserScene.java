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
public class LevelChooserScene implements Scene, Keyboard.Listener, Pointer.Listener {
	TrainBox trainBox; 
    int width = graphics().width();
	int height = graphics().height();
	CanvasImage bgImage = graphics().createImage(graphics().width(),graphics().height());
    ImageLayer bgLayer;
    GroupLayer demoLayer;
    ImageLayer doneButtonImageLayer;
    
	public LevelChooserScene (final TrainBox trainBox) {
		this.trainBox = trainBox;
        bgLayer = graphics().createImageLayer(bgImage);
		Canvas canvas = bgImage.canvas();
        final Image backgroundImage = assets().getImage("images/pngs/startPage.png");
		canvas.drawImage(backgroundImage, 0, 0);
		
		// Create the demoLayer that contains the demo pages
        demoLayer = graphics().createGroupLayer();
        demoLayer.setTranslation(width/20+40, height/20);
      
        final Image doneButtonImage = assets().getImage("images/pngs/doneButton.png");
        doneButtonImageLayer = graphics().createImageLayer(doneButtonImage);
        demoLayer.add(doneButtonImageLayer);
        doneButtonImageLayer.setTranslation(680, 520);
        doneButtonImageLayer.setVisible(true);
        doneButtonImageLayer.addListener(new Mouse.Listener() {

			@Override
			public void onMouseDown(ButtonEvent event) {
				// set the correct level scene
				trainBox.setScene(trainBox.getLevelScene());

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
