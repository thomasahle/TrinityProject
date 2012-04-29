package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.List;

import playn.core.Layer;
import pythagoras.f.Point;

/**
 * An interesting component that trains can go into and maybe come out.
 */
public interface UIComponent extends TrainTaker {
	/**
	 * We need a way to get hold of the trains from the top level.
	 * And as the components can create more trains as we go along,
	 * we've got to collect them through recursion.
	 */
	public List<UITrain> getCarriages();
	/**
	 * Change: Return a layer of the right size instead. Right?
	 * No: you cant get the size of a layer
	 */
	public pythagoras.f.Dimension getSize();
	/**
	 * This layer will be added by the parent Composite somewhere nice.
	 * Later we may draw our stuff on it.
	 */
	public Layer getLayer();
	/**
	 * Notice that UIComponents can't refuse trains the same way normal components can.
	 * @param train A train that must now be animated and controlled by the component.
	 */
	public void enterTrain(UITrain train);
	/**
	 * The uicomponent can use this to move the trains it controls.
	 * Do we also need a paint(delta) method?
	 * @param delta
	 */
	public void update(float delta);
	void setTrainTaker(TrainTaker listener);
	void setPosition(Point position);
	Point getPosition();
}
