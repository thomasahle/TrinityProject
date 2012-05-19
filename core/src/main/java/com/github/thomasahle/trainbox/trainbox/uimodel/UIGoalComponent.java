package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;
import static playn.core.PlayN.assets;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import pythagoras.f.Dimension;

import com.github.thomasahle.trainbox.trainbox.model.Train;

public class UIGoalComponent extends AbstractComponent implements TrainTaker,
		UIComponent {

	private static final int RED = 0xffff3300;
	private static final int GREEN = 0xff00ff33;
	private static final int DEFCOLOR = 0xff816647;
	private static final int LIGHT_GREEN = 0xff40B23d;
	private final static int HEIGHT = 100;
	private int mWidth;
	
	private UITrain mostRecentTrain;
	private Image finishChecker = assets().getImage("images/pngs/finishChecker.png");
	private ImageLayer mCheckerLayer, mTrackLayer;
	private GroupLayer mBackLayer;
	private GroupLayer mFrontLayer;
	private String deliveredCargoString = "";
	String cargoGoalString = "";
	private int deliveredCount = 0;
	List<Train> deliveredCargoList = new ArrayList<Train>();
	List<Train> cargoGoalList = new ArrayList<Train>();

	private LinkedList<UITrain> currentTrains = new LinkedList<UITrain>();
	private LevelFinishedListener mListener = new LevelFinishedListener.Null();

	public UIGoalComponent(List<Train> goal) {
		mWidth = 0;
		List<UITrain> trains = new ArrayList<UITrain>();
		for (Train t : goal) {
			UITrain train = new UITrain(t);
			mWidth += train.getSize().width + UITrain.PADDING;
			trains.add(0, train); // TO BE DISPLAYED FIRST TRAIN EXPECTED ON THE RIGHT!
			cargoGoalString = t.toString() + " " + cargoGoalString;
		}
		cargoGoalList = goal;
		mWidth += UITrain.PADDING;

		

		CanvasImage image = graphics().createImage(mWidth, HEIGHT);
		image.canvas().setFillColor(0x33000000);
		image.canvas().setFillPattern(finishChecker.toPattern());
		image.canvas().fillRect(0, 0, mWidth, HEIGHT);
		mCheckerLayer = graphics().createImageLayer(image);
		mCheckerLayer.setAlpha(0.1f);
		mTrackLayer = graphics().createImageLayer();
		mTrackLayer.setAlpha(0.9f);
		
		mFrontLayer = graphics().createGroupLayer();		
		mBackLayer = graphics().createGroupLayer();
		mBackLayer.add(mCheckerLayer);
		mBackLayer.add(mTrackLayer);
		updateTracks(DEFCOLOR);
		xpadding(ComponentHelper.RAIL_EXTRA);

		float x = UITrain.PADDING;
		for (UITrain train : trains) {
			train.setSpeed(0f);
			Layer l = train.getLayer();
			l.setAlpha(0.5f);
			// position expected trains.
			l.setTranslation(x, -train.getSize().height);
			mFrontLayer.add(l);
			x += train.getSize().width + UITrain.PADDING;
		}
	}

	private void updateTracks(int trackColor) {
		int imageWidth = mWidth
				+ (int) Math.ceil(2 * ComponentHelper.RAIL_EXTRA);
		CanvasImage image = graphics().createImage(imageWidth, HEIGHT);
		ComponentHelper.drawTracks(image.canvas(), mWidth, 0xff565248, trackColor);
		mTrackLayer.setImage(image);
	}

	public void setListener(LevelFinishedListener l) {
		mListener = l;
		if (l == null)
			mListener = new LevelFinishedListener.Null();
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
	 * only use if the deliveredCargo list is shorter or of equal length to the
	 * cargoGoalList
	 * 
	 * @return true on match, false on mismatch.
	 */
	public boolean checkDeliveredPrefix() {
		return cargoGoalString.endsWith(deliveredCargoString);
	}

	/**
	 * Checks if the level has been won. Should only be called after
	 * checkDelivered. Notifies all listeners on win.
	 */
	public void checkWin() {

	}

	@Override
	public void takeTrain(UITrain uitrain) {
		mostRecentTrain = uitrain;
		currentTrains.add(uitrain);
		Train train = uitrain.train();
		deliveredCargoList.add(train);
		deliveredCount++;
		deliveredCargoString = train + " " + deliveredCargoString;
		// log().debug("Cargo: "+cargo+" delivered sucessfully!");

	
	}

	@Override
	public List<UITrain> getTrains() {
		return currentTrains;
	}

	/*
	 * This isn't an exact science but it works for all levels so far,
	 * if you find a bug let me know,
	 * Matt
	 */
	public boolean trainsStoppedMoving(){
		int widthFull =0;
		for(UITrain t:currentTrains){
			widthFull += t.getSize().width + UITrain.PADDING;
		}
		widthFull -= UITrain.PADDING;
		float block = this.getPosition().x+mWidth-widthFull;
		//log().debug("MRT : " +mostRecentTrain.getPosition().x);
		//log().debug("block: "+block);
		return mostRecentTrain!= null && 
				(mostRecentTrain.getPosition().x > block); 
	}
	@Override
	public void update(float delta) {
		assert mListener != null;
		if (paused())
			return;
		
		moveTrains(currentTrains, delta);
		
		int trackColor = DEFCOLOR;
		
		if (deliveredCargoList.size() < cargoGoalList.size()) {
			if (checkDeliveredPrefix()) {
				trackColor = LIGHT_GREEN;
			}
			else {
				trackColor = RED;
				if (trainsStoppedMoving())
					mListener.levelFailed("The trains don't match :/");
			}
		}
		if (deliveredCargoList.size() == cargoGoalList.size()) {
			if (checkDeliveredPrefix()) {
				trackColor = GREEN;
				if (trainsStoppedMoving())
					mListener.levelCleared();
			}
			else {
				trackColor = RED;
				// the last train must be the mismatch.
				if (trainsStoppedMoving())
					mListener.levelFailed("Close... but not quite.");
			}
		}
		if (deliveredCargoList.size() > cargoGoalList.size()) {
			trackColor = RED;
			if (trainsStoppedMoving())
				mListener.levelFailed("You sent too many trains!");
		}
		
		updateTracks(trackColor);
	}

	@Override
	public float leftBlock() {
		// Channel leftBlock from previous component
		float res = getTrainTaker().leftBlock();
		// Don't allow trains to jump over us
		res = Math.min(res, getDeepPosition().x + getSize().width - 0.1f);
		// Don't overlap trains we currently manage
		if (!currentTrains.isEmpty())
			res = Math.min(res, currentTrains.getLast().getPosition().x
					- UITrain.PADDING);
		return res;
	}
}