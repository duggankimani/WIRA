package com.duggan.workflow.server.dao.model;

import java.util.ArrayList;
import java.util.List;

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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.duggan.workflow.shared.model.DataType;


@XmlRootElement(name="property")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class ADProperty extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@XmlAttribute
	@Column(length=255)
	private String name;
	
	@XmlAttribute
	@Column(length=255)
	private String caption;
	
	@XmlAttribute
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private DataType type;
	
	@XmlTransient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fieldid",referencedColumnName="id", nullable=true)
	private ADField field;
	
	@XmlTransient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="formid", referencedColumnName="id", nullable=true)
	private ADForm form;
	
	@OneToMany(mappedBy="property", cascade=CascadeType.ALL)
	private List<ADValue> value = new ArrayList<>();

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

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public ADField getField() {
		return field;
	}

	public void setField(ADField field) {
		this.field = field;
	}

	public ADForm getForm() {
		return form;
	}

	public void setForm(ADForm form) {
		this.form = form;
	}

	public ADValue getValue() {
		for(ADValue v : value){
			return v;
		}
		return null;
	}

	public void setValue(ADValue value) {
		this.value.clear();
		this.value.add(value);
		
		if(value!=null)
			value.setProperty(this);
	}

	@Override
	public boolean equals(Object obj) {
		
		if(obj==null)
			return false;
		
		ADProperty other = (ADProperty)obj;
		
		if(name==null || other.name==null){
			//one has a name, the other doesnt - XOR
			return false;
		}
		
		//NAME
		if(name!=null)
			if(!name.equals(other.name)){
				return false;
			}

		//Field
		if(field==null ^ other.field==null){
			//XOR
			//false - true
			return false;
		}
		if(field!=null){
			if(!field.equals(other.field)){
				return false;
			}
		}
		
		//Field
		if(form==null ^ other.form==null){
			//XOR
			return false;
		}		
		if(form!=null){
			if(!form.equals(other.form)){
				return false;
			}
		}
		
		if(form==null && field==null && name==null){
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {

		int hashcode = 7;
		
		if(name!=null)
			hashcode += 7*name.hashCode();
		
		if(id!=null)
			hashcode += 7*id.hashCode();
		
		if(hashcode==7)
			return super.hashCode();
		
		return hashcode;
	}
	
	@Override
	public String toString() {
		
		String str = "id = "+id+
				"; name= "+name+
				((value==null || value.isEmpty()||value.get(0)==null)?"": "; value= "+value.get(0).getStringValue())+
				(field==null? "": "; fieldid=["+field.getId()+" : "+field.getName()+"]")+
				(form==null? "": "; formid=["+form.getId()+" : "+form.getName()+"]");
		
		return str;
	}

}
