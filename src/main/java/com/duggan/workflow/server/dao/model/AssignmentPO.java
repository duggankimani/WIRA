package com.duggan.workflow.server.dao.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.duggan.workflow.shared.model.AssignmentFunction;

@Entity
public class AssignmentPO extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Long nodeId;//Nullable to Accommodate initial input form
	
	private String stepName;
	
	@ManyToOne
	@JoinColumn(name="processDefId")
	private ProcessDefModel processDef;
	
	private AssignmentFunction assignmentFunction = AssignmentFunction.SELFSERVICE_ASSIGNMENT;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AssignmentFunction getAssignmentFunction() {
		return assignmentFunction;
	}

	public void setAssignmentFunction(AssignmentFunction assignmentFunction) {
		this.assignmentFunction = assignmentFunction;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public ProcessDefModel getProcessDef() {
		return processDef;
	}

	public void setProcessDef(ProcessDefModel processDef) {
		this.processDef = processDef;
	}

}
