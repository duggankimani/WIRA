package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.HashMap;
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
	
	public DocumentLine(){
	}
	
	public DocumentLine(String name,Long id, Long documentId,Value ...valueEn){
		this.name = name;
		this.id = id;
		this.documentId = documentId;
		
		if(valueEn!=null){
			for(Value v: valueEn){
				if(v==null){
					continue;
				}
				if(values.get(v.getKey())!=null){
					v.setId(values.get(v.getKey()).getId());
				}
				values.put(v.getKey(), v);
			}
		}
	}

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
		String s="{name:"+getName()+", lineId:"+getId()+", docId: "+getDocumentId()
				+ ", [";
		
		for(Value v: values.values()){
			if(v!=null){
			s=s.concat("{id:"+v.getId()+",key:"+v.getKey()+",value:"+v.getValue()+"},\n");
			}
		}
	
		return s.concat("]}\n");
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		
		DocumentLine other = (DocumentLine)obj;
		
		if(id!=null && other.id!=null){
			return id.equals(other.id);
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

	//frontend Id
	private transient Long tempId=null;
	public void setTempId(Long detailId) {
		this.tempId = detailId;
	}
	
	public Long getTempId(){
		return tempId;
	}
}
