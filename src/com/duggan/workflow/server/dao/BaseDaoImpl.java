package com.duggan.workflow.server.dao;

import java.util.Date;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.PO;
import com.duggan.workflow.server.helper.session.SessionHelper;

public class BaseDaoImpl {

	protected EntityManager em;
	
	public BaseDaoImpl(EntityManager em){
		this.em = em;
	}
		
	public void save(PO po){
		em.persist(po);
	}
	
	public void delete(PO po){
		em.remove(po);
	}
}
