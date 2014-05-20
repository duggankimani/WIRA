package com.duggan.workflow.test.Animal;

public class Dog extends Animal {
	
	public void digHole(){
		System.out.println("Dog dag a hole");
	}
	
	public Dog(){
		super();
		setSound("bark");
		
		flyingType=new CantFly();
	}
}
