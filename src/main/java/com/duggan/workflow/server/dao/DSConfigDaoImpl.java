package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.duggan.workflow.server.dao.model.DataSourceConfig;
import com.duggan.workflow.shared.model.form.KeyValuePair;

public class DSConfigDaoImpl extends BaseDaoImpl{
	
	public DSConfigDaoImpl(EntityManager em) {
		super(em);
	}

	public DataSourceConfig getConfiguration(Long id) {

		Query query = getEntityManager().createQuery("FROM DataSourceConfig ds WHERE ds.id=:id")
				.setParameter("id", id);
		
		try{
			return (DataSourceConfig)query.getSingleResult();
		}catch(Exception e){
			e.printStackTrace();
		}		
		
		return null;
	}

	public List<DataSourceConfig> getConfigurations() {
		
		Query query = getEntityManager().createQuery("FROM DataSourceConfig ds WHERE ds.isActive=:isActive")
				.setParameter("isActive", 1);
		
		try{
			return query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<>();
		
	}

	public List<KeyValuePair> getKeyValuePairs() {
		
		Query query = getEntityManager().createQuery("select new " +
				"com.duggan.workflow.shared.model.form.KeyValuePair(ds.configName,ds.configName) " +
				"FROM DataSourceConfig ds WHERE ds.isActive=:isActive")
				.setParameter("isActive", 1);
		try{
			return getResultList(query);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}

	/**
	 * Get DS by ConfigName
	 * @param configName
	 * @return
	 */
	public DataSourceConfig getConfigurationByName(String configName) {
		logger.debug("configName = "+configName);
		Query query = getEntityManager().createQuery("FROM DataSourceConfig ds WHERE ds.configName=:configName")
				.setParameter("configName", configName);
		
		try{
			return (DataSourceConfig)query.getSingleResult();
		}catch(Exception e){
			e.printStackTrace();
		}		
		
		return null;
	}
	
	

}
