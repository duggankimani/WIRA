package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.Trigger;

public class GetTriggersResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<Trigger> triggers = new ArrayList<Trigger>();
	
	public GetTriggersResponse() {
	}

	public List<Trigger> getTriggers() {
		return triggers;
	}

	public void setTriggers(List<Trigger> triggers) {
		this.triggers = triggers;
	}
	
	public void addDocument(Trigger doc){
		triggers.add(doc);
	}
}
