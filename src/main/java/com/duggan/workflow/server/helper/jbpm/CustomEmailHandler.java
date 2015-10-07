package com.duggan.workflow.server.helper.jbpm;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jbpm.executor.ExecutorModule;
import org.jbpm.executor.api.CommandCodes;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.commands.SendMailCommand;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.model.ADTaskNotification;
import com.duggan.workflow.server.export.DocumentHTMLMapper;
import com.duggan.workflow.server.export.HTMLToPDFConvertor;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.model.NotificationType;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.UserGroup;

public class CustomEmailHandler {

	private static Logger log = Logger.getLogger(CustomEmailHandler.class);
	
//	public void sendNotification(ADTaskNotification template, Map<String, Object> params){
//		String documentId = (String) params.get("DocumentId");
//		log.debug("DocumentId : "+documentId);
//		
//		Document doc = params.get("document")==null? null : (Document)params.get("document");
//		try{
//			doc = DocumentDaoHelper.getDocument(Long.parseLong(documentId));
//		}catch(Exception e){}
//		
//		sendNotification(template, doc, params);
//	}
	
	public void sendNotification(ADTaskNotification template,Document doc, Map<String, Object> params){
		
		String caseNo = null;
		String noteType = null;
		
		String groupId = null;
		String actorId = null;
		String ownerId = null;
		Object isApproved = null;
		String html=null;
		
		if(template!=null && !template.isUseDefaultNotification() && template.getNotificationTemplate()!=null){
			html = template.getNotificationTemplate();
		}

		caseNo = (String) params.get("caseNo");
		//noteType = (String) params.get("NotificationType");
		//NotificationType type = NotificationType.valueOf(noteType);
		groupId = (String) params.get("GroupId");
		actorId = (String) params.get("ActorId");
		ownerId = (String) params.get("ownerId");
		//isApproved = params.get("isApproved");

		String[]groups = null;
		if(groupId!=null){
			groups = groupId.split(",");
		}
		
		log.debug("Class : "+this.getClass());
		log.debug("Subject : "+caseNo);
		log.debug("NotificationType : "+noteType);
		log.debug("GroupId : "+groupId);
		log.debug("ActorId : "+actorId);
		log.debug("OwnerId : "+ownerId);	
		
		List<HTUser> receipients = null;
		
		//notification.setTargetUserId(targetUserId);
		if(actorId!=null && !actorId.trim().isEmpty()){
			receipients = new ArrayList<>();
			receipients.add(LoginHelper.get().getUser(actorId));
		}else if(groups!=null){
			receipients = LoginHelper.get().getUsersForGroups(groups);
		}
		
//		List<HTUser> owner = new ArrayList<>();
//		owner.add(LoginHelper.get().getUser(ownerId));
		
		
		String emailSubject = (doc.getType()==null? "Work item" : 
			doc.getType().getDisplayName())+" "+doc.getCaseNo();
		if(template!=null){
			emailSubject = parse(template.getSubject(), doc);
		}
		
		params.put("emailSubject", emailSubject);
		sendMail(html,doc,receipients, params);
		log.info("EMAILSUBJECT = "+emailSubject);
		log.info("EMAILRECIEPIENTS = "+receipients);
	}

	public void sendMail(String htmlTemplate, Doc doc,List<HTUser> receipients, Map<String, Object> params) {
		
		log.info("Sending email "+doc.getCaseNo()+" to "+receipients);
		if(receipients==null || receipients.isEmpty()){
			return;
		}
		
		/**
		 * Sending mail
		 * 
		 * Schedule async
		 */
		CommandContext context = new CommandContext();
		params.put("callbacks", CommandCodes.SendEmailCallback.name());
		params.put("To", receipients);
		params.put("From", params.get("From")==null? "ebpm.mgr@gmail.com": params.get("From"));
		params.put("docDate", SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(
				doc.getDocumentDate()==null? doc.getCreated(): doc.getDocumentDate()));
		
		String docType = "";
		if(doc instanceof Document){
			docType = ((Document)doc).getType().getDisplayName();
		}else{
			docType = ((HTask)doc).getProcessName();
		}
		
		params.put("DocType",docType);
		params.put("DocumentURL", getDocUrl(doc.getRefId()));
		params.put("ownerId", doc.getOwner());
		
		try{
			if(htmlTemplate==null){
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("email.html");
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				IOUtils.copy(is, bout);
				htmlTemplate = new String(bout.toByteArray());
			}
			
			//Merge params & doc
			for(String key: params.keySet()){
				if(doc.get(key)==null){
					doc.setValue(key, params.get(key)==null? null :
						new StringValue(params.get(key).toString()));
				}
			}
			htmlTemplate = parse(htmlTemplate, doc);
			String subject = (String)params.get(SendMailCommand.SUBJECT);
			subject = parse(subject, doc);
			params.put(SendMailCommand.SUBJECT, subject);
			
			params.put(SendMailCommand.BODY, htmlTemplate);						
		}catch(Exception e){
			e.printStackTrace();
		}
		
		params.put("businessKey", UUID.randomUUID().toString());
		context.setData(params);
		
		log.info("BODY = "+htmlTemplate);
		ExecutorModule.getInstance().getExecutorServiceEntryPoint()		
				.scheduleRequest(CommandCodes.SendEmailCommand, context);
		
	}


	private String parse(String htmlTemplate, Map<String, Object> params) {
		DocumentHTMLMapper mapper = new DocumentHTMLMapper();
		String result = mapper.map(params,htmlTemplate);
		
		return result;
	}

	private String getDocUrl(String docRefId) {
		return SessionHelper.generateDocUrl(docRefId);
	}
	

	private String parse(String html, Doc doc) {
		DocumentHTMLMapper mapper = new DocumentHTMLMapper();
		String result = mapper.map(doc, html);
		
		return result;
	}

	private String getOwner(String ownerId) {
		
		String owner = ownerId;
		if(ownerId!=null){
			HTUser user = LoginHelper.get().getUser(ownerId);
			if(user!=null){
				owner = user.getFullName();
			}
		}
		return owner;
	}

}
