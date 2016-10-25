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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Type;

import com.duggan.workflow.server.dao.hibernate.DocValues;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;

@org.hibernate.annotations.Entity(dynamicUpdate=true)
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
	
	@Column(length=200,nullable=false, unique=true)
	private String caseNo;//caseNo
	
	private String description;
	
	@Column(length=50,nullable=false)
	private String docTypeRefId;
	
	private Date documentDate;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="document")
	private List<LocalAttachment> attachment;
	
	protected Integer priority;
	
	@Enumerated(EnumType.STRING)
	private DocStatus status;
	
	private Long processInstanceId;
	
	private String processId;
	
	private Long sessionId;

	@Column(name="doc")
	@Type(type="JsonDocument")
	private Document document;
	
	@Column(name="data")
	@Type(type="JsonDocValue")
	private DocValues data;
	
	
	public DocumentModelJson(){
		
	}
	
	public DocumentModelJson(Document document, DocValues data){
		document.setValues(data.getValuesMap());
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
		this.caseNo = document.getCaseNo();
		this.description = document.getDescription();
		this.documentDate = document.getDocumentDate();
		this.priority = document.getPriority();
		this.processId = document.getProcessId();
		this.processInstanceId = document.getProcessInstanceId();
		this.sessionId = document.getSessionId();
		this.status = document.getStatus();
		this.docTypeRefId = document.getType().getRefId();
		setRefId(document.getRefId());
	}

	public DocValues getData() {
		return data;
	}

	public void setData(DocValues data) {
		this.data = data;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDocTypeRefId() {
		return docTypeRefId;
	}

	public void setDocTypeRefId(String docTypeRefId) {
		this.docTypeRefId = docTypeRefId;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public List<LocalAttachment> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<LocalAttachment> attachment) {
		this.attachment = attachment;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public DocStatus getStatus() {
		return status;
	}

	public void setStatus(DocStatus status) {
		this.status = status;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	
	@Override
	public void onPrePersist() {
		super.onPrePersist();
		document.setCreated(getCreated());
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return super.equals(obj);
	}
}
