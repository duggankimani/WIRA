package com.duggan.workflow.server.dao.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.NotificationDaoImpl;
import com.duggan.workflow.server.dao.model.NotificationModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.UserDaoHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.wira.commons.shared.models.HTUser;

public class NotificationDaoHelper {

	public static void updateNotification(Long id, boolean isRead){
		NotificationDaoImpl dao = DB.getNotificationDao();
		dao.markRead(id,isRead);
	}
	
	public static Notification saveNotification(Notification notification){
		
		NotificationDaoImpl dao = DB.getNotificationDao();
		
		NotificationModel model = new NotificationModel();
		
		//avoid repetitions of successful submission
		if(notification.getNotificationType()==NotificationType.APPROVALREQUEST_OWNERNOTE){
			List items = dao.getNotification(notification.getDocumentId(), notification.getOwner().getUserId());
			if(items.size()>0){
				copyData(notification,(NotificationModel)items.get(0));
				return notification; 
			}
		}
		
		if(notification.getNotificationType()==NotificationType.TASKDELEGATED){
			Document doc = (Document)DocumentDaoHelper.getDocJson(notification.getDocRefId());
			//Document doc = DocumentDaoHelper.getDocument(notification.getDocumentId());
			DocumentType type = doc.getType();
			notification.setDocumentType(type);
			notification.setOwner(doc.getOwner());
		}
		
		if(notification.getId()!=null){
			model = dao.getNotification(notification.getId());			
		}
		
		copyData(model, notification);
		
		model = dao.saveOrUpdate(model);
		
		notification.setId(model.getId());
		
		return notification;
	}
	
	public static List<Notification> getAllNotifications(String processRefId,String userId){
		NotificationDaoImpl dao = DB.getNotificationDao();
		return  dao.getAllNotifications(processRefId,userId);
	}
	
	public static List<Notification> getAllNotifications(Long documentId, NotificationType...notificationTypes){
		NotificationDaoImpl dao = DB.getNotificationDao();
		List<NotificationModel> models = dao.getAllNotificationsByDocumentId(documentId, notificationTypes);
		
		return copyData(models);
	}
	
	public static List<Notification> getAllNotificationsByRefId(String docRefId, NotificationType...notificationTypes){
		NotificationDaoImpl dao = DB.getNotificationDao();
		List<NotificationModel> models = dao.getAllNotificationsByDocRefId(docRefId, notificationTypes);
		
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
				notificationTo.setCreatedBy(SessionHelper.getCurrentUser().getUserId());
		}else{
			notificationTo.setUpdated(new Date());
			if(SessionHelper.getCurrentUser()!=null)
				notificationTo.setUpdatedBy(SessionHelper.getCurrentUser().getUserId());
		}
		
		notificationTo.setDocRefId(modelFrom.getDocRefId());
		notificationTo.setOwner(modelFrom.getOwner().getUserId());
		if(modelFrom.getTargetUserId()!=null)
			notificationTo.setTargetUserId(modelFrom.getTargetUserId().getUserId());
		
		notificationTo.setNotificationType(modelFrom.getNotificationType());
		notificationTo.setRead(modelFrom.IsRead());	
		notificationTo.setSubject(modelFrom.getSubject());
		notificationTo.setApproverAction(modelFrom.getApproverAction());
		notificationTo.setFileId(modelFrom.getFileId());
		notificationTo.setFileName(modelFrom.getFileName());
		notificationTo.setDocumentTypeDesc(modelFrom.getDocumentTypeDesc());
	}
	
	private static void copyData(Notification notificationTo,
			NotificationModel modelFrom) {
	
		Long documentId = modelFrom.getDocumentId();
		notificationTo.setDocumentId(modelFrom.getDocumentId());
		notificationTo.setDocRefId(modelFrom.getDocRefId());
		
		String owner = modelFrom.getOwner();
		HTUser htOwner = UserDaoHelper.getInstance().getUser(owner);
		notificationTo.setOwner(htOwner);
		notificationTo.setNotificationType(modelFrom.getNotificationType());
		notificationTo.setRead(modelFrom.IsRead());	
		notificationTo.setSubject(modelFrom.getSubject());
		notificationTo.setCreated(modelFrom.getCreated());
		if(modelFrom.getTargetUserId()!=null)
			notificationTo.setTargetUserId(UserDaoHelper.getInstance().getUser(modelFrom.getTargetUserId()));
		notificationTo.setRead(modelFrom.IsRead());
		
		String createdBy = modelFrom.getCreatedBy();
		HTUser user = UserDaoHelper.getInstance().getUser(createdBy);
		notificationTo.setCreatedBy(user);
		notificationTo.setId(modelFrom.getId());
		
//		ADDocType documentType = DB.getDocumentDao().getDocumentTypeByDocumentId(modelFrom.getDocumentId());
//		notificationTo.setDocumentType(getType(documentType));
		
		DocumentType type = new DocumentType();
		type.setDisplayName(DB.getDocumentDao().getDocumentTypeDisplayNameByDocumentId(notificationTo.getDocRefId()));
		notificationTo.setDocumentType(type);
		notificationTo.setDocumentTypeDesc(modelFrom.getDocumentTypeDesc());
		
		notificationTo.setApproverAction(modelFrom.getApproverAction());
		
//		notificationTo.setProcessInstanceId(
//				DocumentDaoHelper.getProcessInstanceIdByDocumentId(modelFrom.getDocumentId()));
		
		notificationTo.setFileId(modelFrom.getFileId());
		notificationTo.setFileName(modelFrom.getFileName());
		
	}

	public static void delete(Long id) {
		NotificationDaoImpl dao = DB.getNotificationDao();
		dao.delete(id);
	}

	public static Integer getNotificationCount(String processRefId,String userId) {
		NotificationDaoImpl dao =DB.getNotificationDao();
		Integer count = dao.getAlertCount(processRefId,userId);
		return count;
	}

	public static Notification getNotification(Long noteId) {
		NotificationDaoImpl dao =DB.getNotificationDao();
		NotificationModel model = dao.getNotification(noteId);
		
		Notification notification = new Notification();
		copyData(notification, model);
		return notification;
	}

	public static void getCounts(String processRefId, HashMap<TaskType, Integer> counts) {
		counts.put(TaskType.NOTIFICATIONS, getNotificationCount(processRefId,SessionHelper.getCurrentUser().getUserId()));
	}
}
