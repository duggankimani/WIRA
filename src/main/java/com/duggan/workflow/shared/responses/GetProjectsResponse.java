package com.duggan.workflow.shared.responses;

import java.util.ArrayList;

import com.duggan.workflow.shared.model.ProjectDto;
import com.wira.commons.shared.response.BaseResponse;


public class GetProjectsResponse extends BaseResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ArrayList<ProjectDto> projects = new ArrayList<ProjectDto>();
	
	public GetProjectsResponse() {
		
	}

	public ArrayList<ProjectDto> getProjects() {
		return projects;
	}

	public void setProjects(ArrayList<ProjectDto> projects) {
		this.projects = projects;
	}

}
