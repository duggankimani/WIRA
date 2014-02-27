package com.duggan.workflow.client.ui.activityfeed.components;

import static com.duggan.workflow.client.ui.util.DateUtils.getTimeDifferenceAsString;

import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.ApproverAction;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TaskActivity extends Composite {

	private static TaskActivityUiBinder uiBinder = GWT
			.create(TaskActivityUiBinder.class);

	interface TaskActivityUiBinder extends UiBinder<Widget, TaskActivity> {
	}
	
	@UiField SpanElement spnAction;
	//@UiField SpanElement spnSubject;
	@UiField Anchor aDocument;
	
	@UiField SpanElement spnTime;
	@UiField SpanElement spnUser;

	public TaskActivity(Notification notification) {
		initWidget(uiBinder.createAndBindUi(this));
		
		String text = "";
		
		String subject=notification.getSubject();
		DocumentType documentType=notification.getDocumentType();
		
		NotificationType notificationType=notification.getNotificationType();
		HTUser ownerObj=notification.getOwner();
		HTUser targetUser=notification.getTargetUserId();
		HTUser createdBy=notification.getCreatedBy();
		ApproverAction approverAction=notification.getApproverAction();
		Long processInstanceId=notification.getProcessInstanceId();
		String time=getTimeDifferenceAsString(notification.getCreated());
		
		String prefix = documentType.getDisplayName();
		subject = prefix + " " + subject;

		String action = "";
		
		if(approverAction!=null){
			action = approverAction.getAction();
		}
		
		String owner = ownerObj.getSurname();
		if(AppContext.isCurrentUser(ownerObj.getUserId())){
			owner = "You";
		}
		
		String approver = createdBy.getSurname();
		if(AppContext.isCurrentUser(createdBy.getUserId())){
			approver="You";
		}
		
		String target = targetUser.getSurname();
		if(AppContext.isCurrentUser(targetUser.getUserId())){
			target="You";
		}
		
		switch (notificationType) {
		case APPROVALREQUEST_APPROVERNOTE:
			//safeHtml = Template1.render(subject, owner, time);
			spnUser.setInnerText(owner);
			text = "submitted for approval ";
			break;

		case APPROVALREQUEST_OWNERNOTE:
//			if(isNotification)
//				safeHtml = Template2.render(owner, subject, time);
//			else{
//				safeHtml2= Template6.render(owner,subject, time);
//			}
			spnUser.setInnerText(owner);
			text = "forwarded for approval ";
			break;

		case TASKCOMPLETED_APPROVERNOTE:
//			safeHtml = Template3.render(subject, owner, time, action, 
//					ApproverAction.APPROVED.equals(approverAction)? "icon-check": "icon-remove-sign");
			
			spnUser.setInnerText(owner);
			text =action +" ";
			break;
		case TASKCOMPLETED_OWNERNOTE:
//			if(isNotification)
//			safeHtml = Template4.render(subject, approver, time, action, 
//					ApproverAction.APPROVED.equals(approverAction)? "icon-check": "icon-remove-sign");
//			else
//			safeHtml2 =Template5.render(subject, approver, time, 
//					ApproverAction.APPROVED.equals(approverAction)? "icon-check": "icon-remove-sign",action)
			spnUser.setInnerText(approver);
			text =action+" ";
			break;
		case TASKDELEGATED:
			
//			if(isNotification){
//				safeHtml = Template9.render(subject, owner, target, time, action,"icon-signin");
//			}else{
//				safeHtml2 = Template8.render(approver,subject, target, time);
//			}
		
			spnUser.setInnerText(approver);
			text = "delegated to "+target+" "; 
			
			break;
		default:
			//safeHtml= "<p>You have no new notification</p>";
			break;
		}
		
//		if(safeHtml!=null){
//			aDocument.setHTML(safeHtml);
//			aDocument.removeStyleName("hidden");
//		}
//		
//		if(safeHtml2!=null){
//			divActivity.getElement().setInnerSafeHtml(safeHtml2);	
//			divActivity.removeStyleName("hidden");
//			aDocument.addStyleName("hidden");
//		}
//		
		aDocument.setText(subject);
		if(processInstanceId!=null){
			aDocument.setHref("#home;type=search;pid="+processInstanceId);
		}

		spnAction.setInnerText(text);
		//spnSubject.setInnerText(subject);
		spnTime.setInnerText(time);
	}

}
