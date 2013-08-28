package xtension.workitems;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.drools.runtime.process.ProcessContext;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.jbpm.executor.ExecutorModule;
import org.jbpm.executor.api.CommandCodes;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.commands.SendMailCommand;
import org.jbpm.executor.impl.ExecutorFactory;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.task.identity.LDAPUserGroupCallbackImpl;
import org.jbpm.workflow.instance.node.HumanTaskNodeInstance;

import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.dao.NotificationDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.ApproverAction;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;
import static com.duggan.workflow.shared.model.NotificationType.*;

/**
 * This class is responsible for generating
 * <ul>
 * <li>System Notification - Synchronous
 * </ul>
 * 
 * @author duggan
 * 
 */
public class GenerateNotificationWorkItemHandler implements WorkItemHandler {

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
		
//		System.err.println("Class : "+this.getClass());
//		System.err.println("Subject : "+subject);
//		System.err.println("NotificationType : "+noteType);
//		System.err.println("DocumentId : "+documentId);
//		System.err.println("GroupId : "+groupId);
//		System.err.println("ActorId : "+actorId);
//		System.err.println("OwnerId : "+ownerId);		

		Notification notification = new Notification();
		notification.setCreated(new Date());
		notification.setDocumentId(new Long(documentId));
		notification.setNotificationType(type);
		notification.setOwner(ownerId);
		notification.setRead(false);
		notification.setSubject(subject);
		Document doc = DocumentDaoHelper.getDocument(notification.getDocumentId());
		notification.setDocumentType(doc.getType());
		
		List<HTUser> actors = null;
		List<HTUser> potentialActors = null;
		
		//notification.setTargetUserId(targetUserId);
		if(actorId!=null && !actorId.trim().isEmpty()){
			actors = new ArrayList<>();
			actors.add(LoginHelper.get().getUser(actorId));
		}
		
		//potential users
		if(groupId!=null && !groupId.trim().isEmpty()){
			potentialActors = LoginHelper.get().getUsersForGroup(groupId);
		}
		
		List<HTUser> owner = new ArrayList<>();
		owner.add(LoginHelper.get().getUser(ownerId));
		
		switch (type) {
		case APPROVALREQUEST_OWNERNOTE:
			generateNotes(owner, notification);
			break;
		case APPROVALREQUEST_APPROVERNOTE:
			if(actors!=null){
				generateNotes(actors, notification);
			}else{
				generateNotes(potentialActors, notification);
			}
			break;
		case TASKCOMPLETED_APPROVERNOTE:	
			notification.setApproverAction((Boolean)isApproved? ApproverAction.APPROVED: ApproverAction.REJECTED);
			generateNotes(actors, notification);
			break;
		case TASKCOMPLETED_OWNERNOTE:
			notification.setApproverAction((Boolean)isApproved? ApproverAction.APPROVED: ApproverAction.REJECTED);
			generateNotes(owner, notification);
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

		manager.completeWorkItem(workItem.getId(), workItem.getParameters());
	}

	private void generateNotes(List<HTUser> users, Notification notification) {
		
		for(HTUser user: users){
			Notification note = notification.clone();
			note.setTargetUserId(user.getId());
			note.setRead(false);
			
			if(note.getTargetUserId()==null)
				throw new IllegalArgumentException("Target Id must not be null");
			NotificationDaoHelper.saveNotification(note);
		}
		
	}

	
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

	}

}
