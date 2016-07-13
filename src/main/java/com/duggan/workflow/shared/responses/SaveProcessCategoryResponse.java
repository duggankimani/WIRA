package com.duggan.workflow.shared.responses;

import com.duggan.workflow.shared.model.ProcessCategory;
import com.wira.commons.shared.response.BaseResponse;



public class SaveProcessCategoryResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ProcessCategory category;
	
	public SaveProcessCategoryResponse() {
		
	}

	public ProcessCategory getCategory() {
		return category;
	}

	public void setCategory(ProcessCategory category) {
		this.category = category;
	}
}
