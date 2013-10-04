package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.duggan.workflow.server.dao.model.DocumentModel}
 * @author duggan
 *
 */
public class Document extends DocSummary implements Serializable, BusinessKey{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private DocumentType type;
	
	private HTUser owner;
	
	protected String subject;
	
	protected String description;
	
	protected Date created;
	
	protected Date documentDate;
	
	protected Integer priority;
	
	private String partner;
	
	private String value;
	
	private DocStatus status;
	
	private Date dateDue;
	
	private Long processInstanceId;
	
	private Long sessionId;

	public Document() {
		//serialization
		//super();
	}
	
	public DocumentType getType() {
		return type;
	}

	public void setType(DocumentType type) {
		this.type = type;
	}

	public HTUser getOwner() {
		return owner;
	}

	public void setOwner(HTUser owner) {
		this.owner = owner;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
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

	public HTSummary toTask(){
		HTSummary summary = new HTSummary();
		summary.setCreated(new Date());
		summary.setDateDue(dateDue);
		summary.setDescription(description);
		summary.setDocumentRef(id);
		summary.setPriority(priority);
		summary.setSubject(subject);
		summary.setTaskName(type.getDisplayName()+" - "+subject);
		
		return summary;
	}

	public Date getDateDue() {
		return dateDue;
	}

	public void setDateDue(Date dateDue) {
		this.dateDue = dateDue;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	@Override
	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public String toString() {
	
		return "DocumentId = "+id+", subject="+subject;
	}
}
