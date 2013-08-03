package com.duggan.workflow.client.ui.notifications.note;

import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.NotificationType;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class NoteView extends ViewImpl implements
		NotePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, NoteView> {
	}

	@UiField Anchor aDocument;
	@UiField SpanElement spnActor;
	@UiField SpanElement spnSubject;
	@UiField SpanElement spnTime;
	
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
			NotificationType notificationType, String owner, String targetUserId, 
			String time, boolean isRead) {
		spnActor.setInnerText(targetUserId);
		spnSubject.setInnerText(documentType==null? "Document": documentType.getDisplayName()+" "+subject);
		spnTime.setInnerText(time);
		if(isRead){
			aDocument.removeStyleName("unread");
			aDocument.addStyleName("read");
		}else{
			aDocument.removeStyleName("read");
			aDocument.addStyleName("unread");
		}
	}
	
	public HasClickHandlers getDocumentBtn(){
		return aDocument;				
	}
}
