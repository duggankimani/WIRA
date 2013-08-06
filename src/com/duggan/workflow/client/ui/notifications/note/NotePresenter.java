package com.duggan.workflow.client.ui.notifications.note;

import java.util.Date;

import com.duggan.workflow.client.service.TaskServiceCallback;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.duggan.workflow.shared.requests.SaveNotificationRequest;
import com.duggan.workflow.shared.responses.SaveNotificationRequestResult;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.datepicker.client.CalendarUtil;

public class NotePresenter extends
		PresenterWidget<NotePresenter.MyView> {

	public interface MyView extends View {
		
		HasClickHandlers getDocumentBtn();

		void setValues(String subject, DocType documentType,
				NotificationType notificationType, String owner,
				String targetUserId, String time, boolean isRead, String createdBy);
	}

	private Notification note;

	@Inject PlaceManager placeManager;
	
	@Inject DispatchAsync dispatcher;
		
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
									notification.IsRead()==null? false: notification.IsRead(), notification.getCreatedBy());
						};
					});
					
				}
			}
		});
	}
	
	public void setNotification(Notification notification){
		this.note = notification;
		String time=getTimeDifferenceAsString(notification.getCreated());
		getView().setValues(notification.getSubject(),
				notification.getDocumentType(),
				notification.getNotificationType(),
				notification.getOwner(),
				notification.getTargetUserId(),time,
				notification.IsRead()==null? false: notification.IsRead(), notification.getCreatedBy());
	}
	
	public String getTimeDifferenceAsString(Date createdDate){

		if(createdDate==null){
			return "";
		}
	
		Date today =new Date(); 
		long now = today.getTime();
		long created = createdDate.getTime();
		long diff = now -created;
		
		StringBuffer buff = new StringBuffer();
		
		long dayInMillis = 24*3600*1000;
		long hourInMillis = 3600*1000;
		long minInMillis = 60*1000;
		
		if(diff>5*dayInMillis){
			return DateUtils.DATEFORMAT.format(createdDate);
		}
		
		if(diff>dayInMillis){
			int days = CalendarUtil.getDaysBetween(createdDate, today);
			if(days==1){
				return "yesterday";
			}
			
			return days+" days ago";
		}
				
		if(!CalendarUtil.isSameDate(createdDate, new Date())){
			return "yesterday";
		}
		
		if(diff>hourInMillis){
			long hrs = diff/hourInMillis;
			buff.append(hrs+" "+((hrs)==1? "hr":"hrs"));
			diff= diff%hourInMillis;
		}
		
		if(diff>minInMillis){
			long mins = diff/minInMillis;
			buff.append(mins+" "+((mins)==1? "min":"mins"));
			diff= diff%minInMillis;
		}
		
		if(buff.length()==0){
			long secs = diff/1000;
			buff.append(secs+" "+(secs==1? "sec":"secs"));
		}
		
		buff.append(" ago");
		return buff.toString();
	
	}
}
