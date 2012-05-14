package com.github.thomasahle.trainbox.trainbox.scenes;

import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory.UIToken;

public interface ToolListener {
	
	public void toolSelected(UIToken currentTool);
	/***
	 * Called when no tool is selected.
	 */
	public void toolsUnselected();

}
