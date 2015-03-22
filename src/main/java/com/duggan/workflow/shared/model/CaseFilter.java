package com.duggan.workflow.shared.model;

import java.io.Serializable;

public class CaseFilter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String processId;
	private String userId;
	private String caseNo;
	
	public CaseFilter() {
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	
	@Override
	public String toString() {
		return "{processId:"+processId+",userId:"+userId+",caseNo:"+caseNo+"}";
	}
}
