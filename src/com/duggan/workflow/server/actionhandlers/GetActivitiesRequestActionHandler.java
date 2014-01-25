package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.duggan.workflow.server.helper.dao.CommentDaoHelper;
import com.duggan.workflow.server.helper.dao.NotificationDaoHelper;
import com.duggan.workflow.shared.model.Activity;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.duggan.workflow.shared.requests.GetActivitiesRequest;
import com.duggan.workflow.shared.responses.BaseResponse;
import com.duggan.workflow.shared.responses.GetActivitiesResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetActivitiesRequestActionHandler extends
		BaseActionHandler<GetActivitiesRequest, GetActivitiesResponse> {

	@Inject
	public GetActivitiesRequestActionHandler() {
	}

	@Override
	public void execute(GetActivitiesRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		Long documentId = action.getDocumentId();
		List<Activity> activities = new ArrayList<>();		
		activities.addAll(NotificationDaoHelper.getAllNotifications(documentId,
				NotificationType.TASKCOMPLETED_OWNERNOTE,
				NotificationType.APPROVALREQUEST_OWNERNOTE,
				NotificationType.TASKDELEGATED));
				
		activities.addAll(CommentDaoHelper.getAllComments(documentId));		
		Collections.sort(activities);
				
		Map<Activity, List<Activity>> activityMap = new LinkedHashMap<>();
		for(Activity activity: activities){
			if(activity instanceof Notification){
				activityMap.put(activity, null);
			}else{
				Comment comment = (Comment)activity;
				//System.err.println("Adding Comment >>>"+comment.getId()+" :: Parent = "+comment.getParentId());
				//check if this is a child
				
				if(comment.getParentId()==null){
					//possible parent
					activityMap.put(comment, new ArrayList<Activity>());
				}else{
					Comment parent = new Comment();
					parent.setId(comment.getParentId());
					List<Activity> children = activityMap.get(parent);
					
					if(children==null){
						//System.err.println("#############SERVER IGNORING CHILD.............");						
					}else{
						children.add(comment);//activity map loaded						
					}
					
				}
			}
		}
		
		GetActivitiesResponse response = (GetActivitiesResponse)actionResult;
		response.setActivityMap(activityMap);
		
	}
	
	@Override
	public Class<GetActivitiesRequest> getActionType() {
		return GetActivitiesRequest.class;
	}
}
