package com.github.thomasahle.trainbox.trainbox.model;

public class Train {
	private int mCargo;
	private Train mNext;
	
	private Train() {}
	public Train(int cargo, Train next) {
		mCargo = cargo;
		mNext = next;
	}
	
	public int getCargo() {
		return mCargo;
	}
	/**
	 * The 'next train' is the one that arrives at a component after this one.
	 */
	public Train getNext() {
		return mNext;
	}
	
	public static final Train NULL = new NullCarraige();
	private static class NullCarraige extends Train {
		public int getCargo() {
			return 0;
		}
		public Train getNext() {
			return NULL;
		}
	}
}
