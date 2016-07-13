package com.duggan.workflow.client.ui.component;

import java.util.ArrayList;
import java.util.ArrayList;

import com.duggan.workflow.shared.model.RequestInfoDto;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.HtmlEditor;
import com.wira.commons.shared.models.HTUser;

public class Emailer extends Composite {

	private static EmailerUiBinder uiBinder = GWT.create(EmailerUiBinder.class);

	interface EmailerUiBinder extends UiBinder<Widget, Emailer> {
	}

	@UiField TextArea txtSubject;
	@UiField HtmlEditor editor;
	@UiField AutoCompleteField<HTUser> autoUsers;
	
	private RequestInfoDto dto;
	public Emailer() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public Emailer(RequestInfoDto dto) {
		this();
		setRequestInfo(dto);
	}

	private void setRequestInfo(RequestInfoDto dto) {
		this.dto = dto;
		txtSubject.setValue(dto.getSubject());
		editor.setValue(dto.getBody());
		
		ArrayList<HTUser> users = new ArrayList<HTUser>();
		for(HTUser user: dto.getUsers()){
			HTUser modified = new HTUser(){
				public String getDisplayName() {
					return getFullName()+"<"+getEmail()+">";
				}
			};
			modified.setEmail(user.getEmail());
			modified.setSurname(user.getSurname());
			modified.setName(user.getName());
			modified.setUserId(user.getUserId());
			modified.setId(user.getId());
			users.add(modified);
		}
		
		autoUsers.setValues(users);
		autoUsers.select(users);
	}

	public RequestInfoDto getRequestInfo(){
		if(dto==null){
			dto = new RequestInfoDto();
		}
		
		dto.setBody(editor.getValue());
		dto.setSubject(txtSubject.getValue());
		//dto.setUsers(autoUsers.getSelectedItems());
		
		return dto;
	}
}
