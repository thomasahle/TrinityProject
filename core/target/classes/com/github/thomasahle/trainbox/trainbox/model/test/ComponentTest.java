package com.github.thomasahle.trainbox.trainbox.model.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.thomasahle.trainbox.trainbox.model.BoxComponent;
import com.github.thomasahle.trainbox.trainbox.model.Component;
import com.github.thomasahle.trainbox.trainbox.model.ComponentFactory;
import com.github.thomasahle.trainbox.trainbox.model.ConcatComponent;
import com.github.thomasahle.trainbox.trainbox.model.FlipComponent;
import com.github.thomasahle.trainbox.trainbox.model.StartComponent;
import com.github.thomasahle.trainbox.trainbox.model.Train;

public class ComponentTest {
	
	private List<Train> pullAll(Component c) {
		List<Train> trains = new ArrayList<Train>();
		while (trains.size() == 0 || trains.get(trains.size()-1).length() > 0) {
			trains.add(c.pull());
		}
		return trains;
	}
	
	@Test
	public void testStart() {
		List<Train> input = ComponentFactory.parseTrains("1 2 3 4");
		StartComponent start = new StartComponent();
		start.addAllLast(input);
		assertEquals(input, pullAll(start));
	}
	
	@Test
	public void testReverse() {
		List<Train> input = ComponentFactory.parseTrains("1 2 3 4");
		List<Train> output = ComponentFactory.parseTrains("1 2 3 4");
		StartComponent start = new StartComponent();
		start.addAllLast(input);
		Component comp = new ConcatComponent(
				new FlipComponent(
				new BoxComponent(
				new FlipComponent(start))));
		assertEquals(output, pullAll(comp));
	}

}































