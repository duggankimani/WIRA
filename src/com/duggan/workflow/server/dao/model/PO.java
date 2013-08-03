package com.duggan.workflow.server.dao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

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
		
}
