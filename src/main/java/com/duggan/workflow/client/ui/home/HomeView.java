package com.duggan.workflow.client.ui.home;

import java.util.HashMap;

import com.duggan.workflow.client.model.ScreenMode;
import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.reports.ReportsPresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.admin.dashboard.DashboardPresenter;
import com.duggan.workflow.client.ui.fileexplorer.FileExplorerPresenter;
import com.duggan.workflow.client.ui.task.CaseRegistryPresenter;
import com.duggan.workflow.client.ui.task.UnAssignedPresenter;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class HomeView extends ViewImpl implements HomePresenter.IHomeView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HomeView> {
	}

	@UiField
	Anchor btnAdd;

	@UiField(provided = true)
	HomeTabPanel tabPanel;

	@UiField
	HTMLPanel tabContent;
	@UiField
	HTMLPanel divDocPopup;
	@UiField
	HTMLPanel panelDocTree;

	@UiField
	Element sideBarUL;
	
	@UiField HTMLPanel mainContainer;

	private PlaceManager placeManager;

	private String processRefId;

	@Inject
	public HomeView(final Binder binder, HomeTabPanel panel,
			PlaceManager placeManager) {
		this.tabPanel = panel;
		this.placeManager = placeManager;
		widget = binder.createAndBindUi(this);

		bindSlot(HomePresenter.DOCTREE_SLOT, divDocPopup);
		btnAdd.getElement().setId("startprocess");
		btnAdd.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnAdd.getElement().getParentElement().toggleClassName("open");
			}
		});

		bindAnchors(sideBarUL);
		bindAddAnimation(mainContainer.getElement(),tabContent.getElement());
	}
	
	public native void bindAddAnimation(Element parent, Element tabElement)/*-{
		$wnd.jQuery($doc).ready(function(){
			$wnd.jQuery(parent).find('#sidebar-nav a[href=\'#/home\']').click(function(){
				$wnd.jQuery(tabElement).find('.landing-page').fadeOut(500, function(){
						$wnd.jQuery(tabElement).find('.landing-page').fadeIn(500);
					});
			});
		});
		
	}-*/;

	public HasClickHandlers getAddButton() {
		return btnAdd;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public Tab addTab(TabData tabData, String historyToken) {
		showCustom(tabData);
		Tab tab = tabPanel.addTab(tabData, historyToken);
		return tab;
	}

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
	public void removeTab(Tab tab) {
		tabPanel.removeTab(tab);
	}

	@Override
	public void removeTabs() {
		tabPanel.removeTabs();
	}

	@Override
	public void setActiveTab(Tab tab) {

		selectTab(placeManager.getCurrentPlaceRequest());
		tabPanel.setActiveTab(tab);
	}

	@Override
	public void changeTab(Tab tab, TabData tabData, String historyToken) {
		tabPanel.changeTab(tab, tabData, historyToken);
	}

	private void showCustom(TabData tabData) {
		if (tabData == null || tabData.getLabel() == null
				|| !(tabData instanceof TabDataExt)) {
			return;
		}

		TabDataExt data = (TabDataExt) tabData;
		
		switch (data.getLabel()) {
		case UnAssignedPresenter.TABLABEL:
			showItem(getElement(sideBarUL, "#/unassigned"), data.canReveal());
			break;
		case CaseRegistryPresenter.TABLABEL:
			showItem(getElement(sideBarUL, "#/registry"), data.canReveal());
			break;
		case ReportsPresenter.TABLABEL:
			showItem(getElement(sideBarUL, "#/reports"), data.canReveal());
			break;
		case FileExplorerPresenter.TABLABEL:
			showItem(getElement(sideBarUL, "#/explorer"), data.canReveal());
			break;
		case DashboardPresenter.TABLABEL:
			showItem(getElement(sideBarUL, "#/dashboards"), data.canReveal());
			break;
		}
	}

	private void showItem(JavaScriptObject jso, boolean canReveal) {
		Element element = Element.as(jso);
		Element parent = element.getParentElement();
		Element target = null;
		if(parent.hasClassName("accordion-heading")){
			target = parent.getParentElement();
		}else{
			target = parent;
		}
		
		if(target!=null){
			if(canReveal){
				target.removeClassName("hide");
			}else{
				target.addClassName("hide");
			}
		}
	}

	@Override
	public void refreshTabs() {
		tabPanel.refreshTabs();

		for (Tab tab : tabPanel.getTabs()) {

			showCustom(((TabItem) tab).getTabData());
		}
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == HomePresenter.SLOT_SetTabContent) {
			tabContent.clear();
			if (content != null) {
				tabContent.add(content);
			}
		}
		// else if (slot == DOCPOPUP_SLOT) {
		// divDocPopup.clear();
		// if (content != null) {
		// divDocPopup.add(content);
		// }
		// }
		else {
			super.setInSlot(slot, content);
		}

	}

	@Override
	public void bindAlerts(HashMap<TaskType, Integer> alerts, String processRefId) {
		this.processRefId = processRefId;
		alerts.put(TaskType.INBOX, getValue(alerts.get(TaskType.MINE)
				+ getValue(alerts.get(TaskType.QUEUED))));
		for (TaskType type : alerts.keySet()) {
			String text = (type.getTitle() + " (" + alerts.get(type) + ")");
			tabPanel.changeTab(type, text);

			changeCustomTabText(type, text);
		}
	}

	private void changeCustomTabText(TaskType type, String newTabText) {

		String href = null;

		switch (type) {
		case ALL:
			href="#/inbox/all";
			break;
		case MINE:
//			href="#/inbox/mine";
			href="#/inbox/all";
			break;
		case QUEUED:
//			href="#/inbox/queued";
			href="#/inbox/all";
			break;
		case INBOX:
			//href="#collapseOne";
			href="#/inbox/all";
			break;
		case COMPLETED:
			href="#/participated";
			break;
		case DRAFT:
			href="#/drafts";
			break;
		case PARTICIPATED:
			href="#/participated";
			break;
		case SUSPENDED:
			href="#/suspended";
			break;
		case UNASSIGNED:
			href = "#/unassigned";
			break;
		default:
			break;
		}

		if(href==null || href.isEmpty()){
			return;
		}
		
		
		JavaScriptObject obj = getElement(sideBarUL, href);
		if (obj != null) {
			Element el = Element.as(obj);

			if (el != null) {
				el.setInnerText(newTabText);
			}
		}

		
		//Change url
		JavaScriptObject anchorJs = getLink(sideBarUL, href);
		//Change URL
		if(type!=TaskType.INBOX && processRefId!=null){
			AnchorElement anchor = (AnchorElement)Element.as(anchorJs);
			href = href+"/"+processRefId;
			anchor.setHref(href);
		}

		
	}

	private native JavaScriptObject getElement(Element parentUl, String historyToken) /*-{
																				//$wnd.console.log('find element href='+historyToken);
																				
																				var searchQuery = '#accordion2 a[href^=\''+historyToken+'\']';
																				
																				var anchor = $wnd.jQuery(parentUl).find(searchQuery).get(0);
																				
																				var span = $wnd.jQuery(anchor).find('span').get(0);
																				
																				return span;
																				
																				}-*/;
	
	private native JavaScriptObject getLink(Element parentUl, String historyToken)/*-{
		var searchQuery = '#accordion2 a[href^=\''+historyToken+'\']';
		
		var anchor = $wnd.jQuery(parentUl).find(searchQuery).get(0);
		
		return anchor;
	}-*/;

	private Integer getValue(Integer val) {
		return val == null ? 0 : val.intValue();
	}

	@Override
	public void showDocsList() {

	}

	@Override
	public void showmask(boolean b) {

	}

	@Override
	public void load() {
		initTree();
	}

	public native void initTree() /*-{
									
									var el = $wnd.jQuery('div.dropdown-menu');
									
									$wnd.jQuery('body').on('click', function (e) { 
									if (!el.is(e.target) && 
									el.has(e.target).length === 0 && 
									$wnd.jQuery('.open').has(e.target).length === 0
									&& !(e.target.className=='com-sencha-gxt-theme-base-client-tree-TreeBaseAppearance-TreeBaseStyle-joint')) {
										$wnd.jQuery('.open').removeClass('open'); 
										} 
									});
									}-*/;

	@Override
	public void closeDocTypePopup() {
		btnAdd.getElement().getParentElement().toggleClassName("open");
	}

	private native void bindAnchors(Element parentUl)/*-{
														var view = this;
														$wnd.jQuery($doc).ready(function(){
															//$wnd.console.log('Ready.....to bind ');
															
															$wnd.jQuery(parentUl).find('#accordion2 a').each(function() {
															var el = $wnd.jQuery(this);
																$wnd.jQuery(el).click(function(e){
																//clear Existing selection
																view.@com.duggan.workflow.client.ui.home.HomeView::clearAnchors()();
																
																var parent = $wnd.jQuery(el).parent();
																parent.addClass('active');
																});
															});
														
//															$wnd.jQuery(parentUl).find('#inbox').click(function(e){
//																var inbox = $wnd.jQuery(e.target);
//																var caret = inbox.find('i.icon-caret-right').get(0);
//																if(caret==undefined){
//																	caret = inbox.find('i.icon-caret-down').get(0);
//																}
//																
//																$wnd.jQuery(caret).toggleClass('icon-caret-right');
//																$wnd.jQuery(caret).toggleClass('icon-caret-down');
//															});
														});
														
														}-*/;

	void redirectInbox() {
		placeManager.revealPlace(new PlaceRequest.Builder()
				.nameToken(NameTokens.inboxwithparams).with("filter", "mine")
				.build());
	}

	public void clearAnchors() {
		clearAllAnchors(sideBarUL);
	}

	private native void clearAllAnchors(Element parent)/*-{
														$wnd.jQuery(parent).find('#accordion2 a').each(function() {
														var parent = $wnd.jQuery(this).parent();
														parent.removeClass('active');
														});
														}-*/;

	private void selectTab(PlaceRequest currentPlaceRequest) {
		String nameToken = currentPlaceRequest.getNameToken();
		nameToken = placeManager.buildHistoryToken(currentPlaceRequest);
		clearAllAnchors(sideBarUL);
		//single inbox
		if(nameToken.startsWith("/inbox")){
			nameToken = "/inbox/all";;
		}
		selectTab(sideBarUL, "#" + nameToken);
	}

	private native void selectTab(Element parent, String historyToken)/*-{
																		//$wnd.console.log("Select tab - "+historyToken);
																		var searchQuery = '#accordion2 a[href^=\''+historyToken+'\']';
																		
																		$wnd.jQuery(parent).find(searchQuery).each(function() {
																		var el = $wnd.jQuery(this);
																		var elParent = $wnd.jQuery(el).parent();
																		elParent.addClass('active');
																		
																		if(elParent.hasClass("accordion-inner")){
																			var accordionBody = elParent.closest("div.accordion-body", parent);
																			accordionBody.removeClass("collapse");
																			
																			//Toggle caret
																			var caret = accordionBody.closest('div.accordion', parent).find(".accordion-toggle i.icon-caret-right").get(0);
																			$wnd.jQuery(caret).toggleClass('icon-caret-right');
																			$wnd.jQuery(caret).toggleClass('icon-caret-down');
																		}
																		});
																		}-*/;

	@Override
	public void setScreenMode(ScreenMode screenMode) {
		Element sideBar = mainContainer.getElementById("sidebar-nav");
		
		//showSideBar(sideBar,screenMode==ScreenMode.SMALLSCREEN);
		if(screenMode==ScreenMode.FULLSCREEN){
			sideBar.getStyle().setDisplay(Display.NONE);
			tabContent.getElement().getStyle().setLeft(0, Unit.PX);
		}else{
			sideBar.getStyle().setDisplay(Display.INITIAL);
			tabContent.getElement().getStyle().setLeft(200, Unit.PX);
		}
	}

	private native void showSideBar(Element sideBar, boolean isShow) /*-{
		if(isShow){
			$wnd.jQuery(sideBar).show(1000);
		}else{
			$wnd.jQuery(sideBar).hide(1000);
		}
		
		
	}-*/;
	
	

}
