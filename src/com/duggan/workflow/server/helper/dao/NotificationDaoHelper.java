package com.duggan.workflow.server.helper.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.NotificationDaoImpl;
import com.duggan.workflow.server.dao.model.NotificationModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Notification;

public class NotificationDaoHelper {

	public static void updateNotification(Long id, boolean isRead){
		NotificationDaoImpl dao = DB.getNotificationDao();
		dao.markRead(id,isRead);
	}
	
	public static Notification saveNotification(Notification notification){
		
		NotificationDaoImpl dao = DB.getNotificationDao();
		
		NotificationModel model = new NotificationModel();
		
		if(notification.getId()!=null){
			model = dao.getNotification(notification.getId());			
		}
		
		copyData(model, notification);
		
		model = dao.saveOrUpdate(model);
		
		notification.setId(model.getId());
		
		return notification;
	}
	
	public static List<Notification> getAllNotifications(String userId){
		NotificationDaoImpl dao = DB.getNotificationDao();
		List<NotificationModel> models = dao.getAllNotifications(userId);
		
		return copyData(models);
	}
	
	private static List<Notification> copyData(List<NotificationModel> models) {
		List<Notification> notifications = new ArrayList<>();
		
		for(NotificationModel m:models){
			Notification note = new Notification();
			copyData(note, m);
			notifications.add(note);
		}
		
		return notifications;
	}


	private static void copyData(NotificationModel notificationTo,
			Notification modelFrom) {
		if(notificationTo.getId()==null){
			notificationTo.setCreated(new Date());
			
			if(SessionHelper.getCurrentUser()!=null)
				notificationTo.setCreatedBy(SessionHelper.getCurrentUser().getId());
		}else{
			notificationTo.setUpdated(new Date());
			if(SessionHelper.getCurrentUser()!=null)
				notificationTo.setUpdatedBy(SessionHelper.getCurrentUser().getId());
		}
		
		notificationTo.setDocumentId(modelFrom.getDocumentId());
		notificationTo.setOwner(modelFrom.getOwner());
		notificationTo.setTargetUserId(modelFrom.getTargetUserId());
		notificationTo.setNotificationType(modelFrom.getNotificationType());
		notificationTo.setRead(modelFrom.IsRead());	
		notificationTo.setRead(modelFrom.IsRead());
		notificationTo.setSubject(modelFrom.getSubject());
		notificationTo.setApproverAction(modelFrom.getApproverAction());
	}
	
	private static void copyData(Notification notificationTo,
			NotificationModel modelFrom) {
				
		notificationTo.setDocumentId(modelFrom.getDocumentId());
		notificationTo.setOwner(modelFrom.getOwner());
		notificationTo.setNotificationType(modelFrom.getNotificationType());
		notificationTo.setRead(modelFrom.IsRead());	
		notificationTo.setSubject(modelFrom.getSubject());
		notificationTo.setCreated(modelFrom.getCreated());
		notificationTo.setTargetUserId(modelFrom.getTargetUserId());
		notificationTo.setRead(modelFrom.IsRead());
		notificationTo.setCreatedBy(modelFrom.getCreatedBy());
		notificationTo.setId(modelFrom.getId());
		DocType documentType = DB.getDocumentDao().getDocumentType(modelFrom.getDocumentId());
		notificationTo.setDocumentType(documentType);
		notificationTo.setApproverAction(modelFrom.getApproverAction());
		notificationTo.setProcessInstanceId(
				DocumentDaoHelper.getProcessInstanceIdByDocumentId(modelFrom.getDocumentId()));
		
	}

	public static void delete(Long id) {
		NotificationDaoImpl dao = DB.getNotificationDao();
		dao.delete(id);
	}

	public static Integer getNotificationCount(String userId) {
		NotificationDaoImpl dao =DB.getNotificationDao();
		Integer count = dao.getAlertCount(userId);
		return count;
	}

	public static Notification getNotification(Long noteId) {
		NotificationDaoImpl dao =DB.getNotificationDao();
		NotificationModel model = dao.getNotification(noteId);
		
		Notification notification = new Notification();
		copyData(notification, model);
		return notification;
	}

	public static void getCounts(HashMap<TaskType, Integer> counts) {
		counts.put(TaskType.NOTIFICATIONS, getNotificationCount(SessionHelper.getCurrentUser().getId()));
	}
}
