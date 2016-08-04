package com.duggan.workflow.test.export.html2pdf;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.export.DocumentHTMLMapper;
import com.duggan.workflow.server.export.HTMLToPDFConvertor;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.HTask;
import com.itextpdf.text.DocumentException;

public class TestHTMLToPDFExport {

	@Before
	public void setup(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
		//ProcessMigrationHelper.start(2L);
	}
	
	@Test
	public void mapTaskToHTML() throws IOException{
		Long taskid = 130L;
		HTask task = JBPMHelper.get().getTask(taskid);
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("comment-email.html");
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		IOUtils.copy(is, bout);
		String htmlTemplate = new String(bout.toByteArray());
		
		String html = new DocumentHTMLMapper().map(task, htmlTemplate);
		System.out.println(html);
		
		List<DocumentLine> lines= task.getDetails().get("approvalLines");
		for(DocumentLine l: lines){
			System.err.println(l);
		}
		
	}
	
	@Ignore
	public void mapHTML(){
		
		Document doc = DocumentDaoHelper.getDocument(4L);
		
		String html = OutputDocumentDaoHelper.getHTMLTemplate("ENTRY_APPLICATION");
		html = new DocumentHTMLMapper().map(doc, html);
		
		System.out.println(html);
		
	}
	
	@Ignore
	public void HashMap() throws FileNotFoundException, IOException, SAXException, ParserConfigurationException, FactoryConfigurationError, DocumentException{
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
