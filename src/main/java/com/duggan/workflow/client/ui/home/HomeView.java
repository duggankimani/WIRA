package com.duggan.workflow.client.ui.home;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.reports.ReportsPresenter;
import com.duggan.workflow.client.ui.admin.TabDataExt;
import com.duggan.workflow.client.ui.fileexplorer.FileExplorerPresenter;
import com.duggan.workflow.client.ui.task.CaseRegistryPresenter;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.ViewImpl;

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
	
	@UiField Element aCaseReg;
	@UiField Element aReports;
	@UiField Element aExplorer;
	
	@Inject
	public HomeView(final Binder binder, HomeTabPanel panel) {
		this.tabPanel = panel;
		widget = binder.createAndBindUi(this);
		
		bindSlot(HomePresenter.DOCTREE_SLOT, divDocPopup);
		btnAdd.getElement().setId("startprocess");
		btnAdd.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				btnAdd.getElement().getParentElement().toggleClassName("open");
			}
		});
	}

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
		Tab tab =  tabPanel.addTab(tabData, historyToken);
		return tab;
	}

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
		tabPanel.setActiveTab(tab);
	}

	@Override
	public void changeTab(Tab tab, TabData tabData, String historyToken) {
		tabPanel.changeTab(tab, tabData, historyToken);
	}

	private void showCustom(TabData tabData) {
		if(tabData==null || tabData.getLabel()==null || ! (tabData instanceof TabDataExt)){
			return;
		}
		
		TabDataExt data = (TabDataExt)tabData;
		switch (data.getLabel()) {
		case CaseRegistryPresenter.TABLABEL:
			showLi(aCaseReg, data.canReveal());
			break;
		case ReportsPresenter.TABLABEL:
			showLi(aReports, data.canReveal());
			break;
		case FileExplorerPresenter.TABLABEL:
			showLi(aExplorer, data.canReveal());
			break;
		}
	}

	@Override
	public void refreshTabs() {
		tabPanel.refreshTabs();
		
		for(Tab tab: tabPanel.getTabs()){
			showCustom(((TabItem)tab).getTabData());
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
//		else if (slot == DOCPOPUP_SLOT) {
//			divDocPopup.clear();
//			if (content != null) {
//				divDocPopup.add(content);
//			}
//		} 
		else {
			super.setInSlot(slot, content);
		}

	}

	@Override
	public void bindAlerts(HashMap<TaskType, Integer> alerts) {
		alerts.put(TaskType.INBOX, getValue(alerts.get(TaskType.MINE) + getValue(alerts.get(TaskType.QUEUED))));
		for (TaskType type : alerts.keySet()) {
			String text = (type.getTitle() + " (" + alerts.get(type) + ")");
			tabPanel.changeTab(type, text);
		}
	}

	private Integer getValue(Integer val) {
		return val==null? 0: val.intValue();
	}

	@Override
	public void showDocsList() {

	}

	@Override
	public void showmask(boolean b) {

	}
	
	@Override
	public native void load() /*-{
								
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

}
