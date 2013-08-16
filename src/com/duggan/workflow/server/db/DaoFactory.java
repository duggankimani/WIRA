package com.duggan.workflow.server.db;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.AttachmentDaoImpl;
import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.ErrorDaoImpl;
import com.duggan.workflow.server.dao.NotificationDaoImpl;

class DaoFactory {

	DocumentDaoImpl documentDao = null;
	ErrorDaoImpl errorDao = null;
	NotificationDaoImpl notificationDao;
	AttachmentDaoImpl attachmentDao;
	
	DocumentDaoImpl getDocumentDao(EntityManager em) {
		if (documentDao == null) {
			synchronized (DaoFactory.class) {
				if (documentDao == null) {
					documentDao = new DocumentDaoImpl(em);
				}
			}

		}

		return documentDao;
	}

	ErrorDaoImpl getErrorDao(EntityManager entityManager) {
		if (errorDao == null) {
			synchronized (DaoFactory.class) {
				if (errorDao == null) {
					errorDao = new ErrorDaoImpl(entityManager);
				}
			}
		}

		return errorDao;
	}

	NotificationDaoImpl getNotificationDao(EntityManager em) {
		if (notificationDao == null) {
			synchronized (DaoFactory.class) {
				if (notificationDao == null) {
					notificationDao = new NotificationDaoImpl(em);
				}
			}
		}

		return notificationDao;
	}

	public AttachmentDaoImpl getAttachmentDaoImpl(EntityManager em) {

		if(attachmentDao ==null){
			synchronized (DaoFactory.class) {
				if(attachmentDao == null){
					attachmentDao = new AttachmentDaoImpl(em);
				}
			}
		}
		
		return attachmentDao;
	}

}
