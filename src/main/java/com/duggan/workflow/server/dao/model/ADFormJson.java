package com.duggan.workflow.server.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Type;

import com.duggan.workflow.shared.model.form.Form;

@Entity
@Table(name="adform_json")
public class ADFormJson extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@XmlAttribute
	@Column(length=255,unique=true)
	private String name;
	
	@XmlAttribute
	@Column(length=255)
	private String caption;
	
	@XmlAttribute
	private String processRefId;
	
	@Column
	@Type(type="JsonForm")
	private Form form;
	
	public ADFormJson() {
	}
	
	public ADFormJson(Form form) {
		setForm(form);
	}

	@Override
	public Long getId() {
		return id;
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

	public void setId(Long id) {
		this.id = id;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
		setRefId(form.getRefId());
		this.caption = form.getCaption();
		this.name = form.getName();
		this.processRefId= form.getProcessRefId();
	}

}
