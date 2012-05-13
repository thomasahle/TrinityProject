package com.github.thomasahle.trainbox.trainbox.uimodel;

import java.rmi.activation.UnknownObjectException;
import java.util.ArrayList;
import java.util.List;

import com.github.thomasahle.trainbox.trainbox.model.Component;

public final class UIComponentFactory {

	public enum UIToken {
		BOX("box"), CAT("cat"), DUP("dup"), ID("id"), FLIP("flip"), MERG("||");
		final String repr;

		UIToken(String repr) {
			this.repr = repr;
		}
	};
	
	public static UIComponent fromTok(UIToken tok){
		System.out.println(tok);
		switch (tok) {
		case BOX:
			return new UIJoinComponent(80);
		case DUP:
			return new UIDupComponent(80);
		case CAT:
			return new UISeparateComponent(80);
		case FLIP:
			return new UIFlipComponent(80);
		case MERG:
			return new UISplitMergeComponent(new UIHorizontalComponent(80), new UIHorizontalComponent(80));
		default:
			return null;
		}
		
	}

	public static UIComponent fromModel(Component component) {
		// TODO: Create this factory. Perhaps using the visitor pattern.
		return null;
	}
	
	/**
	 * @param cargos The cargos in order such that cargos[0] will be the first in line.
	 * @return trains such that trains[0] is the first in line. The trains are all singleton.
	 */
	public static List<UITrain> trainsFromCargos(List<Integer> cargos) {
		List<UITrain> trains = new ArrayList<UITrain>();
		for (Integer cargo : cargos) {
			trains.add(new UITrain(cargo));
		}
		return trains;
	}
}
