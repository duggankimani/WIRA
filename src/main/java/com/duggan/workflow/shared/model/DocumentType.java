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
	private String className;
	private Long formId;//start-up form for this process
	private String processId;
	
	public DocumentType(){
		
	}
	
	public DocumentType(Long id, String name, String display, String className){
		this.id = id;
		this.name = name;
		this.display = display;
		this.className=className;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
}
