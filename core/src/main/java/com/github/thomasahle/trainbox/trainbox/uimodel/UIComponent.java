package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.List;

import playn.core.Layer;
import pythagoras.f.Point;

/**
 * An interesting component that trains can go into and maybe come out.
 */
public interface UIComponent extends TrainTaker {
	/**
	 * We need a way to get hold of the trains from the top level. And as the
	 * components can create more trains as we go along, we've got to collect
	 * them through recursion.
	 */
	public List<UITrain> getTrains();
	/**
	 * This is necessary since layers don't have a size.
	 * We use this to layout components.
	 * @return The size of the component.
	 */
	public pythagoras.f.Dimension getSize();
	/**
	 * @return A layer to be placed behind the trains. Draw whatever you like.
	 */
	public Layer getBackLayer();
	/**
	 * @return A layer to be placed in front of trains. Draw whatever you like.
	 */
	public Layer getFrontLayer();
	/**
	 * The uicomponent can use this to move the trains it controls.
	 * @param delta seconds since laste update
	 */
	public void update(float delta);
	/**
	 * @param trainTaker the thing that takes over a train when the component is done with it.
	 */
	public void setTrainTaker(TrainTaker trainTaker);
	/**
	 * @return the TrainTaker we are currently passing trains to,
	 * or NullTaker if no such taker is set.
	 */
	public TrainTaker getTrainTaker();
	/**
	 * @param position The position is the location of the component inside its parent.
	 */
	public void setPosition(Point position);
	/**
	 * @return The position is the location of the component inside its parent.
	 */
	public Point getPosition();
	/**
	 * @return The position of the component relative to the entire track
	 */
	public Point getDeepPosition();
	/**
	 * Called when the component is added to another one.
	 * @param parent The parent that now owns us
	 */
	public void onAdded(UIComposite parent);
	/**
	 * Called when the component is removed from another one.
	 * @param parent The parent that used to own us
	 */
	public void onRemoved(UIComposite parent);
	/**
	 * @return the parent of the component or null if there is no parent
	 */
	public UIComposite getParent();
	/**
	 * @param listener A listener that is notified when the component creates
	 * or destroys trains.
	 */
	public void setTrainsChangedListener(TrainsChangedListener listener);
	/**
	 * @param paused true if the component should stop moving trains around. False otherwise.
	 * The component will still receive update calls, so it can animate itself.
	 */
	public void paused(boolean paused);
	/**
	 * @return the current pause status of the component
	 */
	public boolean paused();
	/**
	 * @param listener A listener that is notified when the component changes size.
	 * This mostly happens when new components are added inside, but in the future
	 * it may also stem from automatic resizing to fit trains.
	 */
	public void setSizeChangedListener(SizeChangedListener listener);
}
