package com.duggan.workflow.server.dao.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.Table;

import com.duggan.workflow.shared.model.DocStatus;

@Entity
@Table(name="localdocument", indexes = {
		@javax.persistence.Index(name = "idx_subject", columnList = "subject"),
		@javax.persistence.Index(name = "idx_docdate", columnList = "documentDate")
		})
public class DocumentModel extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(length=200,nullable=false, unique=true)
	private String subject;//caseNo
	
	@Column(length=350,nullable=false)
	private String description;
	
	@ManyToOne
	@JoinColumn(name="docType", referencedColumnName="id")
	private ADDocType type;
	
	private Date documentDate;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="document")
	private List<LocalAttachment> attachment;
	
	protected Integer priority;
	
	private String partner;
	
	private String value;
	
	@Enumerated(EnumType.STRING)
	private DocStatus status;
	
	private Long processInstanceId;
	
	private String processId;
	
	private Long sessionId;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="document",cascade={CascadeType.REMOVE,CascadeType.PERSIST})
	private Collection<ADValue> values = new HashSet<>(); 
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="document", cascade={CascadeType.REMOVE,CascadeType.PERSIST})
	private Collection<DetailModel> details = new HashSet<>();
	
	public DocumentModel(){
		
	}
	
	public DocumentModel(Long id, String subject, String description, ADDocType type){
		this.id=id;
		this.subject=subject;
		this.description=description;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ADDocType getType() {
		return type;
	}

	public void setType(ADDocType type) {
		this.type = type;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public DocStatus getStatus() {
		return status;
	}

	public void setStatus(DocStatus status) {
		this.status = status;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}
	
	@Override
	public String toString() {
		return type+" - "+subject+" - "+getCreated()+" - "+getCreatedBy();
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	
	public void addValue(ADValue value){
		if(value!=null){
			value.setDocument(this);
		}
		
		values.add(value);
	}
	
	public void addDetail(DetailModel model){
		model.setDocument(this);
		details.add(model);
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public void setValues(Collection<ADValue> values) {
		this.values = values;
	}

}
