package com.duggan.workflow.server.dao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.duggan.workflow.server.helper.session.SessionHelper;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
@MappedSuperclass
public abstract class PO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlTransient
	@Column
	private String createdBy;
	
	@XmlTransient
	@Column
	private String updatedBy;
	
	@XmlTransient
	@Column
	private Date created;
	
	@XmlTransient
	@Column
	private Date updated;
	
	@Column(columnDefinition="int default 1")
	private int isActive=1;

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

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}		
	
	public Date getLastModified(){
		if(updated!=null){
			return updated;
		}
		
		return created;
	}
}
