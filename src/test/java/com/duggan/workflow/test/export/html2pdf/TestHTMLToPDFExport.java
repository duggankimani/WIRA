package com.duggan.workflow.test.export.html2pdf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.OutputDocumentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.export.DocumentHTMLMapper;
import com.duggan.workflow.server.export.HTMLToPDFConvertor;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.itextpdf.text.DocumentException;

public class TestHTMLToPDFExport {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
		//ProcessMigrationHelper.start(1L);
	}
	
	@Ignore
	public void mapHTML(){
		
		Document doc = DocumentDaoHelper.getDocument(4L);
		
		String html = OutputDocumentDaoHelper.getHTMLTemplate("ENTRY_APPLICATION");
		html = new DocumentHTMLMapper().map(doc, html);
		
		System.out.println(html);
		
	}
	
	@Test
	public void map() throws FileNotFoundException, IOException, SAXException, ParserConfigurationException, FactoryConfigurationError, DocumentException{
		Doc doc = DocumentDaoHelper.getDocument(4L);//JBPMHelper.get().getTask(JBPMHelper.get().getSysTask(taskId));
		
		DocumentHTMLMapper mapper = new DocumentHTMLMapper();
		List<String> vals=IOUtils.readLines(
				new FileInputStream("/home/duggan/Projects/EntryPermit/entrypermit.html"));
		StringBuffer input = new StringBuffer();
		for(String in:vals){
			input.append(in);
		}
		
		String html = input.toString();
		mapper.map(doc, html);
		System.err.println(html);

		byte[] bites = new HTMLToPDFConvertor().convert(doc, input.toString());
		IOUtils.write(bites, new FileOutputStream("permit.pdf"));		
	}
	
	@After
	public void close(){
		DB.commitTransaction();
		DB.closeSession();
	}
}
