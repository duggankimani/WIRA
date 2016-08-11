package com.duggan.workflow.server.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.duggan.workflow.shared.model.Value;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DocValues implements Serializable{

	/**
	 * Problem solved, thank you stackoverflow. 
	 * Issue: Unmarshalling List<Intefaces> fails 
	 * 
	 * @XmlAnyElement(lax=true). The lax flag tells the JAXB (JSR-222) implementation 
	 * to match elements to domain objects based their
	 *  @XmlRootElement and @XmlElementDecl annotations. 
	 *  Without it the contents are treated as DOM nodes.
	 */
	@XmlElementWrapper
	@XmlAnyElement(lax=true)
	ArrayList<Value> values = new ArrayList<Value>();
	
	public DocValues() {
	}

	public DocValues(HashMap<String, Value> valuesMap) {
		for (String key : valuesMap.keySet()) {
			Value val = (Value) valuesMap.get(key);
			if (val.getValue() == null) {
				continue;
			}

			val.setKey(key);
			values.add(val);
		}

	}

	public ArrayList<Value> getValues() {
		return values;
	}

	
	public void setValues(ArrayList<Value> values) {
		this.values = values;
	}

	public HashMap<String, Value> getValueMap() {
		HashMap<String, Value> valuesMap = new HashMap<String, Value>();
		
		for(Value v: values){
			valuesMap.put(v.getKey(), v);
		}
		
		return valuesMap;
	}
	
}
