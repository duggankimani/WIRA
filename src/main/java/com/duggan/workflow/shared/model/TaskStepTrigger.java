package com.duggan.workflow.shared.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.wira.commons.shared.models.SerializableObj;

public class TaskStepTrigger extends SerializableObj implements Serializable,IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ID = "id";

	public static final String TRIGGER = "trigger";

	public static final String CONDITION = "condition";

	public static final String TYPE = "type";
	
	private Long id;
	private Trigger trigger;
	private TriggerType type;
	private Long taskStepId;
	private String condition;
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

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}
