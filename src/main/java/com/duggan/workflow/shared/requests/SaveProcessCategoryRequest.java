package com.duggan.workflow.shared.requests;

import com.duggan.workflow.shared.model.ProcessCategory;
import com.duggan.workflow.shared.responses.SaveProcessCategoryResponse;
import com.wira.commons.shared.request.BaseRequest;
import com.wira.commons.shared.response.BaseResponse;

public class SaveProcessCategoryRequest extends BaseRequest<SaveProcessCategoryResponse> {

	private ProcessCategory category;
	
	public SaveProcessCategoryRequest() {
	}
	
	public SaveProcessCategoryRequest( ProcessCategory category) {
		this.category = category;
	}
	
	@Override
	public BaseResponse createDefaultActionResponse() {
	
		return new SaveProcessCategoryResponse();
	}

	public ProcessCategory getCategory() {
		return category;
	}
}
