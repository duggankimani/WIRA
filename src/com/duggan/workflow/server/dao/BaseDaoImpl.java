package com.duggan.workflow.server.dao;

import java.util.Date;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.PO;
import com.duggan.workflow.server.helper.session.SessionHelper;

public class BaseDaoImpl<T extends PO> {

	protected EntityManager em;
	
	public BaseDaoImpl(EntityManager em){
		this.em = em;
	}

	public T saveOrUpdate(T model){		

		if(model.getId()==null){
			model.setCreated(new Date());
			model.setCreatedBy(SessionHelper.getCurrentUser().getId());
		}else{
			model.setUpdated(new Date());
			model.setUpdatedBy(SessionHelper.getCurrentUser().getId());
		}
		
		em.persist(model);
		
		return model;
	}
}
