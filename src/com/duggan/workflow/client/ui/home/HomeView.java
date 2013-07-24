package com.duggan.workflow.client.ui.home;

import java.util.HashMap;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.gwtplatform.mvp.client.ViewImpl;
import com.github.gwtbootstrap.client.ui.base.InlineLabel;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.duggan.workflow.client.ui.home.HomePresenter.*;

public class HomeView extends ViewImpl implements
		HomePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HomeView> {
	}
	
	@UiField Button btnAdd;
		
	@UiField HTMLPanel docContainer;
	
	@UiField BulletListPanel ulTaskGroups;
	
	@UiField HeadingElement hCategory;
	
	@UiField Hyperlink aDrafts;
	@UiField Hyperlink aProgress;
	@UiField Hyperlink aApproved;
	@UiField Hyperlink aRejected;
	@UiField Hyperlink aNewReq;
	@UiField Hyperlink aRecentApprovals;
	@UiField Hyperlink aFlagged;
	@UiField Anchor aRefresh;
	//@UiField SpanElement iRefresh;
	
	@Inject
	public HomeView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		ulTaskGroups.setId("navigation-menu");
		
		btnAdd.getElement().setAttribute("type","button");

		//InlineLabel l;
		//l.addClickHandler(handler)
		docContainer.addHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//System.err.println("### ABS");
			}
		}, ClickEvent.getType());
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}	
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		
		if(slot==DATEGROUP_SLOT){
			
			ulTaskGroups.clear();
			if(content!=null){
				ulTaskGroups.add(content);
			}
			
		}else if(slot==DOCUMENT_SLOT){
			docContainer.clear();
			
			if(content!=null){
				docContainer.add(content);
			}
		}	
		else{
			super.setInSlot(slot, content);
		}
	}
	
	@Override
	public void addToSlot(Object slot, Widget content) {
		
		if(slot==DATEGROUP_SLOT){
			if(content!=null){				
				ulTaskGroups.add(content);
			}			
		}else{
			super.addToSlot(slot, content);
		}
	}

	public HasClickHandlers getAddButton(){
		return btnAdd;
	}

	public void setHeading(String heading){
		hCategory.setInnerText(heading);
	}
	
	public HasClickHandlers getRefreshButton(){
		return aRefresh;
	}
	
	@Override
	public void bindAlerts(HashMap<TaskType, Integer> alerts) {
		
		for(TaskType type: alerts.keySet()){
			String txt = type.getTitle();
			switch(type){
				case APPROVALREQUESTDONE:					
					aRecentApprovals.setText(txt+" ("+alerts.get(type)+")");
					break;				
				case APPROVALREQUESTNEW:
					aNewReq.setText(txt+" ("+alerts.get(type)+")");
					break;
				case DRAFT:
					aDrafts.setText(txt+" ("+alerts.get(type)+")");
					break;
				case APPROVED:
					aApproved.setText(txt+" ("+alerts.get(type)+")");
					break;		
				case INPROGRESS:
					aProgress.setText(txt+" ("+alerts.get(type)+")");
					break;
				case REJECTED:
					aRejected.setText(txt+" ("+alerts.get(type)+")");
					break;
				case FLAGGED:
					aFlagged.setText(txt+" ("+alerts.get(type)+")");
					break;
			}
		}
		
	}
	
}
