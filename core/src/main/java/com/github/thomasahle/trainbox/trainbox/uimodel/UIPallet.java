package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.Layer.HitTester;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class UIPallet implements Listener, HitTester{
	
	private GroupLayer mLayer;
	private List<UIComponentButton> compList;
	private Dimension mSize = new Dimension(0, 0);
	private ImageLayer background;
	private CanvasImage rect;
	

	public UIPallet() {
		mLayer = graphics().createGroupLayer();

		setBackground();

		compList = new ArrayList<UIComponentButton>();
	}

	private void setBackground() {
		rect = graphics().createImage((int)mSize.width+20, (int)mSize.height+20);
		rect.canvas().clear();
		rect.canvas().setFillColor(0xffffff00);
		rect.canvas().fillRect(0, 0, mSize.width+20, mSize.height+20);
		background = graphics().createImageLayer(rect);
		background.setDepth(-1);
		mLayer.add(background);
		background.setTranslation(-10, -10);
	}

	public void add(UIComponentButton but){
		compList.add(but);
		mLayer.add(but.getLayer());
		sizeChanged();
	}
	
	public void setUnselected(){
		for (UIComponentButton but : compList) {
			but.setUnselected();
		}
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
	
	public void sizeChanged() {
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
		background.destroy();
		setBackground();
	}

	public Layer getLayer() {
		return mLayer;
	}

}
