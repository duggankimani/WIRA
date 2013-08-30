package com.duggan.workflow.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.Priority;
import com.duggan.workflow.shared.model.SearchFilter;

public class TestSearchDocument {

	DocumentDaoImpl dao;
	
	@Before
	public void setup(){
		DBTrxProvider.init();
		
		DB.beginTransaction();
		dao = DB.getDocumentDao();
	}
	
	
	@Test
	public void search(){
		SearchFilter filter = new SearchFilter();
		//filter.setSubject("900");
		//filter.setPhrase("DN");
		//filter.setDocType(DocType.LPO);
		//filter.setPriority(Priority.CRITICAL.ordinal());
		//filter.setPriority(Priority.HIGH.ordinal());
		
		//time is an issue
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		c.set(2013, 7, 27,0,0,0);		
		//filter.setStartDate(c.getTime());		
		
		c.set(2013, 7, 30,0,0,0);
		filter.setEndDate(c.getTime());
		
		List<DocumentModel> models = dao.search(filter);
		
		Assert.assertTrue(models!=null);
		Assert.assertTrue(models.size()>0);
		
		for(DocumentModel m: models){
			System.err.println(m);
		}
	}
	
	@After
	public void destroy(){
		DB.closeSession();
	}
}
