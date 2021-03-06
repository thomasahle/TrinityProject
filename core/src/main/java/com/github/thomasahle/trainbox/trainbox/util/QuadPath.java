package com.github.thomasahle.trainbox.trainbox.util;

import static playn.core.PlayN.log;

import java.util.Arrays;

import pythagoras.f.FloatMath;
import pythagoras.f.IdentityTransform;
import pythagoras.f.Path;
import pythagoras.f.PathIterator;
import pythagoras.f.Rectangle;

public class QuadPath {
	
	private static final float SMOOTHNES = 0.5f;
	
	public final pythagoras.f.Path pytagPath;
	
	private float minx = Float.MAX_VALUE;
	private float maxx = Float.MIN_VALUE;
	private float miny = Float.MAX_VALUE;
	private float maxy = Float.MIN_VALUE;
	
	
	public QuadPath() {
		pytagPath = new Path();
	}
	
	public void moveTo(float x, float y) {
		pytagPath.moveTo(x, y);
		minx = Math.min(minx, x);
		maxx = Math.max(maxx, x);
		miny = Math.min(miny, y);
		maxy = Math.max(maxy, y);
	}

	public void quadraticCurveTo(float cpx, float cpy, float x, float y) {
		pytagPath.quadTo(cpx, cpy, x, y);
		minx = Math.min(minx, x);
		maxx = Math.max(maxx, x);
		miny = Math.min(miny, y);
		maxy = Math.max(maxy, y);
	}
	
	public float length() {
		return calculateT(maxx);
	}
	
	public Rectangle bounds() {
		return new Rectangle(minx, miny, maxx-minx, maxy-miny);
	}
	
	public playn.core.Path paintPath(playn.core.Path path) {
		PathIterator iter = pytagPath.pathIterator(new IdentityTransform());
		float[] buffer = new float[6];
		while (!iter.isDone()) {
			switch (iter.currentSegment(buffer)) {
			case PathIterator.SEG_MOVETO:
				path.moveTo(buffer[0], buffer[1]);
				break;
			case PathIterator.SEG_LINETO:
				path.lineTo(buffer[0], buffer[1]);
				break;
			case PathIterator.SEG_QUADTO:
				path.quadraticCurveTo(buffer[0], buffer[1], buffer[2], buffer[3]);
				break;
			}
			iter.next();
		}
		return path;
	}
	
	public void print12() {
		PathIterator iter = pytagPath.pathIterator(new IdentityTransform(), SMOOTHNES);
		float[] buffer = new float[6];
		while (!iter.isDone()) {
			log().debug(""+iter.currentSegment(buffer));
			log().debug(Arrays.toString(buffer));
			iter.next();
		}
	}
	
	/**
	 * @param t how far to walk along the path
	 * @return the position on the path that we are at after distance t
	 */
	public float[] evaluate(float t) {
		PathIterator iter = pytagPath.pathIterator(new IdentityTransform(), SMOOTHNES);
		float[] buffer = new float[6];
		int type = iter.currentSegment(buffer);
		assert type == PathIterator.SEG_MOVETO;
		float lastx = buffer[0];
		float lasty = buffer[1];
		if (t <= 0) {
			buffer[0] += t;
			return buffer;
		}
		iter.next();
		while (!iter.isDone()) {
			int type2 = iter.currentSegment(buffer);
			assert type2 == PathIterator.SEG_LINETO;
			float segLength = dist(lastx-buffer[0], lasty-buffer[1]);
			if (segLength >= t)
				return lineCut(lastx, lasty, buffer, t);
			t -= segLength;
			iter.next();
			lastx = buffer[0];
			lasty = buffer[1];
		}
		buffer[0] += t;
		return buffer;
	}
	
	public float[] evaluateSlope(float t) {
		if (t <= 0)
			return new float[] {1, 0};
		PathIterator iter = pytagPath.pathIterator(new IdentityTransform(), SMOOTHNES);
		float[] buffer = new float[6];
		int type = iter.currentSegment(buffer);
		assert type == PathIterator.SEG_MOVETO;
		float lastx = buffer[0];
		float lasty = buffer[1];
		iter.next();
		while (!iter.isDone()) {
			int type2 = iter.currentSegment(buffer);
			assert type2 == PathIterator.SEG_LINETO;
			float segLength = dist(lastx-buffer[0], lasty-buffer[1]);
			if (segLength >= t) {
				float dx = (buffer[0]-lastx)/segLength;
				float dy = (buffer[1]-lasty)/segLength;
				return new float[] {dx, dy};
			}
			t -= segLength;
			iter.next();
			lastx = buffer[0];
			lasty = buffer[1];
		}
		return new float[] {1, 0};
	}
	
	public void print() {
		PathIterator iter = pytagPath.pathIterator(new IdentityTransform(), SMOOTHNES);
		float[] old = new float[6];
		float[] buffer = new float[6];
		while (!iter.isDone()) {
			log().debug(""+iter.currentSegment(buffer)+" "+Arrays.toString(buffer));
			log().debug("<"+(buffer[0]-old[0])+" "+(buffer[1]-old[1])+">");
			iter.next();
			System.arraycopy(buffer, 0, old, 0, 6);
		}
		
	}
	
	public float calculateT(float x) {
		float t = 0;
		PathIterator iter = pytagPath.pathIterator(new IdentityTransform(), SMOOTHNES);
		float[] buffer = new float[6];
		int type = iter.currentSegment(buffer);
		assert type == PathIterator.SEG_MOVETO;
		float lastx = buffer[0];
		float lasty = buffer[1];
		// If we haven't reached the path yet
		if (x <= lastx) {
			return x - lastx;
		}
		// If it is in the path
		iter.next();
		while (!iter.isDone()) {
			int type2 = iter.currentSegment(buffer);
			assert type2 == PathIterator.SEG_LINETO;
			float segLength = dist(lastx-buffer[0], lasty-buffer[1]);
			if (buffer[0] >= x) {
				//log().debug("Xs "+buffer[0]+" "+x+" "+lastx);
				return t + (x-lastx)/(buffer[0]-lastx) * segLength;
			}
			t += segLength;
			iter.next();
			lastx = buffer[0];
			lasty = buffer[1];
		}
		// If we are after the path
		return t + x - buffer[0];
	}
	
	private float[] lineCut(float lastx, float lasty, float[] buffer, float t) {
		float d = dist(lastx-buffer[0], lasty-buffer[1]);
		float x = lastx + t/d*(buffer[0]-lastx);
		float y = lasty + t/d*(buffer[1]-lasty);
		return new float[] {x, y};
	}

	private float dist(float dx, float dy) {
		return FloatMath.sqrt(dx*dx+dy*dy);
	}
}
