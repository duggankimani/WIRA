package com.duggan.workflow.shared.model;

import java.io.Serializable;

public class TaskStepTrigger implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Trigger trigger;
	private TriggerType type;
	private Long taskStepId;
	private boolean isActive=true;
	
	public TaskStepTrigger() {
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Trigger getTrigger() {
		return trigger;
	}
	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}
	public TriggerType getType() {
		return type;
	}
	public void setType(TriggerType type) {
		this.type = type;
	}

	public Long getTaskStepId() {
		return taskStepId;
	}

	public void setTaskStepId(Long taskStepId) {
		this.taskStepId = taskStepId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
