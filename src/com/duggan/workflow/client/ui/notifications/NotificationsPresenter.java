package com.duggan.workflow.client.ui.notifications;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.duggan.workflow.client.service.ServiceCallback;
import com.duggan.workflow.client.ui.notifications.note.NotePresenter;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HasWidgets;

public class NotificationsPresenter extends
		PresenterWidget<NotificationsPresenter.MyView> {

	public interface MyView extends View {
	}

	@Inject
	DispatchAsync dispatcher;

	IndirectProvider<NotePresenter> notesFactory;
	
	public static final Object NOTE_SLOT = new Object();
	
	@Inject
	public NotificationsPresenter(final EventBus eventBus, final MyView view,
			Provider<NotePresenter> noteProvider) {
		super(eventBus, view);
		notesFactory = new StandardProvider(noteProvider);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	@Override
	protected void onReveal() {
		super.onReveal();

		List<Notification> notes = new ArrayList<Notification>();
		for (int i = 0; i < 10; i++) {
			Notification notification = new Notification();
			notification.setCreated(new Date());
			notification.setDocumentId(100L);
			notification.setDocumentType(DocType.INVOICE);
			notification.setId(1L);
			notification
					.setNotificationType(NotificationType.APPOVALREQUEST_OWNERNOTE);
			notification.setOwner("Calcacuervo");
			notification.setRead(false);
			notification.setSubject("INV/20001/13");
			notification.setTargetUserId("Calcacuervo");
			notes.add(notification);
		}
		
		//clear
		NotificationsPresenter.this.setInSlot(NOTE_SLOT, null);
		for(final Notification note: notes){
			notesFactory.get(new ServiceCallback<NotePresenter>() {
				@Override
				public void processResult(NotePresenter result) {
					result.setNotification(note);
					NotificationsPresenter.this.addToSlot(NOTE_SLOT, result);
				}
			});
		}
	}
}
