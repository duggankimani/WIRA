package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.duggan.workflow.server.dao.hibernate.ListValuesAdapter;
import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.KeyValuePair;
import com.duggan.workflow.shared.model.form.Property;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.wira.commons.shared.models.SerializableObj;

/**
 *  
 * @author duggan
 *  Represents document lines/details (e.g invoice details)
 */

@XmlSeeAlso({KeyValuePair.class,Property.class,Field.class})
@XmlRootElement(name="docline")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentLine extends IsDoc{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String docRefId;
	@XmlTransient
	private Long id;	
	@XmlTransient
	private Long documentId;
	private String name;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		
		if(getRefId()!=null && other.getRefId()!=null){
			return getRefId().equals(other.getRefId());
		}
		
		return super.equals(obj);
	}

	public DocumentLine clone(boolean fullClone){
		DocumentLine line = new DocumentLine();
		line.setDocumentId(documentId);
		line.setName(name);
		
		HashMap<String,Value> vals = new HashMap<String, Value>();
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

	public String getDocRefId() {
		return docRefId;
	}

	public void setDocRefId(String docRefId) {
		this.docRefId = docRefId;
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}

}
