package com.duggan.workflow.test.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.email.EmailServiceHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.jbpm.ProcessMigrationHelper;
import com.duggan.workflow.shared.model.Document;

public class TestEmail {


	Document doc;
	
	boolean isDBProcess = true;
	
	public void initDB(){
		DBTrxProvider.init();
		DB.beginTransaction();
		ProcessMigrationHelper.start(4L);
		ProcessMigrationHelper.start(17L);
		doc = DocumentDaoHelper.getDocument(359L);
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
	
	@Test
	public void sendEmailNew() throws IOException, MessagingException{
		InputStream is = TestEmail.class.getClass().getResourceAsStream("/email.html");
		String body = IOUtils.toString(is);
		assert body!=null;
		
		EmailServiceHelper.sendEmail(body, "RE: Wira Enhanced mailing", 
				Arrays.asList(LoginHelper.get().getUser("mariano"),
						LoginHelper.get().getUser("james")),
				LoginHelper.get().getUser("pnjenga"));
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
