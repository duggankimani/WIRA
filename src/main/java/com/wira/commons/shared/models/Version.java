package com.wira.commons.shared.models;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Build Info
 * 
 * @author duggan
 *
 */
public class Version implements Serializable,IsSerializable {

	private static final long serialVersionUID = 4362162629668925874L;
	private String version;
	private Date created;
	private String date;

	public Version() {
	}

	public Version(String buildVersion, Date created, String buildDate) {
		this.version = buildVersion;
		this.created = created;
		this.date = buildDate;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getVersion() {
		return version;
	}

	public Date getCreated() {
		return created;
	}

	public String getDate() {
		return date;
	}

	public void from(Version version) {
		setCreated(version.getCreated());
		setDate(version.getDate());
		setVersion(version.getVersion());
	}
}
