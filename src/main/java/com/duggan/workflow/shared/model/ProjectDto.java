package com.duggan.workflow.shared.model;

import com.wira.commons.shared.models.Listable;
import com.wira.commons.shared.models.SerializableObj;

public class ProjectDto extends SerializableObj implements Listable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private String groupId;
	private String version;
	
	public ProjectDto(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String getDisplayName() {
		return groupId+":"+name+":"+version;
	}
	
	@Override
	public boolean equals(Object obj) {
		ProjectDto other = (ProjectDto)obj;
		return groupId.equals(other.groupId) && name.equals(other.name);
	}
}
