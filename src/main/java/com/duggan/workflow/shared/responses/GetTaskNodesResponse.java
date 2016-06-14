package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.TaskNode;

public class GetTaskNodesResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ArrayList<TaskNode> taskNodes;

	public GetTaskNodesResponse() {
	}

	public ArrayList<TaskNode> getTaskNodes() {
		return taskNodes;
	}

	public void setTaskNodes(ArrayList<TaskNode> taskNodes) {
		this.taskNodes = taskNodes;
	}

}
