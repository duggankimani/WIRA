package com.duggan.workflow.test;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.NotificationDaoImpl;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.dao.NotificationDaoHelper;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;

public class TestNotificationDao {

	NotificationDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProvider.init();
		
		DB.beginTransaction();
		dao = DB.getNotificationDao();
	}
	
	@Test
	public void createNotification(){
		
		Notification notification = new Notification();
		notification.setCreated(new Date());
		notification.setDocumentId(31L);
		//notification.setDocumentType(DocType.INVOICE);
		notification.setNotificationType(NotificationType.APPROVALREQUEST_APPROVERNOTE);
		notification.setOwner("calcacuervo");
		notification.setRead(false);
		notification.setSubject("Inv/001/01");
		notification.setTargetUserId("mariano");
		notification = NotificationDaoHelper.saveNotification(notification);		
		DB.commitTransaction();
		Assert.assertNotNull(notification.getId());
				
		DB.beginTransaction();
		NotificationDaoHelper.delete(notification.getId());		
		DB.commitTransaction();
		
		Assert.assertTrue(NotificationDaoHelper.getAllNotifications("mariano").isEmpty());
	}
	
	@After
	public void destroy(){
		DB.rollback();
		DB.closeSession();
	}
}
