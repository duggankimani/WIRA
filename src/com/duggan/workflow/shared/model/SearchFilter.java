package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;
import java.lang.Integer;
import java.lang.Boolean;

public class SearchFilter implements Serializable {

	private static final long serialVersionUID = -8494519116994121416L;
	private String subject;
	private String phrase;
	private Date startDate;
	private Date endDate;
	private Integer priority;
	private Boolean hasAttachment;
	private DocType docType;

	public SearchFilter() {
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setAttachment(Boolean hasAttachment) {
		this.hasAttachment = hasAttachment;
	}

	public String getSubject() {
		return subject;
	}

	public String getPhrase() {
		return phrase;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Integer getPriority() {
		return priority;
	}

	public Boolean hasAttachment() {
		return hasAttachment;
	}

	public DocType getDocType() {
		return docType;
	}

	public void setDocType(DocType docType) {
		this.docType = docType;
	}
}
