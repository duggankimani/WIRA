package com.duggan.workflow.shared.model;

import java.util.Date;

public class Notification extends Activity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	/**
	 * Original Document Owner
	 */
	private HTUser owner;
	private HTUser targetUserId;
	private Long documentId;
	private NotificationType notificationType;
	private String subject;//case Number
	private DocumentType documentType;
	private String documentTypeDesc;
	private Boolean isRead;
	private Date created;
	private HTUser createdBy;
	private ApproverAction approverAction;
	private Long processInstanceId;
	private String fileName;
	private Long fileId;
	
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
	public DocumentType getDocumentType() {
		return documentType;
	}
	public void setDocumentType(DocumentType documentType) {
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

	public HTUser getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(HTUser targetUserId) {
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
		note.setDocumentTypeDesc(documentTypeDesc);
		note.setNotificationType(notificationType);
		note.setOwner(owner);
		note.setRead(isRead);
		note.setSubject(subject);
		note.setTargetUserId(targetUserId);
		note.setApproverAction(approverAction);
		note.setProcessInstanceId(processInstanceId);
		
		return note;
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

	@Override
	public String getStatement() {
		
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		Notification other = (Notification)obj;
		
		if(id!=null && other.id!=null){
			return id.equals(other.id) && notificationType.equals(other.notificationType);
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		if(this.id!=null){
			return (id+notificationType.name()).hashCode();
		}
		
		return super.hashCode();
	}

	public HTUser getOwner() {
		return owner;
	}

	public void setOwner(HTUser owner) {
		this.owner = owner;
	}

	public HTUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(HTUser createdBy) {
		this.createdBy = createdBy;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getDocumentTypeDesc() {
		return documentTypeDesc;
	}

	public void setDocumentTypeDesc(String documentTypeDesc) {
		this.documentTypeDesc = documentTypeDesc;
	}
}
