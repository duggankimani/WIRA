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
	
	protected void prepare(PO model){
		if(model.getId()==null){
			model.setCreated(new Date());
			model.setCreatedBy(SessionHelper.getCurrentUser().getUserId());
		}else{
			model.setUpdated(new Date());
			model.setUpdatedBy(SessionHelper.getCurrentUser().getUserId());
		}
	}
	
	public void save(PO po){
		prepare(po);
		em.persist(po);
	}
	
	public void delete(PO po){
		em.remove(po);
	}
}
