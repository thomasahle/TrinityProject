package com.github.thomasahle.trainbox.trainbox.core;

import playn.core.Game;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.Pointer;

import com.github.thomasahle.trainbox.trainbox.scenes.DemoScene;
import com.github.thomasahle.trainbox.trainbox.scenes.MenuScene;
import com.github.thomasahle.trainbox.trainbox.scenes.MoveScene;
import com.github.thomasahle.trainbox.trainbox.scenes.Scene;
import com.github.thomasahle.trainbox.trainbox.scenes.StartScene;


public class TrainBox implements Game{
	
	Scene currentScene;
	
	@Override
	public void init() {
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
