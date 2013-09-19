package com.duggan.workflow.shared.model;

import java.io.Serializable;

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
}
