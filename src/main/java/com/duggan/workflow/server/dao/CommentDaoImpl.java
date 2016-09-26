package com.duggan.workflow.server.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.time.DateUtils;

import com.duggan.workflow.server.dao.model.CommentModel;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Comment;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;

public class CommentDaoImpl extends BaseDaoImpl{


	public CommentDaoImpl(EntityManager em) {
		super(em);
	}

	public CommentModel getComment(Long id) {

		List lst = em.createQuery("FROM CommentModel n where id= :id")
				.setParameter("id", id).getResultList();

		if (lst.size() > 0) {
			return (CommentModel) lst.get(0);
		}
		return null;
	}

	public CommentModel saveOrUpdate(CommentModel comment) {
		if (comment.getId() == null) {
			comment.setCreated(new Date());
			comment.setCreatedBy(SessionHelper.getCurrentUser().getUserId());
		} else {
			comment.setUpdated(new Date());
			comment.setUpdatedBy(SessionHelper.getCurrentUser().getUserId());
		}
		em.persist(comment);
		return comment;
	}

	public List<Comment> getAllComments(String processRefId, String userId) {

		String query = "select c.id,"
				+ "c.refId,"
				+ "c.created,"
				+ "c.createdby,"
				+ "u.firstname creatorfirstname,"
				+ "u.lastname creatorlastname, "
				+ "c.comment, "
				+ "c.docrefid, "
				+ "c.documentid, "
				+ "c.parentid, "
				+ "c.userid, "
				+ "u1.firstname targetfirstname, "
				+ "u1.lastname targetlastname, "
				+ "t.display,"
				+ "d.caseno "
				+ "from localcomment c "
				+ "inner join documentjson d on (d.refId=c.docRefId) "
				+ "inner join addoctype t on (t.refId=d.docTypeRefId) "
				+ "inner join processdefmodel p on (p.id=t.processDefId) "
				+ "inner join buser u on (c.createdBy=u.userId) "
				+ "left join buser u1 on (c.userid=u1.userid) "
				+ "where c.userId=:userId "
				+ "and (:processRefId='' or :processRefId=p.refId) "
				+ "and c.created>:created "
				+ "order by c.created desc";
		
		List<Object[]> result = getResultList(em.createNativeQuery(query)
				.setParameter("userId", userId)
				.setParameter("processRefId", processRefId==null? "": processRefId)
				.setParameter("created", DateUtils.addDays(new Date(), -30))
				);
		
		List<Comment> comments = new ArrayList<Comment>();
		
		for(Object[] row: result){
			Object value = null;
			int i=0;
			Long id = ((value=row[i++])==null? null: ((Number)value).longValue());
			String refId = ((value=row[i++])==null? null: ((String)value));
			Date created = ((value=row[i++])==null? null: ((Date)value));
			String createdby = ((value=row[i++])==null? null: ((String)value));
			String creatorfirstname = ((value=row[i++])==null? null: ((String)value));
			String creatorlastname = ((value=row[i++])==null? null: ((String)value));
			String comment = ((value=row[i++])==null? null: ((String)value));
			String docrefid = ((value=row[i++])==null? null: ((String)value)); 
			Long documentid = ((value=row[i++])==null? null: ((Number)value).longValue());
			Long parentid = ((value=row[i++])==null? null: ((Number)value).longValue()); 
			String userid = ((value=row[i++])==null? null: ((String)value));
			String targetfirstname = ((value=row[i++])==null? null: ((String)value));
			String targetlastname = ((value=row[i++])==null? null: ((String)value));
			String docType = ((value=row[i++])==null? null: ((String)value));
			String caseno = ((value=row[i++])==null? null: ((String)value));
			
			Comment dto = new Comment();
			dto.setId(id);
			dto.setRefId(refId);
			dto.setCreated(created);
			dto.setCreatedBy(new HTUser(createdby, creatorfirstname, creatorlastname));
			dto.setComment(comment);
			dto.setDocRefId(docrefid);
			dto.setDocType(docType);
			dto.setDocumentId(documentid);
			dto.setParentId(parentid);
			dto.setUserId(userid);
			dto.setSubject(caseno);
			
			comments.add(dto);
		}
		
		return comments;
	}

	public void delete(Long id) {
		em.remove(getComment(id));
	}

	@SuppressWarnings("unchecked")
	public List<CommentModel> getAllComments(Long documentId) {

		if (documentId == null) {
			return getAllComments();
		}

		return em
				.createQuery(
						"FROM CommentModel n where n.documentId=:documentId order by created desc")
				.setParameter("documentId", documentId).getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<CommentModel> getAllCommentsByDocRefId(String docRefId) {

		if (docRefId == null) {
			return getAllComments();
		}

		return em
				.createQuery(
						"FROM CommentModel n where n.docRefId=:docRefId order by created desc")
				.setParameter("docRefId", docRefId).getResultList();

	}

	public List<CommentModel> getAllComments() {

		String userId = SessionHelper.getCurrentUser().getUserId();

		List<UserGroup> groups = LoginHelper.getHelper().getGroupsForUser(
				userId);
		String groupsIds = "";
		for (UserGroup group : groups) {
			groupsIds = groupsIds.concat(group.getName() + ",");
		}

		if (groupsIds.isEmpty()) {
			return new ArrayList<>();
		}

		groupsIds = groupsIds.substring(0, groupsIds.length() - 1);

		StringBuffer hql = new StringBuffer(
				"Select c.id FROM localcomment c "
						+ "inner join documentjson d on (d.id=c.documentId) "
						+ "left join Task t on (t.processInstanceId=d.processInstanceId) "
						+ "left join OrganizationalEntity owner on (owner.id= t.actualOwner_id and owner.DTYPE='User') "
						+ "left join PeopleAssignments_PotOwners potowners on (potowners.task_id=t.id)  "
						+ "where "
						+ "t.archived = 0 and "
						+ "(d.createdBy=? or "
						+ " owner.id = ? or "
						+ "( potowners.entity_id = ? or potowners.entity_id in (?) )) "
						+ "and c.created>? " + " order by c.created desc");

		Query query = em.createNativeQuery(hql.toString())
				.setParameter(1, userId).setParameter(2, userId)
				.setParameter(3, userId).setParameter(4, groupsIds)
				.setParameter(5, DateUtils.addDays(new Date(), -30));

		@SuppressWarnings("unchecked")
		List<BigInteger> commentIds = query.getResultList();

		List<Long> ids = new ArrayList<>();
		for (BigInteger id : commentIds) {
			ids.add(id.longValue());
		}

		if (ids == null || ids.isEmpty()) {
			return new ArrayList<>();
		}

		@SuppressWarnings("unchecked")
		List<CommentModel> comments = em
				.createQuery("FROM CommentModel where id in (:ids)")
				.setParameter("ids", ids).getResultList();

		return comments;
	}

	public List<HTUser> getEmailRecipients(Long documentId) {
		String sql = "select t.actualOwner_id from "
				+ "Task t inner join documentjson d "
				+ "on (t.processInstanceId=d.processInstanceId) "
				+ "left join OrganizationalEntity owner "
				+ "on (owner.id= t.actualOwner_id and owner.DTYPE='User') "
				+ "left join PeopleAssignments_PotOwners potowners on "
				+ "(potowners.task_id=t.id) "
				+ "where t.archived = 0 and d.id=:documentId";

		Query query = em.createNativeQuery(sql).setParameter("documentId", documentId);
		List<String> usersIds = query.getResultList();

		List<HTUser> users = new ArrayList<>();
		HTUser currentUser = SessionHelper.getCurrentUser();

		for (String userId : usersIds) {
			HTUser user = new UserDaoHelper().getUser(userId);
			if (!users.contains(user) && !user.equals(currentUser)) {
				users.add(user);
			}

		}
		
		String createdBy = (String)em.createNativeQuery("select createdBy from documentjson where id="
		+documentId).getSingleResult();
		if(!currentUser.getUserId().equals(createdBy)){
			users.add(new UserDaoHelper().getUser(createdBy));
		}
		
		return users;

	}

}
