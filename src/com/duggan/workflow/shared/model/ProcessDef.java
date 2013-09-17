package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.List;

public class ProcessDef implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	private String processId;
	
	private List<DocType> docTypes;

	public ProcessDef() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public List<DocType> getDocTypes() {
		return docTypes;
	}

	public void setDocTypes(List<DocType> docTypes) {
		this.docTypes = docTypes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
