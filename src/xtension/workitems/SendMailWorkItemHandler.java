package xtension.workitems;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpRequest;
import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;
import org.jbpm.executor.ExecutorModule;
import org.jbpm.executor.api.CommandCodes;
import org.jbpm.executor.api.CommandContext;

import com.duggan.workflow.client.ui.util.DateUtils;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.dao.NotificationDaoHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.ApproverAction;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;

/**
 * Send Asynchronous Email
 * 
 * @author duggan
 *
 */
public class SendMailWorkItemHandler implements WorkItemHandler {

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		String subject = (String) workItem.getParameter("Subject");
		String noteType = (String) workItem.getParameter("NotificationType");
		NotificationType type = NotificationType.valueOf(noteType);
		String documentId = (String) workItem.getParameter("DocumentId");
		String groupId = (String) workItem.getParameter("GroupId");
		String actorId = (String) workItem.getParameter("ActorId");
		String ownerId = (String) workItem.getParameter("OwnerId");
		Object isApproved = workItem.getParameter("isApproved");

		Document doc = null;
		try{
			doc = DocumentDaoHelper.getDocument(Long.parseLong(documentId));
		}catch(Exception e){}
		
		System.err.println("Class : "+this.getClass());
		System.err.println("Subject : "+subject);
		System.err.println("NotificationType : "+noteType);
		System.err.println("DocumentId : "+documentId);
		System.err.println("GroupId : "+groupId);
		System.err.println("ActorId : "+actorId);
		System.err.println("OwnerId : "+ownerId);	

		Map<String, Object> params = workItem.getParameters();
		
		List<HTUser> users = null;
		//notification.setTargetUserId(targetUserId);
		if(actorId!=null && !actorId.trim().isEmpty()){
			users = new ArrayList<>();
			users.add(LoginHelper.get().getUser(actorId));
		}else if(groupId!=null && !groupId.trim().isEmpty()){
			users = LoginHelper.get().getUsersForGroup(groupId);
		}
		
		List<HTUser> owner = new ArrayList<>();
		owner.add(LoginHelper.get().getUser(ownerId));
		
		String body = "";
		String approver = groupId;
		if(approver==null){
			approver = actorId;
		}
		
		if(approver==null){
			approver="";
		}
		
		switch (type) {
		case APPROVALREQUEST_OWNERNOTE:
			subject = subject+" Approval Request Submitted";
			body = "Your document #"+subject+" was submitted to "+approver;
			break;
		case APPROVALREQUEST_APPROVERNOTE:
			subject = subject+" Approval Request -"+approver;
			body =  "The following document requires your review and approval.";
			owner = users;
			break;
		case TASKCOMPLETED_APPROVERNOTE:
			String action = (Boolean)isApproved? "Approved": "Denied";
			subject = subject+" - "+action;
			body =  "You "+action.toLowerCase()+" Document #"+subject;
			owner= users;
			break;
		case TASKCOMPLETED_OWNERNOTE:
			String noteaction = (Boolean)isApproved? "Approved": "Denied";			
			subject = subject+" - "+approver+" "+noteaction;
			body =  "The following document "+noteaction.toLowerCase()+" by "+approver;
			break;
		case PROCESS_COMPLETED:
			
			break;

		case TASK_REMINDER:

			break;
		case COMMENT:
			break;
		default:
			break;
		}
		
		params.put("Body", body);
		params.put("Subject", subject);
		sendMail(doc,owner, params);
		manager.completeWorkItem(workItem.getId(), workItem.getParameters());
	}

	private void sendMail(Document doc,List<HTUser> users, Map<String, Object> params) {
		
		StringBuffer recipient=new StringBuffer();
		
		Iterator<HTUser> iter = users.iterator();
		
		while(iter.hasNext()){
			recipient.append(iter.next().getEmail());
			
			if(iter.hasNext())
				recipient.append(",");
		}
		
		
		/**
		 * Sending mail
		 * 
		 * Schedule async
		 */
		CommandContext context = new CommandContext();
		params.put("callbacks", CommandCodes.SendEmailCallback.name());
		params.put("To", recipient.toString());
		params.put("From", params.get("From")==null? "ebpm.mgr@gmail.com": params.get("From"));
		
		try{
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("email.html");			
			String html = "";			
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			IOUtils.copy(is, bout);
			html = new String(bout.toByteArray());
			
			String owner = params.get("OwnerId")==null? "Unknown":params.get("OwnerId").toString();
			String desc = params.get("Description")==null?
					doc.getDescription() : params.get("Description").toString();  
			String body = params.get("Body")==null?"":params.get("Body").toString();
			
			html = html.replace("${Request}",body);
			html = html.replace("${OwnerId}", owner);			
			html = html.replace("${Description}",desc);
			HttpServletRequest request = SessionHelper.getHttpRequest();
			if(request!=null){
				String requestURL = request.getRequestURL().toString();
				String servletPath = request.getServletPath();
				requestURL = requestURL.replace(servletPath, "?#home;type=search;did="+doc.getId());
				html = html.replace("${DocumentURL}", requestURL);
			}else{
				html = html.replace("${DocumentURL}", "#");
			}
			html = html.replace("${DocSubject}", doc.getSubject());
			html = html.replace("${DocumentDate}", SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(doc.getCreated()));
			html = html.replace("${Value}", doc.getValue());
			html = html.replace("${BPartner}", doc.getPartner());
			
			params.put("Body", html);
						
		}catch(Exception e){
			e.printStackTrace();
		}
		
		params.put("businessKey", UUID.randomUUID().toString());
		context.setData(params);

		ExecutorModule.getInstance().getExecutorServiceEntryPoint()		
				.scheduleRequest(CommandCodes.SendEmailCommand, context);
		
	}


	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

	}

}
