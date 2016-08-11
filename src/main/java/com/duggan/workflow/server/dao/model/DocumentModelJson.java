package com.duggan.workflow.server.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.duggan.workflow.server.dao.hibernate.DocValues;
import com.duggan.workflow.shared.model.Document;

@Entity
@Table(name="documentjson")
public class DocumentModelJson extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="doc")
	@Type(type="JsonDocument")
	private Document document;
	
	@Column(name="data")
	@Type(type="JsonDocValue")
	private DocValues data;
	
	
	public DocumentModelJson(){
		
	}
	
	public DocumentModelJson(Document document){
		setDocument(document);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
		this.data = new DocValues(document.getValues());
		setRefId(document.getRefId());
	}

	public DocValues getData() {
		return data;
	}

	public void setData(DocValues data) {
		this.data = data;
	}
}
