package com.duggan.workflow.test.ddlutils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.server.dao.model.CatalogModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;

public class TestCatalogDao {

	@Before
	public void init(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void export(){
		String xml = CatalogDaoHelper.exportTable(1L);
		System.err.println(xml);
		
		CatalogModel model = CatalogDaoHelper.importTable(xml);
		System.err.println("Size: "+model.getColumns().size() + "\n"
				+model.getName()+"\n"+CatalogDaoHelper.exportTable(model));
		
	}
	

	@After
	public void destroy(){
		DB.commitTransaction();
		DB.closeSession();
	}
}
