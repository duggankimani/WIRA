package com.duggan.workflow.shared.responses;

import com.gwtplatform.dispatch.shared.Result;

/**
 * Base Class For All responses
 * 
 * @author duggan
 *
 */
public class BaseResult implements Result{
	
	/**
	 * 0 - OK
	 * 1 - Error
	 */
	int errorCode=0;
	String errorMessage;
	Integer errorId;
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public Integer getErrorId() {
		return errorId;
	}
	public void setErrorId(Integer errorId) {
		this.errorId = errorId;
	}
}
