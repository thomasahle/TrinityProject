package com.github.thomasahle.trainbox.trainbox.util;

import playn.core.PlayN;
import playn.core.Storage;

public class LevelTracker {
	
	private static final String KEY = "LEVEL_NUMBER";
	
	public void updateLevel(int completedLevel){ 
		int prevLevel;
		
		try{
			String s = PlayN.storage().getItem(KEY);
			prevLevel = Integer.parseInt(s);
		}catch(Exception e){
			prevLevel = 1;
		}
		
		if(completedLevel>prevLevel){
			PlayN.storage().setItem(KEY, ((Integer)completedLevel).toString());
		}
		
	}
	
	public static int getHighestAvalibleLevel(){
		try{
			String s = PlayN.storage().getItem(KEY);
			int prevLevel = Integer.parseInt(s);
			if(prevLevel>=1) return prevLevel;
		}catch(Exception e){}
		return 1;
	}
}
