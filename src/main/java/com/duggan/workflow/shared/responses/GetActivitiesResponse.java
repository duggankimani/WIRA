package com.duggan.workflow.shared.responses;

import java.util.List;
import java.util.Map;

import com.duggan.workflow.shared.model.Activity;

public class GetActivitiesResponse extends BaseResponse {

	public Map<Activity, List<Activity>> activityMap;
	
	public GetActivitiesResponse() {
	}

	public Map<Activity, List<Activity>> getActivityMap() {
		return activityMap;
	}

	public void setActivityMap(Map<Activity, List<Activity>> activityMap) {
		this.activityMap = activityMap;
	}
}
