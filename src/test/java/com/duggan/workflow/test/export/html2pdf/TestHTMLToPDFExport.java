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
import org.junit.Test;
import org.xml.sax.SAXException;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.export.DocumentHTMLMapper;
import com.duggan.workflow.server.export.HTMLToPDFConvertor;
import com.duggan.workflow.shared.model.Document;
import com.itextpdf.text.DocumentException;

public class TestHTMLToPDFExport {

	@Before
	public void setup(){
		DBTrxProvider.init();
		DB.beginTransaction();
	}
	
	@Test
	public void map() throws FileNotFoundException, IOException, SAXException, ParserConfigurationException, FactoryConfigurationError, DocumentException{
		long documentId = 7L;
		Document doc = DocumentDaoHelper.getDocument(documentId);
		
		DocumentHTMLMapper mapper = new DocumentHTMLMapper();
		List<String> vals=IOUtils.readLines(new FileInputStream("/home/duggan/Downloads/PO (1).html"));
		StringBuffer input = new StringBuffer();
		for(String in:vals){
			input.append(in);
		}
		
		String out = mapper.map(doc, input.toString());
		
		byte[] bites = new HTMLToPDFConvertor().convert(doc, out);
		IOUtils.write(bites, new FileOutputStream("PO.pdf"));		
	}
	
	@After
	public void close(){
		DB.rollback();
		DB.closeSession();
	}
}
