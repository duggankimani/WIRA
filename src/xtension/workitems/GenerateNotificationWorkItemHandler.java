package xtension.workitems;

import java.util.Date;
import java.util.HashMap;
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
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;
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

		Notification notification = new Notification();
		notification.setCreated(new Date());
		notification.setDocumentId(new Long(documentId));
		notification.setNotificationType(type);
		notification.setOwner(ownerId);
		notification.setRead(false);
		notification.setSubject(subject);
		Document doc = DocumentDaoHelper.getDocument(notification.getDocumentId());
		notification.setDocumentType(doc.getType());
		
		//notification.setTargetUserId(targetUserId);
		
		//LoginHelper.get().get
		switch (type) {
		case APPOVALREQUEST_OWNERNOTE:
			notification.setTargetUserId(ownerId);
			break;
		case APPROVALREQUEST_APPROVERNOTE:
			notification.setTargetUserId(ownerId);
			break;
		case TASKCOMPLETED_APPROVERNOTE:

			break;
		case TASKCOMPLETED_OWNERNOTE:

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

		ProcessContext kcontext = null;
		((HumanTaskNodeInstance) kcontext.getNodeInstance()).getWorkItem()
				.getParameter("GroupId");
		((HumanTaskNodeInstance) kcontext.getNodeInstance()).getWorkItem()
				.getParameter("ActorId");
		Map<String, Object> map = new HashMap<>();
		kcontext.getVariable("GroupId");
		kcontext.getVariable("ActorId");
		kcontext.getVariable("OwnerId");
		kcontext.getVariable("DocumentId");
		kcontext.getVariable("Subject");
		kcontext.setVariable("GroupId", "HOD_DEV");
		kcontext.setVariable("ActorId", null);

		map.put("OwnerId", kcontext.getVariable("GroupId"));
		map.put("ActorId", kcontext.getVariable("ActorId"));
		map.put("OwnerId", kcontext.getVariable("OwnerId"));
		map.put("DocumentId", kcontext.getVariable("DocumentId"));
		map.put("Subject", kcontext.getVariable("Subject"));
		kcontext.getKnowledgeRuntime().startProcess("beforetask-notification",
				map);

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
		// if (isApproved != null && documentId!=null) {
		// if (notificationType == NotificationType.TASKCOMPLETED_DENIED
		// && !isApproved) {
		//
		// DocumentDaoHelper.saveApproval(new Long(documentId.toString()),
		// (Boolean) isApproved);
		// } else if (notificationType == NotificationType.PROCESS_COMPLETED &&
		// isApproved) {
		// //end node & approved
		// DocumentDaoHelper.saveApproval(new Long(documentId.toString()),
		// (Boolean) isApproved);
		// }
		// }
		//

		/**
		 * Sending mail
		 * 
		 * Schedule async
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
