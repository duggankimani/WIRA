package com.duggan.workflow.client.ui.home;

import static com.duggan.workflow.client.ui.home.HomePresenter.ACTIVITIES_SLOT;
import static com.duggan.workflow.client.ui.home.HomePresenter.DATEGROUP_SLOT;
import static com.duggan.workflow.client.ui.home.HomePresenter.DOCUMENT_SLOT;
import static com.duggan.workflow.client.ui.home.HomePresenter.FILTER_SLOT;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class HomeView extends ViewImpl implements HomePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HomeView> {
	}

	@UiField Button btnAdd;
	@UiField HTMLPanel docContainer;
	@UiField BulletListPanel ulTaskGroups;
	@UiField HeadingElement hCategory;
	@UiField Anchor aDrafts;
	@UiField Anchor aProgress;
	@UiField Anchor aApproved;
	@UiField Anchor aRejected;
	@UiField Anchor aNewReq;
	@UiField Anchor aRecentApprovals;
	@UiField Anchor aFlagged;
	@UiField Anchor aRefresh;
	@UiField Element liDrafts;
	@UiField Element liApproved;
	@UiField Element liRejected;
	@UiField Element liNewReq;
	@UiField Element liRecentApprovals;
	@UiField Element liProgress;
	@UiField Element liFlagged;
	@UiField Anchor iFilterdropdown;
	@UiField HTMLPanel filterDialog;
	@UiField InlineLabel spnNoItems;
	@UiField HTMLPanel activityContainer;
	@UiField Element divDocListing;
	@UiField Element divDocView;
	@UiField HTMLPanel wholeContainer;
	@UiField SpanElement loadingtext;
	
	@Inject
	public HomeView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		ulTaskGroups.setId("navigation-menu");

		btnAdd.getElement().setAttribute("type", "button");
		
		divDocListing.setId("middle-nav");
		divDocView.setId("detailed-info");
		
		loadingtext.setId("loading-text");
		

		// InlineLabel l;
		// l.addClickHandler(handler)
		docContainer.addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// System.err.println("### ABS");
			}
		}, ClickEvent.getType());
		
		iFilterdropdown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				filterDialog.removeStyleName("hidden");
			}
		});
		
	}

	public Anchor getaDrafts() {
		return aDrafts;
	}

	public Anchor getaProgress() {
		return aProgress;
	}

	public Anchor getaApproved() {
		return aApproved;
	}

	public Anchor getaRejected() {
		return aRejected;
	}

	public Anchor getaNewReq() {
		return aNewReq;
	}

	public Anchor getaRecentApprovals() {
		return aRecentApprovals;
	}

	public Anchor getaFlagged() {
		return aFlagged;
	}

	public Anchor getaRefresh() {
		return aRefresh;
	}

	public HTMLPanel getActivityContainer() {
		return activityContainer;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setInSlot(Object slot, Widget content) {	
		if (slot == DATEGROUP_SLOT) {
			showActivitiesPanel(false);
			ulTaskGroups.clear();
			if (content != null) {
				ulTaskGroups.add(content);
			}

		} else if (slot == DOCUMENT_SLOT) {
			showActivitiesPanel(false);
			docContainer.clear();
			if (content != null) {
				docContainer.add(content);
			}
		} else if (slot == FILTER_SLOT) {
			filterDialog.clear();
			if (content != null) {
				filterDialog.add(content);
			}
			
		}else if(slot== ACTIVITIES_SLOT){
			showActivitiesPanel(true);
			activityContainer.clear();
			if(content != null){
				
				activityContainer.add(content);
			}
		}		
		else {
			super.setInSlot(slot, content);
		}
	}

	@Override
	public void addToSlot(Object slot, Widget content) {
		if (slot == DATEGROUP_SLOT) {
			if (content != null) {
				ulTaskGroups.add(content);
			}
		} else {
			super.addToSlot(slot, content);
		}
	}

	public HasClickHandlers getAddButton() {
		return btnAdd;
	}

	public void setHeading(String heading) {
		hCategory.setInnerText(heading);
	}

	public HasClickHandlers getRefreshButton() {
		return aRefresh;
	}

	@Override
	public void bindAlerts(HashMap<TaskType, Integer> alerts) {

		for (TaskType type : alerts.keySet()) {
			String txt = type.getTitle();
			switch (type) {
			case APPROVALREQUESTDONE:
				aRecentApprovals.setText(txt + " (" + alerts.get(type) + ")");
				break;
			case APPROVALREQUESTNEW:
				aNewReq.setText(txt + " (" + alerts.get(type) + ")");
				break;
			case DRAFT:
				aDrafts.setText(txt + " (" + alerts.get(type) + ")");
				break;
			case APPROVED:
				aApproved.setText(txt + " (" + alerts.get(type) + ")");
				break;
			case INPROGRESS:
				aProgress.setText(txt + " (" + alerts.get(type) + ")");
				break;
			case REJECTED:
				aRejected.setText(txt + " (" + alerts.get(type) + ")");
				break;
			case FLAGGED:
				aFlagged.setText(txt + " (" + alerts.get(type) + ")");
				break;
			}
		}

	}

	public void setHasItems(boolean hasItems) {
		UIObject.setVisible(spnNoItems.getElement(), !hasItems);
		if (!hasItems) {
			spnNoItems.setText("No items to display");
		}
	}

	@Override
	public void setTaskType(TaskType currentTaskType) {
		liDrafts.removeClassName("active");
		liApproved.removeClassName("active");
		liFlagged.removeClassName("active");
		liNewReq.removeClassName("active");
		liRejected.removeClassName("active");
		liRecentApprovals.removeClassName("active");
		liProgress.removeClassName("active");

		switch (currentTaskType) {
		case APPROVALREQUESTDONE:
			liRecentApprovals.addClassName("active");
			break;

		case APPROVALREQUESTNEW:
			liNewReq.addClassName("active");
			break;

		case APPROVED:
			liApproved.addClassName("active");
			break;

		case DRAFT:
			liDrafts.addClassName("active");
			break;

		case FLAGGED:
			liFlagged.addClassName("active");
			break;

		case INPROGRESS:
			liProgress.addClassName("active");
			break;

		case REJECTED:
			liRejected.addClassName("active");
			break;
		}
	}

	@Override
	public void showActivitiesPanel(boolean show) {
		if(show){
			activityContainer.removeStyleName("hide");
			divDocListing.addClassName("hide");
			divDocView.addClassName("hide");
		}else{
			activityContainer.addStyleName("hide");
			divDocListing.removeClassName("hide");
			divDocView.removeClassName("hide");
		}
	}
	
	public HTMLPanel getWholeContainer() {
		return wholeContainer;
	}
	
	public SpanElement getLoadingtext() {
		return loadingtext;
	}

}
