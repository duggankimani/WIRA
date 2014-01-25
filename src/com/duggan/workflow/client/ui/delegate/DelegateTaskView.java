package com.duggan.workflow.client.ui.delegate;

import java.util.ArrayList;
import java.util.List;

import com.duggan.workflow.client.ui.events.PresentUserEvent;
import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.UserGroup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class DelegateTaskView extends Composite {

	private static DelegateTaskViewUiBinder uiBinder = GWT
			.create(DelegateTaskViewUiBinder.class);

	interface DelegateTaskViewUiBinder extends
			UiBinder<Widget, DelegateTaskView> {
	}
	
	@UiField HTMLPanel container;
	
	public DelegateTaskView(List<HTUser> users) {
		initWidget(uiBinder.createAndBindUi(this));
		
		List<UserGroup> groups = new ArrayList<UserGroup>();
		int i=1;
		
		for(HTUser user:users){
			if(AppContext.isCurrentUser(user.getUserId())){
				//cannot delegate to yourself
				continue;
			}
			
			List<UserGroup> usergroups = user.getGroups();
			if(usergroups==null){
				continue;
			}
			
			for(UserGroup g: usergroups){
				if(groups.contains(g)){
					AppContext.fireEvent(new PresentUserEvent(user, g));
				}else{
					DelegationGroupView view = new DelegationGroupView(g,i++);
					container.add(view);
					AppContext.fireEvent(new PresentUserEvent(user, g));
					groups.add(g);
				}
			}
		}
	}	

	public HTUser getSelectedUser() {
		int count = container.getWidgetCount();
		for(int i=0; i<count; i++){
			DelegationGroupView view = (DelegationGroupView)container.getWidget(i);
			HTUser user = view.getSelectedUser();
			if(user!=null){
				return user;
			}
		}
		return null;
	}

}
