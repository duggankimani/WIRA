package com.duggan.workflow.client.ui.notifications.note;

import static com.duggan.workflow.client.ui.util.DateUtils.getTimeDifferenceAsString;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.shared.model.ApproverAction;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.duggan.workflow.shared.requests.SaveNotificationRequest;
import com.duggan.workflow.shared.responses.SaveNotificationRequestResult;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class NotePresenter extends
		PresenterWidget<NotePresenter.MyView> {

	public interface MyView extends View {
		
		HasClickHandlers getDocumentBtn();

		void setValues(String subject, DocumentType documentType,
				NotificationType notificationType, HTUser owner,
				String targetUserId, String time, boolean isRead, 
				HTUser createdBy,ApproverAction approverAction, Long processInstanceId, boolean isNotification);
	}

	private Notification note;

	@Inject PlaceManager placeManager;
	
	@Inject DispatchAsync dispatcher;
		
	boolean isNotification = false;
	
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
				if(!note.IsRead()){
					SaveNotificationRequest request = new SaveNotificationRequest(note.getId(), true);
					dispatcher.execute(request, new TaskServiceCallback<SaveNotificationRequestResult>() {
						public void processResult(SaveNotificationRequestResult result) {
							Notification notification = result.getNotification();
							String time=getTimeDifferenceAsString(notification.getCreated());
							getView().setValues(notification.getSubject(),
									notification.getDocumentType(),
									notification.getNotificationType(),
									notification.getOwner(),
									notification.getTargetUserId(),time,
									notification.IsRead()==null? false: notification.IsRead(),
											notification.getCreatedBy(),
											notification.getApproverAction(),
											notification.getProcessInstanceId(), isNotification);
//							
//							PlaceRequest request = new PlaceRequest("home")
//							.with("type", TaskType.SEARCH.getURL())
//							.with("pid", notification.getProcessInstanceId()+"");
//							
//							placeManager.revealPlace(request);
						};
					});
					
				}
			}
		});
	}
	
	public void setNotification(Notification notification, boolean isNotification){
		this.isNotification=isNotification;
		this.note = notification;
		String time=getTimeDifferenceAsString(notification.getCreated());
		getView().setValues(notification.getSubject(),
				notification.getDocumentType(),
				notification.getNotificationType(),
				notification.getOwner(),
				notification.getTargetUserId(),time,
				notification.IsRead()==null? false: notification.IsRead(),
						notification.getCreatedBy(),
						notification.getApproverAction(),notification.getProcessInstanceId(), isNotification);
	}
	
}
