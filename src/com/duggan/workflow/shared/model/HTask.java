package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.lang.Long;
import com.duggan.workflow.shared.model.HTUser;

public class HTask implements Serializable {

	private static final long serialVersionUID = 7213500641662371515L;
	private Integer priority;
	private String description;
	private String name;
	private String subject;
	private Long id;
	private Integer version;
	private HTUser taskInitiator;
	private HTData data;

	public HTask() {
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public void setTaskInitiator(HTUser taskInitiator) {
		this.taskInitiator = taskInitiator;
	}

	public Integer getPriority() {
		return priority;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public String getSubject() {
		return subject;
	}

	public Long getId() {
		return id;
	}

	public Integer getVersion() {
		return version;
	}

	public HTUser getTaskInitiator() {
		return taskInitiator;
	}

	public HTData getData() {
		return data;
	}

	public void setData(HTData data) {
		this.data = data;
	}
}
