package com.duggan.workflow.client.ui.header;

import java.util.Date;

import com.duggan.workflow.client.model.ScreenMode;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.events.ScreenModeChangeEvent;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.client.util.AppContext;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.ViewImpl;
import com.wira.commons.client.security.CurrentUser;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.PermissionName;

public class HeaderView extends ViewImpl implements HeaderPresenter.IHeaderView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HeaderView> {
	}

	@UiField
	SpanElement spnCompanyName;

	@UiField
	Image imgSmall;
	@UiField
	Image img;

	@UiField
	SpanElement spnUser;
	@UiField
	SpanElement spnUserPull;
	@UiField
	Anchor aNotifications;
	@UiField
	Anchor aLogout;
	@UiField
	HTMLPanel notificationsContainer;
	@UiField
	HTMLPanel divNavbar;

	@UiField
	Anchor aProfile;

	@UiField
	FocusPanel popupContainer;
	@UiField
	SpanElement lblCount;
	@UiField
	SpanElement spnUserGroup;
	@UiField
	DivElement spnVersion;

	@UiField
	TextField txtSearch;

	@UiField
	Element ulNav;

	boolean isSelected = false;

	private CurrentUser currentUser;

	@Inject
	public HeaderView(final Binder binder, CurrentUser currentUser) {
		this.currentUser = currentUser;
		widget = binder.createAndBindUi(this);
		spnUser.setId("logged-in-user");
		txtSearch.getElement().setId("prependedDropdownButton");

		aNotifications.setTabIndex(3);
		aNotifications.getElement().setAttribute("data-toggle", "dropdown");

		imgSmall.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				imgSmall.setUrl("img/blueman(small).png");
			}
		});

		img.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				img.setUrl("img/blueman.png");
			}
		});
		
		attachToggleFullScreen(divNavbar.getElementById("aMaximize"));
		googleOAuthHandler(divNavbar.getElement());
	}

	private native void googleOAuthHandler(Element divNavBar) /*-{
		$wnd.jQuery($doc).ready(function(){
			//alert('Logged in = '+$wnd.auth2.isSignedIn.get());
			$wnd.refreshAuthState(divNavBar);
		});
		
	}-*/;

	private native void attachToggleFullScreen(Element aMaximize) /*-{
		var that = this;
		$wnd.jQuery($doc).ready(function(){
			$wnd.jQuery(aMaximize).click(function(){
				var title = $wnd.jQuery(aMaximize).prop("title");
				if(title=='Maximize'){
					//toggle title
					title="Minimize";
					that.@com.duggan.workflow.client.ui.header.HeaderView::showFullScreen(I)(1);
				}else{
					title= "Maximize"; 
					that.@com.duggan.workflow.client.ui.header.HeaderView::showFullScreen(I)(0);
				}
				
				$wnd.jQuery(aMaximize).prop("title", title);
			});
		});
		
	}-*/;
	
	void showFullScreen(int isFullScreen){
		if(isFullScreen==1){
			//true
			AppContext.getEventBus().fireEvent(new ScreenModeChangeEvent(ScreenMode.FULLSCREEN));
		}else{
			//false
			AppContext.getEventBus().fireEvent(new ScreenModeChangeEvent(ScreenMode.SMALLSCREEN));
		}
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public HasClickHandlers getLogout() {
		return aLogout;
	}

	public Anchor getNotificationsButton() {
		return aNotifications;
	}

	public HasBlurHandlers getpopupContainer() {
		return popupContainer;
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == HeaderPresenter.NOTIFICATIONS_SLOT) {
			notificationsContainer.clear();
			if (content != null) {
				notificationsContainer.add(content);
			}
		}
		super.setInSlot(slot, content);
	}

	public void setValues(String user_names, String userGroups, String orgName) {

		if (user_names != null) {
			spnUser.setInnerText(user_names);
			spnUserPull.setInnerText(user_names);
		} else {
			spnUser.setInnerText("");
			spnUserPull.setInnerText("");
		}

		if (userGroups != null) {
			spnUserGroup.setInnerText(userGroups);
		}

		if (orgName != null) {
			spnCompanyName.setInnerHTML("Wira BPM |&nbsp;" + orgName);
			spnCompanyName.setTitle(orgName);
		}
	}

	public void removePopup() {
		popupContainer.removeStyleName("is-visible");
		isSelected = false;
	}

	@Override
	public void setPopupVisible() {
		if (isSelected) {
			popupContainer.removeStyleName("is-visible");
			isSelected = false;
		} else {
			popupContainer.addStyleName("is-visible");
			isSelected = true;
		}
	}

	public void setCount(Integer count) {
		lblCount.setInnerText(count + "");
	}

	@Override
	public void setLoading(boolean loading) {
		if (loading) {
			notificationsContainer.setStyleName("loading");
		} else {
			notificationsContainer.removeStyleName("loading");
		}

	}

	@Override
	public void setAdminPageLookAndFeel(boolean isAdminPage) {
		if (isAdminPage) {
			divNavbar.addStyleName("navbar-inverse");
		} else {
			divNavbar.removeStyleName("navbar-inverse");
		}
	}

	@Override
	public void changeFocus() {
		popupContainer.setFocus(true);
	}

	@Override
	public void showAdminLink(boolean isAdmin) {
	}

	@Override
	public void setVersionInfo(Date created, String date, String version) {
		String versionDate = date;
		if (created != null) {
			versionDate = DateUtils.CREATEDFORMAT.format(created);
		}

		spnVersion.setInnerHTML("Version " + version
				+ ", <span title=\"Build Date\">" + versionDate + "</span>");
	}

	public void setImage(HTUser user) {
		String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
		if (moduleUrl.endsWith("/")) {
			moduleUrl = moduleUrl.substring(0, moduleUrl.length() - 1);
		}
		// moduleUrl
		// =moduleUrl+"/getreport?ACTION=GetUser&width=40&height=70&userId="+user.getUserId();
		String url1 = moduleUrl
				+ "/getreport?ACTION=GetUser&width=48.0&height=48.0&userId="
				+ user.getUserId();
		imgSmall.setUrl(url1);

		String url2 = moduleUrl
				+ "/getreport?ACTION=GetUser&width=70.0&height=70.0&userId="
				+ user.getUserId();
		img.setUrl(url2);
	}

	public TextBox getSearchField() {
		return txtSearch;
	}

	/**
	 * This only works on AdminHomePresenter.bind() - meaning it wont be fired
	 * unless the user navigates to admin
	 */
	@Override
	public void showTab(Tab tab) {
	}

	private static native Element getElement(Element parent, String elementId)/*-{
																				
																				return $wnd.$(parent).find("a[id='"+elementId+"']").next(); 		
																				}-*/;

	private void showLi(Element el, boolean isShow) {
		if (el.getParentElement() != null) {
			show(el.getParentElement(), isShow);
		}
	}

	private void show(Element el, boolean isShow) {
		if (isShow) {
			el.removeClassName("hide");
		} else {
			el.addClassName("hide");
		}
	}

	@Override
	public void refreshLinks() {
		boolean hasAdminRight = false;
		
		hasAdminRight = hasAdminRight | currentUser.hasPermissions(new String[]{PermissionName.ACCESSMGT_CAN_VIEW_ACCESSMGT.name()});
		showLi(getEl("usermgt"), currentUser.hasPermissions(new String[]{PermissionName.ACCESSMGT_CAN_VIEW_ACCESSMGT.name()}));
		
		hasAdminRight = hasAdminRight | currentUser.hasPermissions(new String[]{PermissionName.PROCESSES_CAN_VIEW_PROCESSES.name()});
		showLi(getEl("processes"), currentUser.hasPermissions(new String[]{PermissionName.PROCESSES_CAN_VIEW_PROCESSES.name()}));
		
		hasAdminRight = hasAdminRight | currentUser.hasPermissions(new String[]{PermissionName.DASHBOARDS_CAN_VIEW_DASHBOARDS.name()});
		showLi(getEl("dashboards"), currentUser.hasPermissions(new String[]{PermissionName.DASHBOARDS_CAN_VIEW_DASHBOARDS.name()}));
		
		hasAdminRight = hasAdminRight | currentUser.hasPermissions(new String[]{PermissionName.MAILLOG_CAN_VIEW_MAILLOG.name()});
		showLi(getEl("messages"), currentUser.hasPermissions(new String[]{PermissionName.MAILLOG_CAN_VIEW_MAILLOG.name()}));
		
		hasAdminRight = hasAdminRight | currentUser.hasPermissions(new String[]{PermissionName.DATATABLES_CAN_VIEW_DATATABLES.name()});
		showLi(getEl("dataTables"), currentUser.hasPermissions(new String[]{PermissionName.DATATABLES_CAN_VIEW_DATATABLES.name()}));
		
		hasAdminRight = hasAdminRight | currentUser.hasPermissions(new String[]{PermissionName.DATASOURCES_CAN_VIEW_DATASOURCES.name()});
		showLi(getEl("datasources"), currentUser.hasPermissions(new String[]{PermissionName.DATASOURCES_CAN_VIEW_DATASOURCES.name()}));
		
		hasAdminRight = hasAdminRight | currentUser.hasPermissions(new String[]{PermissionName.SETTINGS_CAN_VIEW.name()});
		showLi(getEl("settings"), currentUser.hasPermissions(new String[]{PermissionName.SETTINGS_CAN_VIEW.name()}));
		
		hasAdminRight = hasAdminRight | currentUser.hasPermissions(new String[]{PermissionName.UNASSIGNED_CAN_VIEW_UNASSIGNEDTASKS.name()});
		showLi(getEl("unassignedTasks"),currentUser.hasPermissions(new String[]{PermissionName.UNASSIGNED_CAN_VIEW_UNASSIGNEDTASKS.name()}));
		
		hasAdminRight = hasAdminRight | currentUser.hasPermissions(new String[]{PermissionName.CASEREGISTRY_CAN_VIEW_CASES.name()});
		showLi(getEl("caseRegistry"),currentUser.hasPermissions(new String[]{PermissionName.CASEREGISTRY_CAN_VIEW_CASES.name()}));
		
		hasAdminRight = hasAdminRight |  currentUser.hasPermissions(new String[]{PermissionName.REPORTS_CAN_VIEW_REPORTS.name()});
		showLi(getEl("reports"),currentUser.hasPermissions(new String[]{PermissionName.REPORTS_CAN_VIEW_REPORTS.name()}));
		
		hasAdminRight = hasAdminRight | currentUser.hasPermissions(new String[]{PermissionName.REPORTS_CAN_VIEW_REPORTS.name()});
		showLi(getEl("fileExplorer"), currentUser.hasPermissions(new String[]{PermissionName.REPORTS_CAN_VIEW_REPORTS.name()}));
		
		showLi(getEl("adminSettings"), hasAdminRight);
	}

	private Element getEl(String id) {
		Element el = divNavbar.getElementById(id);
		return el;
	}

}
