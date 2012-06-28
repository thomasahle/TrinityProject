package com.github.thomasahle.trainbox.trainbox.uimodel;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import playn.core.GroupLayer;
import playn.core.Layer;
import playn.core.Layer.HitTester;
import playn.core.PlayN;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class UIHorizontalComponent extends AbstractComposite {
	
	public final int padding;
	
	// Invariant: |mComponents| > 0
	private List<UIComponent> mComponents = new ArrayList<UIComponent>();
	private GroupLayer mBackLayer = graphics().createGroupLayer();
	private GroupLayer mFrontLayer = graphics().createGroupLayer();

	private Dimension mSize = new Dimension(0,0);
	
	public UIHorizontalComponent(int padding) {
		this.padding = padding;
		
		insert(new UIIdentityComponent(padding), 0);
	}
	
	public void add(UIComponent comp) {
		insert(comp, getChildren().size());
		insert(new UIIdentityComponent(padding), getChildren().size());
	}
	
	@Override
	public boolean insertChildAt(UIComponent child, Point position) {
		// We accept positions that are on top of an identity component.
		// For the user that corresponds to the spaces between 'real' components.
		
		PlayN.log().debug("At: " + position);
		
		for (int p = 0; p < mComponents.size(); p++) {
			UIComponent c = mComponents.get(p);
			if (c.getPosition().x <= position.x
					&& position.x < c.getPosition().x+c.getSize().width) {
				// Okay, this is not terribly object oriented. But it works for now.
				if (c instanceof UIComposite) {
					Point recursivePoint = new Point(position.x-c.getPosition().x, position.y-c.getPosition().y);
					return ((UIComposite)c).insertChildAt(child, recursivePoint);
				}
				else if (c instanceof UIIdentityComponent) {
					log().debug("Inserting at position "+p);
					// Insert a new identity before the new component
					// this must be done first to maintain the first component of a Horizontal component
					// being a ID component.
					UIIdentityComponent newIDComp = new UIIdentityComponent(padding);
					insert(newIDComp, p);
					// Insert the new component before the identity clicked on
					insert(child, p+1);
					// TODO: Do we also need to shift the trains, or do we assume
					// that this is only called when trains are stopped?
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public UIComponent deleteChildAt(Point position) {
		// find the component at the point passed.
		for (int p = 0; p < mComponents.size(); p++) {
			UIComponent c = mComponents.get(p);
			if (c.getPosition().x <= position.x
					&& position.x < c.getPosition().x+c.getSize().width
					&& c.getPosition().y <= position.y
					&& position.y < c.getPosition().y+c.getSize().height) {
		// got the component (c) at the point passed
				
				// Okay, this is not terribly object oriented. But it works for now.
				// if the child-Component is a composite component, call same method on child. 
				// OR if the composite is empty, delete the composite
				if (c instanceof UIComposite) {
					if(!c.locked()){ // composites are locked while they have children.
						delete(p);
					
						delete(p-1); // also delete the leading ID Component
						return c;
					}else{
						Point recursivePoint = new Point(position.x-c.getPosition().x, position.y-c.getPosition().y);
						return ((UIComposite)c).deleteChildAt(recursivePoint);						
					}
				}
				else{ // child not a composite
					if(!c.locked()){
						delete(p);
						delete(p-1); // also delete the leading ID Component
						return c;
					}
				}
			}
		}
		// point doesn't correspond to any child component or the component is locked and can't be deleted
		return null;
	}
	
	@Override
	public boolean shouldBeDeleted() {
		return mComponents.size() == 0;
	}
	
	private void insert(UIComponent comp, int pos) {
		assert 0 <= pos && pos <= mComponents.size();
		
		Dimension oldSize = getSize();
		
		// Insert component correctly in the 'TrainTaker' chain
		if (pos > 0)
			mComponents.get(pos-1).setTrainTaker(comp);
		if (pos < mComponents.size())
			comp.setTrainTaker(mComponents.get(pos));
		if (pos == mComponents.size())
			comp.setTrainTaker(getTrainTaker());
		
		// Add the new layer
		mBackLayer.add(comp.getBackLayer());
		mFrontLayer.add(comp.getFrontLayer());
		
		// Install in data structures
		mComponents.add(pos, comp);
		super.install(comp);
		
		// Update size
		onSizeChanged(comp, new Dimension(0,0));
		fireSizeChanged(oldSize);
	}
	
	private void delete(int pos) {
		
		Dimension oldSize = getSize();
		UIComponent comp = mComponents.get(pos);
		
		// Remove component correctly in the 'TrainTaker' chain
		if (pos+1 == mComponents.size())
			mComponents.get(pos-1).setTrainTaker(getTrainTaker());
		if (pos > 0 && pos+1 < mComponents.size())
			mComponents.get(pos-1).setTrainTaker(mComponents.get(pos+1));
		
		// Remove the layers
		mBackLayer.remove(comp.getBackLayer());
		mFrontLayer.remove(comp.getFrontLayer());
		
		// Install in data structures
		mComponents.remove(pos);
		super.uninstall(comp);
		
		// Update size
		onSizeChanged(comp, new Dimension(0,0));
		fireSizeChanged(oldSize);
	}
	
	@Override
	public void onSizeChanged(UIComponent source, Dimension oldSize) {
		// Recalculate size
		float width = 0;
		float height = 0;
		for (UIComponent child : getChildren()) {
			width += child.getSize().width;
			height = Math.max(height, child.getSize().height);
		}
		Dimension myOldSize = mSize;
		mSize = new Dimension(width, height);
		if (!myOldSize.equals(mSize)) {
			// Reposition layers
			float x = 0;
			for (UIComponent child : getChildren()) {
				child.setPosition(new Point(x, height/2-child.getSize().height/2));
				x += child.getSize().width;
			}
			fireSizeChanged(myOldSize);
		}
	}
	
	@Override
	public List<UIComponent> getChildren() {
		return Collections.unmodifiableList(mComponents);
	}

	@Override
	public Dimension getSize() {
		return mSize;
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
	public void setTrainTaker(TrainTaker listener) {
		super.setTrainTaker(listener);
		mComponents.get(mComponents.size()-1).setTrainTaker(listener);
	}

	@Override
	public void takeTrain(UITrain train) {
		log().debug("Passing train down from "+this+" to "+mComponents.get(0));
		mComponents.get(0).takeTrain(train);
	}

	@Override
	public float leftBlock() {
		return mComponents.get(0).leftBlock();
	}
	
	@Override
	public boolean locked(){
		return (locked || getChildren().size()>1);
		//locked if it has more than it's initial identity child or it is manually locked using locked variable.
	}
	
	@Override
	public void updateMaxLengthTrainExpected(int compNum, int len){
		this.maxExpectedLength = len;
		log().debug("Max length of train expected for component " + compNum + ":   " + len);
		this.getChildren().get(0).updateMaxLengthTrainExpected(compNum +1,len);
	}
}
