package com.github.thomasahle.trainbox.trainbox.model;

import java.util.LinkedList;
import java.util.Queue;


public class SplitComponents {
	
	private Component prev, next;
	
	public final Component fst;
	public final Component snd;
	
	/**
	 * @param fst The 'top' component
	 * @param snd The 'bottom' component
	 */
	public SplitComponents(Component prev) {
		this.prev = prev;
		next = fst = new Hook();
		snd = new Hook();
		((Hook)fst).other = (Hook)snd;
		((Hook)snd).other = (Hook)fst;
	}
	
	private class Hook implements Component {
		protected Queue<Train> buffer = new LinkedList<Train>();
		protected Hook other;
		@Override
		public Train pull() {
			if (!buffer.isEmpty())
				return buffer.poll();
			if (this == next) {
				next = other;
				return prev.pull();
			}
			Train t = prev.pull();
			if (t.length() != 0) {
				other.buffer.add(t);
				return prev.pull();
			}
			return Train.EMPTY;
		}
	}
}
