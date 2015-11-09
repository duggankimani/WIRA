package com.duggan.workflow.ouputdocs;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.export.DocumentHTMLMapper;
import com.duggan.workflow.shared.model.Document;

public class TestOutputDocs {
	

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void map() throws FileNotFoundException, IOException{
		long documentId = 187L;
		Document doc = DocumentDaoHelper.getDocument(documentId);
		
		DocumentHTMLMapper mapper = new DocumentHTMLMapper();
		List<String> vals=IOUtils.readLines(new FileInputStream("/home/duggan/Downloads/PO (1).html"));
		StringBuffer input = new StringBuffer();
		for(String in:vals){
			input.append(in);
		}
		String out = mapper.map(doc, input.toString());
		IOUtils.copy(new ByteArrayInputStream(out.getBytes()), new FileOutputStream("PO.html"));
		
	}
	
	@After
	public void close(){
		DB.rollback();
		DB.closeSession();
	}
}
