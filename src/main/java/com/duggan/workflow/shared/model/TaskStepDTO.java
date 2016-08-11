package com.duggan.workflow.shared.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TaskStepDTO implements Serializable, IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long nodeId;
	private String stepName;
	
	private int sequenceNo;
	private MODE mode;
	private String condition;
	private Long processDefId;
	private String processRefId;
	
	private String formName;
	private String formRefId;
	
	private String outputDocName;
	private Long outputDocId;
	private boolean isActive = true;
	private transient TriggerType triggerType;
	
	public TaskStepDTO() {
	
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public int getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
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
	public Long getProcessDefId() {
		return processDefId;
	}
	public void setProcessDefId(Long processDefId) {
		this.processDefId = processDefId;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getOutputDocName() {
		return outputDocName;
	}
	public void setOutputDocName(String outputDocName) {
		this.outputDocName = outputDocName;
	}
	
	public Long getOutputDocId() {
		return outputDocId;
	}
	public void setOutputDocId(Long outputDocId) {
		this.outputDocId = outputDocId;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public TriggerType getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(TriggerType triggerType) {
		this.triggerType = triggerType;
	}
	
	public TaskStepDTO clone() {
		
		TaskStepDTO dto = new TaskStepDTO();
		dto.setActive(isActive);
		dto.setCondition(condition);
		dto.setFormRefId(formRefId);
		dto.setFormName(formName);
		dto.setId(id);
		dto.setMode(mode);
		dto.setNodeId(nodeId);
		dto.setOutputDocId(outputDocId);
		dto.setOutputDocName(outputDocName);
		dto.setProcessDefId(processDefId);
		dto.setProcessRefId(processRefId);
		dto.setSequenceNo(sequenceNo);
		dto.setStepName(stepName);
		dto.setTriggerType(triggerType);
		
		return dto;
	}
	public String getProcessRefId() {
		return processRefId;
	}
	public void setProcessRefId(String processRefId) {
		this.processRefId = processRefId;
	}
	public String getFormRefId() {
		return formRefId;
	}
	public void setFormRefId(String formRefId) {
		this.formRefId = formRefId;
	}
}
