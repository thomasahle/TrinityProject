package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayList;
import java.util.List;

import playn.core.GroupLayer;
import playn.core.Layer;
import playn.core.Layer.HitTester;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

import com.github.thomasahle.trainbox.trainbox.model.Level;
import com.github.thomasahle.trainbox.trainbox.model.Train;
import com.github.thomasahle.trainbox.trainbox.scenes.LevelScene;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory.UIToken;

public class UILevel implements TrainsChangedListener, LevelFinishedListener, Listener, HitTester, ToolListener {
	
	private GroupLayer mLayer;
	private GroupLayer mTrainLayer;
	private UIStartComponent mStart;
	private UIGoalComponent mGoal;
	private UIComposite mTrack;
	private Level mLevel;	
	private LevelFinishedListener mListener;
	boolean isCompSelected = false;
	UIToken compSelected = null;
	private LevelScene levelScene;
	
	public UILevel(LevelScene levelScene, Level level) {
		this.levelScene = levelScene;
		mLevel = level;
		mLayer = graphics().createGroupLayer();
		
		mTrainLayer = graphics().createGroupLayer();
		List<UITrain> trains = new ArrayList<UITrain>();
		for (Train train : level.input) {
			UITrain uitrain = new UITrain(train);
			trains.add(uitrain);
			mTrainLayer.add(uitrain.getLayer());
		}
		
		UIHorizontalComponent track = new UIHorizontalComponent(100); 
		mStart = new UIStartComponent(trains);
		mGoal = new UIGoalComponent(400, mLevel.goal);
		mGoal.addListener(this);
		
		
		track.add(mStart);
		track.add(new UISeparateComponent(100));
		track.add(new UIIdentityComponent(100));
		track.add(new UIIdentityComponent(100));
		track.add(new UIIdentityComponent(100));
		
		UIHorizontalComponent top = new UIHorizontalComponent(60);
		UIHorizontalComponent bot = new UIHorizontalComponent(60);
		track.add(new UISplitMergeComponent(top, bot));
		
		UIHorizontalComponent top1 = new UIHorizontalComponent(40);
		UIHorizontalComponent bot1 = new UIHorizontalComponent(40);
		top.add(new UISplitMergeComponent(top1,bot1));
		top1.add(new UIJoinComponent(80));
		top1.add(new UIJoinComponent(80));
		bot1.add(new UIJoinComponent(80));
		bot1.add(new UIJoinComponent(80));
		
		UIHorizontalComponent top2 = new UIHorizontalComponent(40);
		UIHorizontalComponent bot2 = new UIHorizontalComponent(40);
		bot.add(new UISplitMergeComponent(top2,bot2));
		top2.add(new UIJoinComponent(80));
		top2.add(new UIJoinComponent(80));
		bot2.add(new UIJoinComponent(80));
		bot2.add(new UIJoinComponent(80));
		
		track.add(mGoal);
		
		
		mTrack = track;
		mTrack.paused(true);
		
		mLayer.add(mTrack.getBackLayer());
		mLayer.add(mTrainLayer);
		mLayer.add(mTrack.getFrontLayer());
		mLayer.setHitTester(this);
		
		mTrack.setTrainsChangedListener(this);
		mTrack.getBackLayer().addListener(this);
		//mGoal.setListener(this);
	}
	
	public void update(float delta) {
		mTrack.update(delta);
	}
	
	
	public Layer layer() {
		return mLayer;
	}
	@Override
	public void onTrainCreated(UITrain train) {
		mTrainLayer.add(train.getLayer());
	}
	@Override
	public void onTrainDestroyed(UITrain train) {
		mTrainLayer.remove(train.getLayer());
	}
	
	
	public boolean paused() {
		return mTrack.paused();
	}
	public void paused(boolean paused) {
		mTrack.paused(paused);
	}
	
	public void setDragMode(){
		isCompSelected = false;
	}
	
	public void setCompSel(UIToken tok){
		isCompSelected = true;
		compSelected = tok;
	}
	
	public void setListener(LevelFinishedListener listener) {
		mListener = listener;
	}
	@Override
	public void levelCleared() {
		log().debug("LEVEL CLEARED !!!");
		mListener.levelCleared();
	}
	@Override
	public void levelFailed() {
		log().debug("LEVEL FAILED !!!");
		mListener.levelFailed();
	}

	@Override
	public void onPointerStart(Event event) {
		if (isCompSelected) {
			Point p = new Point(event.localX(), event.localY());
			mTrack.insertChildAt(UIComponentFactory.fromTok(compSelected), p);
		}
	}

	@Override
	public void onPointerEnd(Event event) {
	}

	@Override
	public void onPointerDrag(Event event) {
	}

	@Override
	public Layer hitTest(Layer layer, Point p) {
		float x = mTrack.getPosition().x;
		float y = mTrack.getPosition().y + mTrack.getSize().height*0.85f;
		float x1 = x + mTrack.getSize().width;
		float y1 = y + mTrack.getSize().height;
		if (x <= p.x && p.x < x1 && y <= p.y && p.y < y1 || !isCompSelected) {
			isCompSelected = false;
			return layer;
		}
		return mTrack.getBackLayer().hitTest(p);
	}
	
	public void setTrainSpeed(float s){
		for(UITrain t: mTrack.getTrains()){
			t.setSpeed(s);
		}
	}
	public void increaseTrainSpeed(float ds){
		for(UITrain t: mTrack.getTrains()){
			t.setSpeed(t.getSpeed()+ds);
		}
	}

	public void decreaseTrainSpeed(float ds) {
		for(UITrain t: mTrack.getTrains()){
			if(t.getSpeed()-ds >= 0)
			t.setSpeed(t.getSpeed()-ds);
		}
	}
	
	public Dimension getSize(){
		return mTrack.getSize();
	}

	@Override
	public void toolSelected(UIToken currentTool) {
		compSelected = currentTool;
		isCompSelected = true;
	}

	@Override
	public void toolsUnselected() {
		isCompSelected = false;
	}
}
