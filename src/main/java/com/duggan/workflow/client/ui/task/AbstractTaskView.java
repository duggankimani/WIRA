package com.duggan.workflow.client.ui.task;

import static com.duggan.workflow.client.ui.task.AbstractTaskPresenter.*;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.duggan.workflow.client.ui.task.DraftsPresenter.IDraftsView;
import com.duggan.workflow.client.ui.task.ParticipatedPresenter.IParticipatedView;
import com.duggan.workflow.client.ui.task.SuspendedTaskPresenter.ISuspendedView;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AbstractTaskView extends ViewImpl implements AbstractTaskPresenter.ITaskView, IDraftsView, IParticipatedView, ISuspendedView{

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, AbstractTaskView> {
	}

	@UiField TextBox txtSearch;
	//@UiField HTMLPanel divDocPopup;
	@UiField Element divDocListing;
	@UiField Element divDocView;
	@UiField BulletListPanel ulTaskGroups;
	@UiField HeadingElement hCategory;
	@UiField Anchor aRefresh;
	@UiField Anchor iFilterdropdown;
	@UiField HTMLPanel filterDialog;
	@UiField InlineLabel spnNoItems;
	@UiField HTMLPanel docContainer;
	
	//Filter Dialog Caret
	boolean isNotDisplayed=true;
	
	@Inject
	public AbstractTaskView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		ulTaskGroups.setId("navigation-menu");
		txtSearch.getElement().setAttribute("placeholder", "Search...");
		divDocListing.setId("middle-nav");
		divDocView.setId("detailed-info");

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
				if(isNotDisplayed){
				filterDialog.removeStyleName("hidden");
				isNotDisplayed=false;
				}else{
				filterDialog.addStyleName("hidden");
				isNotDisplayed=true;
				}
			}
		});
		
		txtSearch.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				//hideFilterDialog();
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == DATEGROUP_SLOT) {
			ulTaskGroups.clear();
			if (content != null) {
				ulTaskGroups.add(content);
			}

		} else if (slot == DOCUMENT_SLOT) {
			docContainer.clear();
			if (content != null) {
				docContainer.add(content);
			}
		} 
//		else if(slot== ACTIVITIES_SLOT){
//			activityContainer.clear();
//			if(content != null){
//				activityContainer.add(content);
//			}
//		}
		else if (slot == FILTER_SLOT) {
			filterDialog.clear();
			if (content != null) {
				filterDialog.add(content);
			}
			
		}	
//		else if (slot == DOCPOPUP_SLOT) {
//			divDocPopup.clear();
//			if (content != null) {
//				divDocPopup.add(content);
//			}
//			
//		}
		else{
			super.setInSlot(slot, content);
		}
		
	}

	@Override
	public void addToSlot(Object slot, IsWidget content) {
		if (slot == DATEGROUP_SLOT) {
			if (content != null) {
				ulTaskGroups.add(content);
			}
		} else {
			super.addToSlot(slot, content);
		}
	}


	public void setHeading(String heading) {
		hCategory.setInnerText(heading);
	}

	public HasClickHandlers getRefreshButton() {
		return aRefresh;
	}
	
	public void setHasItems(boolean hasItems) {
		UIObject.setVisible(spnNoItems.getElement(), !hasItems);
		if (!hasItems) {
			spnNoItems.setText("No items to display");
		}
	}
	
	public TextBox getSearchBox(){
		return txtSearch;
	}

	@Override
	public void hideFilterDialog() {
		filterDialog.addStyleName("hidden");
		isNotDisplayed=true;
	}
	
	@Override
	public void setSearchBox(String text) {
		txtSearch.setValue(text);
	}

	@Override
	public void setTaskType(TaskType currentTaskType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Anchor getaRefresh() {
		// TODO Auto-generated method stub
		return null;
	}

}
