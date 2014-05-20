package com.duggan.workflow.client.ui.admin;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Tab;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.ViewImpl;

public class AdminHomeView extends ViewImpl implements
		AdminHomePresenter.MyView {

	private final Widget widget;
	
	@UiField(provided=true) TabPanel tabPanel;
	
	public interface Binder extends UiBinder<Widget, AdminHomeView> {
	}

	@Inject
	public AdminHomeView(final Binder binder, TabPanel panel) {
		this.tabPanel = panel;
		widget = binder.createAndBindUi(this);
	}
	
	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if(slot == AdminHomePresenter.SLOT_SetTabContent){
			tabPanel.setPanelContent(content);
		}else{
			super.setInSlot(slot, content);
		}
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
    public Tab addTab(TabData tabData, String historyToken) {
        return tabPanel.addTab(tabData, historyToken);
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
    
    @Override
    public void refreshTabs() {
        tabPanel.refreshTabs();
    }
}
