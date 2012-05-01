package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.List;

import pythagoras.f.Point;

/**
 * A component that can have children.
 * 
 * There needs to be something set up, so we recursively can ask "what component is at this (x,y) position".
 * We need this in order to insert things while trying to solve a level.
 */
public interface UIComposite extends UIComponent {
	public List<UIComponent> getChildren();
	public boolean insertChildAt(UIComponent child, Point position);
}
