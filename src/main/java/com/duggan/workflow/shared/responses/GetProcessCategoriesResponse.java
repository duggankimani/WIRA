package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.ProcessCategory;
import com.wira.commons.shared.response.BaseResponse;


public class GetProcessCategoriesResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ArrayList<ProcessCategory> categories = new ArrayList<ProcessCategory>();
	
	public GetProcessCategoriesResponse() {
		
	}

	public ArrayList<ProcessCategory> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<ProcessCategory> categories) {
		this.categories = categories;
	}

}
