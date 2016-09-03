package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import com.duggan.workflow.server.dao.hibernate.JsonType;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.SerializableObj;

/**
 * DTO for {@link com.duggan.workflow.server.dao.model.DocumentModel}
 * 
 * @author duggan
 *
 */
@XmlSeeAlso({Document.class,DocumentLine.class,StringValue.class,DoubleValue.class,
	IntValue.class,BooleanValue.class,DateValue.class,Doc.class,SerializableObj.class
	})
@XmlRootElement(name="document")
@XmlAccessorType(XmlAccessType.FIELD)
public class Document extends Doc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	private Long id;

	private DocumentType type;

	private HTUser owner;

	protected String description;

	protected Date created;

	protected Date dateSubmitted;

	protected Date documentDate;

	protected Integer priority;

	private String partner;

	private String value;

	private DocStatus status;

	private Date dateDue;

	private Long processInstanceId;

	private Long sessionId;

	public Document() {
		// serialization
		// super();
	}

	public DocumentType getType() {
		return type;
	}

	public void setType(DocumentType type) {
		this.type = type;
	}

	public HTUser getOwner() {
		return owner;
	}

	public void setOwner(HTUser owner) {
		this.owner = owner;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
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

	public HTSummary toTask() {
		HTSummary summary = new HTSummary();
		summary.setCreated(new Date());
		// summary.setStartDateDue(dateDue);
		summary.setDescription(description);
		summary.setDocumentRef(id);
		summary.setPriority(priority);
		summary.setCaseNo(caseNo);
		summary.setTaskName(type.getDisplayName() + " - " + caseNo);

		return summary;
	}

	public Date getDateDue() {
		return dateDue;
	}

	public void setDateDue(Date dateDue) {
		this.dateDue = dateDue;
	}

	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public void setValues(HashMap<String, Value> values) {
		for (String key : values.keySet()) {
			if (key == null) {
				continue;
			}
			Object val = null;

			Value fieldValue = values.get(key);

			try {
				if (fieldValue != null) {
					val = fieldValue.getValue();
				}
				if (key.equals("description"))
					setDescription(val == null ? null : val.toString());

				if (key.equals("dueDate"))
					setDateDue(val == null ? null : (Date) val);

				if (key.equals("docDate"))
					setDocumentDate(val == null ? null : (Date) val);

				if (key.equals("partner"))
					setPartner(val == null ? null : val.toString());

				if (key.equals("subject")) {
					setCaseNo(val == null ? null : val.toString());
				}

				if (key.equals("docType"))
					setType(val == null ? null : (DocumentType) val);

				if (key.equals("value"))
					setValue(val == null ? null : val.toString());
			} catch (Exception e) {
			}

			setValue(key, values.get(key));
		}

		// super.setValues(values);
	}

	@Override
	public String toString() {

		return "DocRefId = " + getRefId() + ", subject=" + caseNo;
	}

	public Document clone() {
		return clone(false);
	}

	public Document clone(boolean fullClone) {

		Document document = new Document();
		document.setRefId(getRefId());
		document.setCreated(created);
		document.setDateDue(dateDue);
		document.setCaseNo(caseNo);
		document.setDescription(description);
		document.setDocumentDate(documentDate);
		document.setOwner(owner);
		document.setPartner(partner);
		document.setPriority(priority);
		document.setProcessInstanceId(processInstanceId);
		document.setSessionId(sessionId);
		document.setStatus(status);
		document.setType(type);
		document.setValue(value);

		HashMap<String, Value> vals = new HashMap<String, Value>();

		HashMap<String, Value> pv = getValues();
		for (String key : pv.keySet()) {
			Value v = pv.get(key);
			if (v != null)
				vals.put(key, v.clone(fullClone));
		}
		document.setValues(vals);

		for (String key : details.keySet()) {
			ArrayList<DocumentLine> linez = details.get(key);
			Collections.reverse(linez);
			for (DocumentLine line : linez) {
				DocumentLine clone = line.clone(fullClone);
				clone.setName(key);
				document.addDetail(clone);
			}

		}

		return document;
	}

	// public HashMap<String, Value>

	@Override
	public Long getDocumentId() {

		return id;
	}

	public Date getDateSubmitted() {
		return dateSubmitted;
	}

	public void setDateSubmitted(Date dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}
	
	@Override
	public String getProcessName() {
		if(super.getProcessName()==null && type!=null){
			return type.getDisplayName();
		}
		return super.getProcessName();
	}
	
	/**
	 * Duggan - 12-08-2016
	 * This method has an impact on {@link JsonType} equality 
	 * and influences when Hibernate auto updates rows.
	 * 
	 * This therefore means the method should be all encompassing (Include more/ all fields) to enable hibernate 
	 * determine when the object has changed.
	 */
	@Override
	public boolean equals(Object obj) {
		
		Document other = (Document)obj;
		
		if(this.description!=null && !this.description.equals(other.description)){
			return false;
		}
		
		if(this.created!=null && !this.created.equals(other.created)){
			return false;
		}
		
		if(this.dateSubmitted!=null && !this.dateSubmitted.equals(other.dateSubmitted)){
			return false;
		}
		
		if(this.documentDate!=null && !this.documentDate.equals(other.documentDate)){
			return false;
		}
		
		if(this.priority!=null && !this.priority.equals(other.priority)){
			return false;
		}
		
		if(this.partner!=null && !this.partner.equals(other.partner)){
			return false;
		}
		
		if(this.status!=null && !this.status.equals(other.status)){
			return false;
		}
		
		if(this.dateDue!=null && !this.dateDue.equals(other.dateDue)){
			return false;
		}
		
		if(this.processInstanceId!=null && !this.processInstanceId.equals(other.processInstanceId)){
			return false;
		}
		
		if(this.sessionId!=null && !this.sessionId.equals(other.sessionId)){
			return false;
		}
		
		boolean equal = super.equals(obj);
				
		return equal;
	}
}
