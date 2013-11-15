package com.duggan.workflow.server.dao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.duggan.workflow.server.helper.session.SessionHelper;

@MappedSuperclass
public abstract class PO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	private String createdBy;
	
	@Column
	private String updatedBy;
	
	@Column
	private Date created;
	
	@Column
	private Date updated;

	public void init(){
		if(this.getId()==null){
			created = new Date(System.currentTimeMillis());
			createdBy = null;
		}else{
			updated= new Date(System.currentTimeMillis());
			createdBy=null;
		}
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
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
	
	public abstract Long getId();
	
	@PrePersist
	public void onPrePersist(){
		this.created=new Date();
		this.createdBy = SessionHelper.getCurrentUser()==null? null : SessionHelper.getCurrentUser().getUserId();
	}
	
	@PreUpdate
	public void onPreUpdate(){
		this.updated=new Date();
		this.updatedBy = SessionHelper.getCurrentUser()==null? null : SessionHelper.getCurrentUser().getUserId();
	}
		
}
