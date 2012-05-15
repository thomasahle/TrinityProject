package com.github.thomasahle.trainbox.trainbox.scenes;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import java.util.HashMap;
import java.util.Map;

import playn.core.Canvas.LineJoin;
import playn.core.CanvasImage;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.Pointer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

import com.github.thomasahle.trainbox.trainbox.uimodel.ToolManager;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory.UIToken;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComposite;


public class UIComponentButton implements ToolListener, Pointer.Listener {
	
	private GroupLayer layer;
	
	private UIComponent component;
	
	private UIComposite mParent;
	private Point mPosition = new Point();
	private Dimension mSize = new Dimension(100,100);
	
	CanvasImage outline;

	private boolean selected;

	private ImageLayer imageLayer;
	private Layer imageSelLayer;

	private Layer outlineLayer;

	private ToolManager toolMan;

	private UIToken tool;

	private boolean mEnabled = true;

	private Image mImage;

	
	private static final Map<UIToken, Image> MAP = new HashMap<UIComponentFactory.UIToken, Image>();{{
		MAP.put(UIToken.DUP, assets().getImage("images/pngs/dupComponentButtonImage.png"));
		MAP.put(UIToken.BOX, assets().getImage("images/pngs/boxComponentButtonImage.png"));
		MAP.put(UIToken.FLIP, assets().getImage("images/pngs/flipComponentButtonImage.png"));
		MAP.put(UIToken.CAT, assets().getImage("images/pngs/concatComponentButtonImage.png"));
		MAP.put(UIToken.MERG, assets().getImage("images/pngs/splitComponentButtonImage.png"));
	}};
	
	private static final Map<UIToken, Image> MAPsel = new HashMap<UIComponentFactory.UIToken, Image>();{{
		MAP.put(UIToken.DUP, assets().getImage("images/pngs/dupComponentButtonImagePressed.png"));
		MAP.put(UIToken.BOX, assets().getImage("images/pngs/boxComponentButtonImagePressed.png"));
		MAP.put(UIToken.FLIP, assets().getImage("images/pngs/flipComponentButtonImagePressed.png"));
		MAP.put(UIToken.CAT, assets().getImage("images/pngs/concatComponentButtonImagePressed.png"));
		MAP.put(UIToken.MERG, assets().getImage("images/pngs/splitComponentButtonImagePressed.png"));
	}};
	
	private static final Image mDisabledImage = graphics().createImage(1, 1);
	
	public UIComponentButton(final ToolManager toolMan, final UIToken comp){
		this.toolMan = toolMan;
		toolMan.add(this);
		this.tool = comp;
//		component = UIComponentFactory.fromTok(comp);

//		CanvasImage image = graphics().createImage((int)mSize.width, (int)mSize.height);
//		image.canvas().setFillColor(0xffaa00aa);
//		image.canvas().fillCircle(mSize.width/ 2.f, mSize.height / 2.f, mSize.width / 2.f);
//		imageLayer = graphics().createImageLayer(image);
		
//		imageLayer = component.getBackLayer();
//		imageLayer.setScale((float) 0.5); 
		
		layer = graphics().createGroupLayer();
		
		mImage = MAP.get(comp);
		imageLayer = graphics().createImageLayer(mImage);
		
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
		
		imageLayer.addListener(this);
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
	
	public void enabled(boolean enabled) {
		mEnabled = enabled;
		if (enabled)
			imageLayer.setImage(mImage);
		if (!enabled)
			imageLayer.setImage(mDisabledImage);
	}
	public boolean enabled() {
		return mEnabled;
	}
	
	@Override
	public void onPointerStart(Event event) {
		if (enabled())
			toolMan.setTool(tool);
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
