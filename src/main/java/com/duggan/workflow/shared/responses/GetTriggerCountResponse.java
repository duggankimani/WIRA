package com.duggan.workflow.shared.responses;

import java.util.HashMap;

import com.duggan.workflow.shared.model.TriggerType;

public class GetTriggerCountResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashMap<TriggerType, Integer> counts = new HashMap<TriggerType, Integer>();

	public GetTriggerCountResponse() {
	}

	public void addCount(TriggerType type, int count){
		counts.put(type, count);
	}
	
	public HashMap<TriggerType, Integer> getCounts() {
		return counts;
	}
}
