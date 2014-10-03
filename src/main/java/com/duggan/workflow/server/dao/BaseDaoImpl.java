package com.duggan.workflow.server.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.duggan.workflow.server.dao.model.PO;

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
	
	@SuppressWarnings("unchecked")
	public <T> T getSingleResultOrNull(Query query){
		T value = null;
		try{
			value = (T)query.getSingleResult();
		}catch(Exception e){
			if(!(e instanceof NoResultException)){
				e.printStackTrace();
			}
			
		}
		
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getResultList(Query query){
		List<T> values = null;
		values = query.getResultList();
		return values;
	}
	
	public <T> T getById(Class<T> clazz, long id){
		
		return em.find(clazz, id);
	}
}
