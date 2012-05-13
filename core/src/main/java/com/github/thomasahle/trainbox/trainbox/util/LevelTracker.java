package com.github.thomasahle.trainbox.trainbox.util;

import playn.core.PlayN;

public class LevelTracker {
	
	private static final String KEY = "LEVEL_NUMBER";
	public static final int STARTLEVEL = 0;
	
	/**This method should called every time a new level is started.
	 * @param levelStarted  -  the level that has just been started (we assume that the first level has number STARTLEVEL=0)
	 * */
	public static void updateLevel(int levelStarted){ 
		int prevLevel;
		
		try{
			String s = PlayN.storage().getItem(KEY);
			prevLevel = Integer.parseInt(s);
		}catch(Exception e){
			prevLevel = STARTLEVEL;
		}
		
		if(levelStarted>prevLevel){
			PlayN.storage().removeItem(KEY);
			PlayN.storage().setItem(KEY, ""+levelStarted);
		}
		
	}
	
	/**Sets i as the highest available level.
	 * This does not need to be called at the start*/
	public static void setCurrentProgress(int i){
		PlayN.storage().setItem(KEY, ""+i);
	}
	
	/**Returns the current progress*/
	public static int getCurrentProgress(){
		try{
			String s = PlayN.storage().getItem(KEY);
			int prevLevel = Integer.parseInt(s);
			if(prevLevel>=STARTLEVEL) return prevLevel;
		}catch(Exception e){}
		return 1;
	}
}
