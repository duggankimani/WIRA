package com.duggan.workflow.server.helper.email;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import org.jbpm.executor.commands.SendMailCommand;

import com.duggan.workflow.server.dao.helper.OutputDocumentDaoHelper;
import com.duggan.workflow.server.export.DocumentHTMLMapper;
import com.duggan.workflow.server.helper.jbpm.CustomEmailHandler;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.HTUser;

public class EmailUtil {

	/**
	 * 
	 * @param subject
	 * @param templateName
	 * @param emailAddresses
	 */
	public static void sendEmail(String subject,String templateName,Doc doc,String...emailAddresses){
		
		HTUser [] users = new HTUser[emailAddresses.length];
		
		//for()
		sendEmail(subject, templateName,doc,users);
	}
	
	public static void sendEmail(String subject,String email,Doc doc,HTUser...users){
		Map<String, Object> params= new HashMap<>();
		params.put("caseNo", doc.getCaseNo());
		params.put("ownerId", doc.getOwner());
		params.put(SendMailCommand.SUBJECT, subject);
		
//		if(templateName==null){
//			throw new IllegalArgumentException("Email Template cannot be null");
//		}
//		
//		String htmlTemplate= OutputDocumentDaoHelper.getHTMLTemplate(templateName);
//		
//		if(htmlTemplate==null){
//			throw new IllegalArgumentException("Email Template "+templateName+" Not Found");
//		}
		
		new CustomEmailHandler().sendMail(email, doc, Arrays.asList(users), params);
	}
}
