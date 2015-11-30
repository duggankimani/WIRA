package com.duggan.workflow.server.dao;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.ErrorLog;

public class ErrorDaoImpl {
	EntityManager em;
	
	public Long saveError(ErrorLog log){
		em.persist(log);
		
		return log.getId();
	}
	
	public ErrorLog retrieveError(Long logId){
		return em.find(ErrorLog.class, logId);
	}
}
