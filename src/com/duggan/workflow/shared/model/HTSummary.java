package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;

public class HTSummary extends DocSummary implements Serializable{

	private static final long serialVersionUID = -3021583190508951117L;
	private long id;
	private String taskName;
	private Date dateDue;
	private Date lastUpdate;
	private HTStatus status;
	private Date created;
	private String subject;
	private Integer priority;
	private String description;
	private Long documentRef;
	private Long processInstanceId;
	
	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public HTSummary() {
	}

	public HTSummary(long id) {
		this.id= id;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setDateDue(Date dateDue) {
		this.dateDue = dateDue;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getTaskName() {
		return taskName;
	}

	public Date getDateDue() {
		return dateDue;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public HTStatus getStatus() {
		return status;
	}

	public void setStatus(HTStatus status) {
		this.status = status;
	}

	@Override
	public Date getCreated() {
		
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getDocumentRef() {
		return documentRef;
	}

	public void setDocumentRef(Long documentRef) {
		this.documentRef = documentRef;
	}

	@Override
	public Date getDocumentDate() {
		
		return null;
	}
}
