package com.duggan.workflow.shared.model;

import java.io.Serializable;

import com.wira.commons.shared.models.Listable;
import com.wira.commons.shared.models.SerializableObj;

public class OutputDocument extends SerializableObj implements Serializable, Listable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String code;
	private Long id;
	private String description;
	private boolean isActive=true;
	private Long attachmentId;
	private String attachmentName;
	private String path;
	private String processRefId;
	private String template;
	
	public OutputDocument() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Long getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	@Override
	public String getDisplayName() {
	
		return name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getProcessRefId() {
		return processRefId;
	}

	public void setProcessRefId(String processRefId) {
		this.processRefId = processRefId;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}
