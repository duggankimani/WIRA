package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskNotification implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private NotificationCategory category;
	private Long id;
	private boolean isEnableNotification;
	private Actions action;
	private boolean useDefaultNotification;
	private List<String> targets = new ArrayList<String>();
	private String notificationTemplate; 
	private Long nodeId;//Accommodate initial input form
	private String stepName;
	private Long processDefId;
	private String subject;
	
	public TaskNotification() {
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

	public List<String> getTargets() {
		return targets;
	}

	public void setTargets(Collection<String> targets) {
		this.targets.clear();
		this.targets.addAll(targets);
	}

	public String getNotificationTemplate() {
		return notificationTemplate;
	}

	public void setNotificationTemplate(String notificationTemplate) {
		this.notificationTemplate = notificationTemplate;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	
}
