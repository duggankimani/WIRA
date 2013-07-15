package com.duggan.workflow.client.ui.home;

import com.duggan.workflow.client.ui.component.BulletListPanel;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.duggan.workflow.client.ui.home.HomePresenter.*;

public class HomeView extends ViewImpl implements
		HomePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HomeView> {
	}

	@UiField Anchor aSimulate;
	
	@UiField Button btnAdd;
	
	@UiField Anchor aEdit;
	
	@UiField HTMLPanel docContainer;
	
	@UiField BulletListPanel ulTaskGroups;
	
	@UiField HeadingElement hCategory;
		
	@Inject
	public HomeView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		UIObject.setVisible(aEdit.getElement(), false);
		ulTaskGroups.setId("navigation-menu");		
		aEdit.getElement().setAttribute("type","button");
		aSimulate.getElement().setAttribute("type","button");
		btnAdd.getElement().setAttribute("type","button");

		docContainer.addHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				System.err.println("### ABS");
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

	public HasClickHandlers getSimulationBtn(){
		return aSimulate;
	}
	
	public HasClickHandlers getAddButton(){
		return btnAdd;
	}
	
	public HasClickHandlers getEditButton(){
		return aEdit;
	}

	public void setHeading(String heading){
		hCategory.setInnerText(heading);
	}
	
	@Override
	public void showEdit(boolean displayed) {
		UIObject.setVisible(aEdit.getElement(), displayed);
	}
	
}
