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
			2, "Be paired",
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
			5, "New component: split",
			"7 6-5 4 3-2-1", "7 6 5 4 3 2 1",
			0,0,1,0,0
		));
		mLevels.add(new Level(
			6, "Re-twist",
			"1 2", "1 2 1 2",
			1,1,1,0,0
		));
		mLevels.add(new Level(
			7, "New component: flip",
			"2 1 4 3", "1 2 3 4",
			0,0,0,1,0
		));
		mLevels.add(new Level(
			8, "A half reverse",
			"1 2 3 4", "3 4 1 2",
			0,1,1,1,0
		));
		mLevels.add(new Level(
			9, "New component: track-split",
			"1 2", "1 2 1",
			1,0,0,0,1
		));
		mLevels.add(new Level(
			10, "But, it's odd?",
			"1", "1 1 1",
			2,0,0,0,1
		));
		mLevels.add(new Level(
			11, "A new train order",
			"3 2 1", "1 2 3",
			0,0,0,1,1
		));
		mLevels.add(new Level(
			12, "Reflection",
			"2 1", "1 2 2 1",
			1,0,0,1,1
		));
		mLevels.add(new Level(
			13, "There is no cow matrix",
			"1-2 3-4", "1-3 2-4",
			0,2,1,0,1,
			0,1,2,0,1
		));
		mLevels.add(new Level(
			14, "Reverse Express",
			"4 3 2 1", "1 2 3 4",
			0,0,0,3,1,
			0,1,1,2,0
		));
		mLevels.add(new Level(
			15, "More, more, more!",
			"1", "1 1 1 1 1 1",
			3,0,0,0,1, // dup dup (dup||id)
			5,0,0,0,3 // dup (dup (dup||id) || dup (dup||id))
		));
		mLevels.add(new Level(
			16, "The small difference",
			"0 1", "0-0-1-1 0-1-0-1",
			3,4,0,0,1
		));
		mLevels.add(new Level(
			17, "Back to front",
			"2 3 1", "1 2 3",
			0,1,1,1,1 // (flip box||) cat
		));
		mLevels.add(new Level(
			18, "Montgomery Reshuffle",
			"1-2 3-4 5-6 7-8", "1-3-5-7 2-4-6-8",
			0,4,1,0,1,
			0,3,2,0,3
		));
		mLevels.add(new Level(
			19, "Reverse Mega-Freight",
			"8 7 6 5 4 3 2 1", "1 2 3 4 5 6 7 8",
			0,2,1,3,0,
			0,0,0,7,3
		));
		mLevels.add(new Level(
			20, "Overtaking",
			"2 3 4 1", "1 2 3 4",
			0,0,0,2,1, // flip (flip||id)
			0,1,1,3,1
		));
		mLevels.add(new Level(
			21, "Mirror Reshuffle",
			"1-2-3-4", "1-4 2-3 3-2 4-1",
			1,2,3,2,1 // dup (cat||cat flip box flip cat) box
		));
		mLevels.add(new Level(
			22, "There are nine",
			"1", "1 1 1 1 1 1 1 1 1",
			4,0,0,0,3, // dup dup dup (((dup||)||)||)
			7,0,0,0,3 // dup (dup (dup(dup||)||dup) || dup dup)
		));
		mLevels.add(new Level(
			23, "Triple Team",
			"1 2 3", "1-2-3",
			0,3,1,0,2 // (box||) cat (box||) box
		));
		mLevels.add(new Level(
			24, "Isolation",
			"1 2 3", "1 2-3",
			0,2,1,2,2 // (box||) flip cat (box||) flip
		));
		mLevels.add(new Level(
			25, "Lame Octopus",
			"7 6 5 4 3 2 1", "1 2 3 4 5 6 7",
			0,1,1,3,2
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
			3,2,1,0,1, // dup (dup || box dup cat) box
			3,8,1,0,4 // "The small difference" then transpose
		));
		mLevels.add(new Level(
			29, "Single cut",
			"1-2-3-4-5-6", "1-2-3 4-5-6",
			0,4,3,1,3
		));
		// Some reserve levels of different difficulty
		/*
		mLevels.add(new Level(
			25, "Rotate Mega-Freight",
			"2 3 4 5 6 7 8 1", "1 2 3 4 5 6 7 8",
			0,0,0,3,2, // flip (flip (flip||id)||id)
			0,3,2,6,2
		));
		mLevels.add(new Level(
			23, "Flip most",
			"1 2 3 4 5 6 7", "1 3 2 5 4 7 6",
			0,6,2,0,6 // ((||box)||) cat (box||(box||)) box (||cat(box||)box) cat
		));
		 * 
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
