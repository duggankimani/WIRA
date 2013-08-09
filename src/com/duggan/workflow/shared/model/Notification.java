package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.Date;

public class Notification implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String owner;
	private String targetUserId;
	private Long documentId;
	private NotificationType notificationType;
	private String subject;
	private DocType documentType;
	private Boolean isRead;
	private Date created;
	private String createdBy;
	private ApproverAction approverAction;
	private Long processInstanceId;
	
	public Notification() {
	}
	
	public Long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
	public NotificationType getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public DocType getDocumentType() {
		return documentType;
	}
	public void setDocumentType(DocType documentType) {
		this.documentType = documentType;
	}

	public Boolean IsRead() {
		return isRead;
	}

	public void setRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public Notification clone(){

		Notification note = new Notification();
		note.setCreated(created);
		note.setDocumentId(documentId);
		note.setDocumentType(documentType);
		note.setNotificationType(notificationType);
		note.setOwner(owner);
		note.setRead(isRead);
		note.setSubject(subject);
		note.setTargetUserId(targetUserId);
		note.setApproverAction(approverAction);
		note.setProcessInstanceId(processInstanceId);
		
		return note;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public ApproverAction getApproverAction() {
		return approverAction;
	}

	public void setApproverAction(ApproverAction approverAction) {
		this.approverAction = approverAction;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
}
