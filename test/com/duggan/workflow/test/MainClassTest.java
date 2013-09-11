package com.duggan.workflow.test;

public class MainClassTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FinalTest testing1 = new FinalTest("Tom", "Kimani");
		FinalTest testing2 = new FinalTest("Duggan", "Njoroge");
		FinalTest testing3 = new FinalTest("Paul", "wakana");
		
		
		System.out.printf("%s",FinalTest.getMembers());
		
		/*for(int i=0; i<=5;i++){
			testing1.sum();
			System.out.printf("%s",testing1);
		}*/
		
	}
}
