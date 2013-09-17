package com.duggan.workflow.server.dao.model;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

@Entity
public class ProcessDefModel extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String processId;
	
	private boolean isArchived;
	
	@OneToMany(mappedBy="processDef", cascade={CascadeType.ALL})
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private List<ProcessDocModel> processDocuments;
		
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

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public boolean isArchived() {
		return isArchived;
	}

	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

	public List<ProcessDocModel> getProcessDocuments() {
		return processDocuments;
	}

	public void setProcessDocuments(List<ProcessDocModel> processDocuments) {
		this.processDocuments = processDocuments;
	}
	
}
