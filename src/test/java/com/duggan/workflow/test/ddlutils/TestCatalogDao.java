package com.duggan.workflow.test.ddlutils;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.server.dao.model.CatalogModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.shared.model.DBType;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.DoubleValue;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;

public class TestCatalogDao {

	@Before
	public void init(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void saveData(){
		//Catalog catalog = CatalogDaoHelper.getCatalog(24L);
		
		List<DocumentLine> lines = new ArrayList<>();
		DocumentLine line = new DocumentLine();
		line.addValue("administration", new DoubleValue(0.0));
		lines.add(line);
		CatalogDaoHelper.saveData(26L, lines);
	}
	
	@Ignore
	public void createTable(){
		Catalog catalog = new Catalog();
		catalog.setDescription("Test Table");
		catalog.setName("Test");
		catalog.setProcess("xxx");
		catalog.setRecordCount(30);
		
		List<CatalogColumn> columns = new ArrayList<>();
		CatalogColumn col = new CatalogColumn();
		col.setAutoIncrement(true);
		col.setLabel("ID");
		col.setName("id");
		col.setNullable(false);
		col.setPrimaryKey(true);
		col.setSize(10);
		col.setType(DBType.INTEGER);
		columns.add(col);
		
		col = new CatalogColumn();
		col.setLabel("Name");
		col.setName("name");
		col.setSize(100);
		col.setType(DBType.VARCHAR);
		columns.add(col);
		
		col = new CatalogColumn();
		col.setLabel("Description");
		col.setName("description");
		col.setSize(250);
		col.setType(DBType.VARCHAR);
		columns.add(col);
		
		catalog.setColumns(columns);
		CatalogDaoHelper.save(catalog);
	}
	
	@Ignore
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
