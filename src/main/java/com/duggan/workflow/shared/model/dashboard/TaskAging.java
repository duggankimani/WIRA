package com.duggan.workflow.shared.model.dashboard;

import com.wira.commons.shared.models.SerializableObj;

public class TaskAging extends SerializableObj{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String period;
	private int taskCount;
	private int percentage;
	private int position;
	
	public TaskAging() {
		
	}
	
	public TaskAging(String period, int taskCount, int position){
		this.period = period;
		this.taskCount = taskCount;
		this.position = position;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public int getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
