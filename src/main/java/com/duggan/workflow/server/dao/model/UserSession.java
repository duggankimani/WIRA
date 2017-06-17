package com.duggan.workflow.server.dao.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

//@Index
@Entity
@Table(indexes={@Index(name="idx_usersession_userref", columnList="userRefId"),
		@Index(name="idx_usersession_cookie", columnList="cookie")})
public class UserSession extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String userRefId;
	private String cookie;

	public UserSession() {
	}

	public UserSession(String userRefId, String cookie) {
		super();

		this.userRefId = userRefId;
		this.cookie = cookie;
	}

	public String getCookie() {
		return cookie;
	}

	public String getUserRefId() {
		return userRefId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}