package com.duggan.workflow.test;



public class Apples {

	/**
	 * @author Tosh
	 */
	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Fatty fatty=new Fatty();
		Food potpie = new Potpie();
		
		fatty.digest(potpie);
		/* 
		 * Polymorphic Arrays

		Food food[]=new Food[2];
		
		food[0]= new Potpie();
		food[1]= new Tuna();

		for(int i=0;i<2;i++){
			food[i].eat();
		}
		/*
		 * Learn Enums
		for(Tuna people:Tuna.values()){
			System.out.printf("%s\t%s\t%s\n", people,people.getDesc(),people.getYear());
		}
		
		//For a range 
		for(Tuna people:EnumSet.range(Tuna.Julie, Tuna.Mariana))
			System.out.printf("%s\t%s\t%s\n", people,people.getDesc(),people.getYear());
		}
		
		 */
	}
	
}
