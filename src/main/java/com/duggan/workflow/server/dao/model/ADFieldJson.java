package com.duggan.workflow.server.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Type;

import com.duggan.workflow.shared.model.form.Field;
import com.duggan.workflow.shared.model.form.KeyValuePair;

@XmlSeeAlso({KeyValuePair.class,ADProperty.class,ADFieldJson.class})
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@org.hibernate.annotations.Entity(dynamicInsert=true, dynamicUpdate=true)
public class ADFieldJson extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="field")
	@Type(type="JsonField")
	private Field field;
	
	public ADFieldJson() {
	}
	
	public ADFieldJson(Field field) {
		setField(field);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Two properties sharing the same name are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		
		ADFieldJson field = (ADFieldJson)obj;
		
		if(this.field!=null){
			if(this.field.equals(field.field)){
				return true;
			}
		}
		
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		
		int hashcode = 7;
		
		if(id !=null){
			hashcode+=hashcode*id.hashCode();
			return hashcode;
		}
		
		if(field!=null)
			hashcode += 13*field.hashCode();
		
		if(hashcode==7)
			return super.hashCode();
		
		return hashcode;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
		setRefId(field.getRefId());
	}
	
}
