package com.duggan.workflow.client.ui.delegate.userDisplay;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.events.UserSelectedEvent;
import com.duggan.workflow.client.ui.events.UserSelectedEvent.UserSelectedHandler;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.HTUser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class UserDisplay extends Composite implements UserSelectedHandler {

	private static UserDisplayUiBinder uiBinder = GWT
			.create(UserDisplayUiBinder.class);

	interface UserDisplayUiBinder extends UiBinder<Widget, UserDisplay> {
	}
	
	@UiField CheckBox chkSelect;
	@UiField SpanElement spnNames;
	@UiField Image img;
	
	private HTUser user;
	
	public UserDisplay(final HTUser user) {
		initWidget(uiBinder.createAndBindUi(this));
		this.user = user;
		setSpnNames(user.getSurname()+" " +user.getName());
		
		chkSelect.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if(event.getValue()){
					AppContext.fireEvent(new UserSelectedEvent(user));
				}
			}
		});
		
		img.addErrorHandler(new ErrorHandler() {
			
			@Override
			public void onError(ErrorEvent event) {
				img.setUrl("img/blueman.png");
			}
		});
		setImage(user);
	}
	
	void setSpnNames(String text) {
		spnNames.setInnerText(text);
	}
	
	/**
	 * 
	 * @param type
	 * @param handler
	 */
	public void addRegisteredHandler(Type<? extends EventHandler> type, UserDisplay handler){
		@SuppressWarnings("unchecked")
		HandlerRegistration hr = AppContext.getEventBus().addHandler(
				(GwtEvent.Type<EventHandler>)type, handler);
		handlers.add(hr);
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		addRegisteredHandler(UserSelectedEvent.TYPE, this);
	}
	
	@Override
	protected void onUnload() {
		super.onUnload();
		cleanUpEvents();
	}
	
	/**
	 * 
	 */
	private void cleanUpEvents() {
		for(HandlerRegistration hr: handlers){
			hr.removeHandler();
		}
		handlers.clear();
	}
	
	List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();

	@Override
	public void onUserSelected(UserSelectedEvent event) {
		if(!(event.getUser().equals(user))){
			chkSelect.setValue(false);
		}
	}

	public HTUser getSelectedUser() {
		if(chkSelect.getValue()!=null && chkSelect.getValue()){
			return user;
		}
		
		return null;
	}
	
	private void setImage(HTUser user) {
		String moduleUrl = GWT.getModuleBaseURL().replace("/gwtht", "");
		if(moduleUrl.endsWith("/")){
			moduleUrl = moduleUrl.substring(0, moduleUrl.length()-1);
		}
		moduleUrl =moduleUrl+"/getreport?ACTION=GetUser&width=50&userId="+user.getUserId();
		img.setUrl(moduleUrl);
	}
}
