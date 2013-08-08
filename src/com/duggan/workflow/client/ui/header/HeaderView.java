package com.duggan.workflow.client.ui.header;

import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

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
	@UiField SpanElement lblCount;
	boolean isSelected=false;
	
	@Inject
	public HeaderView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		aNotifications.setTabIndex(3);
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public HasClickHandlers getLogout(){
		return aLogout;
	}
	
	public Anchor getNotificationsButton(){
		return aNotifications;
	}
	
	public HasBlurHandlers getpopupContainer(){
		return popupContainer;
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
		}else{
			notificationsContainer.addStyleName("loading");
			popupContainer.addStyleName("is-visible");
			isSelected=true;
		}	
	}
	
	public void setCount(Integer count){
		lblCount.setInnerText(count+"");
	}
}
