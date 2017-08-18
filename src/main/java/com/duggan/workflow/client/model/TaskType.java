package com.duggan.workflow.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum TaskType implements IsSerializable{

	DRAFT("drafts", "Drafts"),
//	INPROGRESS("inprog", "In Progress"),
//	APPROVED("approved", "Approved"),
//	REJECTED("rejected", "Rejected"),
	INBOX("appreqnew", "Inbox"),
	MINE("appreqnew", "Mine"),
	QUEUED("appreqnew", "Queued"),
	ALL("appreqnew", "All"),
	COMPLETED("appredone", "Completed"),
	SUSPENDED("suspended", "Suspended"),
	NOTIFICATIONS("notifications", "Notifications"),
	SEARCH("search","Search"),
	PARTICIPATED("participated", "Participated"),
	UNASSIGNED("UnAssigned", "UnAssigned"),
	CASEVIEW("registry", "Case Registry");
	
	String url;
	String title;
	
	private TaskType(String url, String title){
		this.url = url;
		this.title=title;
	}
	
	public String getURL(){
		return url;
	}
	
	public static TaskType getTaskType(String url){
		for(TaskType type: TaskType.values()){
			if(type.url.equals(url)){
				return type;
			}
		}
		
		return null;
	}

	public String getTitle() {

		return title;
	}
	
}
