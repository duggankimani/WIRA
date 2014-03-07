package com.duggan.workflow.server.rest.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class KeyValuePair {

	/**
	 * Field Name
	 */
	public String name;
	
	public Object value;
	
	private String stringValue;
	
	private Boolean booleanValue;
	
	private Long longValue;
	
	private Double doubleValue;
	
	private Date dateValue;
	
	private List<KeyValuePair> line;
	
	public KeyValuePair(){		
	}
	
	public KeyValuePair(String key, Object value){
		this.name= key;
		
		if(value==null)
			return;
		
		if(value instanceof String){
			stringValue = value.toString();
			this.value=stringValue;
			
		}else if(value instanceof Boolean){
			booleanValue = (Boolean)value;
			this.value=booleanValue;
			
		}else if(value instanceof Long){
			longValue=(Long)value;
			this.value=longValue;
			
		}else if(value instanceof Integer){
			longValue = new Long(value.toString());
			this.value=longValue;
			
		}else if(value instanceof Double){
			doubleValue = (Double)value;
			this.value=doubleValue;
			
		}else if(value instanceof Number){
			doubleValue = ((Number)value).doubleValue();
			this.value=doubleValue;
			
		}else if(value instanceof Date){
			dateValue = (Date)value;
			this.value=dateValue;
			
		}else if(value instanceof Map){
			
			Map<String, Object> values = (Map)value;
			line = new ArrayList<>();
			for(String keyValue: values.keySet()){
				line.add(new KeyValuePair(keyValue, values.get(keyValue)));
			}
		}
		
	}

	public String getName() {
		return name;
	}

	public String getStringValue() {
		return stringValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public Long getLongValue() {
		return longValue;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public Object getValue() {
		if(line!=null && !line.isEmpty()){
			return line;
		}
		
		return value;
	}
	
}
