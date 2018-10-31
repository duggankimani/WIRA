package com.duggan.workflow.server.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.time.DateUtils;

import com.duggan.workflow.server.dao.model.NotificationModel;
import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.ApproverAction;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;

public class NotificationDaoImpl extends BaseDaoImpl{

	
	public NotificationDaoImpl(EntityManager em) {
		super(em);
	}

	public NotificationModel getNotification(Long id) {
		
		List lst = em.createQuery("FROM NotificationModel n where id= :id").setParameter("id", id).getResultList();
		
		if(lst.size()>0){
			return (NotificationModel)lst.get(0);
		}
		return null;
	}

	public NotificationModel saveOrUpdate(NotificationModel model) {
		em.persist(model);
		return model;
	}

	@SuppressWarnings("unchecked")
	public List<Notification> getAllNotifications(String processRefId, String userId) {
		
		String query = "select n.id,"
				+ "n.refid,"
				+ "n.created,"
				+ "n.createdby,"
				+ "u.firstname creatorfirstname,"
				+ "u.lastname creatorlastname, "
				+ "n.approveraction,"
				+ "n.docrefid, "
				+ "n.documentid, "
				+ "n.documenttypedesc,"
				+ "n.fileid, "
				+ "n.filename, "
				+ "n.isread, "
				+ "n.isseen, "
				+ "n.notificationtype, "
				+ "n.owner, "
				+ "u1.firstname ownerfirstname, "
				+ "u1.lastname ownerlastname, "
				+ "n.targetuserid, "
				+ "u2.firstname targetfirstname, "
				+ "u2.lastname targetlastname, "
				+ "t.display,"
				+ "t.name,"
				+ "d.caseno, "
				+ "d.processinstanceid "
				+ "from localnotification n "
				+ "inner join documentjson d on (d.refId=n.docRefId)  "
				+ "inner join addoctype t on (t.refId=d.docTypeRefId)  "
				+ "inner join processdefmodel p on (p.id=t.processDefId) "
				+ "inner join buser u on (n.createdby=u.userid) "
				+ "left join buser u1 on (n.owner=u1.userid) "
				+ "left join buser u2 on (n.targetuserid=u2.userid) "
				+ "where "
				+ "(n.targetuserid=:userId or (n.createdBy=:userId and n.notificationType=:notificationType)) "
				+ "and n.created>:thirtyDays "
				+ "and (:processRefId='' or p.refId=:processRefId)"
				+ "order by created desc";

		List<Object[]> results = getResultList(em.createNativeQuery(query)
				.setParameter("userId", userId)
				.setParameter("notificationType", NotificationType.TASKDELEGATED.name())
				.setParameter("thirtyDays", DateUtils.addDays(new Date(), -30))
				.setParameter("processRefId", processRefId==null? "":processRefId));

		List<Notification> notifications = new ArrayList<Notification>();
		for(Object[] row: results){
			Object value = null;
			int i=0;
			Long id = ((value=row[i++])==null? null: ((Number)value).longValue());
			String refid = ((value=row[i++])==null? null: ((String)value));
			Date created = ((value=row[i++])==null? null: ((Date)value));
			String createdby = ((value=row[i++])==null? null: ((String)value));
			String creatorfirstname = ((value=row[i++])==null? null: ((String)value));
			String creatorlastname = ((value=row[i++])==null? null: ((String)value));
			String approveraction = ((value=row[i++])==null? null: ((String)value));
			String docrefid = ((value=row[i++])==null? null: ((String)value));
			Long documentid = ((value=row[i++])==null? null: ((Number)value).longValue());
			String documenttypedesc = ((value=row[i++])==null? null: ((String)value));
			Long fileid = ((value=row[i++])==null? null: ((Number)value).longValue());
			String filename = ((value=row[i++])==null? null: ((String)value));
			Boolean isread = ((value=row[i++])==null? null: ((Boolean)value));
			Boolean isseen = ((value=row[i++])==null? null: ((Boolean)value));
			String notificationtype = ((value=row[i++])==null? null: ((String)value));
			String owner = ((value=row[i++])==null? null: ((String)value));
			String ownerfirstname = ((value=row[i++])==null? null: ((String)value));
			String ownerlastname = ((value=row[i++])==null? null: ((String)value));
			String targetuserid = ((value=row[i++])==null? null: ((String)value));
			String targetfirstname = ((value=row[i++])==null? null: ((String)value));
			String targetlastname = ((value=row[i++])==null? null: ((String)value));
			String display = ((value=row[i++])==null? null: ((String)value));
			String docTypeName = ((value=row[i++])==null? null: ((String)value));
			String caseno = ((value=row[i++])==null? null: ((String)value));
			Long processInstanceId = ((value=row[i++])==null? null: ((Number)value).longValue());
			
			Notification notification = new Notification();
			if(approveraction!=null)
			notification.setApproverAction(ApproverAction.valueOf(approveraction));
			notification.setCreated(created);
			notification.setCreatedBy(new HTUser(createdby, creatorfirstname, creatorlastname));
			notification.setDocRefId(docrefid);
			notification.setDocumentId(documentid);
			notification.setDocumentType(new DocumentType(processInstanceId, docTypeName, display, ""));
			notification.setDocumentTypeDesc(display);
			notification.setFileId(fileid);
			notification.setFileName(filename);
			notification.setId(id);
			notification.setNotificationType(NotificationType.valueOf(notificationtype));
			notification.setOwner(new HTUser(owner, ownerfirstname, ownerlastname));
			notification.setProcessInstanceId(processInstanceId);
			notification.setRead(isread);
			notification.setRefId(refid);
			notification.setSubject(caseno);
			notification.setTargetUserId(new HTUser(targetuserid, targetfirstname, targetlastname));
			notifications.add(notification);
		}
		
		return notifications;
	}

	public void markRead(Long id, boolean isRead) {
		NotificationModel model = getNotification(id);
		model.setRead(isRead);		
		saveOrUpdate(model);
	}

	public void delete(Long id) {
		em.remove(getNotification(id));
	}

	/**
	 * 
	 * @param userId
	 * @param userId 
	 * @return count
	 */
	public Integer getAlertCount(String processRefId, String userId) {
		
		String hql = "select count(n) from localnotification n ";
		if(processRefId!=null){
			hql = hql+ " inner join documentjson d on (d.refId=n.docRefId) "
					+ "inner join addoctype t on (t.refId=d.docTypeRefId) "
					+ "inner join processdefmodel p on (p.id=t.processDefId) ";
		}
		
		hql = hql+" where " +  
		(processRefId==null? "":" p.refId=:processRefId and ")+
		"(n.targetUserId=:userId or (n.createdBy=:userId and n.notificationType=:notificationType))  " +
		"and n.isRead=:isRead and n.created>:thirtyDays";
		
		Query query = em.createNativeQuery(hql)
		.setParameter("userId", userId)
		.setParameter("notificationType", NotificationType.TASKDELEGATED.name())
		.setParameter("isRead",false)
		.setParameter("thirtyDays", DateUtils.addDays(new Date(), -30));
		if(processRefId!=null){
			query.setParameter("processRefId", processRefId);
		}
		
		Number count = getSingleResultOrNull(query);
		return count.intValue();
	}

	/**
	 * To handle repetition of Doc Forwarded Notification 
	 * @param documentId
	 * @param owner
	 */
	public List getNotification(Long documentId, String owner) {

		List models = em.createQuery("FROM NotificationModel n where documentId= :documentId " +
				"and notificationType=:notificationType and owner=:owner")
				.setParameter("documentId", documentId)
				.setParameter("notificationType", NotificationType.APPROVALREQUEST_OWNERNOTE)
				.setParameter("owner", owner)
				.getResultList();
		return models;
	}

	@SuppressWarnings("unchecked")
	public List<NotificationModel> getAllNotificationsByDocumentId(Long documentId, NotificationType[] notificationTypes) {

		if(documentId==null){
			return getAllNotificationsByDocumentId(notificationTypes);
		}
		
		List<NotificationType> notes = new ArrayList<>();
		
		if(notificationTypes!=null)
		for(NotificationType type: notificationTypes){
			notes.add(type);
		}
		
		return em.createQuery("FROM NotificationModel n " +
				"where n.documentId=:documentId " +
				"and n.notificationType in (:notificationType)" +
				"order by created desc")
		.setParameter("documentId", documentId)
		.setParameter("notificationType", notes)
		.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<NotificationModel> getAllNotificationsByDocRefId(String docRefId, NotificationType[] notificationTypes) {

		if(docRefId==null){
			return getAllNotificationsByDocumentId(notificationTypes);
		}
		
		List<NotificationType> notes = new ArrayList<>();
		
		if(notificationTypes!=null)
		for(NotificationType type: notificationTypes){
			notes.add(type);
		}
		
		return em.createQuery("FROM NotificationModel n " +
				"where n.docRefId=:docRefId " +
				"and n.notificationType in (:notificationType)" +
				"order by created desc")
		.setParameter("docRefId", docRefId)
		.setParameter("notificationType", notes)
		.getResultList();
		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<NotificationModel> getAllNotificationsByDocumentId(NotificationType[] notificationTypes) {

		List<String> notes = new ArrayList<>();
		
		if(notificationTypes!=null)
		for(NotificationType type: notificationTypes){
			notes.add(type.name());
		}
		
		String userId = SessionHelper.getCurrentUser().getUserId();
		
		List<UserGroup> groups = UserDaoHelper.getInstance().getGroupsForUser(userId);
		String groupsIds="";
		for(UserGroup group: groups){
			groupsIds = groupsIds.concat(group.getName()+",");
		}
		
		if(groupsIds.isEmpty()){
			return new ArrayList<>();
		}
		groupsIds = groupsIds.substring(0, groupsIds.length()-1);
		
		StringBuffer hql = new StringBuffer("Select n.id FROM localnotification n "+
		//"d.id documentId "+
		"inner join LocalDocument d on (d.id=n.documentId) "+
		"left join Task t on (t.processInstanceId=d.processInstanceId) "+
		"left join OrganizationalEntity owner on (owner.id= t.actualOwner_id and owner.DTYPE='User') "+ 
		"left join PeopleAssignments_PotOwners potowners on (potowners.task_id=t.id)  "+
		"where "+
		"t.archived = 0 and "+ 
		"(d.createdBy=? or " +
		" owner.id = ? or "+
		"( potowners.entity_id = ? or potowners.entity_id in (?) )) and " +
		"n.notificationType in (:noteTypes) and "+
		"n.created>:thirtyDays"
		 );
		
		Query query = em.createNativeQuery(hql.toString())
				.setParameter(1, userId)
				.setParameter(2, userId)
				.setParameter(3, userId)
				.setParameter(4, groupsIds)
				.setParameter("noteTypes", notes)
				.setParameter("thirtyDays", DateUtils.addDays(new Date(), -30));
				
		
		List<BigInteger> commentIds = query.getResultList(); 
		
		List<Long> ids = new ArrayList<>();
		for(BigInteger id: commentIds){
			ids.add(id.longValue());
		}

		if(ids==null || ids.isEmpty()){
			return new ArrayList<>();
		}
		
		return em.createQuery("FROM NotificationModel n " +
				"where n.id in (:ids) "+		
				"order by created desc")
		.setParameter("ids", ids)
		.getResultList();
		
	}

}
