package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ProcessLog implements Serializable,IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long taskId;
	private Long docId;
	private String docRefId;
	private String caseNo;
	private int processState;
	private String taskStatus;
	private Date startDate;
	private Date endDate;
	private String processId;
	private String processName;
	private Long processinstanceid;
	private String taskOwner;
	private String potOwners;
	private String initiator;
	private String taskName;
	
	public ProcessLog() {
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Long getProcessinstanceid() {
		return processinstanceid;
	}

	public void setProcessinstanceid(Long processinstanceid) {
		this.processinstanceid = processinstanceid;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	@Override
	public String toString() {
		return "{id:"+processinstanceid+",name:"+processId+",status:"+processState
				+",taskName:"+taskName+", taskOwner:"+taskOwner+",potOwners:"+potOwners+"}";
	}

	@Deprecated
	public Long getDocId() {
		return docId;
	}

	@Deprecated
	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getPotOwners() {
		return potOwners;
	}

	public void setPotOwners(String potOwners) {
		this.potOwners = potOwners;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public String getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}

	public int getProcessState() {
		return processState;
	}

	public void setProcessState(int processState) {
		this.processState = processState;
	}

	public String getDocRefId() {
		return docRefId;
	}

	public void setDocRefId(String docRefId) {
		this.docRefId = docRefId;
	}
}
