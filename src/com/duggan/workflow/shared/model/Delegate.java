package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.Date;

public class Delegate  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long taskId;
	private String userId;
	private String delegateTo;
	private Date created;

	public Delegate() {
	}
	
	public Delegate(Long id, Long taskId, String userId,String delegateTo){
		this.id = id;
		this.taskId = taskId;
		this.userId = userId;
		this.delegateTo = delegateTo;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDelegateTo() {
		return delegateTo;
	}
	public void setDelegateTo(String delegateTo) {
		this.delegateTo = delegateTo;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
