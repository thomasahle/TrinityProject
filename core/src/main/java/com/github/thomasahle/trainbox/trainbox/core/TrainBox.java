package com.github.thomasahle.trainbox.trainbox.core;

import static playn.core.PlayN.graphics;
import playn.core.Game;

import com.github.thomasahle.trainbox.trainbox.scenes.DemoScene;
import com.github.thomasahle.trainbox.trainbox.scenes.LevelScene;
import com.github.thomasahle.trainbox.trainbox.scenes.MoveScene;
import com.github.thomasahle.trainbox.trainbox.scenes.NullScene;
import com.github.thomasahle.trainbox.trainbox.scenes.Scene;
import com.github.thomasahle.trainbox.trainbox.scenes.StartScene;

public class TrainBox implements Game{
	Scene demoScene; 
	Scene levelScene;
	Scene moveScene;
	Scene startScene;
	
	
	
	Scene mScene = new NullScene();
	
	@Override
	public void init() {
		graphics().setSize(600, 500);  // this changes the size of the main window
		startScene = new StartScene(this);
		demoScene = new DemoScene(this);
		levelScene = new LevelScene(this);
		moveScene = new MoveScene(this);
		
		setScene(startScene);
	}

	
	
	@Override
	public void update(float delta) {
		mScene.update(delta);
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
	
	public Scene getDemoScene() {
		return demoScene;
	}
	
	public Scene getLevelScene() {
		return levelScene;
	}
	
	public Scene getStartScene() {
		return startScene;
	}

	public Scene getMoveScene() {
		return moveScene;
	}
	
	public void setScene(Scene scene) {
		mScene.onDetach();
		mScene = scene;
		scene.onAttach();
	}
	
	public Scene getScene() {
		return mScene;
	}
	

}
