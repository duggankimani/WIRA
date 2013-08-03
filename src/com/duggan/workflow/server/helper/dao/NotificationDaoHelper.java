package com.duggan.workflow.server.helper.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.duggan.workflow.server.dao.NotificationDaoImpl;
import com.duggan.workflow.server.dao.model.NotificationModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.session.SessionHelper;
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
	}

	public static void delete(Long id) {
		NotificationDaoImpl dao = DB.getNotificationDao();
		dao.delete(id);
	}

	public static Integer getNotificationCount(String userId) {
		NotificationDaoImpl dao = DB.getNotificationDao();
		Integer count = dao.getAlertCount(userId);
		return count;
	}
}
