package com.github.thomasahle.trainbox.trainbox.uimodel;

import pythagoras.f.Dimension;

public interface SizeChangedListener {
	public void onSizeChanged(UIComponent source, Dimension oldSize);
}
