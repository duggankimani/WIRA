package com.duggan.workflow.client.ui.home;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
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
		}else {
			super.setInSlot(slot, content);
		}
//		else if (slot == DATEGROUP_SLOT) {
//			showActivitiesPanel(false);
//			ulTaskGroups.clear();
//			if (content != null) {
//				ulTaskGroups.add(content);
//			}
//
//		} else if (slot == DOCUMENT_SLOT) {
//			showActivitiesPanel(false);
//			docContainer.clear();
//			if (content != null) {
//				docContainer.add(content);
//			}
//		} else if(slot== ACTIVITIES_SLOT){
//			showActivitiesPanel(true);
//			activityContainer.clear();
//			if(content != null){
//				activityContainer.add(content);
//			}
//		}else if (slot == FILTER_SLOT) {
//			filterDialog.clear();
//			if (content != null) {
//				filterDialog.add(content);
//			}
//			
//		}	
			
		
	}


	@Override
	public void bindAlerts(HashMap<TaskType, Integer> alerts) {
		for (TaskType type : alerts.keySet()) {
			String text = (type.getTitle() + " (" + alerts.get(type) + ")");
			tabPanel.changeTab(type,text);
		}
	}

	

//	@Override
//	public void setTaskType(TaskType currentTaskType) {
//		liDrafts.removeClassName("active");
//		liApproved.removeClassName("active");
//		liFlagged.removeClassName("active");
//		liNewReq.removeClassName("active");
//		liRejected.removeClassName("active");
//		liRecentApprovals.removeClassName("active");
//		liProgress.removeClassName("active");
//
//		if(currentTaskType==null)
//			return;
//		
//		switch (currentTaskType) {
//		case APPROVALREQUESTDONE:
//			liRecentApprovals.addClassName("active");
//			break;
//
//		case APPROVALREQUESTNEW:
//			liNewReq.addClassName("active");
//			break;
//
//		case APPROVED:
//			liApproved.addClassName("active");
//			break;
//
//		case DRAFT:
//			liDrafts.addClassName("active");
//			break;
//
//		case FLAGGED:
//			liFlagged.addClassName("active");
//			break;
//
//		case INPROGRESS:
//			liProgress.addClassName("active");
//			break;
//
//		case REJECTED:
//			liRejected.addClassName("active");
//			break;
//		}
//	}

//	@Override
//	public void showActivitiesPanel(boolean show) {
//		if(show){
//			activityContainer.removeStyleName("hide");
//			wholeContainer.addStyleName("hide");
//		}else{
//			activityContainer.addStyleName("hide");
//			wholeContainer.removeStyleName("hide");
//		}
//	}
	
//	public HTMLPanel getWholeContainer() {
//		return wholeContainer;
//	}
	
	@Override
	public void showmask(boolean mask) {
		if(mask){
			//wholeContainer.addStyleName("working-request");
		}else{
			//wholeContainer.removeStyleName("working-request");
		}
	}

	@Override
	public void showDocsList() {
		
	}

}
