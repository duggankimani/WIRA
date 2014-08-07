package com.duggan.workflow.server.dao.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

public class ADOutputDoc extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String description;
	private String code;
	
	@OneToOne(mappedBy="outputDoc",orphanRemoval=true)
	private LocalAttachment attachment;
	
	public ADOutputDoc() {
	}
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public LocalAttachment getAttachment() {
		return attachment;
	}

	public void setAttachment(LocalAttachment attachment) {
		this.attachment = attachment;
	}
	
	

}
