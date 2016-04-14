package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.duggan.workflow.server.dao.model.Group;
import com.duggan.workflow.server.dao.model.User;

public class UserGroupDaoImpl extends BaseDaoImpl{

	Properties dbProperties = new Properties();
	
	public UserGroupDaoImpl(EntityManager em){
		super(em);
	}
	
	public User getUser(String userId){
		Query query = em.createNamedQuery("User.getUserByUserId");
		query.setParameter("userId", userId);
		Object user= null;
		try{
			user = query.getSingleResult();
			return (User)user;
		}catch(NoResultException e){
			
		}
		
		return null;
	}
	
	public Group getGroup(String groupId){
		Query query = em.createNamedQuery("Group.getGroupByGroupId");
		query.setParameter("name", groupId);
		
		Object group =null;
		
		try{
			group  = query.getSingleResult();
		}catch(NoResultException e){}
		
		return (Group)group;
	}
	
	public void saveGroup(Group group){
		save(group);
	}
	
	public void saveUser(User user){
		List<Group> groups = new ArrayList<>();
		groups.addAll(user.getGroups());
		
		user.setGroups(null);
		save(user);
				
		user.setGroups(groups);
		em.merge(user);		
	}

	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		Query query = em.createQuery("FROM BUser u order by u.lastName, u.firstName");		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Group> getAllGroups() {
		Query query = em.createQuery("FROM BGroup b order by b.fullName");		
		return query.getResultList();
	}

	public User getUser(Long id) {
		
		return em.find(User.class, id);
	}

	public Collection<Group> getAllGroups(String userId) {
		
		User user = getUser(userId);
		
		assert user!=null;
		
		return user.getGroups();
	}

	public Collection<User> getAllUsers(String groupId) {
		Group group = getGroup(groupId);
		if(group==null){
			return new ArrayList<>();
		}
		return group.getMembers();
	}
	

}
