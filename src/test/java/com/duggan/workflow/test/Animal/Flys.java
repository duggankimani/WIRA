package com.duggan.workflow.test.Animal;

public interface Flys {
 	String Fly();
}

class ItFly implements Flys{
	public String Fly() {
		return "I am Flying";
	}
}

class CantFly implements Flys{
	public String Fly() {
		return "Cant Fly";
	}
	
}

