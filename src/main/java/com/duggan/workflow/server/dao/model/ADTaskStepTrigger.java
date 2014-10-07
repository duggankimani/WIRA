package com.duggan.workflow.server.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.duggan.workflow.shared.model.TriggerType;

@Entity
public class ADTaskStepTrigger extends PO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="taskstepId", referencedColumnName="id",nullable=false)
	private TaskStepModel taskStep;
	
	@ManyToOne
	@JoinColumn(name="triggerid",referencedColumnName="id",nullable=false)
	private ADTrigger trigger;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private TriggerType type;
	
	public ADTaskStepTrigger() {
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TaskStepModel getTaskStep() {
		return taskStep;
	}
	public void setTaskStep(TaskStepModel taskStep) {
		this.taskStep = taskStep;
	}
	public ADTrigger getTrigger() {
		return trigger;
	}
	public void setTrigger(ADTrigger trigger) {
		this.trigger = trigger;
	}
	public TriggerType getType() {
		return type;
	}
	public void setType(TriggerType type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object obj) {
		ADTaskStepTrigger other = (ADTaskStepTrigger)obj;		
		return other.trigger.equals(trigger) && other.taskStep.equals(taskStep);
		
	}
}
