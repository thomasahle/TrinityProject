package com.github.thomasahle.trainbox.trainbox.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {

	public final int levelNumber;
	public final String title;
	public final List<Train> input, goal;
	
	/**The ideal number of each type of component - solutions with in this should get maximum points*/
	public final int dupsBest,chainsBest,unchainsBest,flipsBest,splitsBest;

	/**Solutions with in this should get some points. Solutions out-side this should still be accepted
	 * WARNING: Some of these stats may be even tighter (i.e. less) than the "Best" stats.
	 * */
	public final int dupsGood,chainsGood,unchainsGood,flipsGood,splitsGood;

	
	/**@param levelNumber - number each level starting from zero, so that each level knows its own index*/
	public Level(
		int levelNumber,
		String title,
		String input,
		String goal,
		int dupsBest,
		int chainsBest,
		int unchainsBest,
		int flipsBest,
		int splitsBest,
		int dupsGood,
		int chainsGood,
		int unchainsGood,
		int flipsGood,
		int splitsGood

	){
		this.levelNumber=levelNumber;
		this.title=title;
		this.input=ComponentFactory.parseTrains(input);
		this.goal=ComponentFactory.parseTrains(goal);
		this.dupsBest=dupsBest;
		this.chainsBest=chainsBest;
		this.unchainsBest=unchainsBest;
		this.flipsBest=flipsBest;
		this.splitsBest=splitsBest;
		this.dupsGood=dupsGood;
		this.chainsGood=chainsGood;
		this.unchainsGood=unchainsGood;
		this.flipsGood=flipsGood;
		this.splitsGood=splitsGood;
	}
	
	public Level(
		int levelNumber,
		String title,
		String input,
		String goal,
		int dupsBest,
		int chainsBest,
		int unchainsBest,
		int flipsBest,
		int splitsBest
	){
		this.levelNumber=levelNumber;
		this.title=title;
		this.input=ComponentFactory.parseTrains(input);
		this.goal=ComponentFactory.parseTrains(goal);
		this.dupsBest=dupsBest;
		this.chainsBest=chainsBest;
		this.unchainsBest=unchainsBest;
		this.flipsBest=flipsBest;
		this.splitsBest=splitsBest;
		this.dupsGood=dupsBest;
		this.chainsGood=chainsBest;
		this.unchainsGood=unchainsBest;
		this.flipsGood=flipsBest;
		this.splitsGood=splitsBest;
	}

	
	
	private static final List<Level> mLevels = new ArrayList<Level>();
	public static final List<Level> levels = Collections.unmodifiableList(mLevels);
	static {
		/** Here we code all of the levels */
		mLevels.add(new Level(
			0, "Let's go!",
			"1 2 3", "1 2 3",
			0,0,0,0,0
		));
		mLevels.add(new Level(
			1, "Seeing double",
			"1 2-3", "1 1 2-3 2-3",
			1,0,0,0,0
		));
		mLevels.add(new Level(
			2, "Be pre-paired",
			"1 1 1 2", "1-1 1-2",
			0,1,0,0,0
		));
		mLevels.add(new Level(
			3, "Twins", // what do they call it on school trips when everyone partners up, that could be the title here too.
			"1 2", "1-2 1-2",
			1,1,0,0,0
		));
		mLevels.add(new Level(
			4, "Twisted Train Builder",
			"1 2", "1-1-1-1 2-2-2-2",
			2,2,0,0,0
		));
		mLevels.add(new Level(
			5, "Break it up",
			"7 6-5 4 3-2-1", "7 6 5 4 3 2 1",
			0,0,1,0,0
		));
		mLevels.add(new Level(
			6, "Re-twist",
			"1 2", "1 2 1 2",
			1,1,1,0,0
		));
		mLevels.add(new Level(
			7, "Warm-up (flip)",
			"2 1 4 3", "1 2 3 4",
			0,0,0,1,0
		));
		mLevels.add(new Level(
			8, "Even one out",
			"1 2", "1 2 1",
			1,0,0,0,1
		));
		mLevels.add(new Level(
			9, "But, it's odd? (track-split)",
			"1", "1 1 1",
			2,0,0,0,1
		));
		mLevels.add(new Level(
			10, "Flip or mirror",
			"2 1", "1 2 2 1",
			1,0,0,1,1
		));
		mLevels.add(new Level(
			11, "Reverse Express",
			"4 3 2 1", "1 2 3 4",
			0,0,0,3,1,
			0,1,1,2,0
		));
		mLevels.add(new Level(
			12, "Same same, but smaller?",
			"3 2 1", "1 2 3",
			0,0,0,1,1
		));
		mLevels.add(new Level(
			13, "There is no cow matrix",
			"1-2 3-4", "1-3 2-4",
			0,2,1,0,1,
			0,1,2,0,1
		));
		mLevels.add(new Level(
			14, "Reverse Mega-Freight",
			"8 7 6 5 4 3 2 1", "1 2 3 4 5 6 7 8",
			0,2,1,3,0,
			0,0,0,7,3
		));
		mLevels.add(new Level(
			15, "The small difference",
			"0 1", "0-0-1-1 0-1-0-1",
			3,4,0,0,1
		));
		mLevels.add(new Level(
			16, "Not just random",
			"1 2 3 4 5 6 7 8", "1 5 2 6 3 7 4 8",
			0,2,2,0,1
		));
		mLevels.add(new Level(
			17, "Overtaking",
			"2 3 4 1", "1 2 3 4",
			0,0,0,2,1, // flip (flip||id)
			0,1,1,3,1
		));
		mLevels.add(new Level(
			18, "Montgomery Reshuffle",
			"1-2 3-4 5-6 7-8", "1-3-5-7 2-4-6-8",
			0,4,1,0,1,
			0,3,2,0,3
		));
		mLevels.add(new Level(
			19, "Bigger and Better Builder",
			"1", "1 1 1 1 1",
			3,0,0,0,2, // dup dup ((dup||id)||id)
			3,1,1,0,1
		));
		mLevels.add(new Level(
			20, "Mirror Reshuffle",
			"1-2-3-4", "1-4 2-3 3-2 4-1",
			1,2,3,2,1 // dup (cat||cat flip box flip cat) box
		));
		mLevels.add(new Level(
			21, "There are seven",
			"1", "1 1 1 1 1 1 1",
			4,0,0,0,2, // dup dup (dup||(dup||id))
			5,0,0,0,2
		));
		mLevels.add(new Level(
			22, "Rotate Small-Train",
			"3 2 1", "1 3 2",
			0,1,1,1,1
		));
		mLevels.add(new Level(
			23, "Triple Team",
			"3 2 1", "3-2-1",
			0,3,1,0,2
		));
		mLevels.add(new Level(
			24, "Lame Octopus",
			"7 6 5 4 3 2 1", "1 2 3 4 5 6 7",
			0,1,1,3,2
		));
		mLevels.add(new Level(
			25, "Rotate Mega-Freight",
			"2 3 4 5 6 7 8 1", "1 2 3 4 5 6 7 8",
			0,0,0,3,2, // flip (flip (flip||id)||id)
			0,3,2,6,2
		));
		mLevels.add(new Level(
			26, "Mafia Gang",
			"5 4 3 2 1", "5-4-3-2-1",
			0,6,3,0,6
		));
		mLevels.add(new Level(
			27, "Permutations",
			"0 0 0 1", "0-0-0-0 1-0-0-0 0-1-0-0 0-0-1-0 0-0-0-1",
			3,5,1,0,2
		));
		mLevels.add(new Level(
			28, "Zero to Three",
			"0 1", "0-0 0-1 1-0 1-1",
			3,8,1,0,4
		));
		mLevels.add(new Level(
			29, "Single cut",
			"1-2-3-4-5-6", "1-2-3 4-5-6",
			0,4,3,1,3
		));
		// The following might not be solvable
		/*
		 * mLevels.add(new Level(
			29, "Autsch! Meine Schwänze!",
			"1-2-3-4", "1 1-2 1-2-3-4",
			10,8,10,10,10
		));
		mLevels.add(new Level(
			30, "Call me with the solution",
			"1-4-7 2-5-8 3-6-9", "1-2-3 4-5-6 7-8-9",
			10,8,10,10,10
		));*/
		/*
		 * mLevels.add(new Level(
			30, "Thomas's testing",
			"1-2-3-4 1-2-3-4", "1 1 2 2 3 3 4 4",
			0,4,3,1,3
		));
		 */
		for (int i = 0; i < mLevels.size(); i++)
			assert i == mLevels.get(i).levelNumber;
	}
	
}
