package com.github.thomasahle.trainbox.trainbox.model;

import java.util.ArrayDeque;
import java.util.List;

public class StartComponent implements Component {
	
	private ArrayDeque<Train> mTrains = new ArrayDeque<Train>(); 
	
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
