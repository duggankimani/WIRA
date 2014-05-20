package com.duggan.workflow.test;

import com.duggan.workflow.server.helper.jbpm.VersionManager;

public class MainClassTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		VersionManager.getVersion();
		
//		String format= "Request/{No}/{MM}/{YYYY}";
//		
//		
//		SimpleDateFormat formatter = new SimpleDateFormat("YY");
//		String yy = formatter.format(new Date());
//		
//		formatter = new SimpleDateFormat("yyyy");
//		String yyyy = formatter.format(new Date());
//		
//		formatter = new SimpleDateFormat("MM");
//		String mm = formatter.format(new Date());
//		
//		System.err.println(format.replaceAll("\\{No\\}", "909")
//				.replaceAll("\\{YY\\}",yy)
//				.replaceAll("\\{YYYY\\}",yyyy)
//				.replaceAll("\\{MM\\}",mm));
		
	}
}
