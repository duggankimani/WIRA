package com.duggan.workflow.client.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.duggan.workflow.shared.model.form.Field;

/**
 * Assumptions - All fields have unique names
 * 
 * @author duggan
 *
 */
public class ENV {

	private static Map<String, Long> observableFields= new HashMap<String,Long>(); //Observable Registration
	private static Map<String, Object> values = new HashMap<String,Object>(); //Field Values
	private static Map<String, List<String>> nameToQualifieldNameMap = new HashMap<String, List<String>>();
	
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
		values.clear();
		nameToQualifieldNameMap.clear();
	}

	public static boolean containsObservable(String name){
		
		boolean contained = observableFields.keySet().contains(name);
		//System.err.println("Contains : "+name+" : "+contained);
		return contained;
	}
	
	public static boolean containsObservable(String name, Long parentId) {
		boolean contained = observableFields.keySet().contains(name);
		if(contained){
			observableFields.put(name, parentId);
		}
		return contained;
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
	
	/**
	 * Values are stored using their fully qualified fieldNames
	 * @param qualifiedFieldName
	 * @param value
	 */
	public static void setContext(Field field, Object value){
		
		String fieldName=field.getDocSpecificName();
		String qualifiedFieldName=field.getQualifiedName();
		
		if(!fieldName.equals(qualifiedFieldName)){
			//For detail values - GridRow col Values
			List<String> fields = nameToQualifieldNameMap.get(fieldName);
			if(fields==null){
				fields = new ArrayList<String>();
			}
			if(!fields.contains(qualifiedFieldName)){
				fields.add(qualifiedFieldName);
			}
			
			nameToQualifieldNameMap.put(fieldName, fields);
		}
		
		
		values.put(qualifiedFieldName, value);
	}
	
	public static void removeContext(Field field){
		String fieldName=field.getDocSpecificName();
		nameToQualifieldNameMap.remove(fieldName);
		
		String qualifiedFieldName=field.getQualifiedName();
		values.remove(qualifiedFieldName);
	}
	
	public static List<String> getQualifiedNames(String fieldName){
		return nameToQualifieldNameMap.get(fieldName);
	}

	public static void removeContext(String qualifiedFieldName){
		values.remove(qualifiedFieldName);
	}
	
	static List<String> specialNames = Arrays.asList("current_timestamp","current_date","current_time","UserID");
	
	public static Object getValue(String key) {
		
		if(specialNames.contains(key)){
			//special name
			//.....
			return generate(key);
		}
		
		return values.get(key);
	}

	private static Object generate(String key) {
		
		return null;
	}

	public static boolean isAggregate(String fld) {
		
		return observableFields.get(fld)!=null;
	}

	public static void setContext(String qualifiedName, Object value) {
		
		values.put(qualifiedName, value);
	}

}
