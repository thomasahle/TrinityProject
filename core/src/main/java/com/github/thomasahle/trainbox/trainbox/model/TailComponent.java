package com.github.thomasahle.trainbox.trainbox.model;

public class TailComponent implements Component{

	private final Component prev;
	
	public TailComponent(Component prev){
		this.prev = prev;
	}
	
	@Override
	public Train pull() {
		
		Train t = prev.pull();
		while(t.length()<2){
			if(t == Train.EMPTY){
				return Train.EMPTY;
			}else{
				t = prev.pull();	
			}
		}
		return t.tail();
	}

}
