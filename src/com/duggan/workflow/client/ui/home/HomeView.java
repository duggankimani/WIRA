package com.duggan.workflow.client.ui.home;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.ButtonElement;
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
import com.sencha.gxt.widget.core.client.ContentPanel;

import static com.duggan.workflow.client.ui.home.HomePresenter.*;

public class HomeView extends ViewImpl implements
		HomePresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, HomeView> {
	}

	@UiField HTMLPanel container;
	
	@UiField Button btnSimulate;
	
	@UiField Button btnAdd;
	
	@UiField Button btnEdit;
	
	@UiField Anchor aLogout;
	
	@UiField HTMLPanel docContainer;
	
	@Inject
	public HomeView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		UIObject.setVisible(btnEdit.getElement(), false);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}	
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		
		if(slot==ITEM_SLOT){
			
			container.clear();
			
			if(content!=null){
				
				container.add(content);
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
		
		if(slot==ITEM_SLOT){
			if(content!=null){
				
				container.add(content);
			}
			
		}else{
			super.addToSlot(slot, content);
		}
	}
	
	public Button getSimulationBtn(){
		return btnSimulate;
	}
	
	public Button getAddButton(){
		return btnAdd;
	}
	
	public Button getEditButton(){
		return btnEdit;
	}

	@Override
	public void showEdit(boolean displayed) {
		UIObject.setVisible(btnEdit.getElement(), displayed);
	}
	
	public HasClickHandlers getLogout(){
		return aLogout;
	}
	
}
