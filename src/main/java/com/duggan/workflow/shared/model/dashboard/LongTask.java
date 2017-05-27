package com.duggan.workflow.shared.model.dashboard;

import com.wira.commons.shared.models.SerializableObj;

public class LongTask extends SerializableObj{

	private static final long serialVersionUID = -26425705263928669L;
	private String taskName;
	private String processId;
	private Double averageTime;
	private Integer noOfTasks;
	private Integer peopleCount;
	private String peopleNames;

	public LongTask() {
	}

	public LongTask(String taskName, String processId, Double averageTime,
			Integer noOfTasks) {
		this.taskName = taskName;
		this.processId = processId;
		this.averageTime = averageTime;
		this.noOfTasks = noOfTasks;
	}

	public LongTask(String taskName, String processId, Double averageTime,
			Integer noOfTasks, Integer peopleCount, String peopleNames) {
		this.taskName = taskName;
		this.processId = processId;
		this.averageTime = averageTime;
		this.noOfTasks = noOfTasks;
		this.peopleCount = peopleCount;
		this.peopleNames = peopleNames;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskName() {
		return taskName;
	}

	public Integer getNoOfTasks() {
		return noOfTasks;
	}

	public void setNoOfTasks(Integer noOfTasks) {
		this.noOfTasks = noOfTasks;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Integer getPeopleCount() {
		return peopleCount;
	}

	public void setPeopleCount(Integer peopleCount) {
		this.peopleCount = peopleCount;
	}

	public String getPeopleNames() {
		return peopleNames;
	}

	public void setPeopleNames(String peopleNames) {
		this.peopleNames = peopleNames;
	}

	public Double getAverageTime() {
		return averageTime;
	}

	public void setAverageTime(Double averageTime) {
		this.averageTime = averageTime;
	}
}
