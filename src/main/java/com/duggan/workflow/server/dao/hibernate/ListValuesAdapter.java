package com.duggan.workflow.server.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.duggan.workflow.server.dao.hibernate.ListValuesAdapter.Values;
import com.duggan.workflow.shared.model.Value;

public class ListValuesAdapter extends XmlAdapter<Values, HashMap<String,Value>>{
	
	public ListValuesAdapter() {
	}

	public static class Values{
		
		@XmlElement
		ArrayList<Value> values=  new ArrayList<Value>();

		public Values() {
		}
		
		public void add(Value val) {
			values.add(val);
		}
		public int size() {
			return values.size();
		}

		public ArrayList<Value> getList() {
			return values;
		}
	}
	
	@Override
	public Values marshal(HashMap<String, Value> valuesMap)
			throws Exception {
		
		Values values = new Values();
		for(String key: valuesMap.keySet()){
			Value val = valuesMap.get(key);
			if(val.getValue()==null){
				continue;
			}
			
			val.setKey(key);
			values.add(val);
		}
		
		return values;
	}
	
	@Override
	public HashMap<String, Value> unmarshal(Values values){
		
		HashMap<String, Value> map = new HashMap<String, Value>();
		for(Value v: values.getList()){
			map.put(v.getKey(), v);			
		}
		return map;
	}
}
