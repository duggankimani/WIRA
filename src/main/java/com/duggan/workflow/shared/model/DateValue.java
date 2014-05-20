package com.duggan.workflow.shared.model;

import java.util.Date;

public class DateValue implements Value{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date value;
	private String key;
	private Long id;
	
	public DateValue(){	
	}
	
	public DateValue(Date date){
		this.value = date;
	}
	
	public DateValue(Long id, String key, Date value){
		this.id=id;
		this.key=key;
		this.value=value;
	}
	
	public Date getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = (Date)value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public DataType getDataType() {
		return DataType.DATE;
	}
	
	public DateValue clone(boolean fullClone){
		Long identify=null;
		if(fullClone){
			identify = id;
		}
		DateValue dvalue = new DateValue(identify, key, value);
		return dvalue;
	}
	
	@Override
	public String toString() {
		return key+":"+value;
	}
}