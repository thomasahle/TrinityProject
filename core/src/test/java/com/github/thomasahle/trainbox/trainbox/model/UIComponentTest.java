package com.github.thomasahle.trainbox.trainbox.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pythagoras.f.Point;

import com.github.thomasahle.trainbox.trainbox.uimodel.TrainTaker;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UIHorizontalComponent;
import com.github.thomasahle.trainbox.trainbox.uimodel.UITrain;

public class UIComponentTest {
	
	private static final int RUNS = 1000;
	
	private void addTrains(UIComponent track, List<Train> trains) {
		for (Train train : trains) {
			UITrain uitrain = new UITrain(train);
			uitrain.setPosition(new Point(-uitrain.getSize().width, 0));
			track.takeTrain(uitrain);
		}
	}
	
	private List<Train> run(UIComponent track) {
		MemoryTaker taker = new MemoryTaker();
		track.setTrainTaker(taker);
		for (int i = 0; i < RUNS; i++)
			track.update(Float.MAX_VALUE);
		return taker.trains;
	}
	
	private class MemoryTaker implements TrainTaker {
		public List<Train> trains = new ArrayList<Train>();
		@Override
		public void takeTrain(UITrain train) {
			trains.add(train.train());
		}
		@Override
		public float leftBlock() {
			return Float.MAX_VALUE;
		}
	}
	
	// ----------- Tests ------------------------------------------------------
	
	@Test
	public void testSimple() {
		Level level = new Level(
				ComponentFactory.parseTrains("1-2-3-4"),
				ComponentFactory.parseTrains("1-2-3-4"));
		UIHorizontalComponent track = new UIHorizontalComponent(1);
		addTrains(track, level.input);
		assertEquals(run(track), level.goal);
	}
}































