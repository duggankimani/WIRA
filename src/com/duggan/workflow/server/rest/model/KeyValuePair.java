package com.duggan.workflow.server.rest.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

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
	
	public KeyValuePair(){		
	}
	
	public KeyValuePair(String key, Object value){
		this.name= key;
		
		if(value==null)
			return;
		
		if(value instanceof String){
			stringValue = value.toString();
			value=stringValue;
			
		}else if(value instanceof Boolean){
			booleanValue = (Boolean)value;
			value=booleanValue;
			
		}else if(value instanceof Long){
			longValue=(Long)value;
			value=longValue;
			
		}else if(value instanceof Integer){
			longValue = new Long(value.toString());
			value=longValue;
			
		}else if(value instanceof Double){
			doubleValue = (Double)value;
			value=doubleValue;
			
		}else if(value instanceof Number){
			doubleValue = ((Number)value).doubleValue();
			value=doubleValue;
			
		}else if(value instanceof Date){
			dateValue = (Date)value;
			value=dateValue;
			
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
	
}
