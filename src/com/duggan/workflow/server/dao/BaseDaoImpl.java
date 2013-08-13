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
		

		/*
		 * Do not flush - This reflects data in the database immediately and the BTM transaction 
		 * in my tests so far cannot rollback the flushed data - You can actually query the new values 
		 * directly from the database even as the transaction is ongoing - Could it be the isolation
		 * level being used in the db? - 10/08/2013 - No - Look throught the hibernate objects life cycle
		 * for details
		 * 
		 */
		//em.flush();
		
		em.persist(model);
		
		return model;
	}
}
