package com.duggan.workflow.client.ui.notifications.note;

import com.duggan.workflow.shared.model.ApproverAction;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.NotificationType;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class NoteView extends ViewImpl implements NotePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, NoteView> {
	}

	@UiField
	Anchor aDocument;

	APPROVALREQUEST_APPROVERNOTE_TEMPLATE Template1 = GWT
			.create(APPROVALREQUEST_APPROVERNOTE_TEMPLATE.class);
	APPROVALREQUEST_OWNERNOTE_TEMPATE Template2 = GWT
			.create(APPROVALREQUEST_OWNERNOTE_TEMPATE.class);
	TASKCOMPLETED_APPROVERNOTE_TEMPLATE Template3 = GWT
			.create(TASKCOMPLETED_APPROVERNOTE_TEMPLATE.class);
	TASKCOMPLETED_OWNERNOTE_TEMPLATE Template4 = GWT
			.create(TASKCOMPLETED_OWNERNOTE_TEMPLATE.class);

	@Inject
	public NoteView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setValues(String subject, DocType documentType,
			NotificationType notificationType, String owner,
			String targetUserId, String time, boolean isRead,
			String createdBy, ApproverAction approverAction,
			 Long processInstanceId) {
		
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
		
		String approver = createdBy;
		
		SafeHtml safeHtml = null;
		switch (notificationType) {
		case APPROVALREQUEST_APPROVERNOTE:
			safeHtml = Template1.render(subject, owner, time);
			break;

		case APPROVALREQUEST_OWNERNOTE:
			safeHtml = Template2.render(subject, time);
			break;

		case TASKCOMPLETED_APPROVERNOTE:
			safeHtml = Template3.render(subject, owner, time, action, 
					ApproverAction.APPROVED.equals(approverAction)? "icon-check": "icon-remove-sign");
			break;
		case TASKCOMPLETED_OWNERNOTE:
			safeHtml = Template4.render(subject, approver, time, action, 
					ApproverAction.APPROVED.equals(approverAction)? "icon-check": "icon-remove-sign");
			break;
		default:
			//safeHtml= "<p>You have no new notification</p>";
			break;
		}
		
		if(safeHtml!=null){
			aDocument.setHTML(safeHtml);
		}
		
		if(processInstanceId!=null){
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
				"<span class=\"bluename\">{0}</span>"+
				" succesfully submitted for approval "
				+ " <span class=\"time\"><i class=\"icon-time\">{1}</i></span>")
		public SafeHtml render(String subject, String time);

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
				+ " <span class=\"time\"><i class=\"icon-time\"> {2}</i></span>")
		public SafeHtml render(String subject, String approver, String time,
				String action, String style);

		// e.g Your Invoice INV/001/2013 was approved/Denied by HOD (1hr ago)
	}

	// others later

}
