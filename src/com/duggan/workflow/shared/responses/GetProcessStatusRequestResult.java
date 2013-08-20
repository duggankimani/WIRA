package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.NodeDetail;
import java.util.List;

public class GetProcessStatusRequestResult extends  BaseResponse {
	
	private List<NodeDetail> nodes;

	public GetProcessStatusRequestResult() {
	}

	public GetProcessStatusRequestResult(List<NodeDetail> nodes) {
		this.nodes = nodes;
	}

	public List<NodeDetail> getNodes() {
		return nodes;
	}

	public void setNodes(List<NodeDetail> nodes) {
		this.nodes = nodes;
	}
}
