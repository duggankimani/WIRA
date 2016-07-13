package com.wira.commons.shared.models;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class SerializableObj implements Serializable, IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String refId;

	public SerializableObj() {
	}
	
	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}
	
}
