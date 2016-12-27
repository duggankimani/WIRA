package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.wira.commons.shared.models.SerializableObj;

public class TaskNotification extends SerializableObj implements Serializable, IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CATEGORY = "category";

	public static final String ENABLENOTIFICATION = "enablenotification";

	public static final String ACTION = "action";

	public static final String DEFAULTANOTIFICATION = "usedefaultnotification";

	public static final String TEMPLATE = "template";

	public static final String NODEID = "nodeid";

	public static final String STEPNAME = "stepname";

	public static final String SUBJECT = "subject";

	public static final String RECIPIENTS = "recipients";

	public static final String ID = "ID";
	
	private NotificationCategory category;
	private Long id;
	private boolean isEnableNotification;
	private Actions action;
	private boolean useDefaultNotification;
	private ArrayList<String> targets = new ArrayList<String>();
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

	public ArrayList<String> getTargets() {
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
