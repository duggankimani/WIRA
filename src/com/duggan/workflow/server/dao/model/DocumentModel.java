package com.duggan.workflow.server.dao.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocType;

@Entity
@Table(name="localdocument")
public class DocumentModel extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=200,nullable=false, unique=true)
	private String subject;
	
	@Column(length=350,nullable=false)
	private String description;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private DocType type;
	
	private Date documentDate;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="document")
	private List<LocalAttachment> attachment;
	
	protected Integer priority;
	
	private String partner;
	
	private String value;
	
	@Enumerated(EnumType.STRING)
	private DocStatus status;
	
	private Long processInstanceId;
	
	public DocumentModel(){
		
	}
	
	public DocumentModel(Long id, String subject, String description, DocType type){
		this.id=id;
		this.subject=subject;
		this.description=description;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DocType getType() {
		return type;
	}

	public void setType(DocType type) {
		this.type = type;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public DocStatus getStatus() {
		return status;
	}

	public void setStatus(DocStatus status) {
		this.status = status;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}
	
}
