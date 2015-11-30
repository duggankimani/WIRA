package com.duggan.workflow.server.db;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.AttachmentDaoImpl;
import com.duggan.workflow.server.dao.CatalogDaoImpl;
import com.duggan.workflow.server.dao.CommentDaoImpl;
import com.duggan.workflow.server.dao.DSConfigDaoImpl;
import com.duggan.workflow.server.dao.DashboardDaoImpl;
import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.ErrorDaoImpl;
import com.duggan.workflow.server.dao.FormDaoImpl;
import com.duggan.workflow.server.dao.NotificationDaoImpl;
import com.duggan.workflow.server.dao.OutputDocumentDao;
import com.duggan.workflow.server.dao.ProcessDaoImpl;
import com.duggan.workflow.server.dao.SettingsDaoImpl;
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
	SettingsDaoImpl settingsDao;
	OutputDocumentDao outputDao;
	CatalogDaoImpl catalogDao;
	
	DocumentDaoImpl getDocumentDao() {
		if (documentDao == null) {
			synchronized (DaoFactory.class) {
				if (documentDao == null) {
					documentDao = new DocumentDaoImpl();
				}
			}

		}

		return documentDao;
	}

	ErrorDaoImpl getErrorDao() {
		if (errorDao == null) {
			synchronized (DaoFactory.class) {
				if (errorDao == null) {
					errorDao = new ErrorDaoImpl();
				}
			}
		}

		return errorDao;
	}

	NotificationDaoImpl getNotificationDao() {
		if (notificationDao == null) {
			synchronized (DaoFactory.class) {
				if (notificationDao == null) {
					notificationDao = new NotificationDaoImpl();
				}
			}
		}

		return notificationDao;
	}

	public AttachmentDaoImpl getAttachmentDaoImpl() {

		if(attachmentDao ==null){
			synchronized (DaoFactory.class) {
				if(attachmentDao == null){
					attachmentDao = new AttachmentDaoImpl();
				}
			}
		}
		
		return attachmentDao;
	}

	public CommentDaoImpl getCommentDao() {

		if(commentDaoImpl==null){
			synchronized (DaoFactory.class) {
				if(commentDaoImpl==null){
					commentDaoImpl = new CommentDaoImpl();
				}
			}
		}
		
		return commentDaoImpl;
	}

	public ProcessDaoImpl getProcessDao() {
		if(processDao==null){
			synchronized (DaoFactory.class) {
				if(processDao==null){
					processDao = new ProcessDaoImpl();
				}
			}
		}
		
		return processDao;
	}

	public UserGroupDaoImpl getUserGroupDaoImpl() {
		if(userGroupDao==null){
			synchronized (DaoFactory.class) {
				if(userGroupDao==null){
					userGroupDao = new UserGroupDaoImpl();
				}
			}
		}
		
		return userGroupDao;
	}
	
	public FormDaoImpl getFormDaoImpl() {
		if(formDao==null){
			synchronized (DaoFactory.class) {
				if(formDao==null){
					formDao = new FormDaoImpl();
				}
			}
		}
		
		return formDao;
	}

	public DSConfigDaoImpl getDSConfigDaoImpl() {
		if(dsDao==null){
			synchronized (DaoFactory.class) {
				if(dsDao==null){
					dsDao = new DSConfigDaoImpl();
				}
			}
		}
		
		return dsDao;
	}

	public DashboardDaoImpl getDashboardDaoImpl() {
		
		if(dashDao==null){
			synchronized (DaoFactory.class) {
				if(dashDao==null){
					dashDao = new DashboardDaoImpl();
				}
			}
		}
		
		return dashDao;
	}

	public SettingsDaoImpl getSettingsDaoImpl() {
		if(settingsDao==null){
			synchronized (DaoFactory.class) {
				if(settingsDao==null){
					settingsDao = new SettingsDaoImpl();
				}
			}
		}
		
		return settingsDao;
	}

	public OutputDocumentDao getOuputDocDaoImpl() {
		if(outputDao==null){
			synchronized (DaoFactory.class) {
				if(outputDao==null){
					outputDao = new OutputDocumentDao();
				}
			}
		}
		
		return outputDao;
	}

	public CatalogDaoImpl getCatalogDaoImp() {
		if(catalogDao==null){
			synchronized (DaoFactory.class) {
				if(catalogDao==null){
					catalogDao = new CatalogDaoImpl();
				}
			}
		}
		
		return catalogDao;
	}
}
