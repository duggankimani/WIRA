package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.dashboard.EmployeeWorkload;
import com.duggan.workflow.shared.model.dashboard.LongTask;
import com.duggan.workflow.shared.model.dashboard.ProcessesSummary;
import com.duggan.workflow.shared.model.dashboard.TaskAging;
import com.wira.commons.shared.response.BaseResponse;

public class GetDashboardDataResponse extends BaseResponse {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	private ArrayList<ProcessesSummary> processesSummaries;
	private ArrayList<EmployeeWorkload> workloads;
	private ArrayList<LongTask> longTasks;
	private ArrayList<TaskAging> aging;
	
	public GetDashboardDataResponse() {
	}

	public ArrayList<EmployeeWorkload> getWorkloads() {
		return workloads;
	}

	public void setWorkloads(ArrayList<EmployeeWorkload> workloads) {
		this.workloads = workloads;
	}

	public ArrayList<ProcessesSummary> getProcessesSummaries() {
		return processesSummaries;
	}

	public void setProcessesSummaries(ArrayList<ProcessesSummary> processesSummaries) {
		this.processesSummaries = processesSummaries;
	}

	public ArrayList<LongTask> getLongTasks() {
		return longTasks;
	}

	public void setLongTasks(ArrayList<LongTask> longTasks) {
		this.longTasks = longTasks;
	}

	public ArrayList<TaskAging> getAging() {
		return aging;
	}

	public void setAging(ArrayList<TaskAging> aging) {
		this.aging = aging;
	}

}
