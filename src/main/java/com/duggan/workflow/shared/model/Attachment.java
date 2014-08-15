package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.Date;

public class Attachment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private boolean archived;
	private Long documentid;
	private String fieldName;
	private Long processDefId;
	private Long size;
	private String sizeStr;
	private String contentType;
	private String createdBy;
	private Date created;
	
	private String documentType;
	private String subject;
	
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
	public boolean isArchived() {
		return archived;
	}
	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	public Long getDocumentid() {
		return documentid;
	}
	public void setDocumentid(Long documentid) {
		this.documentid = documentid;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getSizeAsStr() {
		return sizeStr;
	}
	public void setSizeStr(String size) {
		this.sizeStr = size;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getSizeStr() {
		return sizeStr;
	}
	public Long getProcessDefId() {
		return processDefId;
	}
	public void setProcessDefId(Long processDefId) {
		this.processDefId = processDefId;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
}
