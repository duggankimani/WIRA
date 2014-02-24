package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  
 * @author duggan
 *  Represents document lines/details (e.g invoice details)
 */
public class DocumentLine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long documentId;
	private String name;
	private Map<String, Value> values = new HashMap<String, Value>();
	
	public DocumentLine(){}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Value> getValues() {
		return values;
	}

	public void setValues(Map<String, Value> values) {
		this.values = values;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void addValue(String name,Value value){
		value.setKey(name);
		values.put(name, value);
	}
	
	public Value getValue(String name){
		return values.get(name);
	}
	
	@Override
	public String toString() {
		String s="";
		for(Value v: values.values()){
			s=s.concat(getName()+"|"+
					getId()+" | "+v.getId()+"|"+v.getKey()+" :: "+v.getValue()+"\n");
		}
		return s;
	}
	
	@Override
	public boolean equals(Object obj) {
		DocumentLine other = (DocumentLine)obj;
		
		if(id!=null){
			return id.equals(other.id);
		}
		
		if(other.id!=null){
			return other.id.equals(id);
		}
		
		return super.equals(obj);
	}

	public DocumentLine clone(boolean fullClone){
		DocumentLine line = new DocumentLine();
		line.setDocumentId(documentId);
		line.setName(name);
		
		Map<String,Value> vals = new HashMap<String, Value>();
		for(String key:values.keySet()){
			Value val = values.get(key);
			if(val!=null)
				vals.put(key, val.clone(fullClone));
		}
		line.setValues(vals);
		
		return line;
	}
}
