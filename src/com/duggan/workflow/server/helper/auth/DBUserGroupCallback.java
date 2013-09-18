package com.duggan.workflow.server.helper.auth;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jbpm.task.identity.UserGroupCallback;

import com.duggan.workflow.server.dao.model.Group;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;

public class DBUserGroupCallback implements UserGroupCallback{

	@Override
	public boolean existsUser(String userId) {
		
		User user = getUser(userId);
		
		return user!=null;
	}

	@Override
	public boolean existsGroup(String groupId) {
		
		Group group = getGroup(groupId);
		
		return group!=null;
	}

	@Override
	public List<String> getGroupsForUser(String userId, List<String> groupIds,
			List<String> allExistingGroupIds) {
		
		Collection<Group> groups = getUser(userId).getGroups();
		
		if(groups!=null)
		for(Group g: groups){
			groupIds.add(g.getName());
		}
		
		return groupIds;
	}

	public User getUser(String userId){
		EntityManager em = DB.getEntityManager();		
		Query query = em.createQuery("from BUser u where u.userId=:userId");
		query.setParameter("userId", userId);
		Object user = query.getSingleResult();
		
		return (User)user;
	}
	
	public Group getGroup(String groupId){
		EntityManager em = DB.getEntityManager();		
		Query query = em.createQuery("from BGroup p where p.name=:name");
		query.setParameter("name", groupId);
		Object group = query.getSingleResult();
		
		return (Group)group;
	}
	
	public void saveGroup(Group group){
		EntityManager em = DB.getEntityManager();
		em.persist(group);
	}
	
	public void saveUser(User user){
		EntityManager em = DB.getEntityManager();
		em.persist(user);
	}
}
