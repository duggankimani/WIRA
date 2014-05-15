package com.duggan.workflow.shared.responses;

import com.gwtplatform.dispatch.rpc.shared.Result;

/**
 * Base Class For All responses
 * 
 * @author duggan
 *
 */
public class BaseResponse implements Result{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 0 - OK
	 * 1 - Error
	 */
	int errorCode=0;
	String errorMessage;
	Long errorId;
	
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
	public Long getErrorId() {
		return errorId;
	}
	public void setErrorId(Long errorId) {
		this.errorId = errorId;
	}
}
