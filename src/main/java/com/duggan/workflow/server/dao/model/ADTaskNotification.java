package com.duggan.workflow.server.dao.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

import org.hibernate.annotations.CollectionOfElements;

import com.duggan.workflow.client.ui.admin.processitem.NotificationCategory;
import com.duggan.workflow.shared.model.Actions;

@Entity
public class ADTaskNotification extends PO{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private NotificationCategory category;
	private Long taskStepId;
	private boolean isEnableNotification;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Actions action;
	private boolean useDefaultNotification;
	
	@CollectionOfElements(targetElement=String.class)
	@JoinTable(name = "notification_targets",
     joinColumns = @JoinColumn(name = "notificationid"))
	@Column(name = "receipient", nullable = false)
	private Set<String> receipients = new HashSet<>();
	
	@Column(length=5000)
	private String notificationTemplate; 
	
	@Column(nullable=false)
	private Long nodeId;//Accommodate initial input form
	private String stepName;
	
	@Column(nullable=false)
	private Long processDefId;
	private String subject;
	
	public ADTaskNotification() {
	}

	public NotificationCategory getCategory() {
		return category;
	}

	public void setCategory(NotificationCategory category) {
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isEnableNotification() {
		return isEnableNotification;
	}

	public void setEnableNotification(boolean isEnableNotification) {
		this.isEnableNotification = isEnableNotification;
	}

	public Actions getAction() {
		return action;
	}

	public void setAction(Actions action) {
		this.action = action;
	}

	public boolean isUseDefaultNotification() {
		return useDefaultNotification;
	}

	public void setUseDefaultNotification(boolean useDefaultNotification) {
		this.useDefaultNotification = useDefaultNotification;
	}

	public String getNotificationTemplate() {
		return notificationTemplate;
	}

	public void setNotificationTemplate(String notificationTemplate) {
		this.notificationTemplate = notificationTemplate;
	}

	public Long getTaskStepId() {
		return taskStepId;
	}

	public void setTaskStepId(Long taskStepId) {
		this.taskStepId = taskStepId;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public Long getProcessDefId() {
		return processDefId;
	}

	public void setProcessDefId(Long processDefId) {
		this.processDefId = processDefId;
	}

	public Set<String> getReceipients() {
		return receipients;
	}

	public void setReceipients(Collection<String> receipients) {
		this.receipients.clear();
		this.receipients.addAll(receipients);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
