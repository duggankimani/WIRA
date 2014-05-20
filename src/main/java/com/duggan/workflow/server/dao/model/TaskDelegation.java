package com.duggan.workflow.server.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table
public class TaskDelegation extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true)
	private Long taskId;
	private String userId;
	private String delegateTo;
	
	public TaskDelegation() {
	}
	
	public TaskDelegation(Long id, Long taskId, String userId,String delegateTo){
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

}
