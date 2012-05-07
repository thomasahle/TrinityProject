package com.github.thomasahle.trainbox.trainbox.scenes;

import com.github.thomasahle.trainbox.trainbox.core.TrainBox;

import playn.core.Layer;

/**
 * Like in a play, we need to go between different scenes.
 */
public interface Scene {
	public void update(float delta);
	public void onAttach();
	public void onDetach();
}
