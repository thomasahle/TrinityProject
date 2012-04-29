package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.graphics;

import java.util.List;

import com.github.thomasahle.trainbox.trainbox.uimodel.NullTrainTaker;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory;
import com.github.thomasahle.trainbox.trainbox.uimodel.UITrain;


/**
 * This should contain everything we need for a level:
 *  - The track
 *  - Components to add
 *  - The play button
 */
public class LevelScene implements Scene {
	
	private List<UITrain> trains;
	private UIComponent mTrack;
	public LevelScene() {
		mTrack = UIComponentFactory.demo();
		trains = mTrack.getCarriages();
	}
	
	@Override
	public void update(float delta) {
		mTrack.update(delta);
	}

	@Override
	public void onAttach() {
		graphics().rootLayer().add(mTrack.getLayer());
		for (UITrain train : trains)
			graphics().rootLayer().add(train.getLayer());
	}

	@Override
	public void onDetach() {
		graphics().rootLayer().remove(mTrack.getLayer());
		// remove trains
	}

}
