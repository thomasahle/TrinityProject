package com.github.thomasahle.trainbox.trainbox.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ComponentFactory {
	/**
	 * @param description Maybe something like (dup dup || box)
	 * @return A component to use
	 */
	@SuppressWarnings("serial")
	Map<String, Class<? extends Component>> map =
			new HashMap<String, Class<? extends Component>>() {{
		put("dup", DupComponent.class);
		put("box", BoxComponent.class);
		put("cat", ConcatComponent.class);
	}};
	public static Component parseAlgebraic(String description) {
		// TODO: Create this factory
		return null;
	}
	
	/**
	 * On the form "3 5 4-5"
	 * The left most drives first.
	 */
	public static List<Train> parseTrains(String description) {
		List<Train> trains = new ArrayList<Train>();
		for (String trainDesc : description.split(" +")) {
			Train train = Train.EMPTY;
			for (String cargoDesc : trainDesc.split("-")) {
				train = train.addLast(new Carriage(Integer.parseInt(cargoDesc)));
			}
			trains.add(train);
		}
		return trains;
	}
}
