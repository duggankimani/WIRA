package com.duggan.workflow.server.helper.auth.callback;

import java.util.Collection;
import java.util.List;

import org.jbpm.task.identity.UserGroupCallback;

import com.duggan.workflow.server.dao.model.Group;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;

public class DBUserGroupCallbackImpl implements UserGroupCallback{

	@Override
	public boolean existsUser(String userId) {
		
		User user = DB.getUserGroupDao().getUser(userId);
		
		return user!=null;
	}

	@Override
	public boolean existsGroup(String groupId) {
		
		Group group = DB.getUserGroupDao().getGroup(groupId);
		
		return group!=null;
	}

	@Override
	public List<String> getGroupsForUser(String userId, List<String> groupIds,
			List<String> allExistingGroupIds) {
		
		Collection<Group> groups = DB.getUserGroupDao().getUser(userId).getGroups();
		
		if(groups!=null)
		for(Group g: groups){
			groupIds.add(g.getName());
		}
		
		return groupIds;
	}

}
