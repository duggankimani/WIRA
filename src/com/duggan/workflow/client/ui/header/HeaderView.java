package com.duggan.workflow.client.ui.header;

import com.gwtplatform.mvp.client.ViewImpl;
import com.github.gwtbootstrap.client.ui.base.InlineLabel;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class HeaderView extends ViewImpl implements HeaderPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HeaderView> {
	}
	
	@UiField SpanElement spnUser;
	@UiField SpanElement spnUserPull;
	@UiField Anchor aNotifications;
	@UiField Anchor aLogout;
	@UiField HTMLPanel notificationsContainer;
	@UiField FocusPanel popupContainer;
	
	boolean isSelected=false;
	@Inject
	public HeaderView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		popupContainer.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(isSelected){
					popupContainer.removeStyleName("is-visible");
					isSelected=false;
					System.err.println("event.....");
				}else{
					popupContainer.addStyleName("is-visible");
					isSelected=true;
				}
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public HasClickHandlers getLogout(){
		return aLogout;
	}
	
	public HasClickHandlers getNotificationsButton(){
		return aNotifications;
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if(slot==HeaderPresenter.NOTIFICATIONS_SLOT){
			notificationsContainer.clear();
			
			if(content!=null){
				notificationsContainer.add(content);
			}
		}
		super.setInSlot(slot, content);
	}
	public void setValues(String user_names){
		
		if(user_names!=null){
			spnUser.setInnerText(user_names);
			spnUserPull.setInnerText(user_names);
		}
		else{
			spnUser.setInnerText("");
			spnUserPull.setInnerText("");
		}
	}

	@Override
	public void setPopupVisible() {
		if(isSelected){
			popupContainer.removeStyleName("is-visible");
			isSelected=false;
			System.err.println("event.....");
		}else{
			popupContainer.addStyleName("is-visible");
			isSelected=true;
		}
	}
}
