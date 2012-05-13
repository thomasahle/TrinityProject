package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import com.github.thomasahle.trainbox.trainbox.scenes.LevelScene;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory.UIToken;

import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.ImageLayer;
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

	private boolean selected;

	private ImageLayer outlinelayer;
	
	public UIComponentButton(final UILevel level, final UIToken comp){

		layer = graphics().createGroupLayer();

		CanvasImage image = graphics().createImage((int)mSize.width, (int)mSize.height);
		image.canvas().setFillColor(0xffaa00aa);
		image.canvas().fillCircle(mSize.width/ 2.f, mSize.height / 2.f, mSize.width / 2.f);
		layer = graphics().createImageLayer(image);
		
		CanvasImage outline = graphics().createImage((int)mSize.width, (int)mSize.height);
		image.canvas().setStrokeColor(0x00000000);
		image.canvas().setStrokeWidth(2);
		image.canvas().strokeRect(5, 5, mSize.width-5, mSize.height-5);
		outlinelayer = graphics().createImageLayer(image);
		
		layer.addListener(new Listener() {

			@Override
			public void onPointerStart(Event event) {
				level.setCompSel(comp);
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
		layer.setTranslation(point.x, point.y);
	}

	public void setUnselected() {
		selected = false;
		
	}
	
	public void setSelected() {
		selected = true;
	}

}
