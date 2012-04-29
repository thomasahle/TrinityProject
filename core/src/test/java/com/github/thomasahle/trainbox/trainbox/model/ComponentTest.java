package com.github.thomasahle.trainbox.trainbox.model;

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
import com.github.thomasahle.trainbox.trainbox.model.MergeComponent;
import com.github.thomasahle.trainbox.trainbox.model.SplitComponents;
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
	
	@Test
	public void testMattStyleReverse() {
		List<Train> input = ComponentFactory.parseTrains("1 2 3 4");
		List<Train> output = ComponentFactory.parseTrains("4 3 2 1");
		StartComponent start = new StartComponent();
		start.addAllLast(input);
		
		SplitComponents split1 = new SplitComponents(start);
		SplitComponents split2 = new SplitComponents(split1.fst);
		SplitComponents split3 = new SplitComponents(split1.snd);
		Component comp = new MergeComponent(
				new MergeComponent(split3.snd, split3.fst),
				new MergeComponent(split2.snd, split2.fst));
		assertEquals(output, pullAll(comp));
	}
	
	@Test
	public void testTranspose1() {
		List<Train> input = ComponentFactory.parseTrains("1-2 3-4");
		List<Train> output = ComponentFactory.parseTrains("1-3 2-4");
		StartComponent start = new StartComponent();
		start.addAllLast(input);
		
		SplitComponents prevs = new SplitComponents(
				new ConcatComponent(start));
		Component comp = new MergeComponent(
				new BoxComponent(prevs.fst),
				new BoxComponent(prevs.snd));
		assertEquals(output, pullAll(comp));
	}
	
	@Test
	public void testTranspose2() {
		List<Train> input = ComponentFactory.parseTrains("1-2 3-4");
		List<Train> output = ComponentFactory.parseTrains("1-3 2-4");
		StartComponent start = new StartComponent();
		start.addAllLast(input);
		
		SplitComponents prevs = new SplitComponents(start);
		Component comp = new BoxComponent(
				new MergeComponent(
						new ConcatComponent(prevs.fst),
						new ConcatComponent(prevs.snd)));
		assertEquals(output, pullAll(comp));
	}
	
	@Test
	public void testTransposeBig() {
		List<Train> input = ComponentFactory.parseTrains("1-2-3-4 5-6-7-8 9-10-11-12 13-14-15-16");
		List<Train> output = ComponentFactory.parseTrains("1-5-9-13 2-6-10-14 3-7-11-15 4-8-12-16");
		StartComponent start = new StartComponent();
		start.addAllLast(input);
		
		SplitComponents split1 = new SplitComponents(new ConcatComponent(start));
		SplitComponents split2 = new SplitComponents(split1.fst);
		SplitComponents split3 = new SplitComponents(split1.snd);
		Component comp = new MergeComponent(
				new MergeComponent(
						new BoxComponent(new BoxComponent(split2.fst)),
						new BoxComponent(new BoxComponent(split2.snd))),
				new MergeComponent(
						new BoxComponent(new BoxComponent(split3.fst)),
						new BoxComponent(new BoxComponent(split3.snd))));
		assertEquals(output, pullAll(comp));
	}
}































