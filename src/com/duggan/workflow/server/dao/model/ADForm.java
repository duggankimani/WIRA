package com.duggan.workflow.server.dao.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ADForm extends PO implements HasProperties{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(length=255,unique=true)
	private String name;
	
	@Column(length=255)
	private String caption;
	
	@OneToMany(mappedBy="form", cascade=CascadeType.ALL)
	private Collection<ADProperty> properties = new HashSet<>();
	
	@OneToMany(mappedBy="form", cascade=CascadeType.ALL)
	private Collection<ADField> fields = new HashSet<>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Collection<ADProperty> getProperties() {
		return properties;
	}

	public void setProperties(Collection<ADProperty> properties) {
		this.properties = properties;
	}

	public Collection<ADField> getFields() {
		return fields;
	}
	
	public void addProperty(ADProperty property){
		properties.add(property);
		property.setForm(this);
	}

	public void removeProperty(ADProperty property){
		properties.remove(property);
	}
	
	public void setFields(Collection<ADField> fields) {
		this.fields = fields;
	}
	
	public void addField(ADField field){
		fields.add(field);
		field.setForm(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		ADForm other = (ADForm)obj;
		
		if(name==null){
			return false;
		}
		
		return name.equals(other.name);
	}
	
	@Override
	public int hashCode() {
		int hashcode=7;
		
		if(name==null){
			hashcode += hashcode*name.hashCode();
			return hashcode;
		}
		
		return super.hashCode();
	}
}
