package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import com.github.thomasahle.trainbox.trainbox.scenes.LevelScene;

import playn.core.Image;
import playn.core.Layer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Dimension;
import pythagoras.f.Point;


public class UIComponentButton{
	
	private Layer layer;
	
	private UIComposite mParent;
	private Point mPosition = new Point();
	private Dimension mSize = new Dimension(0,0);

	
	public UIComponentButton(UILevel level, UIComponent button){
		Layer layer = button.getBackLayer();
		layer.addListener(new Listener() {

			@Override
			public void onPointerStart(Event event) {
//				level.
			}

			@Override
			public void onPointerEnd(Event event) {
			}

			@Override
			public void onPointerDrag(Event event) {
			}});
	}

	public Dimension getSize() {
		return mSize;
	}

	public Layer getLayer() {
		return layer;
	}

	public void setPosition(Point point) {
		mPosition = point;
	}

}
