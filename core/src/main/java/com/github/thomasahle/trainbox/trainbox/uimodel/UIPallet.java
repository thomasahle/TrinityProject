package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import playn.core.GroupLayer;
import playn.core.Layer;
import playn.core.Layer.HitTester;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class UIPallet implements Listener, HitTester{
	
	private GroupLayer mLayer;
	private List<UIComponentButton> compList;
	private Dimension mSize;
	

	public UIPallet() {
		mLayer = graphics().createGroupLayer();
		compList = new ArrayList<UIComponentButton>();
	}

	public void add(UIComponentButton but){
		compList.add(but);
		mLayer.add(but.getLayer());
	}
	
	@Override
	public void onPointerStart(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerEnd(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPointerDrag(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Layer hitTest(Layer layer, Point p) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onSizeChanged(UIComponent source, Dimension oldSize) {
		// Recalculate size
		float width = 0;
		float height = 0;
		for (UIComponentButton but : compList) {
			width += but.getSize().width;
			height = Math.max(height, but.getSize().height);
		}
		Dimension myOldSize = mSize;
		mSize = new Dimension(width, height);
		if (!myOldSize.equals(mSize)) {
			// Reposition layers
			float x = 0;
			for (UIComponentButton but : compList) {
				but.setPosition(new Point(x, height/2-but.getSize().height/2));
				x += but.getSize().width;
			}
		}
	}

}
