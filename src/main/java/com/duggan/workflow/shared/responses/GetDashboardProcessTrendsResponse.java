package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.dashboard.EmployeeWorkload;
import com.duggan.workflow.shared.model.dashboard.LongTask;
import com.duggan.workflow.shared.model.dashboard.ProcessTrend;
import com.duggan.workflow.shared.model.dashboard.ProcessesSummary;
import com.duggan.workflow.shared.model.dashboard.TaskAging;
import com.wira.commons.shared.response.BaseResponse;

public class GetDashboardProcessTrendsResponse extends BaseResponse {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	private ArrayList<ProcessTrend> startTrend;
	private ArrayList<ProcessTrend> completionTrend;
	
	public GetDashboardProcessTrendsResponse() {
	}

	public ArrayList<ProcessTrend> getStartTrend() {
		return startTrend;
	}

	public void setStartTrend(ArrayList<ProcessTrend> startTrend) {
		this.startTrend = startTrend;
	}

	public ArrayList<ProcessTrend> getCompletionTrend() {
		return completionTrend;
	}

	public void setCompletionTrend(ArrayList<ProcessTrend> completionTrend) {
		this.completionTrend = completionTrend;
	}

}
