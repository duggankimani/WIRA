package com.duggan.workflow.client.ui.admin.adduser;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.PopupView;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;

public class AddUserPresenter extends PresenterWidget<AddUserPresenter.MyView> {

	public interface MyView extends PopupView {

		void setType(TYPE type);
		
		HasClickHandlers getSaveUser();
		
		HasClickHandlers getSaveGroup();

		boolean isValid();
		
	}

	public enum TYPE{
		GROUP, USER
	}
	
	TYPE type;
	@Inject
	public AddUserPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().getSaveUser().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isValid()){
					//save user
				}
			}
		});
		
		getView().getSaveGroup().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getView().isValid()){
					//save  group
				}
			}
		});
	}
	
	public void setType(TYPE type){
		this.type = type;
		getView().setType(type);
	}
}
