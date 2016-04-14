package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author duggan
 *
 */
public class UserGroup implements Serializable, Listable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private String fullName;
	
	private List<PermissionPOJO> permissions = new ArrayList<PermissionPOJO>();
	
	public UserGroup(){
	}

	public UserGroup(String name){
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof UserGroup)){
			return false;
		}
		
		UserGroup other = (UserGroup)obj;
		
		return other.name.equals(name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public String toString() {
		
		return name;
	}

	@Override
	public String getDisplayName() {
		
		return fullName;
	}

	public List<PermissionPOJO> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionPOJO> permissions) {
		this.permissions = permissions;
	}
}
