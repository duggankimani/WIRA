package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

/**
 * 
 * @author duggan
 *
 */
public class HTData implements Serializable {

	private static final long serialVersionUID = -8904914165884376206L;
	private String docType;
	private Long workId;
	private HTUser actualOwner;
	private ArrayList<HTComment> comments;
	private Date completedOn;
	private HTUser createdBy;
	private Long contentId;
	private HTStatus status;
	private String outputType;
	private Date expiryTime;
	private Long parentId;
	private HTAccessType accessType;
	private HTStatus previousStatus;
	private HTAccessType docAccessType;

	public HTData() {
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public Long getWorkId() {
		return workId;
	}

	public void setWorkId(Long workId) {
		this.workId = workId;
	}

	public HTUser getActualOwner() {
		return actualOwner;
	}

	public void setActualOwner(HTUser actualOwner) {
		this.actualOwner = actualOwner;
	}

	public ArrayList<HTComment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<HTComment> comments) {
		this.comments = comments;
	}

	public Date getCompletedOn() {
		return completedOn;
	}

	public void setCompletedOn(Date completedOn) {
		this.completedOn = completedOn;
	}

	public HTUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(HTUser createdBy) {
		this.createdBy = createdBy;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public String getOutputType() {
		return outputType;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

	public Date getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public HTAccessType getAccessType() {
		return accessType;
	}

	public void setAccessType(HTAccessType accessType) {
		this.accessType = accessType;
	}

	public HTStatus getPreviousStatus() {
		return previousStatus;
	}

	public void setPreviousStatus(HTStatus previousStatus) {
		this.previousStatus = previousStatus;
	}

	public HTAccessType getDocAccessType() {
		return docAccessType;
	}

	public void setDocAccessType(HTAccessType docAccessType) {
		this.docAccessType = docAccessType;
	}

	public HTStatus getStatus() {
		return status;
	}

	public void setStatus(HTStatus status) {
		this.status = status;
	}
}
