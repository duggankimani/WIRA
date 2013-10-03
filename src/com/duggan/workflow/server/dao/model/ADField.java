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

import com.duggan.workflow.shared.model.DataType;

@Entity
public class ADField extends PO {

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
	@JoinColumn(name="formid",referencedColumnName="id", nullable=true)
	private ADForm form;
	
	@OneToMany(mappedBy="field", cascade=CascadeType.ALL)
	private Collection<ADProperty> properties = new HashSet<>();
	
	@OneToOne(mappedBy="field", cascade=CascadeType.ALL)
	private ADValue value;
	
	@Enumerated(EnumType.STRING)
	private DataType type;
	
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
		value.setField(this);
	}

	public ADForm getForm() {
		return form;
	}

	public void setForm(ADForm form) {
		this.form = form;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		
		ADField field = (ADField)obj;
		
		if(name==null ^ field.name==null){
			return false;
		}
		
		if(name!=null){
			if(!name.equals(field.name)){
				return false;
			}
		}
		
		if(form==null ^ field.form==null){
			return false;
		}
		
		if(form!=null)
			if(!form.equals(field.form)){
				return false;
			}
		
		if(name==null && form==null){
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		
		int hashcode = 7;
		
		if(name!=null)
			hashcode += hashcode*name.hashCode();
		
		if(form!=null)
			hashcode += hashcode*form.hashCode();
		
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

}
