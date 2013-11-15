package com.duggan.workflow.server.dao.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.duggan.workflow.shared.model.DataType;

@Entity
public class ADValue extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=2000)
	private String stringValue;
	
	private Boolean booleanValue;
	
	private Long longValue;
	
	private Double doubleValue;
	
	private Date dateValue;
	
	private String fieldName;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fieldid",referencedColumnName="id")
	private ADField field;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="propertyid", referencedColumnName="id")
	private ADProperty property;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="documentid", referencedColumnName="id")
	private DocumentModel document;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public ADField getField() {
		return field;
	}

	public void setField(ADField field) {
		this.field = field;
	}

	public ADProperty getProperty() {
		return property;
	}

	public void setProperty(ADProperty property) {
		this.property = property;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public Long getLongValue() {
		return longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		ADValue other = (ADValue)obj;
		
		if(id==null ^ other.id==null){
			return false;
		}
		
		if(field==null ^ other.field==null){
			return false;
		}
		
		if(property==null ^ other.property==null){
			return false;
		}
		
		if(fieldName==null ^ other.fieldName==null){
			return false;
		}
		
		if(document==null ^ other.document==null){
			return false;
		}
		
		if(field!=null && !field.equals(other.field)){
			return false;
		}
		
		if(property!=null && !property.equals(other.property)){
			return false;
		}
		
		if(document!=null && !document.equals(other.document)){
			return false;
		}
		
		if(fieldName!=null && !fieldName.equals(other.fieldName)){
			return false;
		}
		
		if(fieldName==null){
			return false;
		}
		
		System.err.println(">>>>>>>> "+fieldName+" -- Same");
		return true;
	}
	
	@Override
	public int hashCode() {
		int hashcode=7;
		if(id!=null){
			hashcode+=hashcode*17+id.hashCode();
		}
		
		if(field!=null){
			hashcode+=hashcode*13+field.hashCode();
		}
		
		if(document!=null){
			hashcode+=hashcode*13+document.hashCode();
		}
		
		if(property!=null){
			hashcode+=hashcode*13+property.hashCode();
		}
		
		if(document!=null){
			hashcode+=hashcode*13+document.hashCode();
		}
		
		if(fieldName!=null){
			hashcode+= hashcode*31+fieldName.hashCode();
		}
		
		if(hashcode==7)
			return super.hashCode();
		
		return hashcode;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public DocumentModel getDocument() {
		return document;
	}

	public void setDocument(DocumentModel document) {
		this.document = document;
	}
}
