package com.duggan.workflow.client.ui.home;

import java.util.HashMap;

import com.duggan.workflow.shared.model.TaskType;
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

import static com.duggan.workflow.client.ui.home.HomePresenter.*;

public class HomeView extends ViewImpl implements HomePresenter.IHomeView {

	private final Widget widget;
	
	public interface Binder extends UiBinder<Widget, HomeView> {
	}

	@UiField Anchor btnAdd;	
	@UiField(provided=true) HomeTabPanel tabPanel;
	@UiField HTMLPanel tabContent;
	@UiField HTMLPanel divDocPopup;
	@UiField HTMLPanel panelDocTree;
	
	@Inject
	public HomeView(final Binder binder,HomeTabPanel panel) {
		this.tabPanel = panel;
		widget = binder.createAndBindUi(this);	
		btnAdd.getElement().setAttribute("data-toggle", "dropdown");
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

	@Override
	public void setInSlot(Object slot, IsWidget content) {	
		if(slot == HomePresenter.SLOT_SetTabContent){
			tabContent.clear();
			if (content != null) {
				tabContent.add(content);
			}
		}else if (slot == DOCPOPUP_SLOT) {
			divDocPopup.clear();
			if (content != null) {
				divDocPopup.add(content);
			}			
		}else if(slot==DOCTREE_SLOT){
			panelDocTree.clear();
			if(content!=null){
				panelDocTree.add(content);
			}
		}else {
			super.setInSlot(slot, content);
		}
		
	}


	@Override
	public void bindAlerts(HashMap<TaskType, Integer> alerts) {
		for (TaskType type : alerts.keySet()) {
			String text = (type.getTitle() + " (" + alerts.get(type) + ")");
			tabPanel.changeTab(type,text);
		}
	}

	@Override
	public void showDocsList() {
		
	}

	@Override
	public void showmask(boolean b) {
		
	}

}
