package com.duggan.workflow.server.rest.model;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class MapEntry {

	/**
	 * Field Name
	 */
	public String name;
	
	public String type;
	
	private String stringValue;
	
	private Boolean booleanValue;
	
	private Long longValue;
	
	private Double doubleValue;
	
	private Date dateValue;
	
	private List<MapEntry> line;
	
	public MapEntry(){		
	}
	
	public MapEntry(String key, Object value){
		this.name= key;
		
		if(value==null)
			return;
		
		if(value instanceof String){
			stringValue = value.toString();
			this.type="STRING";
			
		}else if(value instanceof Boolean){
			booleanValue = (Boolean)value;
			this.type="BOOLEAN";
			
		}else if(value instanceof Long){
			longValue=(Long)value;
			this.type="LONG";
			
		}else if(value instanceof Integer){
			longValue = new Long(value.toString());
			this.type="INTEGER";
			
		}else if(value instanceof Double){
			doubleValue = (Double)value;
			this.type="DOUBLE";
			
		}else if(value instanceof Number){
			doubleValue = ((Number)value).doubleValue();
			this.type="DOUBLE";
			
		}else if(value instanceof Date){
			dateValue = (Date)value;
			this.type="DATE";
			
		}
//		else if(value instanceof Map){
//			
//			Map<String, Object> values = (Map)value;
//			line = new ArrayList<>();
//			for(String keyValue: values.keySet()){
//				line.add(new KeyValuePair(keyValue, values.get(keyValue)));
//			}
//		}
		
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
		if(type==null){
			if(line!=null && !line.isEmpty()){
				return line;
			}
			return null;
		}
		
		switch (type) {
		case "STRING":
			
			return stringValue;				
		case "BOOLEAN":
			
			return booleanValue;

		case "LONG":
					
			return longValue;
					
		case "INTEGER":
			
			return longValue;
			
		case "DOUBLE":
	
			return doubleValue;
		case "DATE":
			
			return dateValue;
		
		}
		
		return null;
	}
	
}
