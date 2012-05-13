package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayList;
import java.util.Arrays;
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

	private final static int HEIGHT = 120;
	private int mWidth;
	
	private Layer mBackLayer, mImageLayer, textLayer;
	private GroupLayer mFrontLayer;
	private String deliveredCargoString = "";
	String cargoGoalString = "";
	private int deliveredCount =0;
	List<Integer> deliveredCargoList = new ArrayList<Integer>();
	List<Integer> cargoGoalList= new ArrayList<Integer>();
	List<LevelFinishedListener> listeners = new ArrayList<LevelFinishedListener>();
	

	
	public UIGoalComponent(List<Train> goal) {
		mWidth =0;
	    int padding = 5;
		List<UITrain> trains = new ArrayList<UITrain>();
		for(Train t:goal){
			UITrain uit = new UITrain(t);
			for(UICarriage c: uit.getCarriages()){
				UITrain train = new UITrain(Arrays.asList(c));
				mWidth+=train.getSize().width+padding;
				trains.add(0,train);
			}
		}
		mWidth+=padding;
		
		mBackLayer = graphics().createGroupLayer();
		
		CanvasImage image = graphics().createImage(mWidth, HEIGHT);
		image.canvas().setFillColor(0x33775577);
		image.canvas().fillCircle(mWidth/2.f, HEIGHT/2.f, mWidth/2.f);
		mImageLayer = graphics().createImageLayer(image);
		mFrontLayer = graphics().createGroupLayer();
		mFrontLayer.add(mImageLayer);
			  
	    int compCtr =0;
	    for(UITrain train: trains){
    		train.setSpeed(0f);
			Layer l = train.getLayer();
			l.setAlpha(0.4f);
			l.setTranslation(compCtr*train.getSize().width+padding, 0);
			mFrontLayer.add(l);
			List<UICarriage> cs = train.getCarriages();
			for(UICarriage c :cs){
				cargoGoalList.add(c.getCargo());
				cargoGoalString = (c.getCargo()+" | ") + cargoGoalString;
			}
			compCtr ++;
		}
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
		//Destroy the train.
		fireTrainDestroyed(train);
	}
}