package com.duggan.workflow.client.ui.util;

import java.util.ArrayList;

public class ArrayUtil {

	public static <T> ArrayList<T> asList(T ... values){
		ArrayList<T> array = new ArrayList<T>();
		for(T value: values){
			array.add(value);
		}
		
		return array;
	}
}
