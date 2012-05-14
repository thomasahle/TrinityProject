package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import java.util.HashMap;
import java.util.Map;

import com.github.thomasahle.trainbox.trainbox.scenes.LevelScene;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory.UIToken;

import playn.core.Canvas.LineJoin;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Dimension;
import pythagoras.f.Point;
import sun.tools.tree.NewArrayExpression;


public class UIComponentButton implements ToolListener{
	
	private GroupLayer layer;
	
	private UIComponent component;
	
	private UIComposite mParent;
	private Point mPosition = new Point();
	private Dimension mSize = new Dimension(100,100);
	
	CanvasImage outline;

	private boolean selected;

	private Layer imageLayer;
	private Layer imageSelLayer;

	private Layer outlineLayer;

	private ToolManager toolMan;

	private UIToken tool;

	
	private static final Map<UIToken, Image> MAP = new HashMap<UIComponentFactory.UIToken, Image>();{{
		MAP.put(UIToken.DUP, assets().getImage("images/pngs/dupComponentButtonImage.png"));
		MAP.put(UIToken.BOX, assets().getImage("images/pngs/boxComponentButtonImage.png"));
		MAP.put(UIToken.FLIP, assets().getImage("images/pngs/flipComponentButtonImage.png"));
		MAP.put(UIToken.CAT, assets().getImage("images/pngs/concatComponentButtonImage.png"));
		MAP.put(UIToken.MERG, assets().getImage("images/pngs/dupComponentButtonImage.png"));
//		MAP.put(UIToken.MERG, assets().getImage("images/pngs/mergComponentButtonImage.png"));
		
	}};
	
	private static final Map<UIToken, Image> MAPsel = new HashMap<UIComponentFactory.UIToken, Image>();{{
		MAP.put(UIToken.DUP, assets().getImage("images/pngs/dupComponentButtonImagePressed.png"));
		MAP.put(UIToken.BOX, assets().getImage("images/pngs/boxComponentButtonImagePressed.png"));
		MAP.put(UIToken.FLIP, assets().getImage("images/pngs/flipComponentButtonImagePressed.png"));
		MAP.put(UIToken.CAT, assets().getImage("images/pngs/concatComponentButtonImagePressed.png"));
		MAP.put(UIToken.MERG, assets().getImage("images/pngs/dupComponentButtonImagePressed.png"));
//		MAP.put(UIToken.MERG, assets().getImage("images/pngs/mergComponentButtonImagePressed.png"));

	}};
		
	public UIComponentButton(final ToolManager toolMan, final UIToken comp){
		this.toolMan = toolMan;
		toolMan.add(this);
		this.tool = comp;
		component = UIComponentFactory.fromTok(comp);
		layer = graphics().createGroupLayer();

//		CanvasImage image = graphics().createImage((int)mSize.width, (int)mSize.height);
//		image.canvas().setFillColor(0xffaa00aa);
//		image.canvas().fillCircle(mSize.width/ 2.f, mSize.height / 2.f, mSize.width / 2.f);
//		imageLayer = graphics().createImageLayer(image);
		
//		imageLayer = component.getBackLayer();
//		imageLayer.setScale((float) 0.5); 
		
		imageLayer = graphics().createImageLayer(MAP.get(comp));
		imageLayer.setDepth(2);
		
		layer.add(imageLayer);
		
//		imageSelLayer = graphics().createImageLayer(MAPsel.get(comp));

		
		outline = graphics().createImage((int)mSize.width, (int)mSize.height);
		outline.canvas().setStrokeColor(0xaaffffff);
		outline.canvas().setStrokeWidth(6);
		outline.canvas().setLineJoin(LineJoin.ROUND);
		
		outline.canvas().strokeRect(0, 0, mSize.width, mSize.height);
		outlineLayer = graphics().createImageLayer(outline);
		outlineLayer.setVisible(false);
		
		layer.add(outlineLayer);

		
//		layer.add(imageSelLayer);
		
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
//		imageSelLayer.setVisible(false);
	}

	@Override
	public void toolSelected(UIToken currentTool) {
		if (currentTool == tool) {
			selected = true;
			outlineLayer.setVisible(true);
//			imageSelLayer.setVisible(true);
		}
		else toolsUnselected();
	}
}
