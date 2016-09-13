package com.duggan.workflow.server.dao.hibernate;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.Transient;

import org.apache.log4j.Logger;

import com.duggan.workflow.client.ui.util.Formats;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.DoubleValue;
import com.duggan.workflow.shared.model.GridValue;
import com.duggan.workflow.shared.model.IntValue;
import com.duggan.workflow.shared.model.LongValue;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.Value;

public class DocValues implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Transient
	static Logger logger  = Logger.getLogger(DocValues.class);
	
	@Transient
	HashMap<String, Object> rawValues = new HashMap<String, Object>();
	@Transient
	private HashMap<String, Value> valuesMap = new HashMap<String, Value>();

	public DocValues() {
	}

	public DocValues(HashMap<String, Value> valuesMap) {
		this.valuesMap  = valuesMap;
		for (String key : valuesMap.keySet()) {
			Value val = (Value) valuesMap.get(key);
			if (val==null || val.getValue() == null || val instanceof GridValue) {
				continue;
			}

			val.setKey(key);
			
			if(val.getValue() instanceof Date){
				SimpleDateFormat format = new SimpleDateFormat(Formats.datepattern_sys);
				rawValues.put(key, format.format((Date)val.getValue()));
			}else{
				rawValues.put(key, val.getValue());
			}
			
		}

	}

	public HashMap<String, Object> getRawValues() {
		return rawValues;
	}

	public void setRawValues(HashMap<String, Object> rawValues) {
		this.rawValues = rawValues;
	}

	public void add(String name, Object object) {
		rawValues.put(name, object);
		
		Value value = null;
		if(object instanceof Double){
			value = new DoubleValue(null,name,(Double)object);
		}else if(object instanceof Integer){
			value = new IntValue(null,name,(Integer)object);
		}else if(object instanceof Long){
			value = new LongValue(null,name,(Long)object);
		}else if(object instanceof Boolean){
			value = new BooleanValue(null,name,(Boolean)object);
		}else if(object instanceof Date){
			value = new DateValue(null,name,(Date)object);
		}else{
			value = new StringValue(null,name,(String)object);
		}
		
		if(value!=null){
			valuesMap.put(name, value);
		}
	}

	public HashMap<String, Value> getValuesMap() {
		return valuesMap;
	}

	public void setValuesMap(HashMap<String, Value> valuesMap) {
		this.valuesMap = valuesMap;
	}
	
	@Override
	public boolean equals(Object Obj) {
		
		DocValues other = (DocValues)Obj;
		int otherSize = other.getValuesMap().size();
		int thisSize = getValuesMap().size();
		
		boolean isEqual = true;
		if(otherSize!= thisSize){
			logger.debug("DocValues assertion failed this.size="+thisSize +" NOT Equal to other.size="+otherSize);
			isEqual=false;
		}
		
		for(String key: other.getValuesMap().keySet()){
			Object otherObj = other.getRawValues().get(key);
			Object obj = getRawValues().get(key);
			
			if(otherObj!=null && obj!=null){
				if(otherObj instanceof Number){
					isEqual = new Double(otherObj.toString()).equals(new Double(obj.toString()));
				}else if(!otherObj.equals(obj)){
					logger.debug("DocValues values NOT Equal "
							+ "{this."+key+":"+obj+"} != "
							+"{other."+key+":"+otherObj+"}");
					isEqual=false;
				}
			}else if(!(otherObj==null && obj==null)){
				logger.debug("DocValues values NULL comparison failed "
						+ "{this."+key+":"+obj+"} != "
						+"{other."+key+":"+otherObj+"}");
				isEqual=false;
			}
		}
		
		return isEqual;
	}
	
	@Override
	public int hashCode() {
		
		return rawValues.hashCode();
	}

}
