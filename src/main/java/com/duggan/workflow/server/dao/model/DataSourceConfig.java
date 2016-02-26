package com.duggan.workflow.server.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.duggan.workflow.shared.model.RDBMSType;
import com.duggan.workflow.shared.model.Status;

/**
 * Data Source Configs
 * @author duggan
 *
 */
@Entity
@Table(indexes={@Index(name="idx_ref_id",columnList="refId")})
public class DataSourceConfig extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;	

	@Column(unique=true, nullable=false)
	private String configName;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private RDBMSType RDBMS;
	
	private String JNDIName;
	private String Driver;
	private String URL;
	@Column(name="dbuser")
	private String user;
	@Column(name="dbpass")
	private String password;
	private boolean isJNDI;
	private Status status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RDBMSType getRDBMS() {
		return RDBMS;
	}

	public void setRDBMS(RDBMSType rDBMS) {
		RDBMS = rDBMS;
	}

	public String getJNDIName() {
		return JNDIName;
	}

	public void setJNDIName(String jNDIName) {
		JNDIName = jNDIName;
	}

	public String getDriver() {
		return Driver;
	}

	public void setDriver(String driver) {
		Driver = driver;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isJNDI() {
		return isJNDI;
	}

	public void setJNDI(boolean isJNDI) {
		this.isJNDI = isJNDI;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

}
