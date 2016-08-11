package com.duggan.workflow.shared.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder={"key","value"})
@XmlAccessorType(XmlAccessType.FIELD)
public class DoubleValue implements Value {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Double value;
	private String key;
	@XmlTransient
	private Long id;
	
	public DoubleValue(){
	}

	public DoubleValue(Double val){
		value=val;
	}
	
	public DoubleValue(Long id, String key, Double value){
		this.id=id;
		this.key=key;
		this.value=value;
	}
	
	public Double getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = (Double)value;
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
		
		return DataType.DOUBLE;
	}
	
	public DoubleValue clone(boolean fullClone){
		
		Long identify=null;
		if(fullClone){
			identify = id;
		}
		DoubleValue dvalue = new DoubleValue(identify, key, value);
		return dvalue;
	}
	
	@Override
	public String toString() {
		return key+":"+value;
	}
}
