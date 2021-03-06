package com.duggan.workflow.server.helper.email;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jbpm.executor.commands.SendMailCommand;

import com.duggan.workflow.server.dao.model.User;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.jbpm.CustomEmailHandler;
import com.duggan.workflow.shared.model.Doc;
import com.wira.commons.shared.models.HTUser;

public class EmailUtil {

	/**
	 * 
	 * @param subject
	 * @param templateName
	 * @param emailAddresses
	 */
	public static void sendEmail(String subject,String email,Doc doc,String...emailAddresses){
		
		HTUser [] users = new HTUser[emailAddresses.length];
		
		int i=0;
		if(emailAddresses!=null)
		for(String emailAdd: emailAddresses) {
			User u = DB.getUserGroupDao().getUserByEmail(emailAdd);
			if(u==null) {
				HTUser user = new HTUser();
				user.setEmail(emailAdd);
				users[i++] = user;
			}else {
				users[i++] = u.toHTUser();
			}
		}
		sendEmail(subject, email,doc,users);
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
