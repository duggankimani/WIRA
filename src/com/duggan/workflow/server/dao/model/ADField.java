package com.duggan.workflow.server.dao.model;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.engine.Cascade;

import com.duggan.workflow.shared.model.DataType;

@Entity
public class ADField extends PO implements HasProperties{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(length=255)
	private String name;
	
	@Column(length=255)
	private String caption;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="formid",referencedColumnName="id")
	private ADForm form;
	
	@OneToMany(mappedBy="field", cascade=CascadeType.ALL)
	private Collection<ADProperty> properties = new HashSet<>();
	
	@OneToOne(mappedBy="field", cascade=CascadeType.ALL)
	private ADValue value;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private DataType type;
	
	private Integer position;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinColumn(name="parentid",referencedColumnName="id")
	private ADField parentField;
	
	@OneToMany(mappedBy="parentField", cascade=CascadeType.ALL)
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		
		ADField field = (ADField)obj;
		
		if(!(id==null ^ field.getId()==null)){
				
			if(id!=null){
				if(id.equals(field.getId())){
					return true;
				}
			}
		}
		
		if(name==null ^ field.name==null){
			return false;
		}
		
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
	
}
