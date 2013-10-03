package com.duggan.workflow.server.dao;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.PO;

/**
 * 
 * @author duggan
 *
 */
public class FormDaoImpl extends BaseDaoImpl {

	public FormDaoImpl(EntityManager em) {
		super(em);
	}
	
	@Override
	public void save(PO po) {
		
		if(po.getId()!=null){
			prepare(po);
			em.merge(po);
		}
		else
			super.save(po);
	}

}
