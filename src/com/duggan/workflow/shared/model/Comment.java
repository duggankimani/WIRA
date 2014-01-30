package com.duggan.workflow.shared.model;

import java.util.Date;

public class Comment extends Activity {

	private static final long serialVersionUID = 6882858941033696924L;
	private String comment;
	private Long id;
	private HTUser createdBy;
	private Date created;
	private String updatedBy;
	private Date updated;
	private String userId;
	private Long documentId;
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
	public HTUser getCreatedBy() {
		return createdBy;
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
	
	@Override
	public String getStatement() {

		return comment;
	}
	@Override
	public HTUser getTargetUserId() {
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
			return (id+getClass().getName()).hashCode();
		}
		
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {	
		if(!(obj instanceof Comment)){
			return false;
		}
		
		Comment other = (Comment)obj;
		
		if(id!=null && other.id!=null){
			return id.equals(other.id);
		}
		
		return super.equals(obj);
	}
	public void setCreatedBy(HTUser createdBy) {
		this.createdBy = createdBy;
	}
}
