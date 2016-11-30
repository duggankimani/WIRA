package com.duggan.workflow.server.dao.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.duggan.workflow.shared.model.ProjectDto;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;
import com.google.gwt.dev.json.JsonObject;

public class RepositoryDaoHelper {

	Logger logger = Logger.getLogger(RepositoryDaoHelper.class); 
	
	/**
	 * Refs: 
	 * http://mswiderski.blogspot.co.ke/2015/09/unified-kie-execution-server-part-2.html
	 * 
	 * 
	 */
	public void getRepositories(){
		RestClient client = RestClient.getInstance();
		String json = client.executeGet("/repositories", null);
		logger.debug(">> Repositories - "+json);
	}
	
	public ArrayList<ProjectDto> getProjects(){
		Object repo = SettingsDaoHelper.getSettingValue(SETTINGNAME.ORG_REPO);
		if(repo==null){
			throw new RuntimeException("Repository not declared in settings");
		}
		
		return getProjects(repo.toString());
	}
	
	public ArrayList<ProjectDto> getProjects(String repositoryName){
		RestClient client = RestClient.getInstance();
		String json = client.executeGet("/repositories/"+repositoryName+"/projects", null);
		logger.debug(repositoryName+" >> Projects - "+json);
		//[{"name":"Procurement","description":null,"groupId":"ke.co.workpoint","version":"1.0"}]
		ArrayList<ProjectDto> dtos = new ArrayList<ProjectDto>();
		
		try {
			JSONArray arr = new JSONArray(json);
			for(int i=0; i<arr.length(); i++){
				JSONObject project = arr.getJSONObject(i);
				ProjectDto dto = new ProjectDto();
				dto.setName(project.getString("name"));
				dto.setDescription(project.getString("description"));
				dto.setGroupId(project.getString("groupId"));
				dto.setVersion(project.getString("version"));
				dto.setRefId(dto.getGroupId()+":"+dto.getName()+":"+dto.getVersion());
				dtos.add(dto);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dtos;
	}
}
