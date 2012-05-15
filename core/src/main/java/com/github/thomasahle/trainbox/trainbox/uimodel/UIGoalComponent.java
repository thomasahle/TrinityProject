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

/* Once we have Images for the cargo, maybe have this component 'drop' the
 * ground where the alien avatar for the level (planet) can be waiting, and
 * react as each piece of cargo hits the ground?
 */

public class UIGoalComponent extends AbstractComponent implements TrainTaker,
		UIComponent {

	private final static int HEIGHT = 100;
	private int mWidth;

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
	private LevelFinishedListener mListener;

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
		updateTracks();
		xpadding(ComponentHelper.RAIL_EXTRA);

		int compCtr = 0;
		for (UITrain train : trains) {
			train.setSpeed(0f);
			Layer l = train.getLayer();
			l.setAlpha(0.4f);
			// position expected trains.
			l.setTranslation(compCtr * (train.getSize().width + UITrain.PADDING) + UITrain.PADDING, -train.getSize().height);
			mFrontLayer.add(l);
			compCtr++;
		}
	}

	private void updateTracks() {
		int imageWidth = mWidth
				+ (int) Math.ceil(2 * ComponentHelper.RAIL_EXTRA);
		CanvasImage image = graphics().createImage(imageWidth, HEIGHT);
		int c = 0xffffdd00;
		ComponentHelper.drawTracks(image.canvas(), mWidth, c, c);
		mTrackLayer.setImage(image);
	}

	public void setListener(LevelFinishedListener l) {
		mListener = l;
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
	public boolean checkDelivered() {
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
		currentTrains.add(uitrain);
		
		Train train = uitrain.train();
		deliveredCargoList.add(train);
		deliveredCount++;
		deliveredCargoString = train + " " + deliveredCargoString;
		// log().debug("Cargo: "+cargo+" delivered sucessfully!");

		// TODO Tidy up this code.
		if (deliveredCargoList.size() == cargoGoalList.size()) {
			if (checkDelivered()) {
				if (mListener != null) {
					mListener.levelCleared();
				}
			} else {
				if (mListener != null) {
					mListener.levelFailed("Bad prefix");
				}
			}
		} else if (deliveredCargoList.size() < cargoGoalList.size()) {
			log().debug("Goal: " + cargoGoalString);
			log().debug("Current: " + deliveredCargoString);
			if (!checkDelivered()) {
				if (mListener != null) {
					mListener.levelFailed("You sent in "+deliveredCargoString+", but we wanted "+cargoGoalString);
				}
			}
		} else {
			log().debug("Too many trains");
			// LEVEL FAILED
			if (mListener != null) {
				mListener.levelFailed("You sent in "+deliveredCargoList.size()+" trains but we wanted "+cargoGoalList.size());
			}
		}

		
		// Display the cargos delivered
		// Destroy the train.
	}

	@Override
	public List<UITrain> getTrains() {
		return currentTrains;
	}

	@Override
	public void update(float delta) {

		if (paused())
			return;

		moveTrains(currentTrains, delta);
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