package com.duggan.workflow.test.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.ProcessDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;

public class TestProcessExport {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void importProcessFromInputStream() throws IOException{
		String fileName = "Workplan.zip";
		File file = new File(fileName);
		FileInputStream is = new FileInputStream(file);
		ProcessDaoHelper.importProcessAsStream(fileName, file.length(), is);
	}
	
	@Ignore
	public void importProcessFromZip() throws IOException{
		String fileName = "Workplan.zip";
		ProcessDaoHelper.importProcessAsZip(fileName);
	}
	
	@Ignore
	public void exportProcessToFile() throws IOException{
		String processRefId = "p4LppspxJS7yF61q";
		Long processDefId = DB.getProcessDao().getProcessDefId(processRefId);
		String fileName="Workplan.zip";
		ProcessDaoHelper.exportProcessAsFile(processRefId, fileName);
		System.err.println("Generating zip -> "+fileName+" ["+processDefId+"]");
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
		//DB.commitTransaction();
		DB.closeSession();
	}

}
