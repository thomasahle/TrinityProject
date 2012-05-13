package com.github.thomasahle.trainbox.trainbox.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.AssetWatcher;
import playn.core.Game;

import com.github.thomasahle.trainbox.trainbox.model.Level;
import com.github.thomasahle.trainbox.trainbox.scenes.DemoScene;
import com.github.thomasahle.trainbox.trainbox.scenes.LevelScene;
import com.github.thomasahle.trainbox.trainbox.scenes.LevelSelectScene;
import com.github.thomasahle.trainbox.trainbox.scenes.LoadingScene;
import com.github.thomasahle.trainbox.trainbox.scenes.MoveScene;
import com.github.thomasahle.trainbox.trainbox.scenes.NullScene;
import com.github.thomasahle.trainbox.trainbox.scenes.Scene;
import com.github.thomasahle.trainbox.trainbox.scenes.StartScene;
import com.github.thomasahle.trainbox.trainbox.util.LevelTracker;

public class TrainBox implements Game{
	Scene demoScene; 
	Scene moveScene;
	Scene startScene;
	Scene levelSelectScene;
	
	Scene mScene = new NullScene();
	
	AssetWatcher watcher = new AssetWatcher(new AssetWatcher.Listener() {
		public void done() {
			startScene = new StartScene(TrainBox.this);
			demoScene = new DemoScene(TrainBox.this);
			moveScene = new MoveScene(TrainBox.this);
			levelSelectScene = new LevelSelectScene(TrainBox.this);
			
			setScene(startScene);
		}

		public void error(Throwable e) {
		}
	});
	
	@Override
	public void init() {
		
		setScene(new LoadingScene(this));
		graphics().setSize(1024, 640);  // this changes the size of the main window
		
		addResources();
		
		watcher.start();
	}

	private void addResources(){
		//Insertresources here.
		watcher.add(assets().getImage("images/pngs/aboutButton.png"));
		watcher.add(assets().getImage("images/pngs/aboutButtonPressed.png"));
		watcher.add(assets().getImage("images/pngs/aboutPage.png"));
		watcher.add(assets().getImage("images/pngs/backButton.png"));
		watcher.add(assets().getImage("images/pngs/changeLevelButton.png"));
		watcher.add(assets().getImage("images/pngs/demoButton.png"));
		watcher.add(assets().getImage("images/pngs/demoButtonPressed.png"));
		watcher.add(assets().getImage("images/pngs/demoPage1.png"));
		watcher.add(assets().getImage("images/pngs/demoPage2.png"));
		watcher.add(assets().getImage("images/pngs/demoPage3.png"));
		watcher.add(assets().getImage("images/pngs/demoPage4.png"));
		watcher.add(assets().getImage("images/pngs/demoPage5.png"));
		watcher.add(assets().getImage("images/pngs/doneButton.png"));
		watcher.add(assets().getImage("images/pngs/goalBar.png"));
		watcher.add(assets().getImage("images/pngs/goButton.png"));
		watcher.add(assets().getImage("images/pngs/levelCompleteBlurb.png"));
		watcher.add(assets().getImage("images/pngs/levelFailedBlurb.png"));
		watcher.add(assets().getImage("images/pngs/menuBackground.png"));
		watcher.add(assets().getImage("images/pngs/nextButton.png"));
		watcher.add(assets().getImage("images/pngs/pauseButton.png"));
		watcher.add(assets().getImage("images/pngs/playButton.png"));
		watcher.add(assets().getImage("images/pngs/playButtonPressed.png"));
		watcher.add(assets().getImage("images/pngs/standardBackground.png"));
		watcher.add(assets().getImage("images/pngs/startOff.png"));
		watcher.add(assets().getImage("images/pngs/startOn.png"));
		watcher.add(assets().getImage("images/pngs/startPage.png"));
		watcher.add(assets().getImage("images/pngs/trains.png"));
		watcher.add(assets().getImage("images/pngs/watermelonTr.png"));
		watcher.add(assets().getImage("images/basicCarriage.png"));
		watcher.add(assets().getImage("images/bg.png"));
		watcher.add(assets().getImage("images/cloud.png"));
		watcher.add(assets().getImage("images/emptyTrain.png"));
		watcher.add(assets().getImage("images/fullTrain.png"));
		watcher.add(assets().getImage("images/red_train.png"));
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
	
	public Scene getStartScene() {
		return startScene;
	}

	public Scene getMoveScene() {
		return moveScene;
	}
	
	public Scene getLevelSelectScene() {
		return levelSelectScene;
	}
	
	public void setScene(Scene scene) {
		mScene.onDetach();
		mScene = scene;
		scene.onAttach();
	}
	
	public Scene getScene() {
		return mScene;
	}
	
	public void setLevel(int index){
		LevelTracker.updateLevel(index);
		setScene(new LevelScene(this,Level.levels.get(index)));
	}


	

}
