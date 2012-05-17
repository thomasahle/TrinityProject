package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.pointer;

import java.util.ArrayList;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import playn.core.Pointer.Event;
import pythagoras.f.Point;

import com.github.thomasahle.trainbox.trainbox.core.TrainBox;
import com.github.thomasahle.trainbox.trainbox.model.Level;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIFlipComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIHorizontalComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIJoinComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UILevel;
import com.github.thomasahle.trainbox.trainbox.uimodel.UISeparateComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UISplitMergeComponent;

/**
 * It might be cleaner to keep the demo showing off new components and stuff in a seperate scene. 
 */
public class DemoScene implements Scene, Pointer.Listener {
    int width = graphics().width();
	int height = graphics().height();
	CanvasImage bgImage = graphics().createImage(graphics().width(),graphics().height());
    ImageLayer bgLayer;
    GroupLayer demoLayer;
    TrainBox trainBox;
    ArrayList<GroupLayer> demoPages;  // this array contains the pages that the demo contains, allows easy iteration
    int currentDemoIndex;
    private UILevel mLevelPage6;
    private UILevel mLevelPage7;
    private UILevel mLevelPage8;
    private UILevel mLevelPage9;



    
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
        final Image demoPage6Image = assets().getImage("images/pngs/demoPage6.png");
        final Image demoPage7Image = assets().getImage("images/pngs/demoPage7.png");
        final Image demoPage8Image = assets().getImage("images/pngs/demoPage8.png");
        final Image demoPage9Image = assets().getImage("images/pngs/demoPage9.png");

        
        
        
        

        
        final ImageLayer demoPage1ImageLayer = graphics().createImageLayer(demoPage1Image);
        final ImageLayer demoPage2ImageLayer = graphics().createImageLayer(demoPage2Image);
        final ImageLayer demoPage3ImageLayer = graphics().createImageLayer(demoPage3Image);
        final ImageLayer demoPage4ImageLayer = graphics().createImageLayer(demoPage4Image);
        final ImageLayer demoPage5ImageLayer = graphics().createImageLayer(demoPage5Image);
        final ImageLayer demoPage6ImageLayer = graphics().createImageLayer(demoPage6Image);
        final ImageLayer demoPage7ImageLayer = graphics().createImageLayer(demoPage7Image);
        final ImageLayer demoPage8ImageLayer = graphics().createImageLayer(demoPage8Image);
        final ImageLayer demoPage9ImageLayer = graphics().createImageLayer(demoPage9Image);

        GroupLayer demoPage1Layer = graphics().createGroupLayer();
        GroupLayer demoPage2Layer = graphics().createGroupLayer();
        GroupLayer demoPage3Layer = graphics().createGroupLayer();
        GroupLayer demoPage4Layer = graphics().createGroupLayer();
        GroupLayer demoPage5Layer = graphics().createGroupLayer();
        GroupLayer demoPage6Layer = graphics().createGroupLayer();
        GroupLayer demoPage7Layer = graphics().createGroupLayer();
        GroupLayer demoPage8Layer = graphics().createGroupLayer();
        GroupLayer demoPage9Layer = graphics().createGroupLayer();
        
        demoPage1Layer.add(demoPage1ImageLayer);
        demoPage2Layer.add(demoPage2ImageLayer);
        demoPage3Layer.add(demoPage3ImageLayer);
        demoPage4Layer.add(demoPage4ImageLayer);
        demoPage5Layer.add(demoPage5ImageLayer);
        demoPage6Layer.add(demoPage6ImageLayer);
        demoPage7Layer.add(demoPage7ImageLayer);
        demoPage8Layer.add(demoPage8ImageLayer);
        demoPage9Layer.add(demoPage9ImageLayer);

        demoPages = new ArrayList();
        demoPages.add(demoPage1Layer);
        demoPages.add(demoPage2Layer);
        demoPages.add(demoPage3Layer);
        demoPages.add(demoPage4Layer);
        demoPages.add(demoPage5Layer);
        demoPages.add(demoPage6Layer);
        demoPages.add(demoPage7Layer);
        demoPages.add(demoPage8Layer);
        demoPages.add(demoPage9Layer);



        for (int i =0; i<demoPages.size(); i++) {
        	demoLayer.add(demoPages.get(i));
        }
    
		initDemoLevels();

        
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
		backButtonImageLayer.addListener(new Pointer.Listener() {

			@Override
			public void onPointerStart(Pointer.Event event) {
				demoPages.get(currentDemoIndex).setVisible(false);
				demoPages.get(currentDemoIndex - 1).setVisible(true);
				nextButtonImageLayer.setVisible(true);
				doneButtonImageLayer.setVisible(false);
				if ((currentDemoIndex - 1) == 0)
					backButtonImageLayer.setVisible(false);
				currentDemoIndex = currentDemoIndex - 1;
			}

			@Override
			public void onPointerEnd(Event event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPointerDrag(Event event) {
				// TODO Auto-generated method stub

			}

		});
        
        final Image nextButtonImage = assets().getImage("images/pngs/nextButton.png");
        nextButtonImageLayer = graphics().createImageLayer(nextButtonImage);
        demoLayer.add(nextButtonImageLayer);
		nextButtonImageLayer.setTranslation(680, 520);
		nextButtonImageLayer.addListener(new Pointer.Adapter() {

			@Override
			public void onPointerStart(Pointer.Event event) {
				demoPages.get(currentDemoIndex).setVisible(false);
				demoPages.get(currentDemoIndex + 1).setVisible(true);
				backButtonImageLayer.setVisible(true);
				if ((currentDemoIndex + 1) == demoPages.size() - 1) {
					nextButtonImageLayer.setVisible(false);
					doneButtonImageLayer.setVisible(true);
				} else {
					doneButtonImageLayer.setVisible(false);
				}

				currentDemoIndex = currentDemoIndex + 1;

			}

			@Override
			public void onPointerEnd(Event event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPointerDrag(Event event) {
				// TODO Auto-generated method stub
				
			}
		});

		final Image doneButtonImage = assets().getImage("images/pngs/doneButton.png");
        doneButtonImageLayer = graphics().createImageLayer(doneButtonImage);
        demoLayer.add(doneButtonImageLayer);
		doneButtonImageLayer.setTranslation(680, 520);
		doneButtonImageLayer.setVisible(false);
		doneButtonImageLayer.addListener(new Pointer.Listener() {

			@Override
			public void onPointerStart(Pointer.Event event) {
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
			public void onPointerEnd(Event event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPointerDrag(Event event) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	private void initDemoLevels() {
		mLevelPage6 = new UILevel(new Level(
				0, "",
				"2 1", "2-1",
				0,0,0,0,0
			));
		//mLevelPage6.insertChildAt(new UIJoinComponent(), new Point(0,0));
		mLevelPage6.insertChildAt(new UIJoinComponent(), new Point(210,50));
		mLevelPage6.layer().setTranslation(80, 420);
		demoPages.get(5).add(mLevelPage6.layer());
		
		
		mLevelPage7 = new UILevel(new Level(
				0, "",
				"2 1", "2-1",
				0,0,0,0,0
			));
		mLevelPage7.insertChildAt(new UISeparateComponent(), new Point(210,50));
		mLevelPage7.layer().setTranslation(80, 420);
		demoPages.get(6).add(mLevelPage7.layer());
		
		
		mLevelPage8 = new UILevel(new Level(
				0, "",
				"2 1", "2-1",
				0,0,0,0,0
			));
		mLevelPage8.insertChildAt(new UIFlipComponent(), new Point(210,50));
		mLevelPage8.layer().setTranslation(80, 420);
		demoPages.get(7).add(mLevelPage8.layer());
		
		
		mLevelPage9 = new UILevel(new Level(
				0, "",
				"2 1", "2-1",
				0,0,0,0,0
			));
		mLevelPage9.insertChildAt(new UISplitMergeComponent(new UIHorizontalComponent(100), new UIHorizontalComponent(100)), new Point(210,50));
		mLevelPage9.layer().setTranslation(10, 350);
		demoPages.get(8).add(mLevelPage9.layer());
		
		mLevelPage6.paused(false);
		
	}

	@Override
	public void onAttach() {
		graphics().rootLayer().add(bgLayer);
	    graphics().rootLayer().add(demoLayer);
		//graphics().rootLayer().add();
	    pointer().setListener(this);
	}

	@Override
	public void onDetach() {
		graphics().rootLayer().remove(bgLayer);
	    graphics().rootLayer().remove(demoLayer);
		//graphics().rootLayer().remove(mLevel.layer());
	    pointer().setListener(null);



	}
	
	@Override
	public void update(float delta) {
		mLevelPage6.update(delta);
		mLevelPage7.update(delta);
		mLevelPage8.update(delta);
		mLevelPage9.update(delta);
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
	


}
