package com.duggan.workflow.server.rest.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlType(name="detail")
@XmlAccessorType(XmlAccessType.FIELD)
public class Detail {

	private String name;
	
	@XmlJavaTypeAdapter(value=MapAdapter.class)
	private Map<String, Object> details;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}
	
}
