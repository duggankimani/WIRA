package com.duggan.workflow.test.formbuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LearnJunit {
	int i;
	@Before
	public void Beforeexec(){
		
		System.out.println("Called before execution"+ i);
	}
	
	@Test
	public void Withinexec(){
		System.out.println("Called during execution");
	}
	
	@After
	public void Afterexec(){
		System.out.println("Called after execution");
	}

}
