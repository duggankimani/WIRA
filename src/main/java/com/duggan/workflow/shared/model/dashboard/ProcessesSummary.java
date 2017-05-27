package com.duggan.workflow.shared.model.dashboard;

import com.wira.commons.shared.models.SerializableObj;

public class ProcessesSummary extends SerializableObj{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String processId;
	private String name; 
	private int inprogress; 
	private int completed; 
	private int overdue;
	private double avgtot; 
	private int targetDays;
	
	public ProcessesSummary() {
	}

	public ProcessesSummary(String refId, String processId, String name,
			Integer inprogress, Integer completed, Integer overdue,
			Double avgtot, Integer targetDays) {
		setRefId(refId);
		setProcessId(processId);
		setName(name);
		setInprogress(inprogress);
		setCompleted(completed);
		setOverdue(overdue);
		setAvgtot(avgtot);
		setTargetDays(targetDays);
		
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInprogress() {
		return inprogress;
	}

	public void setInprogress(int inprogress) {
		this.inprogress = inprogress;
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	public int getOverdue() {
		return overdue;
	}

	public void setOverdue(int overdue) {
		this.overdue = overdue;
	}

	public int getTargetDays() {
		return targetDays;
	}

	public void setTargetDays(int targetDays) {
		this.targetDays = targetDays;
	}

	public double getAvgtot() {
		return avgtot;
	}

	public void setAvgtot(double avgtot) {
		this.avgtot = avgtot;
	}
	
	public Integer getTotal(){
		return completed + inprogress;
	}
}
