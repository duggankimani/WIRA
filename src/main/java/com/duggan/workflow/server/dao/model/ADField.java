package com.duggan.workflow.server.dao.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.form.KeyValuePair;

@XmlSeeAlso({KeyValuePair.class,ADProperty.class,ADField.class})
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(indexes={@Index(name="idx_ref_id",columnList="refId")})
public class ADField extends PO implements HasProperties{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(length=255)
	private String name;
	
	@Column(length=255)
	private String caption;
	
	@XmlTransient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="formid",referencedColumnName="id")
	private ADForm form;
	
	@XmlElementWrapper(name="properties")
	@XmlElement(name="property")
	@OneToMany(mappedBy="field", cascade=CascadeType.ALL)
	private Collection<ADProperty> properties = new HashSet<>();
	
	@OneToOne(mappedBy="field", cascade=CascadeType.ALL)
	private ADValue value;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private DataType type;
	
	private Integer position;
	
	@XmlTransient
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="parentid",referencedColumnName="id")
	private ADField parentField;
	
	@XmlElementWrapper(name="grid-columns")
	@XmlElement(name="field")
	@OneToMany(mappedBy="parentField", cascade=CascadeType.ALL)
	private Collection<ADField> fields = new HashSet<>();
	
	@Transient
	@XmlElementWrapper(name="kvp")
	@XmlElement(name="keyvalue")
	private Collection<KeyValuePair> keyValuePairs = new ArrayList<>();
	
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

	public ADValue getValue() {
		return value;
	}

	public void setValue(ADValue value) {
		this.value = value;
		
		if(value!=null)
		value.setField(this);
	}

	public ADForm getForm() {
		return form;
	}

	public void setForm(ADForm form) {
		this.form = form;
	}
	
	public void addProperty(ADProperty property){
		properties.add(property);
		property.setField(this);
	}
	
	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Collection<ADField> getFields() {
		return fields;
	}

	public void setFields(Collection<ADField> fields) {
		this.fields = fields;
	}

	public ADField getParentField() {
		return parentField;
	}

	public void setParentField(ADField parentField) {
		this.parentField = parentField;
	}

	public void addField(ADField adfield) {
		adfield.setParentField(this);
		fields.add(adfield);
	}
	
	/**
	 * Two properties sharing the same name are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		
		ADField field = (ADField)obj;
		
		if(name!=null){
			if(name.equals(field.name)){
				return true;
			}
		}
		
		if(form==null ^ field.form==null){
			return false;
		}
		
		if(form!=null)
			if(!form.equals(field.form)){
				return false;
			}
		
		if(parentField==null ^ field.parentField==null){
			return false;
		}
		
		if(parentField!=null){
			if(!parentField.equals(field.parentField)){
				return false;
			}
		}
		
		if(name==null && form==null){
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		
		int hashcode = 7;
		
		if(id !=null){
			hashcode+=hashcode*id.hashCode();
			return hashcode;
		}
		
		if(name!=null)
			hashcode += 13*name.hashCode();
		
		if(form!=null)
			hashcode += 17*form.hashCode();
		
		if (parentField!=null) {
			hashcode+=19*parentField.hashCode();
		}
		
		if(hashcode==7)
			return super.hashCode();
		
		return hashcode;
	}

	public Collection<KeyValuePair> getKeyValuePairs() {
		return keyValuePairs;
	}

	public void setKeyValuePairs(Collection<KeyValuePair> keyValuePairs) {
		this.keyValuePairs = keyValuePairs;
	}

	public ADProperty getProperty(String propName) {
		if(properties!=null)
		for(ADProperty prop: properties){
			if(prop.getName()!=null && prop.getName().equals(propName)){
				return prop;
			}
		}
		return null;
	}

	
}
