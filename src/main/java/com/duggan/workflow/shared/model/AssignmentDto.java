package com.duggan.workflow.shared.model;

import com.wira.commons.shared.models.SerializableObj;

public class AssignmentDto extends SerializableObj{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskName;
	private Long nodeId;
	private AssignmentFunction function;
	private String processRefId;
	
	public AssignmentDto() {
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public AssignmentFunction getFunction() {
		return function;
	}

	public void setFunction(AssignmentFunction function) {
		this.function = function;
	}

	public String getProcessRefId() {
		return processRefId;
	}

	public void setProcessRefId(String processRefId) {
		this.processRefId = processRefId;
	}

}
