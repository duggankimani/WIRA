package com.duggan.workflow.client.ui.notifications;

import static com.duggan.workflow.client.ui.notifications.NotificationsPresenter.NOTE_SLOT;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class NotificationsView extends ViewImpl implements
		NotificationsPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, NotificationsView> {
	}
	
	@UiField HTMLPanel panelNotification;
	
	
	@Inject
	public NotificationsView( final Binder binder) {
		widget = binder.createAndBindUi(this);
		/*
		bodyNotification.addFocusHandler(new FocusHandler() {	
			@Override
			public void onFocus(FocusEvent event) {
				if(isSelected){
					popupContainer.removeStyleName("is-visible");
					isSelected=false;
				}else{
					popupContainer.addStyleName("is-visible");
					isSelected=true;
				}
			}
		});*/
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if(slot == NOTE_SLOT){
			panelNotification.clear();
			//panelNotification.removeStyleName("loading");
			
			if(content!=null){
				panelNotification.add(content);
			}
		}else{
			super.setInSlot(slot, content);
		}
		
	}
	
	@Override
	public void addToSlot(Object slot, Widget content) {
		if(slot == NOTE_SLOT){			
			if(content!=null){
				panelNotification.add(content);
			}
		}else{
			super.addToSlot(slot, content);
		}
		
	}

}
