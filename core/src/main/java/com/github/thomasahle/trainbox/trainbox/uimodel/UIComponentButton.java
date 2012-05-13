package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import com.github.thomasahle.trainbox.trainbox.scenes.LevelScene;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory.UIToken;
import com.sun.tools.doclets.internal.toolkit.util.Group;

import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Dimension;
import pythagoras.f.Point;


public class UIComponentButton implements ToolListener{
	
	private GroupLayer layer;
	
	private UIComposite mParent;
	private Point mPosition = new Point();
	private Dimension mSize = new Dimension(80,80);
	
	CanvasImage outline;

	private boolean selected;

	private ImageLayer imageLayer;

	private ImageLayer outlineLayer;

	private ToolManager toolMan;

	private UIToken tool;
	
	public UIComponentButton(final ToolManager toolMan, final UIToken comp){
		this.toolMan = toolMan;
		toolMan.add(this);
		this.tool = comp;
		layer = graphics().createGroupLayer();

		CanvasImage image = graphics().createImage((int)mSize.width, (int)mSize.height);
		image.canvas().setFillColor(0xffaa00aa);
		image.canvas().fillCircle(mSize.width/ 2.f, mSize.height / 2.f, mSize.width / 2.f);
		imageLayer = graphics().createImageLayer(image);
		layer.add(imageLayer);
		
		outline = graphics().createImage((int)mSize.width, (int)mSize.height);
		outline.canvas().setStrokeColor(0xff000000);
		outline.canvas().setStrokeWidth(4);
		outline.canvas().strokeRect(5, 5, mSize.width-5, mSize.height-5);
		outlineLayer = graphics().createImageLayer(outline);
		outlineLayer.setVisible(false);
		
		layer.add(outlineLayer);
		
		imageLayer.addListener(new Listener() {

			@Override
			public void onPointerStart(Event event) {
				toolMan.setTool(tool);
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

	@Override
	public void toolsUnselected() {
		selected = false;
		outlineLayer.setVisible(false);
	}

	@Override
	public void toolSelected(UIToken currentTool) {
		if (currentTool == tool) {
			selected = true;
			outlineLayer.setVisible(true);
		}
		else toolsUnselected();
	}
}
