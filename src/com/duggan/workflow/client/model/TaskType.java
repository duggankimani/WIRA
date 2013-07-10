package com.duggan.workflow.client.model;

public enum TaskType {

	DRAFT("drafts", "Drafts"),
	INPROGRESS("inprog", "Sent"),
	APPROVED("approved", "Approved"),
	REJECTED("rejected", "Rejected"),
	APPROVALREQUESTNEW("appreqnew", "New Requests"),
	APPROVALREQUESTDONE("appredone", "Completed Approvals"),
	FLAGGED("flagged", "Flagged");
	
	String url;
	String title;
	
	private TaskType(String url, String title){
		this.url = url;
		this.title=title;
	}
	
	public String getDisplayName(){
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
