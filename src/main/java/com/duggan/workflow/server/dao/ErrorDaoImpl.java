package com.duggan.workflow.server.dao;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.ErrorLog;

public class ErrorDaoImpl extends BaseDaoImpl{
	
	public ErrorDaoImpl(EntityManager em) {
		super(em);
	}
	
	public Long saveError(ErrorLog log){
		save(log);
		return log.getId();
	}
	
	public ErrorLog retrieveError(Long logId){
		
		return getEntityManager().find(ErrorLog.class, logId);
	}
}
