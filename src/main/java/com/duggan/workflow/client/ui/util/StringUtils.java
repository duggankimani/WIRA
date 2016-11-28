package com.duggan.workflow.client.ui.util;


public class StringUtils {
	
	public static boolean isNullOrEmpty(Object value) {
		if(value==null){
			return true;
		}
			
		String str = value.toString();
		
		return str.trim().isEmpty();
	}
	
	public static String camelCase(String input){
		if(isNullOrEmpty(input)){
			return input;
		}
		
		input = input.replaceAll("\\s", "_");
		//Converter<String, String> converter = CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);
		return input;//converter.convert(input);
	}

	/**
	 * This method returns an String where the ingested value is repeated n times.
	 * @param s Input String
	 * @param n Number of times is repeated
	 * @return new String with the parameter repeated
	 */
	public static String repeat(String s, int n) {
		if (s == null) {
			return null;
		}
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static String toAphanumeric(String name) {
		
		return name.replaceAll("[^a-zA-Z0-9]+", " ").trim();
	}
}
