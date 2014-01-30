package com.duggan.workflow.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainClassTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String format= "Request/{No}/{MM}/{YYYY}";
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("YY");
		String yy = formatter.format(new Date());
		
		formatter = new SimpleDateFormat("yyyy");
		String yyyy = formatter.format(new Date());
		
		formatter = new SimpleDateFormat("MM");
		String mm = formatter.format(new Date());
		
		System.err.println(format.replaceAll("\\{No\\}", "909")
				.replaceAll("\\{YY\\}",yy)
				.replaceAll("\\{YYYY\\}",yyyy)
				.replaceAll("\\{MM\\}",mm));
		
	}
}
