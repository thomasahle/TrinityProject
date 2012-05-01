package com.github.thomasahle.trainbox.trainbox.core;

import static playn.core.PlayN.graphics;
import playn.core.Game;

import com.github.thomasahle.trainbox.trainbox.scenes.LevelScene;
import com.github.thomasahle.trainbox.trainbox.scenes.Scene;
import com.github.thomasahle.trainbox.trainbox.scenes.StartScene;


public class TrainBox implements Game{
	
	Scene currentScene;
	
	@Override
	public void init() {
		graphics().setSize(900, 700);  // this changes the size of the main window
		currentScene = new StartScene();
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
