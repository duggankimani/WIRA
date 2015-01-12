package com.duggan.workflow.test;

import java.io.Serializable;

import org.junit.Test;

import com.duggan.workflow.shared.model.Comment;

public class TestABC {

	static Integer y;
	@Test
	public void comment(){
	
		String key="caseNo";
		
		key = key.substring(0, 1).toUpperCase()+key.substring(1, key.length());
		
		System.err.println(key);
	}
}
