package xtension.workitems;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;
import org.jbpm.executor.ExecutorModule;
import org.jbpm.executor.api.CommandCodes;
import org.jbpm.executor.api.CommandContext;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.NotificationType;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;

/**
 * Send Asynchronous Email
 * 
 * @author duggan
 *
 */
public class SendMailWorkItemHandler implements WorkItemHandler {


	private static Logger log = Logger.getLogger(SendMailWorkItemHandler.class);
	
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

		String[]groups = null;
		if(groupId!=null){
			groups = groupId.split(",");
		}
		
		Document doc = null;
		try{
			doc = DocumentDaoHelper.getDocument(Long.parseLong(documentId));
		}catch(Exception e){}
		
		log.debug("Class : "+this.getClass());
		log.debug("Subject : "+subject);
		log.debug("NotificationType : "+noteType);
		log.debug("DocumentId : "+documentId);
		log.debug("GroupId : "+groupId);
		log.debug("ActorId : "+actorId);
		log.debug("OwnerId : "+ownerId);	

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.putAll(workItem.getParameters());
		
		List<HTUser> users = null;
		//notification.setTargetUserId(targetUserId);
		if(actorId!=null && !actorId.trim().isEmpty()){
			users = new ArrayList<>();
			users.add(LoginHelper.get().getUser(actorId));
		}else if(groups!=null){
			users = LoginHelper.get().getUsersForGroups(groups);
		}
		
		List<HTUser> owner = new ArrayList<>();
		owner.add(LoginHelper.get().getUser(ownerId));
		
		String body = "";
		String approver = "";
		if(groups!=null && groups.length>0){
			List<UserGroup> groupList = LoginHelper.get().getGroupsByIds(groups);
			for(UserGroup group:groupList){
				approver = approver.concat(group.getFullName()+", ");
			}
			
			if(!approver.isEmpty() && approver.endsWith(", ")){
				approver = approver.substring(0, approver.length()-2);
			}
		}
		
		if(approver==null && actorId!=null){
			approver = actorId;
			HTUser user = LoginHelper.get().getUser(actorId);
			if(user!=null)
				approver = user.getFullName();
			
		}
		
		if(approver==null){
			approver="";
		}
		
		params.put("Approver", approver);
		
		String noteaction = isApproved==null?"Completed": 
			(Boolean)isApproved? "Approved": "Denied";
		
		switch (type) {
		case APPROVALREQUEST_OWNERNOTE:
			subject = subject+" Approval Request Submitted";
			body = "Your document #"+subject+" was submitted to "+approver;
			break;
		case APPROVALREQUEST_APPROVERNOTE:
			//subject = subject+" Approval Request from "+getOwner(ownerId);
			subject = "Request #"+subject;
			body =  "The following document requires your review/ approval.";
			owner = users;
			break;
		case TASKCOMPLETED_APPROVERNOTE:
			subject = subject+" - "+noteaction;
			body =  "You "+noteaction.toLowerCase()+" Document #"+subject;
			owner= users;
			break;
		case TASKCOMPLETED_OWNERNOTE:						
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
		
		params.put("title", body);
		params.put("subject", subject);
		
		sendMail(doc,owner, params);
		manager.completeWorkItem(workItem.getId(), workItem.getParameters());
	}

	private void sendMail(Document doc,List<HTUser> users, HashMap<String, Object> params) {
		
//		StringBuffer recipient=new StringBuffer();
		
//		Iterator<HTUser> iter = users.iterator();
//		
//		List<String> usersList = new ArrayList<>();
//		while(iter.hasNext()){
//			String email = iter.next().getEmail();
//			if(email==null){
//				continue;
//			}
//			if(usersList.contains(email)){
//				continue;
//			}
//			usersList.add(email);
//			
//			recipient.append(email);
//			
//			if(iter.hasNext())
//				recipient.append(",");
//		}
		
		if(users==null || users.isEmpty()){
			return;
		}
		
		/**
		 * Sending mail
		 * 
		 * Schedule async
		 */
		CommandContext context = new CommandContext();
		params.put("callbacks", CommandCodes.SendEmailCallback.name());
		params.put("To", users);
		params.put("From", params.get("From")==null? "ebpm.mgr@gmail.com": params.get("From"));
		params.put("docDate", SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(
				doc.getDocumentDate()==null? doc.getCreated(): doc.getDocumentDate()));
		DocumentType type = doc.getType();
		params.put("DocType",type.getDisplayName());
		params.put("DocumentURL", getDocUrl(doc.getRefId()));
		params.put("ownerId", doc.getOwner());
		
		try{
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("email.html");			
			String html = "";			
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			IOUtils.copy(is, bout);
			html = new String(bout.toByteArray());
			
			Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");//pick all content in ${}
			Matcher matcher = pattern.matcher(html);
			while (matcher.find()) {
				String key = matcher.group(1);
				
				String value = params.get(key)==null? null: params.get(key).toString();
				if(value==null){
					//log.debug("SendEmailWorkItemHandler Checking Document for value for key: " + key);
					value = doc.getValues().get(key)==null ? null : 
						doc.getValues().get(key).getValue()==null? null : doc.getValues().get(key).getValue().toString();
				}
				
				if(value==null || value.isEmpty()){
					log.warn("SendEmailWorkItemHandler Missing Value for key: " + key);
					value = "";
				}else{
					log.debug("SendEmailWorkItemHandler found key: " + key+" = "+value);
				}
				
				html = html.replace("${"+key+"}",value);
			}
			
			params.put("Body", html);						
		}catch(Exception e){
			e.printStackTrace();
		}
		
		params.put("businessKey", UUID.randomUUID().toString());
		context.setData(params);

		ExecutorModule.getInstance().getExecutorServiceEntryPoint()		
				.scheduleRequest(CommandCodes.SendEmailCommand, context);
		
	}


	private String getDocUrl(String docRefId) {
		return SessionHelper.generateDocUrl(docRefId);
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

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

	}

}
