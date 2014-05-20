package com.duggan.workflow.client.ui.admin.users;

import static com.duggan.workflow.client.ui.admin.users.UserPresenter.GROUPSLOT;
import static com.duggan.workflow.client.ui.admin.users.UserPresenter.ITEMSLOT;

import com.duggan.workflow.client.place.NameTokens;
import com.duggan.workflow.client.ui.admin.users.save.UserSavePresenter.TYPE;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class UserView extends ViewImpl implements UserPresenter.MyView {

	private final Widget widget;
	@UiField Anchor aNewUser;
	@UiField Anchor aNewGroup;
	@UiField Anchor aUserstab;
	@UiField Anchor aGroupstab;
	@UiField HTMLPanel panelUsers;
	@UiField HTMLPanel panelGroup;
	
	@UiField Element divUserContent;
	@UiField Element divGroupContent;
	
	@UiField Element liGroup;
	@UiField Element liUser;

	public interface Binder extends UiBinder<Widget, UserView> {
	}

	PlaceManager placeManager;
	
	@Inject
	public UserView(final Binder binder, PlaceManager manager) {
		widget = binder.createAndBindUi(this);
		placeManager = manager;
		
		aUserstab.getElement().setAttribute("data-toggle", "tab");
		aGroupstab.getElement().setAttribute("data-toggle", "tab");
		
		divUserContent.setId("user");
		divGroupContent.setId("groups");
		
		aUserstab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				placeManager.revealPlace(new PlaceRequest.Builder()
				.nameToken(NameTokens.usermgt)
				.with("p", "user").build());
			}
		});
		
		aGroupstab.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				placeManager.revealPlace(new PlaceRequest.Builder()
				.nameToken(NameTokens.usermgt)
				.with("p", "group").build());
			}
		});
	}
	
	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if(slot==ITEMSLOT){
			panelUsers.clear();
			
			if(content!=null){
				panelUsers.add(content);
			}
		}if(slot==GROUPSLOT){
			panelGroup.clear();
			
			if(content!=null){
				panelGroup.add(content);
			}
		}
		else{
			super.setInSlot(slot, content);
		}
		
	}
	
	@Override
	public void addToSlot(Object slot, IsWidget content) {
		
		if(slot==ITEMSLOT){
			
			if(content!=null){
				panelUsers.add(content);
			}
		}if(slot==GROUPSLOT){
			
			if(content!=null){
				panelGroup.add(content);
			}
		}else{
			super.addToSlot(slot, content);
		}
		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public HasClickHandlers getaNewUser() {
		return aNewUser;
	}
	
	@Override
	public HasClickHandlers getaNewGroup() {
		return aNewGroup;
	}

	@Override
	public void setType(TYPE type) {
		if(type==TYPE.GROUP){
			aNewUser.addStyleName("hide");
			aNewGroup.removeStyleName("hide");
			liGroup.setClassName("active");
			liUser.removeClassName("active");
			
			divUserContent.removeClassName("in");
			divUserContent.removeClassName("active");
			
			divGroupContent.addClassName("in");
			divGroupContent.addClassName("active");
		}else{
			 aNewUser.removeStyleName("hide");
			 aNewGroup.addStyleName("hide");
			 liGroup.removeClassName("active");
			 liUser.addClassName("active");
			 
			 divUserContent.addClassName("in"); 
			 divUserContent.addClassName("active");
			 
			 divGroupContent.removeClassName("in");
			 divGroupContent.removeClassName("active");
		}
	}
	
}
