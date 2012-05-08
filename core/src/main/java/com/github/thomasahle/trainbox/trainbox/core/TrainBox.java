package com.github.thomasahle.trainbox.trainbox.core;

import static playn.core.PlayN.assets;
import playn.core.AssetWatcher;
import playn.core.Game;

import com.github.thomasahle.trainbox.trainbox.scenes.DemoScene;
import com.github.thomasahle.trainbox.trainbox.scenes.LevelScene;
import com.github.thomasahle.trainbox.trainbox.scenes.LoadingScene;
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
	
	AssetWatcher watcher = new AssetWatcher(new AssetWatcher.Listener() {
		public void done() {
			startScene = new StartScene(TrainBox.this);
			demoScene = new DemoScene(TrainBox.this);
			levelScene = new LevelScene(TrainBox.this);
			moveScene = new MoveScene(TrainBox.this);
			setScene(startScene);
		}

		public void error(Throwable e) {
		}
	});
	
	@Override
	public void init() {
		
		setScene(new LoadingScene(this));
		
		//graphics().setSize(1920, 1080);  // this changes the size of the main window
		watcher.add(	assets().getImage("images/pngs/trains.png"));
		watcher.add(	assets().getImage("images/pngs/exit1Tr.png"));
		watcher.add(	assets().getImage("images/pngs/exit2Tr.png"));
		watcher.add(	assets().getImage("images/pngs/start1Tr.png"));
		watcher.add(	assets().getImage("images/pngs/start2Tr.png"));
		watcher.add(	assets().getImage("images/pngs/watermelonTr.png"));
		
		watcher.start();
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
