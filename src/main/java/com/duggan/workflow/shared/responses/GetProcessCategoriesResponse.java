package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.shared.model.ProcessCategory;


public class GetProcessCategoriesResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<ProcessCategory> categories = new ArrayList<ProcessCategory>();
	
	public GetProcessCategoriesResponse() {
		
	}

	public List<ProcessCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<ProcessCategory> categories) {
		this.categories = categories;
	}

}
