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
import pythagoras.f.Point;

public class UIPallet implements Listener{
	
	private Object mLayer;
	private List<UIComponentButton> compList;

	public UIPallet() {
		mLayer = graphics().createGroupLayer();
		compList = new ArrayList<UIComponentButton>();
	}

	public void add(UIComponentButton but){
//		compList.add();				
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

}
