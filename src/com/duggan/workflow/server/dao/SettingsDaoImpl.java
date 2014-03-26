package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.duggan.workflow.server.dao.model.ADValue;
import com.duggan.workflow.shared.model.settings.SETTINGNAME;

public class SettingsDaoImpl extends BaseDaoImpl {

	public SettingsDaoImpl(EntityManager em) {
		super(em);
	}
	
	public void saveSettings(SETTINGNAME name, ADValue value){
		deleteName(name);
		value.setSettingname(name);
		save(value);
	}

	private void deleteName(SETTINGNAME name) {
		String sql = "update advalue set isActive=0 where settingName=? and isActive=1";
		Query query = em.createNativeQuery(sql).setParameter(1, name.name());
		query.executeUpdate();
	}
	
	public List<ADValue> getSettingValues(List<SETTINGNAME> names){
		String sql = "FROM ADValue where isActive=1 and settingName is not null";
		
		Query query = em.createQuery(sql);
		List<ADValue> values = new ArrayList<>();
		try{
			values = query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return values;
	}

}
