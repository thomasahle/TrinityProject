package com.github.thomasahle.trainbox.trainbox.model;

import java.util.List;

public interface Composite extends Component {
	public List<Component> getChildren();
}
