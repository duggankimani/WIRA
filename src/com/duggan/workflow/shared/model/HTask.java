package com.duggan.workflow.shared.model;

public class HTask extends HTSummary {

	private static final long serialVersionUID = 7213500641662371515L;
	
	private String name;
	private Long taskFormId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTaskFormId() {
		return taskFormId;
	}

	public void setTaskFormId(Long taskFormId) {
		this.taskFormId = taskFormId;
	}
}
