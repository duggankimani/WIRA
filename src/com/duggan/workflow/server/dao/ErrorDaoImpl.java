package com.duggan.workflow.server.dao;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.ErrorLog;

public class ErrorDaoImpl {
	EntityManager em;
	
	public ErrorDaoImpl(EntityManager em){
		this.em = em;
	}
	
	public Integer saveError(ErrorLog log){
		em.persist(log);
		
		return log.getId();
	}
	
	public ErrorLog retrieveError(Integer logId){
		return em.find(ErrorLog.class, logId);
	}
}
