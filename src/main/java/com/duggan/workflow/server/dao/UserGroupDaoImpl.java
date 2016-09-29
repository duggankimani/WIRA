package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	public List<User> getAllUsers(String searchTerm) {
			
		StringBuffer jpql = new StringBuffer("FROM BUser u ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(searchTerm!=null){
			jpql.append(" where (lower(u.userId) like :searchTerm or "
					+ "lower(u.lastName) like :searchTerm or "
					+ "lower(u.firstName) like :searchTerm or "
					+ "lower(u.email) like :searchTerm) and u.isActive=1 ");
			params.put("searchTerm", "%"+searchTerm.toLowerCase()+"%");
		}
		jpql.append(" order by u.lastName, u.firstName");
		
		Query query = em.createQuery(jpql.toString());
		for(String key: params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Group> getAllGroups(String searchTerm) {
		
		StringBuffer jpql = new StringBuffer("FROM BGroup b ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(searchTerm!=null){
			jpql.append(" where (lower(b.name) like :searchTerm "
					+ "or lower(b.fullName) like :searchTerm) and b.isActive=1 ");
			params.put("searchTerm", "%"+searchTerm.toLowerCase()+"%");
		}
		jpql.append(" order by b.fullName");
		
		Query query = em.createQuery(jpql.toString());
		for(String key: params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		return query.getResultList();
	}

	public User getUser(Long id) {
		
		return em.find(User.class, id);
	}

	public Collection<Group> getAllGroupsByUserId(String userId) {
		
		User user = getUser(userId);
		
		assert user!=null;
		
		return user.getGroups();
	}
	
	

	public Collection<User> getAllUsersByGroupId(String groupId) {
		Group group = getGroup(groupId);
		if(group==null){
			return new ArrayList<>();
		}
		return group.getMembers();
	}

	public User getUserByEmail(String email) {
		StringBuffer jpql = new StringBuffer("FROM BUser u where u.email=:email");
		
		return getSingleResultOrNull(getEntityManager()
				.createQuery(jpql.toString())
				.setParameter("email", email)
				);
	}

	public boolean userExists(String actorId) {
		String sql = "select count(*) from buser where userid=:userId";
		Number count = getSingleResultOrNull(getEntityManager()
				.createNativeQuery(sql)
				.setParameter("userId", actorId));
		return count.intValue()>0;
	}

	public boolean usersExist(String groupId) {
		String sql = "select count(*) from buser u "
				+ "inner join usergroup ug on (u.id=ug.userid) "
				+ "inner join bgroup g on (g.id=ug.groupid) "
				+ "where g.name=:groupId";
		
		Number count = getSingleResultOrNull(getEntityManager()
				.createNativeQuery(sql)
				.setParameter("groupId", groupId));
		
		return count.intValue()>0;
	}

	public List<String> getGroupsForUser(String userId) {
		String hql = "select name from bgroup g "
				+ "inner join usergroup ug on (g.id=ug.groupid) "
				+ "inner join buser u on (u.id=ug.userid) where u.userid=:userId";
		return getResultList(em.createNativeQuery(hql).setParameter("userId", userId));
	}
	

}
