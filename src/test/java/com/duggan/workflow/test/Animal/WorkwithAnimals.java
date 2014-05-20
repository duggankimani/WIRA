package com.duggan.workflow.test.Animal;

public class WorkwithAnimals {
	
	public static void main(String[] args) {
		Animal doggy= new Dog();
		Animal birdy= new Bird();
		
		//((Dog)doggy).digHole();
		
		System.out.println("Dog: "+ doggy.DoesItFly());
		System.out.println("Bird: "+ birdy.DoesItFly());
	}
}
