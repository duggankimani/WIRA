package com.duggan.workflow.server.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class KeyValuePair {

	public String name;
	public String value;
	
	public KeyValuePair(){		
	}
	
	public KeyValuePair(String key, Object value){
		this.name= key;
		this.value = value.toString();
	}
}
