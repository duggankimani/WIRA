package com.duggan.workflow.test;

import java.util.ArrayList;
import java.util.List;

public class StrMatches {

	public static void main(String[] args) {
		String txt = "This is {field} test {field2}";
		
		List<String> names = new ArrayList<String>();
		getNames(txt, names, 0);
		
		System.err.println("Names = "+names.size()+" >> "+names);
	}
	
	private static void getNames(String originalStr, List<String> names, int startPos){
		System.err.println("StartPos = "+startPos);
		if(startPos==-1){
			return;
		}
		if(startPos>=originalStr.length()){
			return;
		}
		
		int oIndex = originalStr.indexOf('{',startPos);
		if(oIndex==-1){
			return;
		}
		
		int cIdx = originalStr.indexOf("}", oIndex);
		if(cIdx==-1)
			return;		
		names.add(originalStr.substring(oIndex+1, cIdx));
	
		startPos=cIdx+1;				
		getNames(originalStr, names, startPos);
	}
}
