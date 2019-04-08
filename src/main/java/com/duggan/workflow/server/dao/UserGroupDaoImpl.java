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

import com.duggan.workflow.client.ui.util.StringUtils;
import com.duggan.workflow.server.dao.model.Group;
import com.duggan.workflow.server.dao.model.OrgModel;
import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.dao.model.UserFilter;
import com.duggan.workflow.shared.model.GroupFilter;
import com.wira.commons.shared.models.HTUser;

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

	public List<User> getAllUsers(UserFilter filter, Integer offset, Integer limit) {
			
		StringBuffer jpql = new StringBuffer("select distinct u.id, u.refid, u.firstname, u.lastname, u.userid, u.email, o.id orgid, o.refId,o.description"
				+ " from buser u "
				+ "left join orgmodel o on (u.orgid=o.id) "
				+ "left join UserGroup ug on (u.id=ug.userid) "
				+ "left join BGroup g on (g.id=ug.groupid) "
				+ "where u.isActive=1 "
				+ "and o.isActive=1 ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(!StringUtils.isNullOrEmpty(filter.getSearchTerm())){
			jpql.append(" and (lower(u.userId) like :searchTerm or "
					+ "lower(u.lastName) like :searchTerm or "
					+ "lower(u.firstName) like :searchTerm or "
					+ "lower(u.email) like :searchTerm or "
					+ "lower(o.description) like :searchTerm) ");
			params.put("searchTerm", "%"+filter.getSearchTerm().toLowerCase()+"%");
		}
		
		if(!StringUtils.isNullOrEmpty(filter.getGroupId())){
			jpql.append(" and g.groupId=:groupId");
			params.put("groupId", filter.getGroupId());
		}
		
		if(!StringUtils.isNullOrEmpty(filter.getOrgName())){
			jpql.append(" and o.name=:orgName");
			params.put("orgName", filter.getOrgName());
		}
		
		jpql.append(" order by u.lastName, u.firstName");
		
		Query query = em.createNativeQuery(jpql.toString());
		for(String key: params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		List<User> users = new ArrayList<>();
		List<Object[]> rows = getResultList(query, offset, limit);
		for(Object[] row: rows) {
			Object val = null;
			int col = 0;
			Long id = (val=row[col++]) == null? null : ((Number)val).longValue();
			String refId = (val=row[col++]) == null? null : val.toString();
			String firstName = (val=row[col++]) == null? null : val.toString();
			String lastName = (val=row[col++]) == null? null : val.toString();
			String userId = (val=row[col++]) == null? null : val.toString();
			String email = (val=row[col++]) == null? null : val.toString();
			Long orgId = (val=row[col++]) == null? null : ((Number)val).longValue();
			String orgRefId = (val=row[col++]) == null? null : val.toString();
			String description = (val=row[col++]) == null? null : val.toString();
			
			User user = new User();
			user.setId(id);
			user.setRefId(refId);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);
			user.setUserId(userId);
			OrgModel org = new OrgModel();
			org.setId(orgId);
			org.setRefId(orgRefId);
			org.setDescription(description);
			user.setOrg(org);
			users.add(user);
		}
		
		return users;
	}
	
	public Integer getUserCount(String searchTerm) {
		StringBuffer jpql = new StringBuffer("select count(*) FROM BUser u ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(searchTerm!=null){
			jpql.append(" where (lower(u.userId) like :searchTerm or "
					+ "lower(u.lastName) like :searchTerm or "
					+ "lower(u.firstName) like :searchTerm or "
					+ "lower(u.email) like :searchTerm) and u.isActive=1 ");
			params.put("searchTerm", "%"+searchTerm.toLowerCase()+"%");
		}
		
		Query query = em.createQuery(jpql.toString());
		for(String key: params.keySet()){
			query.setParameter(key, params.get(key));
		}
		
		
		Number count = (Number) query.getSingleResult();
		return count.intValue();
	}

	@SuppressWarnings("unchecked")
	public List<Group> getAllGroups(GroupFilter filter, Integer offset, Integer limit) {
		
		StringBuffer jpql = new StringBuffer("select new com.duggan.workflow.server.dao.model.Group"
				+ "(b.id, b.refId, b.name, b.fullName) from BGroup b "
				+ "join b.members m "
				+ "where b.isActive=1 ");

		
		Map<String, Object> params = new HashMap<String, Object>();
		if(!StringUtils.isNullOrEmpty(filter.getSearchTerm())){
			jpql.append(" and (lower(b.name) like :searchTerm "
					+ "or lower(b.fullName) like :searchTerm) and b.isActive=1 ");
			params.put("searchTerm", "%"+filter.getSearchTerm().toLowerCase()+"%");
		}
		
		if(!StringUtils.isNullOrEmpty(filter.getGroupId())) {
			jpql.append(" and b.name=:groupId");
			params.put("groupdId", filter.getGroupId());
		}
		
		if(!StringUtils.isNullOrEmpty(filter.getUserId())) {
			jpql.append(" and m.userId= :userId");
			params.put("userId", filter.getUserId());
		}
		
		jpql.append(" order by b.fullName");
		
		Query query = em.createQuery(jpql.toString());
		for(String key: params.keySet()){
			query.setParameter(key, params.get(key));
		}

		if(offset==null) {
			offset = 0;
		}
		
		if(limit==null) {
			return query.getResultList();
		}
		return query.setFirstResult(offset).setMaxResults(limit).getResultList();
	}


	public Integer getGroupCount(String searchTerm) {
		StringBuffer jpql = new StringBuffer("select count(b) FROM BGroup b ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(searchTerm!=null){
			jpql.append(" where (lower(b.name) like :searchTerm "
					+ "or lower(b.fullName) like :searchTerm) and b.isActive=1 ");
			params.put("searchTerm", "%"+searchTerm.toLowerCase()+"%");
		}
		
		Query query = em.createQuery(jpql.toString());
		for(String key: params.keySet()){
			query.setParameter(key, params.get(key));
		}

		return ((Number) query.getSingleResult()).intValue();
	}
	
	public User getUser(Long id) {
		
		return em.find(User.class, id);
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

	public boolean isValid(String activationRefId, String userRefId) {
		Number value = getSingleResultOrNull(
				em.createNativeQuery("select id from activation where refId=:refId "
						+ "and userRefId=:userRefId "
						+ "and isActive=1")
				.setParameter("refId", activationRefId)
				.setParameter("userRefId", userRefId));
				
		return value!=null;
	}

	public HTUser getBasicUser(String userRefId) {
		
		String sql = "select refid,email,firstname,lastname from buser where refId=:refId and isActive=1" ;
		
		List<Object[]> row = getResultList(em.createNativeQuery(sql)
				.setParameter("refId", userRefId));
		
		HTUser user = new HTUser();
		if(row.size()==1){
			Object[] arr = row.get(0);
			int i=0;
			String refId = arr[i++].toString();
			String email = arr[i++].toString();
			String firstName = arr[i++].toString();
			String lastName = arr[i++].toString();
			
			user.setRefId(refId);
			user.setSurname(lastName);
			user.setName(firstName);
			user.setEmail(email);
			
			return user;
		}
		return null;
	}

	public String getPassword(String username) {
		StringBuilder builder = new StringBuilder("select password from buser u where u.userid=:username");
		Query query = getEntityManager().createNativeQuery(builder.toString())
				.setParameter("username", username);
		
		String password = getSingleResultOrNull(query);
		return password;
	}

}
