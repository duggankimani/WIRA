package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.dao.model.NotificationModel;
import com.duggan.workflow.shared.model.NotificationType;

public class NotificationDaoImpl {

	EntityManager em;
	
	public NotificationDaoImpl(EntityManager em) {
		this.em = em;
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
	public List<NotificationModel> getAllNotifications(String userId) {

		return em.createQuery("FROM NotificationModel n where n.targetUserId=:userId order by created desc")
				.setParameter("userId", userId)
				.getResultList();
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
	 * @return count
	 */
	public Integer getAlertCount(String userId) {
		
		Long count = (Long)em.createQuery("select count(n) from NotificationModel n where n.targetUserId=:userId and n.isRead=:isRead")
				.setParameter("userId", userId).
				setParameter("isRead",false)
				.getSingleResult();
		
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
}
