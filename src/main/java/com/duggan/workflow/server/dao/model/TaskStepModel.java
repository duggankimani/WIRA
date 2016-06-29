package com.duggan.workflow.server.dao.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.duggan.workflow.shared.model.MODE;

@XmlSeeAlso(ADTaskStepTrigger.class)
@XmlRootElement(name="step")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class TaskStepModel extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlTransient
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@XmlAttribute
	private Long nodeId;//Nullable to Accommodate initial input form
	
	@XmlAttribute
	private String stepName;
	
	@XmlAttribute
	private int sequenceNo;
	
	@XmlAttribute
	@Enumerated(EnumType.STRING)
	private MODE mode;
	
	@XmlAttribute
	private String condition;
	
	@XmlTransient
	@OneToOne
	@JoinColumn(name="formid")
	private ADForm form;
	
	@XmlTransient
	@OneToOne
	@JoinColumn(name="outputdocid")
	private ADOutputDoc doc;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="processDefId")
	private ProcessDefModel processDef;
	
	@XmlElementWrapper(name="triggers")
	@OneToMany(fetch=FetchType.LAZY, mappedBy="taskStep")
	@Cascade(value={CascadeType.DELETE, CascadeType.DELETE_ORPHAN, CascadeType.REMOVE})
	private Collection<ADTaskStepTrigger> taskStepTriggers = new HashSet<>();
	
	@XmlAttribute
	@Transient
	private String formRefId;
	
	@XmlAttribute
	@Transient
	private String outputRefId;
	
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
		
		if(nodeId!=null && other.nodeId!=null){
			return nodeId.equals(other.nodeId);
		}
		
		return super.equals(obj);
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public Collection<ADTaskStepTrigger> getTaskStepTriggers() {
		return taskStepTriggers;
	}
	
	public void addTaskStepTrigger(ADTaskStepTrigger trigger){
		trigger.setTaskStep(this);
		taskStepTriggers.remove(trigger);
		taskStepTriggers.add(trigger);
	}

	public String getFormRefId() {
		return formRefId;
	}

	public void setFormRefId(String formRefId) {
		this.formRefId = formRefId;
	}

	public String getOutputRefId() {
		return outputRefId;
	}

	public void setOutputRefId(String outputRefId) {
		this.outputRefId = outputRefId;
	}

}
