package com.duggan.workflow.server.dao.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.duggan.workflow.shared.model.MODE;

@Entity
public class TaskStepModel extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Long nodeId;//Accommodate initial input form
	
	private String stepName;
	
	private int sequenceNo;
	
	@Enumerated(EnumType.STRING)
	private MODE mode;
	
	private String condition;
	
	@OneToOne
	@JoinColumn(name="formid")
	private ADForm form;
	
	@OneToOne
	@JoinColumn(name="outputdocid")
	private ADOutputDoc doc;
	
	@ManyToOne
	@JoinColumn(name="processDefId")
	private ProcessDefModel processDef;
	
	public TaskStepModel() {
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public MODE getMode() {
		return mode;
	}
	public void setMode(MODE mode) {
		this.mode = mode;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public ADForm getForm() {
		return form;
	}
	public void setForm(ADForm form) {
		this.form = form;
	}
	public ADOutputDoc getDoc() {
		return doc;
	}
	public void setDoc(ADOutputDoc doc) {
		this.doc = doc;
	}

	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public ProcessDefModel getProcessDef() {
		return processDef;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public void setProcessDef(ProcessDefModel processDef) {
		this.processDef = processDef;
	}
	
	@Override
	public boolean equals(Object obj) {
		TaskStepModel other = (TaskStepModel)obj;
		
		if(id!=null && other.id!=null){
			return id.equals(other.id);
		}
		
		return super.equals(obj);
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	
}
