package com.duggan.workflow.shared.responses;

import com.wira.commons.shared.response.BaseResponse;


/**
 * Base Class For All responses
 * 
 * @author duggan
 *
 */
public class BaseListResponse extends BaseResponse{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalCount;
	
	public BaseListResponse() {
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
