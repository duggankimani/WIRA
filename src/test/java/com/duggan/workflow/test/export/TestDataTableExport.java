package com.duggan.workflow.test.export;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.CatalogDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;

public class TestDataTableExport {


	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void importData() throws IOException, JSONException{
		String fileName = "/home/duggan/Downloads/departments-tableonly.json";
//		String fileName = "/home/duggan/Downloads/departments-dataonly.json";
//		String fileName = "/home/duggan/Downloads/departments.json";

		File file = new File(fileName);
		String json = IOUtils.toString(new FileReader(file));
		CatalogDaoHelper.importCatalog(json, new ArrayList<String>());
	}
	
	@Ignore
	public void export() throws JSONException{
		//String refId = "9FDtYtEPagWNC4Io";
		String refId = "O8T3kW7556dVDXaJ";
		
		JSONObject json = CatalogDaoHelper.exportCatalog(refId, "tableonly");
		
		System.out.println(json);
	}
	
	@After
	public void destroy(){
		
		DB.commitTransaction();
		DB.closeSession();
	}

}
