package com.duggan.workflow.test.Animal;

public class Bird extends Animal {
	
	public Bird(){
		super();
		setSound("Tweety");
		
		flyingType=new ItFly();
	}
}
