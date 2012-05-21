package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.Random;

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
	
	private final static int CRATERS = 20;
	private final static int CRATER_AVG = 40;
	private final static int CRATER_SD = 30;
	public static CanvasImage drawMoonCraters(int width, int height, long seed) {
		return drawMoonCraters(width, height, CRATERS, seed);
	}
	public static CanvasImage drawMoonCraters(int width, int height, int craters, long seed) {
		Random random = new Random(seed);
		CanvasImage img = PlayN.graphics().createImage(width, height);
		Canvas ctx = img.canvas();
		ctx.setFillColor(0xffe9b96e);
		ctx.fillRect(0, 0, width, height);
		for (int i = 0; i < craters; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int r = (int)Math.round(random.nextGaussian()*CRATER_SD+CRATER_AVG);
			r = Math.max(r, 1);
			drawCrater(ctx, x, y, r, random);
		}
		return img;
	}
	
	private final static int HIGHLIGHT = 0xfff1d6aa;
	private final static int NORMAL = 0xffd4a863;
	private final static int SHADOW = 0xffb79256;
	private static void drawCrater(Canvas ctx, int x, int y, int r, Random random) {
		float shift = 0.1f + (float)random.nextGaussian()*0.01f;
		float shiftin = 0.08f + (float)random.nextGaussian()*0.01f;
		float outr = 0.97f + (float)random.nextGaussian()*0.01f;
		float inr = 1-shift;
		float inwidthmod = 1.6f;
		
		ctx.setFillColor(HIGHLIGHT);
		ctx.fillCircle(x-shift*r, y-shift*r, r*outr);
		
		ctx.setFillColor(SHADOW);
		ctx.fillCircle(x+r*shift, y+r*shift, r*outr);
		
		ctx.setFillColor(NORMAL);
		ctx.fillCircle(x, y, r);
		
		CanvasImage offScreen = PlayN.graphics().createImage(2*r, 2*r);
		Canvas ctx2 = offScreen.canvas();
		ctx2.setFillColor(SHADOW);
		float inshift = (1 - inr - FloatMath.sqrt(2)*shiftin)/FloatMath.sqrt(2);
		ctx2.fillCircle(r-inshift*r, r-inshift*r, r*inr);
		ctx2.setCompositeOperation(Composite.DST_OUT);
		ctx2.fillCircle(r+inwidthmod*shift*r, r+inwidthmod*shift*r, r);
		ctx.drawImage(offScreen, x-r, y-r);
		
		offScreen = PlayN.graphics().createImage(2*r, 2*r);
		ctx2 = offScreen.canvas();
		ctx2.setFillColor(HIGHLIGHT);
		ctx2.fillCircle(r+inshift*r, r+inshift*r, r*inr);
		ctx2.setCompositeOperation(Composite.DST_OUT);
		ctx2.fillCircle(r-inwidthmod*shift*r, r-inwidthmod*shift*r, r);
		ctx.drawImage(offScreen, x-r, y-r);
	}
}
