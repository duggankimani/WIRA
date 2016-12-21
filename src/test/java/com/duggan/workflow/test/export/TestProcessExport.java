package com.duggan.workflow.test.export;

import java.io.IOException;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.google.gwt.editor.client.Editor.Ignore;

public class TestProcessExport {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void exportProcessToFile() throws IOException{
		String processRefId = "4uSQJcExcNyBGYED";
		String fileName="Expense Claims.zip";
		ProcessDaoHelper.exportProcessAsFile(processRefId, fileName);
	}
	
	@Ignore
	public void export() throws JSONException{
		String processRefId = "4uSQJcExcNyBGYED";
		String json = ProcessDaoHelper.exportProcessJson(processRefId);
		System.out.println(json);
	}
	
	@After
	public void destroy(){
		
		DB.commitTransaction();
		DB.closeSession();
	}

}
