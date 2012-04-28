package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.thomasahle.trainbox.trainbox.model.Carriage;
import com.github.thomasahle.trainbox.trainbox.model.Component;
import com.github.thomasahle.trainbox.trainbox.model.Train;

public final class UIComponentFactory {
	public static UIComponent fromModel(Component component) {
		// TODO: Create this factory. Perhaps using the visitor pattern.
		return null;
	}
	
	/**
	 * @return A simple demo track consisting of first a IdentityComponent to hold the initial trains,
	 * 		   And then a Shed where they run in.
	 * 		   Idea: To prevent the trains from running off immidiately, we could add a 'block' component
	 * 				 in the middle, which would be deactivated by the 'play' button.
	 */
	public static UIComponent demo() {
		List<UITrain> trains = trainsFromCargos(Arrays.asList(1, 2, 3, 4));
		UIComponent track = new UIIdentityComponent(
				new UIShedComponent(
						Arrays.asList(1, 2, 3, 4)));
		for (UITrain train : trains)
			track.enterTrain(train);
		return track;
	}
	
	/**
	 * @param cargos The cargos in order such that cargos[0] will be the first in line.
	 * @return trains such that trains[0] is the first in line. The trains are all singleton.
	 */
	public static List<UITrain> trainsFromCargos(List<Integer> cargos) {
		List<UITrain> trains = new ArrayList<UITrain>();
		for (Integer cargo : cargos) {
			trains.add(new UITrain(new Carriage(cargo, Train.EMPTY)));
		}
		return trains;
	}
}
