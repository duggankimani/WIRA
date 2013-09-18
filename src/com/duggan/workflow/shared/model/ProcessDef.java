package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.Date;
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
	
	private Date lastModified;
	
	private Long fileId;
	
	private String fileName;
	
	private String description;
	
	private ProcessDefStatus status;

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

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProcessDefStatus getStatus() {
		return status;
	}

	public void setStatus(ProcessDefStatus status) {
		this.status = status;
	}
	
}
