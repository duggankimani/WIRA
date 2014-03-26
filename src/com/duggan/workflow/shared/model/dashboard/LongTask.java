package com.duggan.workflow.shared.model.dashboard;

import java.io.Serializable;
import java.lang.String;
import java.lang.Integer;

public class LongTask implements Serializable {

	private static final long serialVersionUID = -26425705263928669L;
	private String taskName;
	private String documentType;
	private Integer averageTime;
	private Integer noOfTasks;

	public LongTask() {
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public void setAverageTime(Integer averageTime) {
		this.averageTime = averageTime;
	}

	public String getTaskName() {
		return taskName;
	}

	public String getDocumentType() {
		return documentType;
	}

	public Integer getAverageTime() {
		return averageTime;
	}

	public Integer getNoOfTasks() {
		return noOfTasks;
	}

	public void setNoOfTasks(Integer noOfTasks) {
		this.noOfTasks = noOfTasks;
	}
}
