package com.duggan.workflow.shared.model;

import com.wira.commons.shared.models.Listable;


public enum NotificationCategory implements Listable{
	
	EMAILNOTIFICATION("Email Nofication"),
	ACTIVITYFEED("Activity Feed");
	
	
	private String displayName;

	private NotificationCategory(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	
}
