package com.github.thomasahle.trainbox.trainbox.model.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.thomasahle.trainbox.trainbox.model.BoxComponent;
import com.github.thomasahle.trainbox.trainbox.model.Carriage;
import com.github.thomasahle.trainbox.trainbox.model.Component;
import com.github.thomasahle.trainbox.trainbox.model.ComponentFactory;
import com.github.thomasahle.trainbox.trainbox.model.ConcatComponent;
import com.github.thomasahle.trainbox.trainbox.model.FlipComponent;
import com.github.thomasahle.trainbox.trainbox.model.StartComponent;
import com.github.thomasahle.trainbox.trainbox.model.Train;

public class ComponentTest {
	
	private List<Train> pullAll(Component c) {
		List<Train> trains = new ArrayList<Train>();
		do {
			trains.add(c.pull());
		} while (trains.get(trains.size()-1).length() > 0);
		trains.remove(trains.size()-1);
		return trains;
	}
	
	@Test
	public void testSimple() {
	}
	
	@Test
	public void testStart() {
		StartComponent start = new StartComponent();
		start.addTrainLast(new Carriage(1));
		assertEquals(1, start.pull().cargo());
		
		List<Train> input = ComponentFactory.parseTrains("1 2 3 4");
		start = new StartComponent();
		start.addAllLast(input);
		assertEquals(input, pullAll(start));
	}
	
	@Test
	public void testFlip() {
		List<Train> input = ComponentFactory.parseTrains("1 2 3 4");
		List<Train> output = ComponentFactory.parseTrains("2 1 4 3");
		StartComponent start = new StartComponent();
		start.addAllLast(input);
		Component comp = new FlipComponent(start);
		assertEquals(output, pullAll(comp));
	}
	
	@Test
	public void testBox() {
		List<Train> input = ComponentFactory.parseTrains("1 2 3 4");
		List<Train> output = ComponentFactory.parseTrains("1-2 3-4");
		StartComponent start = new StartComponent();
		start.addAllLast(input);
		Component comp = new BoxComponent(start);
		assertEquals(output, pullAll(comp));
	}
	
	@Test
	public void testConcat() {
		List<Train> input = ComponentFactory.parseTrains("1-2 3-4");
		List<Train> output = ComponentFactory.parseTrains("1 2 3 4");
		StartComponent start = new StartComponent();
		start.addAllLast(input);
		Component comp = new ConcatComponent(start);
		assertEquals(output, pullAll(comp));
	}
	
	@Test
	public void testReverse() {
		List<Train> input = ComponentFactory.parseTrains("1 2 3 4");
		List<Train> output = ComponentFactory.parseTrains("4 3 2 1");
		StartComponent start = new StartComponent();
		start.addAllLast(input);
		Component comp = new ConcatComponent(
				new FlipComponent(
				new BoxComponent(
				new FlipComponent(start))));
		assertEquals(output, pullAll(comp));
	}

}































