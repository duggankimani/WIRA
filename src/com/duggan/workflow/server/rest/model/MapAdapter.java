package com.duggan.workflow.server.rest.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * JAXB Map Adaptor
 * 
 * @author duggan
 *
 */
public class MapAdapter extends XmlAdapter<Data, Map<String,Object>>{

	@Override
	public Map<String, Object> unmarshal(Data data) throws Exception {
		Map<String, Object> result = new HashMap<>();
		
		if(data!=null)
			for(KeyValuePair pair: data.keyValues){
				result.put(pair.name, pair.value);
			}
		
		return result;
	}

	@Override
	public Data marshal(Map<String, Object> v) throws Exception {
		
		Data data = new Data();
		
		if(v!=null)
			for(Entry<String, Object> e: v.entrySet()){
				data.keyValues.add(new KeyValuePair(e.getKey(), (Serializable)e.getValue()));
			}
		
		return data;
	}
	
}
