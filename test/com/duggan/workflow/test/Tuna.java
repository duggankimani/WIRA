package com.duggan.workflow.test;


public class Tuna extends Food{
	private int hour;
	private int minute;
	private int seconds;
	
	public void setTime(int h, int m, int s){
		hour =((h>=0 && h<24)?h:0);
		minute = ((m>=0 && m<=60)?m:0);
	}
	
	public void eat(){
		System.out.println("This tuna is sweet!");
	}
	
}
/*
 * --- Learn Enums ----- 
public enum Tuna {
	
	Maryann("nice", "24"),
	Julie("wonderful","10"),
	Mariana("wow","15");
	
	public String desc;
	public String year;
	
	Tuna(String description, String yearOfdate){
		desc= description;
		year=yearOfdate;
	}
	
	
	public String getDesc() {
		return desc;
	}
	
	public String getYear() {
		return year;
	}*/
