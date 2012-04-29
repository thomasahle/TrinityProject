package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.graphics;

import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIDupComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIHorizontalComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UITrain;


/**
 * This should contain everything we need for a level:
 *  - The track
 *  - Components to add
 *  - The play button
 */
public class LevelScene implements Scene {
	
	private UIComponent mTrack;
	
	public LevelScene() {
		UIHorizontalComponent track = new UIHorizontalComponent(100);
		track.add(new UIDupComponent(100));
			UIHorizontalComponent nested = new UIHorizontalComponent(100);
			nested.add(new UIDupComponent(100));
		track.add(nested);
		
		track.takeTrain(new UITrain(1));
		track.takeTrain(new UITrain(2));
		
		mTrack = track;
	}
	
	@Override
	public void update(float delta) {
		mTrack.update(delta);
	}

	@Override
	public void onAttach() {
		graphics().rootLayer().add(mTrack.getLayer());
		for (UITrain train : mTrack.getCarriages())
			graphics().rootLayer().add(train.getLayer());
	}

	@Override
	public void onDetach() {
		graphics().rootLayer().remove(mTrack.getLayer());
		for (UITrain train : mTrack.getCarriages())
			graphics().rootLayer().remove(train.getLayer());
	}

}
