package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Attachment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private boolean archived;
	private Long documentid;
	private Long size;
	private String sizeStr;
	private String contentType;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isArchived() {
		return archived;
	}
	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	public Long getDocumentid() {
		return documentid;
	}
	public void setDocumentid(Long documentid) {
		this.documentid = documentid;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getSizeAsStr() {
		return sizeStr;
	}
	public void setSizeStr(String size) {
		this.sizeStr = size;
	}
}