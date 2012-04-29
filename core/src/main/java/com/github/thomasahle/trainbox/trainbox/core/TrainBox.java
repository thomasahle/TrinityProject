package com.github.thomasahle.trainbox.trainbox.core;

import playn.core.Game;

import com.github.thomasahle.trainbox.trainbox.scenes.LevelScene;
import com.github.thomasahle.trainbox.trainbox.scenes.Scene;


public class TrainBox implements Game{
	
	Scene currentScene;
	
	@Override
	public void init() {
		currentScene = new LevelScene();
		currentScene.onAttach();
	}

	@Override
	public void update(float delta) {
		currentScene.update(delta);
	}

	@Override
	public void paint(float alpha) {
		// TODO Auto-generated method stub
	}

	@Override
	public int updateRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	

}
