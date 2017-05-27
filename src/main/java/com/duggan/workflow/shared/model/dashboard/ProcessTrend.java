package com.duggan.workflow.shared.model.dashboard;

import com.wira.commons.shared.models.SerializableObj;

public class ProcessTrend extends SerializableObj{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer period;
	private int requestCount;
	
	public ProcessTrend() {
		
	}
	
	public ProcessTrend(Integer period, int requestCount) {
		this.period = period;
		this.requestCount = requestCount;
	}

	public int getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}
}
