package com.duggan.workflow.shared.model.dashboard;

import com.wira.commons.shared.models.SerializableObj;

public class EmployeeWorkload extends SerializableObj{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ownerid;
	private String fullName;
	private int inprogress;
	private int completed; 
	private int total;
	private int overdue; 
	private double avg;
	
	public EmployeeWorkload() {
		
	}

	public EmployeeWorkload(String refId, String ownerid, String fullName,
			Integer inprogress, Integer completed, Integer total,
			Integer overdue, Double avg) {
		setRefId(refId);
		setOwnerid(ownerid);
		setFullName(fullName);
		setInprogress(inprogress);
		setCompleted(completed);
		setTotal(total);
		setOverdue(overdue);
		setAvg(avg);
	}

	public String getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getOverdue() {
		return overdue;
	}

	public void setOverdue(int overdue) {
		this.overdue = overdue;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}
}
