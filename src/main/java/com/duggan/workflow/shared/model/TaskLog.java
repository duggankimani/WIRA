package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.Date;

public class TaskLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long taskId;
	private String status;
	private Date createdon;
	private Date completedon;
	private Date activationtime;
	private Date expirationtime;
	private Long processinstanceid;
	private HTUser actualOwner;
	private String potOwner;
	private String taskName;
	private boolean isProcessLoaded=true;
	
	public TaskLog() {
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedon() {
		return createdon;
	}

	public void setCreatedon(Date createdon) {
		this.createdon = createdon;
	}

	public Date getCompletedon() {
		return completedon;
	}

	public void setCompletedon(Date completedon) {
		this.completedon = completedon;
	}

	public Date getActivationtime() {
		return activationtime;
	}

	public void setActivationtime(Date activationtime) {
		this.activationtime = activationtime;
	}

	public Date getExpirationtime() {
		return expirationtime;
	}

	public void setExpirationtime(Date expirationtime) {
		this.expirationtime = expirationtime;
	}

	public Long getProcessinstanceid() {
		return processinstanceid;
	}

	public void setProcessinstanceid(Long processinstanceid) {
		this.processinstanceid = processinstanceid;
	}

	public String getPotOwner() {
		return potOwner;
	}

	public void setPotOwner(String potOwner) {
		this.potOwner = potOwner;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	@Override
	public String toString() {
		return "{id="+taskId+",name="+taskName+",status="+status+"}";
	}

	public HTUser getActualOwner() {
		return actualOwner;
	}

	public void setActualOwner(HTUser actualOwner) {
		this.actualOwner = actualOwner;
	}

	public boolean isProcessLoaded() {
		return isProcessLoaded;
	}

	public void setProcessLoaded(boolean isProcessLoaded) {
		this.isProcessLoaded = isProcessLoaded;
	}
}
