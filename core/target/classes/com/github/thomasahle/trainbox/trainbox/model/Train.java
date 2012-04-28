package com.github.thomasahle.trainbox.trainbox.model;

/**
 * Immutable object representing a chain of Carriages.
 */
public interface Train {
	
	public int cargo();
	
	/**
	 * The 'next train' is the one that arrives at a component after this one.
	 */
	public Train next();
	
	/**
	 * The empty train
	 */
	public static final Train EMPTY = new NullTrain();
	
	public Train head();
	
	public int length();
	
	public Train addBehind(Train behind);
}