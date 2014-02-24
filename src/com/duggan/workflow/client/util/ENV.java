package com.duggan.workflow.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Assumptions - All fields have unique names
 * 
 * @author duggan
 *
 */
public class ENV {

	private static Map<String, Long> observableFields= new HashMap<String,Long>();
	
	public static void registerObservable(String fieldName){
		if(observableFields.keySet().contains(fieldName)){
			return;
		}
		observableFields.put(fieldName,null);
	}
	
	public static void registerObservable(List<String> fieldNames){
		for(String fieldName: fieldNames){
			registerObservable(fieldName);
		}
	}
	
	public static void clear(){
		observableFields.clear();
	}

	public static boolean containsObservable(String name, Long fieldId) {
		boolean contained = observableFields.keySet().contains(name);
		if(contained){
			observableFields.put(name, fieldId);
		}
		return contained;
	}

	public static boolean isSameParent(String field1, String field2) {
		
		Long id1 =observableFields.get(field1);
		Long id2 = observableFields.get(field2);
		
		if(id1!=null && id2!=null){
			return id1.equals(id2);
		}
		return false;
	}
	
	public static boolean isParent(String fieldName, Long parentId) {
		
		Long id1 = observableFields.get(fieldName);
		if(id1!=null && parentId!=null){
			return id1.equals(parentId);
		}
		
		return false;
	}
	
	public static Map<String, Long> getValues(){
		return observableFields;
	}

}
