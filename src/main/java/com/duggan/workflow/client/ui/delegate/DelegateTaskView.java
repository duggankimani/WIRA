package com.duggan.workflow.client.ui.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.events.PresentUserEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;

public class DelegateTaskView extends Composite {

	private static DelegateTaskViewUiBinder uiBinder = GWT
			.create(DelegateTaskViewUiBinder.class);

	interface DelegateTaskViewUiBinder extends
			UiBinder<Widget, DelegateTaskView> {
	}
	
	@UiField HTMLPanel container;
	
	public DelegateTaskView(ArrayList<HTUser> users) {
		initWidget(uiBinder.createAndBindUi(this));
	
		int i=1;
		
		HashMap<UserGroup, ArrayList<HTUser>> groupUserMap = new HashMap<UserGroup, ArrayList<HTUser>>();
		for(HTUser user:users){
			if(AppContext.isCurrentUser(user.getUserId())){
				//cannot delegate to yourself
				continue;
			}
			
			ArrayList<UserGroup> usergroups = user.getGroups();
			if(usergroups==null){
				continue;
			}
			
			for(UserGroup group: usergroups){
				ArrayList<HTUser> ArrayList = groupUserMap.get(group);
				if(ArrayList==null){
					ArrayList = new ArrayList<HTUser>();
				}
				ArrayList.add(user);
				groupUserMap.put(group, ArrayList);
			}
			
		}
		
		//Orderring
		Set<UserGroup> groupKeys = groupUserMap.keySet();
		
		ArrayList<UserGroup> orderList = new ArrayList<UserGroup>();
		orderList.addAll(groupKeys);
		Collections.sort(orderList, new Comparator<UserGroup>() {
			@Override
			public int compare(UserGroup o1, UserGroup o2) {
				
				return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
		});
		
		for(UserGroup g: orderList){
			DelegationGroupView view = new DelegationGroupView(g,i++);
			container.add(view);
			
			ArrayList<HTUser> usersInGroup = groupUserMap.get(g);

			for(HTUser user: usersInGroup){
				AppContext.fireEvent(new PresentUserEvent(user, g));
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
