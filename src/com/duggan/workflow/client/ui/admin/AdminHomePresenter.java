package com.duggan.workflow.client.ui.admin;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.MainPagePresenter;
import com.duggan.workflow.client.ui.admin.adduser.AddUserPresenter.TYPE;
import com.duggan.workflow.client.ui.admin.dashboard.DashboardPresenter;
import com.duggan.workflow.client.ui.admin.processes.ProcessPresenter;
import com.duggan.workflow.client.ui.admin.reports.ReportsPresenter;
import com.duggan.workflow.client.ui.admin.users.UserPresenter;
import com.duggan.workflow.client.ui.events.LoadGroupsEvent;
import com.duggan.workflow.client.ui.events.LoadProcessesEvent;
import com.duggan.workflow.client.ui.events.LoadUsersEvent;
import com.duggan.workflow.client.ui.login.LoginGateKeeper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class AdminHomePresenter extends
		Presenter<AdminHomePresenter.MyView, AdminHomePresenter.MyProxy> {

	public interface MyView extends View {
		public void SetDashboardLink(boolean status, ADMINPAGES pages);
		public void SetProcessLink(boolean status, ADMINPAGES pages);
		public void SetUsersLink(boolean status, ADMINPAGES pages);
		public void SetReportLink(boolean status, ADMINPAGES pages);
		public void clearAllLinks();
	}
	
	
	@ProxyCodeSplit
	@NameToken(NameTokens.adminhome)
	@UseGatekeeper(LoginGateKeeper.class)
	public interface MyProxy extends ProxyPlace<AdminHomePresenter> {
	}
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> CONTENT_SLOT = new Type<RevealContentHandler<?>>();
	
	@Inject ProcessPresenter process;
	@Inject UserPresenter users;
	@Inject DashboardPresenter dashboard;
	@Inject ReportsPresenter reports;
	
	enum ADMINPAGES{
		DASHBOARD("Dashboard"), PROCESSES("Process"), USERS("Users"),GROUPS("groups"),
		REPORTS("Reports");
		
		private String displayName;
		
		private ADMINPAGES(String displayName){
			this.displayName = displayName;
		}
		
		public String getDisplayName(){
			return displayName;
		}
		
	}

	ADMINPAGES page=null;
	
	@Inject
	public AdminHomePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
		
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
	}
	

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		 super.prepareFromRequest(request);
		 
		String name = request.getParameter("page", ADMINPAGES.DASHBOARD.toString());
		 
		ADMINPAGES pages = ADMINPAGES.valueOf(name.toUpperCase());
		 
		this.page = pages;
		
		switch (pages) {
		case DASHBOARD:
			showDashBoard();
			break;

		case PROCESSES:
			showProcessPanel();
			fireEvent(new LoadProcessesEvent());
			break;
		
		case USERS:
			showUserPanel();
			fireEvent(new LoadUsersEvent());
			break;
			
		case GROUPS:
			showUserPanel(TYPE.GROUP);
			fireEvent(new LoadGroupsEvent());
			break;
		
		case REPORTS:
			showReportPanel();
			break;
		}
		 
	}
	
	private void showDashBoard() {
		setInSlot(CONTENT_SLOT, null);
		setInSlot(CONTENT_SLOT, dashboard);
		getView().clearAllLinks();
		getView().SetDashboardLink(true, page);
	}
	
	private void showProcessPanel() {
		 setInSlot(CONTENT_SLOT, process);
		 getView().clearAllLinks();
		 getView().SetProcessLink(true,page);
	}
	
	private void showUserPanel(){
		showUserPanel(TYPE.USER);
	}
	
	private void showUserPanel(TYPE type) {
		users.setType(type);
		setInSlot(CONTENT_SLOT, users);
		getView().clearAllLinks();
		getView().SetUsersLink(true,page);
	}
	
	private void showReportPanel() {
	   setInSlot(CONTENT_SLOT, reports);
	   getView().clearAllLinks();
	   getView().SetReportLink(true,page);
	}



	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
	}
}
