package com.duggan.workflow.client.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.duggan.workflow.shared.model.form.Field;
import com.wira.commons.client.util.ArrayUtil;

/**
 * Assumptions - All fields have unique names
 * 
 * @author duggan
 *
 */
public class ENV{

	//<FieldName, FieldId>
	private static HashMap<String, Long> observableFields= new HashMap<String,Long>(); //Observable Registration
	//<FieldName,Value>
	private static HashMap<String, Object> values = new HashMap<String,Object>(); //Field Values
	//<FieldName,QualifiedName>
	private static HashMap<String, ArrayList<String>> nameToQualifieldNameMap = new HashMap<String, ArrayList<String>>();
	
	public static void registerObservable(String fieldName){
		if(observableFields.keySet().contains(fieldName)){
			return;
		}
		observableFields.put(fieldName,null);
	}
	
	public static void registerObservable(ArrayList<String> fieldNames){
		for(String fieldName: fieldNames){
			registerObservable(fieldName);
		}
	}
	
	public static void clear(){
		observableFields.clear();
		values.clear();
		nameToQualifieldNameMap.clear();
	}

	/**
	 * 
	 * @param name Qualified name of a field, for a gridfield the name will be gridname_fieldname
	 * e.g Particularsgrid_unitPrice otherwise, the name of the field is used
	 * </p>
	 * @return true if fieldName is registered as observable (i.e field is an operand in a formula).
	 * This is necessary to manage the number of events fired while interacting with the form
	 */
	public static boolean containsObservable(String fieldName){
		
		boolean contained = observableFields.keySet().contains(fieldName);
		//System.err.println("Contains : "+name+" : "+contained);
		return contained;
	}
	
	/**
	 * 
	 * @param name
	 * @param parentId  - Grid Id
	 * @return
	 */
	public static boolean containsObservable(String name, Long parentId) {
		boolean contained = observableFields.keySet().contains(name);
		if(contained){
			observableFields.put(name, parentId);
		}
		return contained;
	}

	/**
	 * 
	 * @param fieldName
	 * @param parentId
	 * @return true if ParentId is the parent(grid) of fieldName
	 */
	public static boolean isParent(String fieldName, Long parentId) {
		
		Long id1 = observableFields.get(fieldName);
		if(id1!=null && parentId!=null){
			return id1.equals(parentId);
		}
		
		return false;
	}
	
	public static HashMap<String, Object> getValues(){
		return values;
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
			ArrayList<String> fields = nameToQualifieldNameMap.get(fieldName);
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
	/**
	 * Removes the field and its value from ENV
	 * @param field
	 */
	public static void removeContext(Field field){
		String qualifiedFieldName=field.getQualifiedName();
		values.remove(qualifiedFieldName);
		
		String fieldName=field.getDocSpecificName();
		if(field.isGridColumn()){
			//eg. GRID_particulars_qty_1
			ArrayList<String> qualifiedNames = getQualifiedNames(fieldName);
			if(qualifiedNames!=null) {
				qualifiedNames.remove(qualifiedFieldName);
			}
			
		}else{
			//eg. total
			if(nameToQualifieldNameMap!=null) {
				nameToQualifieldNameMap.remove(fieldName);
			}
			
		}
		
		
	}
	
	public static ArrayList<String> getQualifiedNames(String fieldName){
		return nameToQualifieldNameMap.get(fieldName);
	}

	public static void removeContext(String qualifiedFieldName){
		values.remove(qualifiedFieldName);
	}
	
	static ArrayList<String> specialNames = ArrayUtil.asList("current_timestamp","current_date","current_time","UserID");
	
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
