package com.duggan.workflow.server.db;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.ErrorDaoImpl;

class DaoFactory {

	DocumentDaoImpl documentDao=null;
	ErrorDaoImpl errorDao = null;
	
	DocumentDaoImpl getDocumentDao(EntityManager em){
		if(documentDao==null){
			documentDao = new DocumentDaoImpl(em);
		}
		
		return documentDao;
	}

	ErrorDaoImpl getErrorDao(EntityManager entityManager) {
		if(errorDao==null){
			errorDao = new ErrorDaoImpl(entityManager);
		}
		
		return errorDao;
	}
}
