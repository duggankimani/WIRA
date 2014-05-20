package com.duggan.workflow.shared.model;

import java.io.Serializable;
import java.util.Date;

public class DSConfiguration implements Serializable {

	private static final long serialVersionUID = -2679092191650300187L;
	private Long id;
	private String name;
	private RDBMSType RDBMS;
	private String JNDIName;
	private String Driver;
	private String URL;
	private String user;
	private String password;
	private boolean isJNDI;
	
	private Status status;
	private Date lastModified;

	public DSConfiguration() {
	}

	public void setJNDIName(String JNDIName) {
		this.JNDIName = JNDIName;
	}

	public void setDriver(String Driver) {
		this.Driver = Driver;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getJNDIName() {
		return JNDIName;
	}

	public String getDriver() {
		return Driver;
	}

	public String getURL() {
		return URL;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
	
	public Long getId() {
		return id;
	}

	public Status getStatus() {
		return status;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public boolean isJNDI() {
		return isJNDI;
	}

	public void setJNDI(boolean isJNDI) {
		this.isJNDI = isJNDI;
	}

	public RDBMSType getRDBMS() {
		return RDBMS;
	}

	public void setRDBMS(RDBMSType rDBMS) {
		RDBMS = rDBMS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
