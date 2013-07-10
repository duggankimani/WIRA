package com.duggan.workflow.server.db;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.DocumentDaoImpl;

class DaoFactory {

	DocumentDaoImpl documentDao=null;
	
	DocumentDaoImpl getDocumentDao(EntityManager em){
		if(documentDao==null){
			documentDao = new DocumentDaoImpl(em);
		}
		
		return documentDao;
	}
}
