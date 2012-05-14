package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import playn.core.CanvasImage;
import playn.core.GroupLayer;
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

	private Layer mImageLayer;
	private ImageLayer mBackLayer;
	private GroupLayer mFrontLayer;
	private String deliveredCargoString = "";
	String cargoGoalString = "";
	private int deliveredCount = 0;
	List<Integer> deliveredCargoList = new ArrayList<Integer>();
	List<Integer> cargoGoalList = new ArrayList<Integer>();

	private LinkedList<UITrain> currentTrains = new LinkedList<UITrain>();
	private LevelFinishedListener mListener;

	public UIGoalComponent(List<Train> goal) {
		mWidth = 0;
		int padding = 5;
		List<UITrain> trains = new ArrayList<UITrain>();
		for (Train t : goal) {
			UITrain uit = new UITrain(t);
			for (UICarriage c : uit.getCarriages()) {
				UITrain train = new UITrain(Arrays.asList(c));
				mWidth += train.getSize().width + padding;
				trains.add(0, train); // TO BE DISPLAYED FIRST TRAIN EXPECTED ON
										// THE RIGHT!
				cargoGoalList.add(c.getCargo()); // FIRST ELEMENT EXPECTED FIRST
				cargoGoalString = (c.getCargo() + " | ") + cargoGoalString;
			}
		}
		mWidth += padding;

		mBackLayer = graphics().createImageLayer();
		updateTracks();
		xpadding(ComponentHelper.RAIL_EXTRA);

		CanvasImage image = graphics().createImage(mWidth, HEIGHT);
		image.canvas().setFillColor(0x33775577);
		image.canvas().fillCircle(mWidth / 2.f, HEIGHT / 2.f, mWidth / 2.f);
		mImageLayer = graphics().createImageLayer(image);
		mFrontLayer = graphics().createGroupLayer();
		mFrontLayer.add(mImageLayer);

		int compCtr = 0;
		for (UITrain train : trains) {
			train.setSpeed(0f);
			Layer l = train.getLayer();
			l.setAlpha(0.4f);
			// position expected trains.
			l.setTranslation(compCtr * train.getSize().width + padding,
					-train.getSize().height);
			mFrontLayer.add(l);
			compCtr++;
		}
	}

	private void updateTracks() {
		int imageWidth = mWidth
				+ (int) Math.ceil(2 * ComponentHelper.RAIL_EXTRA);
		CanvasImage image = graphics().createImage(imageWidth, HEIGHT);
		ComponentHelper.drawTracks(image.canvas(), mWidth);
		mBackLayer.setImage(image);
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
		boolean match = true;
		for (int i = 0; i < deliveredCargoList.size(); i++) {
			match = match
					&& deliveredCargoList.get(i).equals(cargoGoalList.get(i));
		}
		return match;
	}

	/**
	 * Checks if the level has been won. Should only be called after
	 * checkDelivered. Notifies all listeners on win.
	 */
	public void checkWin() {

	}

	@Override
	public void takeTrain(UITrain train) {
		currentTrains.add(train);
		List<UICarriage> carriages = train.getCarriages();
		// Unload the cargo from each carriage
		for (UICarriage c : carriages) {
			int cargo = c.getCargo();
			deliveredCargoList.add(cargo);
			deliveredCount++;
			deliveredCargoString = cargo + " | " + deliveredCargoString;
			// log().debug("Cargo: "+cargo+" delivered sucessfully!");

			// TODO Tidy up this code.
			if (deliveredCargoList.size() == cargoGoalList.size()) {
				if (checkDelivered()) {
					if (mListener != null) {
						mListener.levelCleared();
					}
				} else {
					if (mListener != null) {
						mListener.levelFailed();
					}
				}
			} else if (deliveredCargoList.size() < cargoGoalList.size()) {
				log().debug("Goal: " + cargoGoalString);
				log().debug("Current: " + deliveredCargoString);
				if (!checkDelivered()) {
					if (mListener != null) {
						mListener.levelFailed();
					}
				}

			} else {
				// LEVEL FAILED
				if (mListener != null) {
					mListener.levelFailed();
				}
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