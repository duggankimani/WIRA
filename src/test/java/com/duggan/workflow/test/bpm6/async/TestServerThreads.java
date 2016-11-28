package com.duggan.workflow.test.bpm6.async;

import org.junit.Test;

import com.duggan.workflow.test.bpm6.AbstractBPM6Test;

public class TestServerThreads extends AbstractBPM6Test{

	@Test
	public void init(){
		try {
			Thread.sleep(30000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
