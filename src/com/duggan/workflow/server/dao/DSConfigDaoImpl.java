package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.duggan.workflow.server.dao.model.DataSourceConfig;

public class DSConfigDaoImpl extends BaseDaoImpl{

	
	public DSConfigDaoImpl(EntityManager em){
		super(em);
	}

	public DataSourceConfig getConfiguration(Long id) {

		Query query = em.createQuery("FROM DataSourceConfig ds WHERE ds.id=:id")
				.setParameter("id", id);
		
		try{
			return (DataSourceConfig)query.getSingleResult();
		}catch(Exception e){
			e.printStackTrace();
		}		
		
		return null;
	}

	public List<DataSourceConfig> getConfigurations() {
		
		Query query = em.createQuery("FROM DataSourceConfig ds WHERE ds.isActive=:isActive")
				.setParameter("isActive", 1);
		
		try{
			return query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<>();
		
	}
	
	

}
