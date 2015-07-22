package com.duggan.workflow.server.dao.model;


import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.duggan.workflow.shared.model.Status;

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
	
	private Status status;
	
	@OneToMany(mappedBy="processDef", cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private Collection<ADDocType> documentTypes = new HashSet<>();
	
	@OneToMany(mappedBy="processDef", cascade=CascadeType.ALL)
	private Collection<TaskStepModel> taskSteps = new HashSet<>();
		
	public ProcessDefModel(){
		status = Status.INACTIVE;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
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

	public Collection<TaskStepModel> getTaskSteps() {
		return taskSteps;
	}

	public void addTaskStep(TaskStepModel step) {
		step.setProcessDef(this);
		taskSteps.add(step);
	}
}
