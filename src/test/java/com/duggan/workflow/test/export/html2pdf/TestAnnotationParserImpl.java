package com.duggan.workflow.test.export.html2pdf;

import java.util.List;

import org.junit.Test;

import com.duggan.workflow.server.export.AnnotationParserImpl;

public class TestAnnotationParserImpl {

	@Test
	public void parseSQL(){
		
		String sql = "Select * from user where departmentid=@@departmentid and @@userid=1";
		
		List<String> annotations = AnnotationParserImpl.extractAnnotations(sql);
		
		for(String s:annotations){
			System.out.println("Found annotation - "+s);
		}
	}
}
