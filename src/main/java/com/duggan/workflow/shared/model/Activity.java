package com.duggan.workflow.shared.model;

import java.util.Date;

import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.SerializableObj;

public abstract class Activity extends SerializableObj implements Comparable<Activity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract HTUser getCreatedBy();
	public abstract Date getCreated();
	public abstract HTUser getTargetUserId();
	public abstract String getStatement();
	
	@Override
	public int compareTo(Activity o) {
		return this.getCreated().compareTo(o.getCreated());
	}
	
}
