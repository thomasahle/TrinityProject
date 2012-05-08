package com.github.thomasahle.trainbox.trainbox.model;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class StartComponent implements Component {
	
	private LinkedList<Train> mTrains;
	
	public StartComponent() {
		mTrains = new LinkedList<Train>();
	}
	
	public StartComponent(List<Train> trains) {
		mTrains = new LinkedList<Train>(trains);
	}
	
	public void addAllLast(List<Train> trains) {
		mTrains.addAll(trains);
	}
	
	public void addTrainLast(Train train) {
		mTrains.addLast(train);
	}
	
	public void addTrainFirst(Train train) {
		mTrains.addFirst(train);
	}
	
	@Override
	public Train pull() {
		if (mTrains.isEmpty())
			return Train.EMPTY;
		return mTrains.poll();
	}
}
