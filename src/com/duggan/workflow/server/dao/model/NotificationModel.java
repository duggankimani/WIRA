package com.duggan.workflow.server.dao.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.duggan.workflow.shared.model.NotificationType;

@Entity
@Table(name="localnotification")
public class NotificationModel extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private Integer documentId;
	
	private Long taskId;
	
	private Long processInstanceId;
	
	private String toUserId;
	
	private String fromUserId;
	
	@Enumerated(EnumType.STRING)
	private NotificationType notificationType;
	
	private Boolean isRead; /*Mark as read/unread*/
	
	public NotificationModel() {
		isRead=true;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public Boolean IsRead() {
		return isRead;
	}

	public void setRead(Boolean isRead) {
		this.isRead = isRead;
	}

}
