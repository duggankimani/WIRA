package com.duggan.workflow.server.actionhandlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.duggan.workflow.server.dao.helper.CommentDaoHelper;
import com.duggan.workflow.server.dao.helper.NotificationDaoHelper;
import com.duggan.workflow.shared.model.Activity;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.duggan.workflow.shared.requests.GetActivitiesRequest;
import com.duggan.workflow.shared.responses.GetActivitiesResponse;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;
import com.wira.commons.shared.response.BaseResponse;

public class GetActivitiesRequestHandler extends
		AbstractActionHandler<GetActivitiesRequest, GetActivitiesResponse> {

	@Inject
	public GetActivitiesRequestHandler() {
	}

	@Override
	public void execute(GetActivitiesRequest action, BaseResponse actionResult,
			ExecutionContext execContext) throws ActionException {
		String docRefId = action.getDocRefId();
		List<Activity> activities = new ArrayList<>();		
		activities.addAll(NotificationDaoHelper.getAllNotificationsByRefId(docRefId,
				NotificationType.TASKCOMPLETED_OWNERNOTE,
				NotificationType.APPROVALREQUEST_OWNERNOTE,
				NotificationType.TASKDELEGATED, NotificationType.FILE_UPLOADED));
				
		//activities.addAll(CommentDaoHelper.getAllCommentsByDocumentId(documentId));
		activities.addAll(CommentDaoHelper.getAllCommentsByDocRefId(docRefId));	
		Collections.sort(activities);
				
		HashMap<Activity, ArrayList<Activity>> activityMap = new LinkedHashMap<>();
		
		for(Activity activity: activities){
			if(activity instanceof Notification || !action.isCategorized()){
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
