package com.duggan.workflow.client.ui.notifications.note;

import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.ApproverAction;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.NotificationType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class NoteView extends ViewImpl implements NotePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, NoteView> {
	}

	@UiField
	Anchor aDocument;
	
	@UiField
	HTMLPanel divActivity;
	
	APPROVALREQUEST_APPROVERNOTE_TEMPLATE Template1 = GWT
			.create(APPROVALREQUEST_APPROVERNOTE_TEMPLATE.class);
	APPROVALREQUEST_OWNERNOTE_TEMPATE Template2 = GWT
			.create(APPROVALREQUEST_OWNERNOTE_TEMPATE.class);
	TASKCOMPLETED_APPROVERNOTE_TEMPLATE Template3 = GWT
			.create(TASKCOMPLETED_APPROVERNOTE_TEMPLATE.class);
	TASKCOMPLETED_OWNERNOTE_TEMPLATE Template4 = GWT
			.create(TASKCOMPLETED_OWNERNOTE_TEMPLATE.class);
	TASKCOMPLETED_OWNERNOTE_ACTIVITY_TEMPLATE Template5 = GWT
			.create(TASKCOMPLETED_OWNERNOTE_ACTIVITY_TEMPLATE.class);
	APPROVALREQUEST_OWNERNOTE_ACTIVITY_TEMPATE Template6 = GWT
			.create(APPROVALREQUEST_OWNERNOTE_ACTIVITY_TEMPATE.class);
	
	APPROVALREQUEST_OWNERNOTE_ACTIVITY_TEMPATE Template7 = GWT
			.create(APPROVALREQUEST_OWNERNOTE_ACTIVITY_TEMPATE.class);
	
	TASKDELEGATED_TEMPATE Template8= GWT
			.create(TASKDELEGATED_TEMPATE.class);
	
	TASKDELEGATED_APPROVERNOTE_TEMPLATE Template9 = GWT.create(TASKDELEGATED_APPROVERNOTE_TEMPLATE.class);
	
	@Inject
	public NoteView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setValues(String subject, DocumentType documentType,
			NotificationType notificationType, HTUser ownerObj,
			HTUser targetUser, String time, boolean isRead,
			HTUser createdBy, ApproverAction approverAction,
			 Long processInstanceId, Long documentId, boolean isNotification) {
		
		String prefix = documentType == null ? "Document" : documentType
				.getDisplayName();
		subject = prefix + " " + subject;

		if (isRead) {
			aDocument.removeStyleName("unread");
			aDocument.addStyleName("read");
		} else {
			aDocument.removeStyleName("read");
			aDocument.addStyleName("unread");
		}

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
		
		SafeHtml safeHtml = null;
		SafeHtml safeHtml2 = null;
		switch (notificationType) {
		case APPROVALREQUEST_APPROVERNOTE:
			safeHtml = Template1.render(subject, owner, time);
			break;

		case APPROVALREQUEST_OWNERNOTE:
			if(isNotification)
			safeHtml = Template2.render(owner, subject, time);
			else{
				safeHtml2= Template6.render(owner,subject, time);
			}
			break;

		case TASKCOMPLETED_APPROVERNOTE:
			safeHtml = Template3.render(subject, owner, time, action, 
					ApproverAction.APPROVED.equals(approverAction)? "icon-check": "icon-remove-sign");
			break;
		case TASKCOMPLETED_OWNERNOTE:
			if(isNotification)
			safeHtml = Template4.render(subject, approver, time, action, 
					ApproverAction.APPROVED.equals(approverAction)? "icon-check": "icon-remove-sign");
			else
			safeHtml2 =Template5.render(subject, approver, time, 
					ApproverAction.APPROVED.equals(approverAction)? "icon-check": "icon-remove-sign",action);
			break;
		case TASKDELEGATED:
			
			if(isNotification){
				safeHtml = Template9.render(subject, owner, target, time, action,"icon-signin");
			}else{
				safeHtml2 = Template8.render(approver,subject, target, time);
			}
			
			
			break;
		default:
			return;
			//safeHtml= "<p>You have no new notification</p>";
			//break;
		}
		
		if(safeHtml!=null){
			aDocument.setHTML(safeHtml);
			aDocument.removeStyleName("hidden");
		}
		
		if(safeHtml2!=null){
			divActivity.getElement().setInnerSafeHtml(safeHtml2);	
			divActivity.removeStyleName("hidden");
			aDocument.addStyleName("hidden");
		}
		
		if(documentId!=null){
			aDocument.setHref("#home;type=search;did="+documentId);
		}else if(processInstanceId!=null){
			aDocument.setHref("#home;type=search;pid="+processInstanceId);
		}

	}

	public HasClickHandlers getDocumentBtn() {
		return aDocument;
	}

	interface APPROVALREQUEST_APPROVERNOTE_TEMPLATE extends SafeHtmlTemplates {
		@Template("<i class=\"icon-signin\"></i>"+
				"<span class=\"bluename\" >{0}</span>"+
				
				" Request for approval from <span class=\"bluename\">{1}</span>"
				+ "."
				+ "<span class=\"time\" ><i class=\"icon-time\"> {2}</i></span>")
		public SafeHtml render(String subject, String owner, String time);
		// eg Request for approval - Invoice INV/001/2013 from Calcacuervo. (2
		// mins ago)
	}
	
	interface APPROVALREQUEST_OWNERNOTE_TEMPATE extends SafeHtmlTemplates {
		@Template("<i class=\"icon-signin\"></i>"+
				"<span class=\"bluename\">{1}</span>"+
				" submitted for approval by <span class=\"bluename\">{0}</span>"
				+ " <span class=\"time\"><i class=\"icon-time\">{2}</i></span>")
		public SafeHtml render(String owner, String subject, String time);

		// e.g You have successfuly submitted Invoice INV/001/2013 for approval
		// (10 seconds ago)
	}
	
	interface APPROVALREQUEST_OWNERNOTE_ACTIVITY_TEMPATE extends SafeHtmlTemplates {
		@Template("<div class=\"feed-icon\"><i class=\"icon-signin\"></i></div>"+
				"<div class=\"feed-subject\"><a><span>{0}</span></a>" +
				" submitted <a><span>{1}</span></a>" +
				" for approval</div>"+
				"<div class=\"feed-actions\">" +
				"<span class=\"time\"><i class=\"icon-time\">{2}</span>" +
				"</div>")
		public SafeHtml render(String owner,String subject, String time);

		// e.g You have successfuly submitted Invoice INV/001/2013 for approval
		// (10 seconds ago)
	}

	interface TASKCOMPLETED_APPROVERNOTE_TEMPLATE extends SafeHtmlTemplates {
		@Template("<i class=\"{4}\"></i>"+
				"<span class=\"bluename\">{0}</span>"+
				" has been {3}. "+
				" (requested by <span class=\"bluename\">{1}</span>.) "
				+ " <span class=\"time\"><i class=\"icon-time\"> {2}</i></span>")
		public SafeHtml render(String subject, String owner, String time,
				String action, String styleName);
		// e.g You approved/denied Invoice INV/001/2013 from calcacuervo (20mins
		// ago)
	}

	interface TASKCOMPLETED_OWNERNOTE_TEMPLATE extends SafeHtmlTemplates {

		@Template("<i class=\"{4}\"></i>"+
				"<span class=\"bluename\">{0}</span>"
				+ " {3} by <span class=\"bluename\">{1}</span>"
				+ " <span class=\"time\"><i class=\"icon-time\">{2}</i></span>")
		public SafeHtml render(String subject, String approver, String time,
				String action, String style);

		// e.g Your Invoice INV/001/2013 was approved/Denied by HOD (1hr ago)
	}

	interface TASKCOMPLETED_OWNERNOTE_ACTIVITY_TEMPLATE extends SafeHtmlTemplates {

		@Template("<div class=\"feed-icon\"><i class=\"{3}\"></i>"+"</div>"+
				"<div class=\"feed-subject\"><a>{1}</a>"+
				" {4} "+"<a>{0}</a></div>"+
				"<div class=\"feed-actions\">" +
				"<span class=\"time\"><i class=\"icon-time\">{2}</i></span>" +
				"</div>"
				)
		public SafeHtml render(String subject, String approver, String time, String style ,String action);

		// e.g Your Invoice INV/001/2013 was approved/Denied by HOD (1hr ago)
	}

	interface TASKDELEGATED_TEMPATE extends SafeHtmlTemplates {
		@Template("<div class=\"feed-icon\"><i class=\"icon-signin\"></i></div>"+
				"<div class=\"feed-subject\"><a><span>{0}</span></a>" +
				" delegated <a><span>{1}</span></a>" +
				" to <a><span>{2}</span></a></div>"+
				"<div class=\"feed-actions\">" +
				"<span class=\"time\"><i class=\"icon-time\">{3}</span>" +
				"</div>")
		public SafeHtml render(String approver, String subject,
				String targetUser, String time);

		// e.g You have delegated task '' to Salaboy
		// Mariano delegated Invoice/xxy/33 to Salaboy
	}
	
	interface TASKDELEGATED_APPROVERNOTE_TEMPLATE extends SafeHtmlTemplates {
		@Template("<i class=\"{5}\"></i>"+
				"<span class=\"bluename\">{0}</span>"+
				" {4}  "+
				" to <span class=\"bluename\">{2}</span> " +
				" by <span class=\"bluename\">{1}</span>."
				+ " <span class=\"time\"><i class=\"icon-time\"> {3}</i></span>")
		public SafeHtml render(String subject, String targetUser, String createdBy,  String time,
				String action, String styleName);
		// e.g You approved/denied Invoice INV/001/2013 from calcacuervo (20mins
		// ago)
	}


	

	// others later

}
