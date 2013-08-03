package com.duggan.workflow.client.ui.notifications.note;

import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;

public class NotePresenter extends
		PresenterWidget<NotePresenter.MyView> {

	public interface MyView extends View {
		
		HasClickHandlers getDocumentBtn();

		void setValues(String subject, DocType documentType,
				NotificationType notificationType, String owner,
				String targetUserId, String time, boolean isRead);
	}

	private Notification notification;

	@Inject PlaceManager placeManager;
	
	@Inject
	public NotePresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getDocumentBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//save read
				getView().setValues(notification.getSubject(),
						notification.getDocumentType(),
						notification.getNotificationType(),
						notification.getOwner(),
						notification.getTargetUserId(), "10 mins",
						true);
				//open 
				//placeManager.revealDefaultPlace();
			}
		});
	}
	
	public void setNotification(Notification notification){
		this.notification = notification;
		getView().setValues(notification.getSubject(),
				notification.getDocumentType(),
				notification.getNotificationType(),
				notification.getOwner(),
				notification.getTargetUserId(), "10 mins",
				notification.IsRead()==null? false: notification.IsRead());
	}
}
