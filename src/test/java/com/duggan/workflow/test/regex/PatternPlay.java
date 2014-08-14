package com.duggan.workflow.test.regex;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.duggan.workflow.server.export.DocumentHTMLMapper;

public class PatternPlay {

	private static Logger log = Logger.getLogger(PatternPlay.class);
	public static void main(String[] args) throws FileNotFoundException, IOException {
		//runTest();				
		String html =  " This is <!--@>POGridField--> <!--@<POGridField--> my test";
		String gname = new DocumentHTMLMapper().getGridName("<!--@>POGridField-->");
		System.err.println("Name = "+gname);
	}

	private static void runTest() throws FileNotFoundException, IOException {
		List<String> vals=IOUtils.readLines(new FileInputStream("/home/duggan/Downloads/PO (1).html"));
		StringBuffer input = new StringBuffer();
		for(String in:vals){
			input.append(in);
		}
		log.debug(input.toString());
		test2(input.toString());
		//test1(input.toString());
	}

	private static void test2(String detail) {
		Pattern pattern = Pattern.compile("<!--\\s*?@[<>]\\w+?\\b\\s*?-->");
		Matcher matcher = pattern.matcher(detail);
		boolean found=false;
		int start=-1;
		int end=-1;
		while(matcher.find()){
			System.err.format("I found the text" +
                    " \"%s\" starting at " +
                    "index %d and ending at index %d.%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());
			if(start==-1){
				start=matcher.end();
			}else{
				
				end=matcher.start();
				log.debug(detail.substring(start,end));
				start=-1;
			}
			found = true;
        }
        if(!found){
        	System.err.format("No match found.%n");
        }else{
        	
        }
	}

	private static void test1(String detail) {
		
		Pattern pattern = Pattern.compile("@[@#]\\w+?\\b");
		Matcher matcher = pattern.matcher(detail);
		boolean found=false;
		while(matcher.find()){
			System.err.format("I found the text" +
                    " \"%s\" starting at " +
                    "index %d and ending at index %d.%n",
                    matcher.group(),
                    matcher.start(),
                    matcher.end());
			found = true;
        }
        if(!found){
        	System.err.format("No match found.%n");
        }
	}
}
