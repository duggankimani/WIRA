package com.duggan.workflow.shared.model;

import java.io.Serializable;

public class DocumentType implements Listable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String display;
	
	public DocumentType(){
		
	}
	
	public DocumentType(Long id, String name, String display){
		this.id = id;
		this.name = name;
		this.display = display;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return display;
	}

	public void setDisplayName(String display) {
		this.display = display;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "["+display+"]";
	}
}
