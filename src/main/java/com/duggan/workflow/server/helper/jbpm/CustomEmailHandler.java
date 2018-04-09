package com.duggan.workflow.server.helper.jbpm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jbpm.executor.ExecutorModule;
import org.jbpm.executor.api.CommandCodes;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.commands.SendMailCommand;

import com.duggan.workflow.server.dao.model.ADTaskNotification;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.export.DocumentHTMLMapper;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.model.StringValue;
import com.wira.commons.shared.models.HTUser;

public class CustomEmailHandler {

	private static Logger log = Logger.getLogger(CustomEmailHandler.class);

	public void sendNotification(ADTaskNotification template, Document doc,
			Map<String, Object> params) throws IOException {

		String caseNo = null;
		String noteType = null;

		String groupId = null;
		String actorId = null;
		String ownerId = null;
		Object isApproved = null;
		String html = null;

		if (template != null && template.getNotificationTemplate() != null) {

			if (template.isUseDefaultNotification()) {
				InputStream is = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("email.html");
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				IOUtils.copy(is, bout);
				html = new String(bout.toByteArray());
			} else {
				html = template.getNotificationTemplate();
			}

		} else {
			// Do not send default emails
			return;
		}

		caseNo = (String) params.get("caseNo");
		// noteType = (String) params.get("NotificationType");
		// NotificationType type = NotificationType.valueOf(noteType);
		groupId = (String) params.get("GroupId");
		actorId = (String) params.get("ActorId");
		ownerId = (String) params.get("ownerId");
		// isApproved = params.get("isApproved");

		String[] groups = null;
		if (groupId != null) {
			groups = groupId.split(",");
		}

		log.debug("Class : " + this.getClass());
		log.debug("Subject : " + caseNo);
		log.debug("NotificationType : " + noteType);
		log.debug("GroupId : " + groupId);
		log.debug("ActorId : " + actorId);
		log.debug("OwnerId : " + ownerId);

		List<HTUser> receipients = null;

		// notification.setTargetUserId(targetUserId);
		if (actorId != null && !actorId.trim().isEmpty()) {
			receipients = new ArrayList<>();
			receipients.add(LoginHelper.get().getUser(actorId));
		} else if (groups != null) {
			receipients = LoginHelper.get().getUsersForGroups(groups);
		}

		// List<HTUser> owner = new ArrayList<>();
		// owner.add(LoginHelper.get().getUser(ownerId));

		String emailSubject = (doc.getType() == null ? "Work item" : doc
				.getType().getDisplayName()) + " " + doc.getCaseNo();
		if (template != null) {
			emailSubject = parse(template.getSubject(), doc);
		}

		params.put("emailSubject", emailSubject);
		sendMail(html, doc, receipients, params);
		log.info("EMAILSUBJECT = " + emailSubject);
		log.info("EMAILRECIEPIENTS = " + receipients);
	}

	public void sendMail(String htmlTemplate, Doc doc,
			List<HTUser> receipients, Map<String, Object> params) {

		log.info("Sending email " + doc.getCaseNo() + " to " + receipients);
		if (receipients == null || receipients.isEmpty()) {
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
				
		if(htmlTemplate==null){
			throw new IllegalArgumentException("Cannot send Email - Email Template is null");
		}
		
		log.debug("CustomEmailHandler [1] - Basic validation done!");		
//		params.put(
//				"docDate",
//				SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM)
//						.format(doc.getDocumentDate() == null ? doc
//								.getCreated() : doc.getDocumentDate()));

		String docType = "";
		Long taskId = null;
		if (doc instanceof Document) {
			docType = ((Document) doc).getType().getDisplayName();
			taskId = ((Document) doc).getCurrentTaskId();
		} else {
			docType = ((HTask) doc).getProcessName();
			taskId = ((HTask) doc).getCurrentTaskId();
		}

		ProcessDefModel model = DB.getProcessDao().getProcessDef(doc.getProcessId());
		params.put("DocType", docType);
		params.put("ownerId", doc.getOwner());
		
		if(model!=null) {
			params.put("DocumentURL", SessionHelper.generateDocUrl(model.getRefId(), taskId));
		}

		try {

			// Merge params & doc
			for (String key : params.keySet()) {
				if (doc.get(key) == null) {
					doc.setValue(key, params.get(key) == null ? null
							: new StringValue(params.get(key).toString()));
				}
			}
			htmlTemplate = parse(htmlTemplate, doc);
			String subject = (String) params.get(SendMailCommand.SUBJECT);
			subject = parse(subject, doc);
			params.put(SendMailCommand.SUBJECT, subject);

			params.put(SendMailCommand.BODY, htmlTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		params.put("businessKey", UUID.randomUUID().toString());
		context.setData(params);

		log.info("BODY = " + htmlTemplate);
		ExecutorModule.getInstance().getExecutorServiceEntryPoint()
				.scheduleRequest(CommandCodes.SendEmailCommand, context);

	}

	public void sendMail(String subject, String htmlTemplate,
			List<HTUser> receipients, Map<String, Object> params) {

		/**
		 * Sending mail
		 * 
		 * Schedule async
		 */
		CommandContext context = new CommandContext();
		params.put("callbacks", CommandCodes.SendEmailCallback.name());
		params.put("To", receipients);
		params.put("From", params.get("From") == null ? "ebpm.mgr@gmail.com"
				: params.get("From"));

		try {
			htmlTemplate = parse(htmlTemplate, params);
			params.put(SendMailCommand.SUBJECT, subject);
			params.put(SendMailCommand.BODY, htmlTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		params.put("businessKey", UUID.randomUUID().toString());
		context.setData(params);

		log.info("BODY = " + htmlTemplate);
		ExecutorModule.getInstance().getExecutorServiceEntryPoint()
				.scheduleRequest(CommandCodes.SendEmailCommand, context);

	}

	private String parse(String htmlTemplate, Map<String, Object> params) {
		DocumentHTMLMapper mapper = new DocumentHTMLMapper();
		String result = mapper.map(params, htmlTemplate);

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
		if (ownerId != null) {
			HTUser user = LoginHelper.get().getUser(ownerId);
			if (user != null) {
				owner = user.getFullName();
			}
		}
		return owner;
	}

}
