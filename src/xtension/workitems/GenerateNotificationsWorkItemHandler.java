package xtension.workitems;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.drools.runtime.process.ProcessContext;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.jbpm.executor.ExecutorModule;
import org.jbpm.executor.api.CommandCodes;
import org.jbpm.executor.api.CommandContext;
import org.jbpm.executor.commands.SendMailCommand;
import org.jbpm.executor.impl.ExecutorFactory;
import org.jbpm.workflow.instance.node.HumanTaskNodeInstance;

import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.dao.NotificationDaoHelper;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;

/**
 * This class is responsible for generating
 * <ul>
 * <li>System Notification - Synchronous
 * <li>Email Notification - Asynchronous
 * <li>Update Document incase rejected
 * </ul>
 * 
 * @author duggan
 * 
 */
public class GenerateNotificationsWorkItemHandler implements WorkItemHandler {

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Map<String, Object> map = workItem.getParameters();
		
		Set<String> keys = map.keySet();
		
		System.out.println("#Executing WorkItem:  values = ");
		for(String key: keys){
			System.err.println(key+" : "+map.get(key));
		}
		
		manager.completeWorkItem(workItem.getId(), workItem.getParameters());
	}

	public void executeWorkItem1(WorkItem workItem, WorkItemManager manager) {

		Long documentId = workItem.getParameter("documentId") == null ? null
				: new Long(workItem.getParameter("documentId").toString());
		DocType docType = DocType.INVOICE;
		NotificationType notificationType = NotificationType.APPROVALREQUEST_APPROVERNOTE;
		String owner = workItem.getParameter("ownerId") == null ? null
				: workItem.getParameter("ownerId").toString();
		Boolean isRead = false;
		String subject = workItem.getParameter("subject") == null ? null
				: workItem.getParameter("subject").toString();
		String targetUserId = workItem.getParameter("actorId") == null ? null
				: workItem.getParameter("actorId").toString();

		ProcessContext kcontext=null;
		((HumanTaskNodeInstance)kcontext.getNodeInstance()).getWorkItem().getParameter("GroupId");
		((HumanTaskNodeInstance)kcontext.getNodeInstance()).getWorkItem().getParameter("ActorId");
		Map<String, Object> map = new HashMap<>();
		kcontext.getVariable("GroupId");
		kcontext.getVariable("ActorId");
		kcontext.getVariable("OwnerId");
		kcontext.getVariable("DocumentId");
		kcontext.getVariable("Subject");
		
		map.put("OwnerId", kcontext.getVariable("GroupId"));
		map.put("ActorId", kcontext.getVariable("ActorId"));
		map.put("OwnerId", kcontext.getVariable("OwnerId"));
		map.put("DocumentId", kcontext.getVariable("DocumentId"));
		map.put("Subject", kcontext.getVariable("Subject"));		
		kcontext.getKnowledgeRuntime().startProcess("beforetask-notification", map);

		Notification notification = new Notification();
		notification.setCreated(new Date());
		notification.setDocumentId(documentId);
		notification.setDocumentType(docType);
		notification.setNotificationType(notificationType);
		notification.setOwner(owner);
		notification.setRead(isRead);
		notification.setSubject(subject);
		notification.setTargetUserId(targetUserId);

		// Save Notification
		NotificationDaoHelper.saveNotification(notification);

		// Update any Rejection action taken after every human task
//		if (isApproved != null && documentId!=null) {
//			if (notificationType == NotificationType.TASKCOMPLETED_DENIED
//					&& !isApproved) {
//				
//				DocumentDaoHelper.saveApproval(new Long(documentId.toString()),
//						(Boolean) isApproved);
//			} else if (notificationType == NotificationType.PROCESS_COMPLETED && isApproved) {
//				//end node & approved
//				DocumentDaoHelper.saveApproval(new Long(documentId.toString()),
//						(Boolean) isApproved);
//			}
//		}
//		

		/**
		 * Sending mail
		 * 
		 *  Schedule async
		 */
		CommandContext context = new CommandContext();
		Map<String, Object> params = workItem.getParameters();
		params.put("callbacks", CommandCodes.SendEmailCallback.name());
		context.setData(params);

		ExecutorModule.getInstance().getExecutorServiceEntryPoint()
				.scheduleRequest(CommandCodes.SendEmailCommand, context);

		// completeness of this process does not depend on email being sent
		manager.completeWorkItem(workItem.getId(), workItem.getParameters());
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

	}

}
