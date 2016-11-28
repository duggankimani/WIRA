package com.duggan.workflow.server.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.persistence.Table;

import com.duggan.workflow.shared.model.TriggerType;

@XmlRootElement(name="steptrigger")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(indexes={@Index(name="idx_ref_id",columnList="refId")})
public class ADTaskStepTrigger extends PO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="taskstepId", referencedColumnName="id",nullable=false)
	private TaskStepModel taskStep;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="triggerid",referencedColumnName="id",nullable=false)
	private ADTrigger trigger;
	
	@XmlAttribute
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private TriggerType type;
	
	@XmlAttribute
	@Transient
	private String triggerName;
	
	private String condition;
	
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

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

}
