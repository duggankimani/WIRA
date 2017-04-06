package com.duggan.workflow.server.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.duggan.workflow.server.dao.hibernate.DocValues;
import com.duggan.workflow.shared.model.DocumentLine;

@org.hibernate.annotations.Entity(dynamicUpdate=true)
@Entity
@Table(name="documentlinejson")
public class DocumentLineJson extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false,length=30)
	private String docRefId;
	
	@Column(nullable=false,length=255)
	private String name;

	@Column(name="line")
	@Type(type="JsonDocLine")
	private DocumentLine documentLine;
	
	@Column(name="data")
	@Type(type="JsonDocValue")
	private DocValues data;
	
	public DocumentLineJson(){
		
	}
	
	public DocumentLineJson(DocumentLine line){
		setDocumentLine(line);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DocumentLine getDocumentLine() {
		return documentLine;
	}

	public void setDocumentLine(DocumentLine documentLine) {
		this.documentLine = documentLine;
		setRefId(documentLine.getRefId());
		setName(documentLine.getName());
		setDocRefId(documentLine.getDocRefId());
		this.data = new DocValues(documentLine.getValues());
	}

	public DocValues getData() {
		return data;
	}

	public void setData(DocValues data) {
		this.data = data;
	}

	public String getDocRefId() {
		return docRefId;
	}

	public void setDocRefId(String docRefId) {
		this.docRefId = docRefId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
