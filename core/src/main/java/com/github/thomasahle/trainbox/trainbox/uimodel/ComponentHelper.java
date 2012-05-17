package com.github.thomasahle.trainbox.trainbox.uimodel;

import playn.core.Canvas;
import playn.core.Canvas.Composite;
import playn.core.CanvasImage;
import playn.core.Path;
import playn.core.PlayN;
import pythagoras.f.FloatMath;

import com.github.thomasahle.trainbox.trainbox.util.CanvasHelper;
import com.github.thomasahle.trainbox.trainbox.util.QuadPath;

public class ComponentHelper {
	public final static int HEIGHT = 100;
	public final static float SLEEPER_HEIGHT = 0.34070f * HEIGHT;
	public final static float SLEEPER_WIDTH = 0.02734f * HEIGHT;
	public final static float SLEEPER_SPACE = 0.05372f * HEIGHT; // Will be adjusted
	public final static float SLEEPER_R = 0.00828f * HEIGHT;
	public final static float RAIL_HEIGHT = 0.01892f * HEIGHT;
	public final static float RAIL_SPACE = 0.19351f * HEIGHT;
	public final static float RAIL_EXTRA = 0.05372f * HEIGHT;
	public final static float RAIL_R = 0.00827f * HEIGHT;
	
	public static void drawTracks(Canvas ctx, float width) {
		drawTracks(ctx, width, 0xff000000, 0xff666666);
	}
	
	public static void drawTracks(Canvas ctx, float width, int sleeper_color, int rail_color) {
		ctx.translate(RAIL_EXTRA, 0);
		
		// The sleepers
		int nSleepers = (int)(width/(SLEEPER_WIDTH + SLEEPER_SPACE));
		nSleepers = Math.max(nSleepers, 1);
		float actualSpace = width/(float)nSleepers - SLEEPER_WIDTH;
		Path path1 = ctx.createPath();
		for (int i = 0; i < nSleepers; i++) {
			float x = i * (SLEEPER_WIDTH + actualSpace) + SLEEPER_SPACE/2;
			float y = HEIGHT/2.f - SLEEPER_HEIGHT/2.f;
			CanvasHelper.roundRect(path1,
					x,
					y,
					x+SLEEPER_WIDTH,
					y+SLEEPER_HEIGHT, SLEEPER_R);
		}
		ctx.setFillColor(sleeper_color);
		ctx.fillPath(path1);
		
		// The rails
		Path path2 = ctx.createPath();
		float railY1 = HEIGHT/2.f-RAIL_SPACE/2.f;
		CanvasHelper.roundRect(path2,
				-RAIL_EXTRA,
				railY1-RAIL_HEIGHT,
				width+RAIL_EXTRA,
				railY1, RAIL_R);
		float railY2 = HEIGHT/2.f+RAIL_SPACE/2.f;
		CanvasHelper.roundRect(path2,
				-RAIL_EXTRA,
				railY2,
				width+RAIL_EXTRA,
				railY2+RAIL_HEIGHT, RAIL_R);
		ctx.setFillColor(rail_color);
		ctx.fillPath(path2);
	}
	
	public static void drawBendTrack(Canvas ctx, QuadPath path) {
		drawBendTrack(ctx, path, 0xff000000, 0xff666666);
	}
	
	public static void drawBendTrack(Canvas ctx, QuadPath path, int sleeper_color, int rail_color) {
		
		// The sleepers
		float width = path.length();
		int nSleepers = (int)(width/(SLEEPER_WIDTH + SLEEPER_SPACE));
		nSleepers = Math.max(nSleepers, 1);
		float actualSpace = width/(float)nSleepers - SLEEPER_WIDTH;
		for (int i = 0; i < nSleepers; i++) {
			float t = (i+0.5f) * (SLEEPER_WIDTH + actualSpace);
			float[] pos = path.evaluate(t);
			float[] slope = path.evaluateSlope(t);
			Path rect = ctx.createPath();
			CanvasHelper.roundRect(rect,
					-SLEEPER_WIDTH/2,
					-SLEEPER_HEIGHT/2,
					SLEEPER_WIDTH/2,
					SLEEPER_HEIGHT/2, SLEEPER_R);
			ctx.save();
			ctx.translate(pos[0], pos[1]);
			ctx.rotate(FloatMath.atan2(slope[1], slope[0]));
			ctx.setFillColor(sleeper_color);
			ctx.fillPath(rect);
			ctx.restore();
		}
		
		// The rails
		float outerRailSize = RAIL_SPACE+2*RAIL_HEIGHT;
		CanvasImage offScreen = PlayN.graphics().createImage(
				(int)(path.bounds().width+path.bounds().x+outerRailSize+2),
				(int)(path.bounds().height+path.bounds().y+outerRailSize+2));
		Canvas ctx2 = offScreen.canvas();
		Path playnPath = path.paintPath(ctx2.createPath());
		ctx2.setStrokeColor(rail_color);
		ctx2.setStrokeWidth(outerRailSize);
		ctx2.strokePath(playnPath);
		ctx2.setCompositeOperation(Composite.DST_OUT); // F*cking awesome feature!
		ctx2.setStrokeWidth(RAIL_SPACE);
		ctx2.strokePath(playnPath);
		ctx.drawImage(offScreen, 0, 0);
	}
}
