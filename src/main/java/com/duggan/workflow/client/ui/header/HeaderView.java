package com.duggan.workflow.client.ui.header;

import java.util.Date;
import java.util.List;

import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.admin.dashboard.DashboardPresenter;
import com.duggan.workflow.client.ui.admin.datatable.DataTablePresenter;
import com.duggan.workflow.client.ui.admin.ds.DataSourcePresenter;
import com.duggan.workflow.client.ui.admin.msgs.MessagesPresenter;
import com.duggan.workflow.client.ui.admin.processes.ProcessListingPresenter;
import com.duggan.workflow.client.ui.admin.processmgt.BaseProcessPresenter;
import com.duggan.workflow.client.ui.admin.users.UserPresenter;
import com.duggan.workflow.client.ui.component.TextField;
import com.duggan.workflow.client.ui.home.TabItem;
import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.shared.model.HTUser;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.ViewImpl;

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
	
	@UiField Element ulNav;

	boolean isSelected = false;

	@Inject
	public HeaderView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		txtSearch.getElement().setId("prependedDropdownButton");

		aNotifications.setTabIndex(3);
		aNotifications.getElement().setAttribute("data-toggle", "dropdown");

		imgSmall.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				imgSmall.setUrl("img/blueman(small).png");
			}
		});

		// imgSmall.getElement().getStyle().setWidth(30.0, Unit.PX);
		// imgSmall.getElement().getStyle().setHeight(50.0, Unit.PX);

		img.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				img.setUrl("img/blueman.png");
			}
		});

		// img.getElement().getStyle().setWidth(70.0, Unit.PX);
		// img.getElement().getStyle().setHeight(90.0, Unit.PX);
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
	
	public TextBox getSearchField(){
		return txtSearch;
	}

	/**
	 * This only works on AdminHomePresenter.bind() - meaning it wont be fired unless the user navigates to admin
	 */
	@Override
	public void showTab(Tab tab) {
		Window.alert(">> "+tab.getText());
		
		TabData tabData = ((TabItem)tab).getTabData();
		TabDataExt data = (TabDataExt)tabData;
		switch (tab.getText()) {
		case UserPresenter.TABLABEL:
			
			//getElement(ulNav, "usermgt")
			//showLi(getElement(ulNav, "usermgt"), data.canReveal());
			break;
		}
//		case BaseProcessPresenter.TABLABEL:
//			showLi(getElement(ulNav, "usermgt"), data.canReveal());
//			break;
//		case DashboardPresenter.TABLABEL:
//			showLi(getElement(ulNav, "usermgt"), data.canReveal());
//			break;
//		case MessagesPresenter.TABLABEL:
//			showLi(getElement(ulNav, "usermgt"), data.canReveal());
//			break;
//		case DataTablePresenter.TABLABEL:
//			showLi(getElement(ulNav, "usermgt"), data.canReveal());
//			break;
//		case DataSourcePresenter.TABLABEL:
//			showLi(getElement(ulNav, "usermgt"), data.canReveal());
//			break;
//		}
	}
	
	private static native Element getElement(Element parent, String elementId)/*-{
		
		return $wnd.$(parent).find("a[id='"+elementId+"']").next(); 		
	}-*/;

	private void showLi(Element el, boolean isShow) {
		if(el.getParentElement()!=null){
			show(el.getParentElement(), isShow);
		}
	}
	
	private void show(Element el, boolean isShow) {
		if(isShow){
			el.removeClassName("hide");
		}else{
			el.addClassName("hide");
		}
	}

}
