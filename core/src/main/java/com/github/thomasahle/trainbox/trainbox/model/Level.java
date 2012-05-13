package com.github.thomasahle.trainbox.trainbox.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {

	public final int levelNumber;
	public final String title;
	public final List<Train> input, goal;
	
	/**The maximum number of each type of component allowed*/
	public final int dups,chains,unchains,flips,splits;
	
	/**@param levelNumber - number each level starting from zero, so that each level knows its own index*/
	public Level(
		int levelNumber,
		String title,
		List<Train> input,
		List<Train> goal,
		int dups,
		int chains,
		int unchains,
		int flips,
		int splits
	){
		this.levelNumber=levelNumber;
		this.title=title;
		this.input=input;
		this.goal=goal;
		this.dups=dups;
		this.chains=chains;
		this.unchains=unchains;
		this.flips=flips;
		this.splits=splits;
	}

	
	
	private static final List<Level> mLevels = new ArrayList<Level>();
	public static final List<Level> levels = Collections.unmodifiableList(mLevels);
	static{
		/** Here we code all of the levels */
		
		mLevels.add(new Level( 0, "Level1",
				ComponentFactory.parseTrains("1-5-9-13 2-6-10-14 3-7-11-15 4-8-12-16"),
				ComponentFactory.parseTrains("1-2-3-4 5-6-7-8 9-10-11-12 13-14-15-16"),
				9,9,9,9,9
				));

		mLevels.add(new Level( 1, "Level2",
				ComponentFactory.parseTrains("1-2"),
				ComponentFactory.parseTrains("1 2"),
				9,9,9,9,9
				));
	}
	
}
