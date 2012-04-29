package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;

import pythagoras.f.Dimension;
import java.util.Collections;
import java.util.List;

import playn.core.CanvasImage;
import playn.core.ImageLayer;
import playn.core.Layer;

import com.github.thomasahle.trainbox.trainbox.model.DupComponent;

public class UIDupComponent implements UIComponent {
	private List<UITrain> mTrains;
	private DupComponent model;
	private UIComponent mNext;
	
	private final static int WIDTH = 100;
	private final static int HEIGHT = 100;
	
	public UIDupComponent (UIComponent next) {
		mNext = next;
	}
	
	@Override
	public List<UITrain> getCarriages() {
		return Collections.unmodifiableList(mTrains);
	}

	@Override
	public pythagoras.f.Dimension getSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	@Override
	public Layer getLayer() {
		// TODO: So how do we decide if the component layer goes over or under the trains?
		//		 I guess we could have two getLayer methods, one for each case.
		CanvasImage image = graphics().createImage(WIDTH, HEIGHT);
		image.canvas().setFillColor(0xff000000);
		image.canvas().fillRect(0, 0, WIDTH, HEIGHT);
		ImageLayer layer = graphics().createImageLayer(image);
		return layer;
	}
	
	@Override
	public void enterTrain(UITrain train) {
		mTrains.add(train);
		
		// FIXME: This might fail.
		// Instead this call should be in the update method, so it can be postponed
		// till the model is ready.
		// model.enter(train.getTrain());  
	}
	
	@Override
	public void update(float delta) {
		// TODO: This is a simple update method, that just make the trains continue
		//		 on the x-axis path towards infinity.
		//		 Clearly this is not what we actually want from this component.
		for (UITrain train : mTrains) {
			Layer layer = train.getLayer();
			float x = layer.transform().tx();
			float y = layer.transform().tx();
			float newx = x + (delta-train.getLastUpdate())*train.SPEED;
			layer.setTranslation(newx, y);
			train.setLastUpdate(delta);
		}
		// TODO: Next up is to decide if the train has moved far enough to be duplicated.
		//		 We also need to talk to the model about this.
		
		//		 When a train is ready we do
		// 		 mNext.enterTrain(train)
		//		 mNext.enterTrain(train)
		//		 and delete it from our own list.
	}

	@Override
	public void setTrainTaker(TrainTaker listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void takeTrain(UITrain train) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float leftBlock() {
		// TODO Auto-generated method stub
		return 0;
	}
}
