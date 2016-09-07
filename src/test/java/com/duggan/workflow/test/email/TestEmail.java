package com.duggan.workflow.test.email;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.duggan.workflow.server.dao.FormDaoImpl;
import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.model.ADTrigger;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProviderImpl;
import com.duggan.workflow.server.export.DocumentHTMLMapper;
import com.duggan.workflow.server.export.HTMLToPDFConvertor;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.email.EmailServiceHelper;
import com.duggan.workflow.server.helper.email.EmailUtil;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.mvel.MVELExecutor;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.itextpdf.text.DocumentException;
import com.wira.commons.shared.models.HTUser;

public class TestEmail {


	Document doc;
	
	boolean isDBProcess = true;
	
	public void initDB(){
		DBTrxProviderImpl.init();
		DB.beginTransaction();
	}
	
	@Test
	public void testEmailUtil(){
		String triggerName = "chasebank.finance.ExpenseClaim.SendFinanceApprovalEmail";
		ADTrigger trigger = DB.getProcessDao().getTrigger(triggerName);
		
		Doc doc = DocumentDaoHelper.getDocJson("xnDsIlNV5Q8mN5Nm");
		doc._s("paymentMade", "Yes");
	
		new MVELExecutor().execute(trigger, doc);
		
	}
	
	
	@Ignore
	public void generateEmail() throws IOException, SAXException, ParserConfigurationException, FactoryConfigurationError, DocumentException{
		doc = DocumentDaoHelper.getDocument(24L);
		
		InputStream is = TestEmail.class.getClass().getResourceAsStream("/email.html");
		String html = IOUtils.toString(is);
		System.out.println(new DocumentHTMLMapper().map(doc, html));
		byte[] out= new  HTMLToPDFConvertor().convert(doc, html);
		
		IOUtils.write(out, new FileOutputStream(new File("email.pdf")));
				
	}
	
	@Before
	public void setup(){
		if(isDBProcess){
			initDB();
		}
		
	}
	
	@Ignore
	public void execute(){
		JBPMHelper.get().createApprovalRequest("calcacuervo",doc);
		
	}
	
	@Ignore
	public void extractVars() throws IOException{
		InputStream is = TestEmail.class.getClass().getResourceAsStream("/email.html");
		String body = IOUtils.toString(is);
		assert body!=null;
		
		Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
		Matcher matcher = pattern.matcher(body);
		while (matcher.find()) {
		    System.out.println("found: " + matcher.group(1));
		}
	}	
	
	@Ignore
	public void sendEmailNew() throws IOException, MessagingException{
		InputStream is = TestEmail.class.getClass().getResourceAsStream("/email.html");
		String body = IOUtils.toString(is);
		assert body!=null;
		
		EmailServiceHelper.sendEmail(body, "RE: Wira Enhanced mailing", 
				Arrays.asList(LoginHelper.get().getUser("mariano"),
						LoginHelper.get().getUser("james")),
				LoginHelper.get().getUser("jshikuku"));
	}
	
	@SuppressWarnings("deprecation")
	@Ignore
	public void sendEmail() throws IOException, MessagingException{
		InputStream is = TestEmail.class.getClass().getResourceAsStream("/email.html");
		String body = IOUtils.toString(is);
		assert body!=null;
		
		EmailServiceHelper.sendEmail(body, "RE: Wira with images BPM", "mdkimani@gmail.com", "pnjenga");
	}

	@org.junit.After
	public void destroy() throws IOException{
		if(isDBProcess){
			DB.rollback();
			DB.closeSession();
		}
	}
}
