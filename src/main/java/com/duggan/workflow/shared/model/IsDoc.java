package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlTransient;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.wira.commons.shared.models.SerializableObj;

public class IsDoc extends SerializableObj implements Serializable, IsSerializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	protected HashMap<String, Value> values = new HashMap<String, Value>();

	public IsDoc() {
	}
	
	public Object get(String key) {
		Value val = values.get(key);
		if (val == null) {
			return null;
		}

		return val.getValue();
	}
	
	public HashMap<String, Value> getValues() {
		return values;
	}

	public HashMap<String, Object> toObjectMap() {
		HashMap<String, Object> conv = new HashMap<String, Object>();
		for (String key : getValues().keySet()) {
			conv.put(key, get(key));
		}
		return conv;
	}
	
	public void setValues(HashMap<String, Value> values) {
		this.values = values;
	}

	public void setValue(String name, Value value) {
		if (value != null) {
			value.setKey(name);
		}

		if (name.equals("subject")) {
			// backward compatibility - Changing subject to-> CaseNo
			setValue("caseNo", value.clone(false));
		}

		if (values.get(name) != null && value != null) {
			// Duggan 15/09/2015- Added this to support Field Triggers that may
			// update a field
			// value with the previous value's id - This update causes
			// duplication of a field value in
			// the db

			//Duggan 12/08/2016 - The above problem is no longer an issue. Document storage has now been upgraded to postgres jsonb format
			//A new issue has arisen due to json storage, which uses duck typing to determine a number class to use i.e Integer, double, long etc
			//This means the frontend might have a double field (DoubleValue) mapped (incorrectly) to an integer value (IntValue) on the server side.
			//Overwriting the value in the document is an easier option to storing types being written
//			Value v = values.get(name);
//			v.setValue(value.getValue());
			values.put(name, value);
		} else {
			values.put(name, value);
		}

	}

	public void _s(String name, Date value) {
		setValue(name, new DateValue(value));
	}

	public void _s(String name, String value) {
		setValue(name, new StringValue(value));
	}

	public void _s(String name, Double value) {
		setValue(name, new DoubleValue(value));
	}

	public void _s(String name, Integer value) {
		setValue(name, new IntValue(value));
	}

	public void _s(String name, Long value) {
		setValue(name, new LongValue(value));
	}

	public void _s(String name, Boolean value) {
		setValue(name, new BooleanValue(value));
	}

	public void copyValue(String name, Value value) {
		Value previous = values.get(name);
		if (previous == null) {
			previous = value.clone(false);
		}

		previous.setKey(name);
		previous.setValue(value.getValue());
		values.put(name, previous);
	}
	

	public void addValue(String name,Value value){
		value.setKey(name);
		values.put(name, value);
	}
	
	public Value getValue(String name){
		return values.get(name);
	}
	
}
