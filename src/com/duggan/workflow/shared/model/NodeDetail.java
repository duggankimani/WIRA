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
		this.isEndNode = isEndNode;
	}

	public boolean isCurrentNode() {
		return isCurrentNode;
	}

	public void setCurrentNode(boolean isCurrentNode) {
		this.isCurrentNode = isCurrentNode;
	}
}
