package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.github.thomasahle.trainbox.trainbox.model.Train;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

/* Once we have Images for the cargo, maybe have this component 'drop' the
 * ground where the alien avatar for the level (planet) can be waiting, and
 * react as each piece of cargo hits the ground?
 */



public class UIGoalComponent extends BlackBoxComponent {

	private final static int HEIGHT = 100;
	private int mWidth;
	
	private CanvasImage textImage;
	private Layer mBackLayer, mImageLayer, textLayer;
	private GroupLayer mFrontLayer;
	private String deliveredCargoString = "";
	String cargoGoalString = "";
	private int deliveredCount =0;
	List<Integer> deliveredCargoList = new ArrayList<Integer>();
	List<Integer> cargoGoalList= new ArrayList<Integer>();
	List<LevelFinishedListener> listeners = new ArrayList<LevelFinishedListener>();
	

	
	public UIGoalComponent(int width, List<Train> goal) {
		mWidth = width;
		for(Train t: goal){
			UITrain train = new UITrain(t);
			List<UICarriage> cs = train.getCarriages();
			for(UICarriage c :cs){
				cargoGoalList.add(c.getCargo());
				cargoGoalString = (c.getCargo()+" | ") + cargoGoalString;
			}
		}
		
		mBackLayer = graphics().createGroupLayer();
		
		CanvasImage image = graphics().createImage(width, HEIGHT);
		image.canvas().setFillColor(0xff775577);
		image.canvas().fillCircle(width/2.f, HEIGHT/2.f, width/2.f);
		mImageLayer = graphics().createImageLayer(image);
		mFrontLayer = graphics().createGroupLayer();
		mFrontLayer.add(mImageLayer);
		
		textImage = graphics().createImage(width *10, HEIGHT); //TODO do something better than an arbitrary constant here.
		//When we have Images as cargo rather than integers we can use the width of those.
	    textLayer = graphics().createImageLayer(textImage);
	    textImage.canvas().setFillColor(0xff000000);
	    mFrontLayer.add(textLayer);
	    textImage.canvas().drawText(cargoGoalString, 10, HEIGHT/4);
	}

	public void addListener(LevelFinishedListener l){
		listeners.add(l);
	}
	

	@Override
	public Dimension getSize() {
		return new Dimension(mWidth, HEIGHT);
	}

	@Override
	public Layer getBackLayer() {
		return mBackLayer;
	}
	@Override
	public Layer getFrontLayer() {
		return mFrontLayer;
	}

/**
 * only use if the deliveredCargo list is shorter or of equal length to the cargoGoalList
 * @return true on match, false on mismatch.
 */
	public boolean checkDelivered(){
		boolean match = true;
		for(int i =0; i< deliveredCargoList.size(); i++){
			match = match && deliveredCargoList.get(i).equals(cargoGoalList.get(i));
		}
		return match;
	}
	
	@Override
	public void onTrainEntered(UITrain train, Queue<UITrain> currentTrains) {
		List<UICarriage> carriages = train.getCarriages();
		// Unload the cargo from each carriage
		for(UICarriage c:carriages){
			int cargo = c.getCargo();
			deliveredCargoList.add(cargo);
			deliveredCount ++;
			deliveredCargoString = cargo+" | " + deliveredCargoString;	
			//log().debug("Cargo: "+cargo+" delivered sucessfully!");
			
			boolean match = true;
			
			//TODO Tidy up this code.
			if(deliveredCargoList.size() == cargoGoalList.size()){
				if(checkDelivered()){
					for(LevelFinishedListener l: listeners){
						l.levelCleared();
					}
				}else{
					for(LevelFinishedListener l: listeners){
						l.levelFailed();
					}
				}
			}else if(deliveredCargoList.size()<cargoGoalList.size()){
				log().debug("Goal: "+cargoGoalString);
				log().debug("Current: "+deliveredCargoString);
				if(!checkDelivered()){
					for(LevelFinishedListener l: listeners){
						l.levelFailed();
					}
				}
				
			}else{
				//LEVEL FAILED
				for(LevelFinishedListener l: listeners){
					l.levelFailed();
				}
			}
			
		}
		//Display the cargos delivered
		textImage.canvas().clear();
		textImage.canvas().drawText(deliveredCargoString, 10, HEIGHT/2);
		textImage.canvas().drawText(cargoGoalString, 10, HEIGHT/4);
		//Destroy the train.
		fireTrainDestroyed(train);
	}
}