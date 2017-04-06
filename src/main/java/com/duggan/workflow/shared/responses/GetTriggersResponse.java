package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.Trigger;
import com.wira.commons.shared.response.BaseResponse;

public class GetTriggersResponse extends BaseResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ArrayList<Trigger> triggers = new ArrayList<Trigger>();
	
	public GetTriggersResponse() {
	}

	public ArrayList<Trigger> getTriggers() {
		return triggers;
	}

	public void setTriggers(ArrayList<Trigger> triggers) {
		this.triggers = triggers;
	}
	
	public void addDocument(Trigger doc){
		triggers.add(doc);
	}
}
