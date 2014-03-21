package com.duggan.workflow.server.db;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.AttachmentDaoImpl;
import com.duggan.workflow.server.dao.CommentDaoImpl;
import com.duggan.workflow.server.dao.DSConfigDaoImpl;
import com.duggan.workflow.server.dao.DashboardDaoImpl;
import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.ErrorDaoImpl;
import com.duggan.workflow.server.dao.FormDaoImpl;
import com.duggan.workflow.server.dao.NotificationDaoImpl;
import com.duggan.workflow.server.dao.ProcessDaoImpl;
import com.duggan.workflow.server.dao.UserGroupDaoImpl;

class DaoFactory {

	DocumentDaoImpl documentDao = null;
	ErrorDaoImpl errorDao = null;
	NotificationDaoImpl notificationDao;
	AttachmentDaoImpl attachmentDao;
	CommentDaoImpl commentDaoImpl;
	ProcessDaoImpl processDao;
	UserGroupDaoImpl userGroupDao;
	FormDaoImpl formDao;
	DSConfigDaoImpl dsDao;
	DashboardDaoImpl dashDao;
	
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

	public CommentDaoImpl getCommentDao(EntityManager entityManager) {

		if(commentDaoImpl==null){
			synchronized (DaoFactory.class) {
				if(commentDaoImpl==null){
					commentDaoImpl = new CommentDaoImpl(entityManager);
				}
			}
		}
		
		return commentDaoImpl;
	}

	public ProcessDaoImpl getProcessDao(EntityManager entityManager) {
		if(processDao==null){
			synchronized (DaoFactory.class) {
				if(processDao==null){
					processDao = new ProcessDaoImpl(entityManager);
				}
			}
		}
		
		return processDao;
	}

	public UserGroupDaoImpl getUserGroupDaoImpl(EntityManager entityManager) {
		if(userGroupDao==null){
			synchronized (DaoFactory.class) {
				if(userGroupDao==null){
					userGroupDao = new UserGroupDaoImpl(entityManager);
				}
			}
		}
		
		return userGroupDao;
	}
	
	public FormDaoImpl getFormDaoImpl(EntityManager entityManager) {
		if(formDao==null){
			synchronized (DaoFactory.class) {
				if(formDao==null){
					formDao = new FormDaoImpl(entityManager);
				}
			}
		}
		
		return formDao;
	}

	public DSConfigDaoImpl getDSConfigDaoImpl(EntityManager entityManager) {
		if(dsDao==null){
			synchronized (DaoFactory.class) {
				if(dsDao==null){
					dsDao = new DSConfigDaoImpl(entityManager);
				}
			}
		}
		
		return dsDao;
	}

	public DashboardDaoImpl getDashboardDaoImpl(EntityManager entityManager) {
		
		if(dashDao==null){
			synchronized (DaoFactory.class) {
				if(dashDao==null){
					dashDao = new DashboardDaoImpl(entityManager);
				}
			}
		}
		
		return dashDao;
	}

}
