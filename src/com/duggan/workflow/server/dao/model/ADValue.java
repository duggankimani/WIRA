package com.duggan.workflow.server.dao.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fieldid",referencedColumnName="id")
	private ADField field;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="propertyid", referencedColumnName="id")
	private ADProperty property;
	
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
}
