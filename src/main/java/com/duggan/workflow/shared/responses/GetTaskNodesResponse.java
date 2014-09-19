package com.duggan.workflow.shared.responses;

import java.util.List;

import com.duggan.workflow.shared.model.TaskNode;

public class GetTaskNodesResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<TaskNode> taskNodes;

	public GetTaskNodesResponse() {
	}

	public List<TaskNode> getTaskNodes() {
		return taskNodes;
	}

	public void setTaskNodes(List<TaskNode> taskNodes) {
		this.taskNodes = taskNodes;
	}

}
