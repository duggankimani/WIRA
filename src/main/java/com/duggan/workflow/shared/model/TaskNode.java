package com.duggan.workflow.shared.model;

import java.io.Serializable;

public class TaskNode implements Serializable,Listable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long nodeId;
	private Integer processInstanceId;
	private String name;
	private String displayName;
	private String groupId;
	private String actorId;
	
	public TaskNode() {
	
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Integer processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}
}
