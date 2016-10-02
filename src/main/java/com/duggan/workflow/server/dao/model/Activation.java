package com.duggan.workflow.server.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes={@Index(name="idx_activation_userref", columnList="userRefId")})
public class Activation extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String userRefId;

	public Activation() {
	}

	public Activation(String userRefId){
		this.userRefId = userRefId;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserRefId() {
		return userRefId;
	}

	public void setUserRefId(String userRefId) {
		this.userRefId = userRefId;
	}
}
