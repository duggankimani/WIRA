package com.duggan.workflow.shared.model;

import java.util.Date;

public class Comment extends Activity {

	private static final long serialVersionUID = 6882858941033696924L;
	private String comment;
	private Long id;
	private String createdBy;
	private Date created;
	private String updatedBy;
	private Date updated;
	private String userId;
	private Long documentId;
	private HTUser createdByUser;
	private Long parentId;
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public Long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdby) {
		this.createdBy = createdby;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public HTUser getCreatedByUser() {
		return createdByUser;
	}
	public void setCreatedByUser(HTUser createdByUser) {
		this.createdByUser = createdByUser;
	}
	
	@Override
	public String getStatement() {

		return comment;
	}
	@Override
	public String getTargetUserId() {
		return null;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@Override
	public int hashCode() {
		
		if(this.id!=null){
			return (id+comment).hashCode();
		}
		
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {	
		Comment other = (Comment)obj;
		
		if(id!=null && other.id!=null){
			return id.equals(other.id) && comment.equals(other.comment);
		}
		
		return super.equals(obj);
	}
}
