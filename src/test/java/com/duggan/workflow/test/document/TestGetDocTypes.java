package com.duggan.workflow.test.document;

import java.io.IOException;
import java.util.List;

import org.jbpm.task.Task;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.model.DocumentType;

public class TestGetDocTypes {

	Task t;

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void getDocTypes(){
		
		List<DocumentType> types = DocumentDaoHelper.getDocumentTypes("Administrator");
		for(DocumentType t: types){
			System.out.println("DocType = "+t.getDisplayName());
		}
	}
	
	@org.junit.After
	public void destroy() throws IOException{
		DB.rollback();
		LoginHelper.get().close();
		DB.closeSession();
	}
}
