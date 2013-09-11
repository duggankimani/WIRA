package com.duggan.workflow.test;

public class FinalTest {
	private int sum;
	private final int NUMBER;
	
	/*Learning Static Variables*/
	private String firstname;
	private String secondname;
	private static int members;
	
	public FinalTest(String first, String second){
		this.NUMBER=0;
		this.firstname= first;
		this.secondname=second;
		members++;
		
		System.out.printf("Constructor for %s: Member %d\n", firstname, members);
	}
	public FinalTest(int x) {
		this.NUMBER=x;
	}
	
	public void sum(){
		sum+=NUMBER;
	}
	
	
	public static int getMembers() {
		return members;
	}
	
	public String toString(){
		return String.format("Sum = %s \n", sum);
	}
}
