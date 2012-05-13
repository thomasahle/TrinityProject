package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import com.github.thomasahle.trainbox.trainbox.scenes.LevelScene;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory.UIToken;

import playn.core.CanvasImage;
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
	private Dimension mSize = new Dimension(80,80);
	private UIToken comp; 
	private UILevel level;

	
	public UIComponentButton(UILevel level, UIToken comp){
		this.comp = comp;
		this.level = level;
		layer = graphics().createGroupLayer();

		CanvasImage image = graphics().createImage((int)mSize.width, (int)mSize.height);
		image.canvas().setFillColor(0xffaa00aa);
		image.canvas().fillCircle(mSize.width/ 2.f, mSize.height / 2.f, mSize.width / 2.f);
		layer = graphics().createImageLayer(image);
		layer.addListener(new Listener() {

			@Override
			public void onPointerStart(Event event) {
				UIComponentButton.this.level.setCompSel(UIComponentButton.this.comp);
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
