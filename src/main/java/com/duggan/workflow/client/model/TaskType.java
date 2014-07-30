package com.duggan.workflow.client.model;

public enum TaskType {

	DRAFT("drafts", "Drafts"),
	INPROGRESS("inprog", "In Progress"),
	APPROVED("approved", "Approved"),
	REJECTED("rejected", "Rejected"),
	INBOX("appreqnew", "Inbox"),
	APPROVALREQUESTDONE("appredone", "Completed"),
	SUSPENDED("suspended", "Suspended"),
	NOTIFICATIONS("notifications", "Notifications"),
	SEARCH("search","Search"),
	PARTICIPATED("participated", "Participated");
	
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
