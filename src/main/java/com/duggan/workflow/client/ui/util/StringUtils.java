package com.duggan.workflow.client.ui.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;

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
		Converter<String, String> converter = CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);
		return converter.convert(input);
	}

}
