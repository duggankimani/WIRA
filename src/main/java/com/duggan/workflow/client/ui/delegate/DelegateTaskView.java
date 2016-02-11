package com.duggan.workflow.client.ui.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.duggan.workflow.client.util.AppContext;
import com.duggan.workflow.shared.events.PresentUserEvent;
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
	
		int i=1;
		
		Map<UserGroup, List<HTUser>> groupUserMap = new HashMap<UserGroup, List<HTUser>>();
		for(HTUser user:users){
			if(AppContext.isCurrentUser(user.getUserId())){
				//cannot delegate to yourself
				continue;
			}
			
			List<UserGroup> usergroups = user.getGroups();
			if(usergroups==null){
				continue;
			}
			
			for(UserGroup group: usergroups){
				List<HTUser> list = groupUserMap.get(group);
				if(list==null){
					list = new ArrayList<HTUser>();
				}
				list.add(user);
				groupUserMap.put(group, list);
			}
			
		}
		
		//Orderring
		Set<UserGroup> groupKeys = groupUserMap.keySet();
		
		List<UserGroup> orderList = new ArrayList<UserGroup>();
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
			
			List<HTUser> usersInGroup = groupUserMap.get(g);

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
