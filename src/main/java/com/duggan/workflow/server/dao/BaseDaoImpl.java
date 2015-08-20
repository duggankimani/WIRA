package com.duggan.workflow.server.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public <T> List<T> getResultList(Query query,Integer offSet, Integer limit){
		List<T> values = null;
		
		if(limit==null || offSet==null){
			values = query.getResultList();
		}else{
			values = query.setFirstResult(offSet).setMaxResults(limit).getResultList();
		}
		
		return values;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getResultList(Query query){
		List<T> values = null;
		values = query.getResultList();
		return values;
	}
	
	
	
	public <T> T findByRef(Class<?> clazz, String refId, boolean throwExceptionIfNull) {
		T po = getSingleResultOrNull(
				em.createQuery("from "+clazz.getName()+" u where u.refId=:refId")
				.setParameter("refId", refId));
		
		return po;
	}

	public <T> T findByRefId(String refId, Class<?> clazz){
		return findByRefId(refId, clazz, true);
	}
	
	public <T> T findByRefId(String refId, Class<?> clazz, boolean throwExceptionIfNull) {
		return findByRefId(refId, clazz, new HashMap<String, Object>(), throwExceptionIfNull);
	}
	
	public <T> T findByRefId(String refId, Class<?> clazz, Map<String, Object> params, boolean throwExceptionIfNull) {
		
		StringBuffer buff = new StringBuffer("from "+clazz.getName()+" c where c.refId=:refId");
		
		//Variables
		if(params!=null){
			for(String key: params.keySet()){
				buff.append(" and "+key+"=:"+key);
			}
		}
		Query query = em.createQuery(buff.toString())
				.setParameter("refId", refId);
		//Params
		if(params!=null){
			for(String key: params.keySet()){
				query.setParameter(key, params.get(key));
			}
		}
		
		T rtn = getSingleResultOrNull(query);
		
		return rtn;
	}

	
	public <T> T getById(Class<T> clazz, long id){
		
		return em.find(clazz, id);
	}
}
