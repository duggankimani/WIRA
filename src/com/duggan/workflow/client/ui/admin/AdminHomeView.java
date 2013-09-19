package com.duggan.workflow.client.ui.admin;

import com.duggan.workflow.client.ui.admin.AdminHomePresenter.ADMINPAGES;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class AdminHomeView extends ViewImpl implements
		AdminHomePresenter.MyView {

	private final Widget widget;
	
	@UiField LIElement liDashboard;
	@UiField LIElement liProcesses;
	@UiField LIElement liUsers;
	@UiField LIElement liReports;
	@UiField SpanElement spanTitle;
	@UiField SpanElement iconTitle;
	
	@UiField HTMLPanel divContent;
	
	public interface Binder extends UiBinder<Widget, AdminHomeView> {
	}

	@Inject
	public AdminHomeView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if(slot == AdminHomePresenter.CONTENT_SLOT){
			divContent.clear();
			if(content!=null)
			divContent.add(content);
		}else{
		super.setInSlot(slot, content);
		}
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public void SetDashboardLink(boolean status, ADMINPAGES page){
		if(status){
			liDashboard.setClassName("active");
			iconTitle.setClassName("icon-dashboard");
			spanTitle.setInnerHTML(page.getDisplayName());
			}else
			liDashboard.removeClassName("active");
	}
	
	public void SetProcessLink(boolean status, ADMINPAGES page){
		if(status){
			liProcesses.setClassName("active");
			iconTitle.setClassName("icon-cogs");
			spanTitle.setInnerHTML(page.getDisplayName());
		}else
			liProcesses.removeClassName("active");
	}
	
	public void SetUsersLink(boolean status, ADMINPAGES page){
		if(status){
			liUsers.setClassName("active");
			iconTitle.setClassName("icon-group");
			spanTitle.setInnerHTML(page.getDisplayName());
		}else
			liUsers.removeClassName("active");
	}
	
	public void SetReportLink(boolean status, ADMINPAGES page){
		if(status){
			liReports.setClassName("active");
			iconTitle.setClassName("icon-bar-chart");
			spanTitle.setInnerHTML(page.getDisplayName());
		}else
			liReports.removeClassName("active");
	}
	@Override
	public void clearAllLinks() {
		liDashboard.removeClassName("active");
		liProcesses.removeClassName("active");
		liUsers.removeClassName("active");
		liReports.removeClassName("active");
	}
}