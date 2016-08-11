package com.duggan.workflow.shared.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder={"key","value"})
@XmlAccessorType(XmlAccessType.FIELD)
public class IntValue implements Value{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer value;
	private String key;
	@XmlTransient
	private Long id;
	
	public IntValue() {
	}
	
	public IntValue(Integer value){
		this();
		this.value = value;
	}
	
	public IntValue(Long id, String key, Integer value){
		this.id=id;
		this.key=key;
		this.value=value;
	}
	
	@Override
	public void setValue(Object value) {
		this.value = (Integer)value;
	}

	@Override
	public Object getValue() {
		
		return value;
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
		return DataType.INTEGER;
	}

	public IntValue clone(boolean fullClone){
		Long identify=null;
		if(fullClone){
			identify = id;
		}
		IntValue ivalue = new IntValue(identify, key, value);
		return ivalue;
	}
	
	@Override
	public String toString() {
		return key+":"+value;
	}
}
