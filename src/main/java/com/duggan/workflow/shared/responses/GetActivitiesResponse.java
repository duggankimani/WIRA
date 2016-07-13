package com.duggan.workflow.shared.responses;

import java.util.ArrayList;
import java.util.HashMap;

import com.duggan.workflow.shared.model.Activity;
import com.wira.commons.shared.response.BaseResponse;

public class GetActivitiesResponse extends BaseResponse {

	public HashMap<Activity, ArrayList<Activity>> activityMap;
	
	public GetActivitiesResponse() {
	}

	public HashMap<Activity, ArrayList<Activity>> getActivityMap() {
		return activityMap;
	}

	public void setActivityMap(HashMap<Activity, ArrayList<Activity>> activityMap) {
		this.activityMap = activityMap;
	}
}
