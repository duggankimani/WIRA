package com.duggan.workflow.client.ui.header;

import java.util.Date;

import com.duggan.workflow.client.ui.util.DateUtils;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class HeaderView extends ViewImpl implements HeaderPresenter.IHeaderView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HeaderView> {
	}
	
	@UiField SpanElement spnUser;
	@UiField SpanElement spnUserPull;
	@UiField Anchor aNotifications;
	@UiField Anchor aLogout;
	@UiField HTMLPanel notificationsContainer;
	@UiField HTMLPanel divNavbar;
	@UiField Anchor aAdmin;
	@UiField Anchor aHome;
	@UiField Anchor aBrand;
	@UiField FocusPanel popupContainer;
	@UiField SpanElement lblCount;
	@UiField SpanElement spnUserGroup;
	@UiField DivElement spnVersion;
	
	boolean isSelected=false;
	
	@Inject
	public HeaderView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		aNotifications.setTabIndex(3);
		aNotifications.getElement().setAttribute("data-toggle", "dropdown");
		UIObject.setVisible(aAdmin.getElement(),false);
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
	public void setValues(String user_names, String userGroups){
		
		if(user_names!=null){
			spnUser.setInnerText(user_names);
			spnUserPull.setInnerText(user_names);
		}
		else{
			spnUser.setInnerText("");
			spnUserPull.setInnerText("");
		}
		
		if(userGroups!=null){
			spnUserGroup.setInnerText(userGroups);
		}
	}

	public void removePopup(){
		popupContainer.removeStyleName("is-visible");
		isSelected=false;
	}
	
	@Override
	public void setPopupVisible() {
		if(isSelected){
			popupContainer.removeStyleName("is-visible");
			isSelected=false;
		}else{			
			popupContainer.addStyleName("is-visible");
			isSelected=true;
		}	
	}
	
	public void setCount(Integer count){
		lblCount.setInnerText(count+"");
	}

	@Override
	public void setLoading(boolean loading) {
		if(loading){
			notificationsContainer.setStyleName("loading");
		}else{
			notificationsContainer.removeStyleName("loading");
		}
		
	}

	@Override
	public void setAdminPageLookAndFeel(boolean isAdminPage) {
		if(isAdminPage){
			divNavbar.addStyleName("navbar-inverse");
		}else{
			divNavbar.removeStyleName("navbar-inverse");
		}
	}

	@Override
	public void changeFocus() {
		popupContainer.setFocus(true);
	}

	@Override
	public void showAdminLink(boolean isAdmin) {
		UIObject.setVisible(aAdmin.getElement(),isAdmin);
	}

	@Override
	public void setVersionInfo(Date created, String date, String version) {
		String versionDate = date;
		if(created!=null){
			versionDate = DateUtils.CREATEDFORMAT.format(created);
		}
		
		spnVersion.setInnerHTML("Version "+version+".1.3.4, <span title=\"Build Date\">"+versionDate+"</span>");
	}
}
