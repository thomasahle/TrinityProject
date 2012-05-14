package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.ArrayList;
import java.util.List;

import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponentFactory.UIToken;

public class ToolManager {
	
	UIToken currentTool;
	boolean isSelected;
	List<ToolListener> list;
	
	public ToolManager() {
		isSelected = false;
		list = new ArrayList<ToolListener>();
	}
	
	public void add(ToolListener listener){
		list.add(listener);
	}
	
	public void setTool(UIToken tool){
		currentTool = tool;
		isSelected = true;
		notifySelect();
	}
	
	public void unselect(){
		currentTool = null;
		isSelected = false;
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
		
}
