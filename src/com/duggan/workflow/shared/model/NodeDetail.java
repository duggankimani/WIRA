package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;

public class NodeDetail implements Serializable {

	private static final long serialVersionUID = 8764691316633514474L;
	private Integer processInstanceId;
	private String name;
	private boolean isStartNode;
	private boolean isEndNode;
	private boolean isCurrentNode;
	private Long nodeId;
	private String actors; //Comma separated list of actor ids
	private String groups; //Comma Separated list of group ids
	
	public NodeDetail() {
	}

	public NodeDetail(String name, boolean isFirstNode, boolean isLastNode) {
		this.name = name;
		this.isStartNode=isFirstNode;
		this.isEndNode = isLastNode;		
	}

	public void setProcessInstanceId(Integer processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProcessInstanceId() {
		return processInstanceId;
	}

	public String getName() {
		return name;
	}

	public boolean isStartNode() {
		return isStartNode;
	}

	public void setStartNode(boolean isStartNode) {
		this.isStartNode = isStartNode;
	}

	public boolean isEndNode() {
		return isEndNode;
	}

	public void setEndNode(boolean isEndNode) {
		if(isEndNode)
			setName("End");
		this.isEndNode = isEndNode;
	}

	public boolean isCurrentNode() {
		return isCurrentNode;
	}

	public void setCurrentNode(boolean isCurrentNode) {
		this.isCurrentNode = isCurrentNode;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	
}
