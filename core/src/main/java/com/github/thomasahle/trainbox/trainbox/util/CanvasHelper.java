package com.github.thomasahle.trainbox.trainbox.util;

import static playn.core.PlayN.graphics;
import playn.core.Layer;
import playn.core.Path;

public class CanvasHelper {
	/**
	 * @author thomas and `6502` (http://stackoverflow.com/questions/9693144/how-to-make-a-pill-shape-in-canvas-rounded-corner-rect-basically)
	 * @param path The path on which to draw the rectangle
	 * @param x0 Upper left corner
	 * @param y0 Upper left corner
	 * @param x1 Lower right corner
	 * @param y1 Lower right corner
	 * @param r Radius of the corners
	 */
	public static void roundRect(Path path, float x0, float y0, float x1, float y1, float r) {
	    float w = x1 - x0;
	    float h = y1 - y0;
	    assert w/2 >= r && h/2 >= r;
	    path.moveTo(x1 - r, y0);
	    path.quadraticCurveTo(x1, y0, x1, y0 + r);
	    path.lineTo(x1, y1-r);
	    path.quadraticCurveTo(x1, y1, x1 - r, y1);
	    path.lineTo(x0 + r, y1);
	    path.quadraticCurveTo(x0, y1, x0, y1 - r);
	    path.lineTo(x0, y0 + r);
	    path.quadraticCurveTo(x0, y0, x0 + r, y0);
	}
	/**
	 * @return A transparent dummy layer.
	 */
	public static Layer newEmptyLayer() {
		return graphics().createGroupLayer();
		//return graphics().createImageLayer(graphics().createImage(1,1));
	}
}
