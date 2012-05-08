package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

//TODO add this to the end of the level for testing.

public class UIEndComponent extends AbstractComponent implements UIComponent, TrainTaker {

	private final static int HEIGHT = 100;
	private int mWidth;
	
	private Layer mBackLayer, mFrontLayer, mCargoDisplayLayer;
	private Canvas textCanvas;
	private Queue<UITrain> mIncomming = new LinkedList<UITrain>();
	private Queue<UITrain> mOutgoing = new LinkedList<UITrain>();
	private List<UICarriage> finishedCarriages = new ArrayList<UICarriage>();
	private String finishedCargoString = "";
	
	public UIEndComponent(int width) {
		mWidth = width;
		
		mBackLayer = graphics().createGroupLayer();
		
		CanvasImage image = graphics().createImage(width, HEIGHT);
		image.canvas().setFillColor(0x0000ffaa);
		image.canvas().fillCircle(width/2.f, HEIGHT/2.f, width/2.f);
		mFrontLayer = graphics().createImageLayer(image);
		
		//TODO find a way to display this layer.
		CanvasImage ti = graphics().createImage(width,HEIGHT);
		textCanvas = ti.canvas();
		mCargoDisplayLayer = graphics().createImageLayer(ti);
		//Position this layer over the component.
		mCargoDisplayLayer.setTranslation(this.getPosition().x, this.getPosition().y);
		
		
	}

	@Override
	public Dimension getSize() {
		return new Dimension(mWidth, HEIGHT);
	}

	@Override
	public Layer getBackLayer() {
		return mBackLayer;
	}
	@Override
	public Layer getFrontLayer() {
		return mFrontLayer;
	}

	@Override
	public void update(float delta) {

		float compLeft = getDeepPosition().x;
		float compRight = compLeft + getSize().width;
		
		// WARNING: Don't get confused by the silliness that is,
		// 			that mIncomming and mSent use queues. They will never contain more than one item.
		
		// Move the trains all the way into the garage
		for (Iterator<UITrain> it = mIncomming.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			float trainLeft = train.getPosition().x;
			
			if (trainLeft >= compLeft) {
				it.remove();
				
				mOutgoing.add(train);
				train.getLayer().setVisible(false);
				
				log().debug("Train captured, moved to middle queue. Length is: "+mOutgoing.size());
			}
			else {
				float newLeft = trainLeft + UITrain.SPEED*delta;
				train.setPosition(new Point(newLeft, train.getPosition().y));
				train.setCropLeft(compRight - newLeft);
			}
		}
		// Wait for the right moment to spit them out
		for (Iterator<UITrain> it = mOutgoing.iterator(); it.hasNext(); ) {
			UITrain train = it.next();
			
			/* get the carriages from the train and iterate over this list removing the cargo.
			 * The idea is that the cargo (numbers at this point) from the carriages will be 
			 * displayed on the end component or below it.
			 * 
			 * Hopefully we will eventually have images representing each type of cargo that we 
			 * can display on the trains and at this point.
			 * 
			 */
			
			List<UICarriage> carriages = train.getCarriages();
			for(UICarriage c : carriages){
				finishedCarriages.add(c);
				finishedCargoString += (" "+c.getCargo());
			}
			
			//TODO Length checking on this string so that lines wrap properly
			textCanvas.clear();
			textCanvas.drawText(finishedCargoString, 0, 0);
			log().debug("Finished: " + finishedCargoString);
			
			/*once the train has moved completely into the end component and we have extracted it's cargo
			 * it doesn't need to be visually represented.
			 */
			train.getLayer().destroy();
		}
		
	}

	@Override
	public void takeTrain(UITrain train) {
		mIncomming.add(train);
		log().debug("Reached end of line. All change please.");
	}

	@Override
	public float leftBlock() {
		// We never block
		return Float.MAX_VALUE;
	}
}