package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.NodeDetail;
import com.wira.commons.shared.response.BaseResponse;

public class GetProcessStatusRequestResult extends  BaseResponse {
	
	private ArrayList<NodeDetail> nodes;

	public GetProcessStatusRequestResult() {
	}

	public GetProcessStatusRequestResult(ArrayList<NodeDetail> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<NodeDetail> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<NodeDetail> nodes) {
		this.nodes = nodes;
	}
}
