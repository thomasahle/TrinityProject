package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.ArrayList;
import java.util.List;

import com.github.thomasahle.trainbox.trainbox.scenes.ToolListener;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory.UIToken;

public class ToolManager {
	
	UIToken currentTool;
	List<ToolListener> list;
	
	public ToolManager() {
		list = new ArrayList<ToolListener>();
	}
	
	public void add(ToolListener listener){
		list.add(listener);
	}
	
	public void setTool(UIToken tool){
		currentTool = tool;
		notifySelect();
	}
	
	public void unselect(){
		currentTool = null;
		notifyUnselect();
	}
	
	public void notifySelect(){
		for(ToolListener lis : list){
			lis.toolSelected(currentTool);
		}
	}
	
	public void notifyUnselect(){
		for(ToolListener lis : list){
			lis.toolsUnselected();		
		}
	}

	public UIToken getCurrentTool() {
		return currentTool;
	}

	public boolean isSelected() {
		return currentTool != null;
	}
		
}
