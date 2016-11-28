package com.duggan.workflow.server.dao.model;

import java.util.Date;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.duggan.workflow.shared.model.settings.SETTINGNAME;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(indexes = {
@javax.persistence.Index(name = "idx_value_settingName", columnList = "settingName"),
@javax.persistence.Index(name = "idx_value_stringValue", columnList = "stringValue"),
@javax.persistence.Index(name = "idx_value_stringValue", columnList = "fieldName")
})
public class ADValue extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@XmlAttribute
	@Enumerated(EnumType.STRING)
	private SETTINGNAME settingName;
	
	@XmlAttribute
	@Column(length=5000)
	private String stringValue;
	
	@XmlAttribute
	@Column(columnDefinition="text")
	private String textValue;
	
	@XmlAttribute
	private Boolean booleanValue;
	
	@XmlAttribute
	private Long longValue;
	
	@XmlAttribute
	private Double doubleValue;
	
	@XmlAttribute
	private Date dateValue;
	
	@XmlAttribute
	private String fieldName;
	
	@XmlTransient
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fieldid",referencedColumnName="id")
	private ADField field;
	
	@XmlTransient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="propertyid", referencedColumnName="id")
	private ADProperty property;
	
	@XmlTransient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="documentid", referencedColumnName="id")
	private DocumentModel document;
	
	@XmlTransient
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="detailid", referencedColumnName="id")
	private DetailModel detail;
	
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
		
		//System.err.println(">>>>>>>> "+fieldName+" -- Same");
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

	public DetailModel getDetail() {
		return detail;
	}

	public void setDetail(DetailModel detail) {
		this.detail = detail;
	}

	public SETTINGNAME getSettingName() {
		return settingName;
	}

	public void setSettingname(SETTINGNAME settingName) {
		this.settingName = settingName;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

}
