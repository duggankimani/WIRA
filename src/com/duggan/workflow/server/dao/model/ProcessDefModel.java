package com.duggan.workflow.server.dao.model;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;

import com.duggan.workflow.shared.model.ProcessDefStatus;

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
	
	@Column(length=2000)
	private String description;
	
	private ProcessDefStatus status;
	
	@OneToMany(mappedBy="processDef")
	private Collection<ADDocType> documentTypes = new HashSet<>();
		
	public ProcessDefModel(){
		status = ProcessDefStatus.INACTIVE;
	}
	
	public ProcessDefModel(Long id, String name, String processId, boolean isArchived,
			String description){
		this.id = id;
		this.name = name;
		this.processId = processId;
		this.isArchived = isArchived;
		this.description = description;
		//this.processDocuments = processDocuments;
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
	
	@Override
	public String toString() {
		String str = "[Name= "+name +"; "+
				"ID= "+id+"; " +
				"Process= "+processId+"]";
		return str;
	}

	public Collection<ADDocType> getDocumentTypes() {
		return documentTypes;
	}

	public void addDocType(ADDocType documentType) {
		documentTypes.add(documentType);
		documentType.setProcessDef(this);
	}
	
	public void clear(){
		for(ADDocType t: documentTypes){
			t.setProcessDef(null);
		}
	}
}
