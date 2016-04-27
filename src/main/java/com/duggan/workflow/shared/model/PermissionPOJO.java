package com.duggan.workflow.shared.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PermissionPOJO implements Serializable, IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PermissionName name;
	private String description;
	private boolean isPermissionGranted;
	
	public PermissionPOJO() {
	}
	
	public PermissionPOJO(String name, String description) {
		this.name = PermissionName.valueOf(name);
		this.description = description;
	}
	
	public PermissionPOJO(String name, String description, boolean isPermissionGranted) {
		this.isPermissionGranted = isPermissionGranted;
		this.name = PermissionName.valueOf(name);
		this.description = description;
	}


	public PermissionName getName() {
		return name;
	}

	public void setName(PermissionName name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPermissionGranted() {
		return isPermissionGranted;
	}

	public void setPermissionGranted(boolean isPermissionGranted) {
		this.isPermissionGranted = isPermissionGranted;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		
		return ((PermissionPOJO) obj).name.equals(name);
	}
}
