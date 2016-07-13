package com.duggan.workflow.test;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.NotificationDaoImpl;
import com.duggan.workflow.server.dao.helper.CommentDaoHelper;
import com.duggan.workflow.server.dao.helper.NotificationDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.model.Comment;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import com.wira.commons.shared.models.HTUser;

public class TestNotificationDao {

	NotificationDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		dao = DB.getNotificationDao();
	}
	
	@Test
	public void createComment(){
		Comment comment = new Comment();
		comment.setComment("Testing");
		comment.setDocumentId(24L);
		comment.setDocRefId("40Uo3sNbskSwkmKw");
		comment.setSubject("Test Subject");
		comment.setUserId("Administrator");
		CommentDaoHelper.saveComment(comment);
	}
	
	@Ignore
	public void createNotification(){
		
		Notification notification = new Notification();
		notification.setCreated(new Date());
		notification.setDocumentId(31L);
		notification.setDocRefId("");
		//notification.setDocumentType(DocType.INVOICE);
		notification.setNotificationType(NotificationType.APPROVALREQUEST_APPROVERNOTE);
		notification.setOwner(LoginHelper.get().getUser("calcacuervo"));
		notification.setRead(false);
		notification.setSubject("Inv/001/01");
		notification.setTargetUserId(new HTUser("mariano"));
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
		DB.commitTransaction();
		DB.closeSession();
	}
}
