package com.duggan.workflow.server.helper.jbpm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.ContextInstance;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.services.task.lifecycle.listeners.TaskLifeCycleEventListener;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.kie.api.task.TaskEvent;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.model.ADTaskNotification;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.NotificationCategory;
import com.duggan.workflow.shared.model.NotificationType;

public class NotificationsTaskEventListener implements
		TaskLifeCycleEventListener {

	Logger logger = Logger.getLogger(getClass());

	@Override
	public void afterTaskStoppedEvent(TaskEvent event) {
		logger.info("NotificationTaskEventListener says Task Stopped "
				+ JBPMHelper.get().getDisplayName(event.getTask()));
	}

	/**
	 * Task Could be reserved by yourself (Through Claim method) or it could be
	 * assigned to you specifically (by the assignee specifying actorId)
	 */
	@Override
	public void afterTaskClaimedEvent(TaskEvent event) {
		logger.info("NotificationTaskEventListener says Task Claimed ");
		onTaskCreated(event.getTask());
	}

	@Override
	public void afterTaskStartedEvent(TaskEvent event) {
		logger.info("NotificationTaskEventListener says Task Started "
				+ JBPMHelper.get().getDisplayName(event.getTask()));
	}

	@Override
	public void afterTaskSkippedEvent(TaskEvent event) {
		logger.info("NotificationTaskEventListener says Task Skipped "
				+ JBPMHelper.get().getDisplayName(event.getTask()));
	}

	@Override
	public void afterTaskReleasedEvent(TaskEvent event) {
		logger.info("NotificationTaskEventListener says Task Released "
				+ JBPMHelper.get().getDisplayName(event.getTask()));
	}

	@Override
	public void afterTaskForwardedEvent(TaskEvent event) {
		logger.info("NotificationTaskEventListener says Task Forwarded "
				+ JBPMHelper.get().getDisplayName(event.getTask()));
	}

	@Override
	public void afterTaskFailedEvent(TaskEvent event) {
		logger.info("NotificationTaskEventListener says Task Failed "
				+ JBPMHelper.get().getDisplayName(event.getTask()));
	}

	/**
	 * SEND BEFORE TASK NOTIFICATIONS
	 * 
	 * TODO: Note: If an exception occurs here, the error if silently handled by
	 * the caller - meaning its not propagated back to startProcess. The caller
	 * however generates the following exception
	 * org.jbpm.workflow.instance.WorkflowRuntimeException: [invoice-approval:55
	 * - HOD Approval:2] -- null ............. caused by
	 * java.lang.NullPointerException at
	 * org.jbpm.process.workitem.wsht.GenericHTWorkItemHandler
	 * .executeWorkItem(GenericHTWorkItemHandler.java:184) ..............
	 * 
	 * This method therefore must handle its own exceptions internally and mark
	 * the Trx for rollback in case an exception occurs to reverse the whole
	 * transaction
	 * 
	 * An Exception Log should also be inserted into ErroLog table to assist in
	 * debugging
	 */
	public void taskCreated(TaskEvent event) {
		// session.startProcess(processId, parameters)

		try {
			logger.info("NotificationTaskEventListener onTaskCreated");
			onTaskCreated(event.getTask());
			logger.info("NotificationTaskEventListener onTaskCreated : End");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("NotificationTaskEventListener onTaskCreated : Fail");
			throw new RuntimeException(e);
		}
	}

	/**
	 * SEND AFTER TASK NOTIFICATIONS
	 * 
	 * TODO: Note: If an exception occurs here, the error if silently handled by
	 * the caller - meaning its not propagated back to startProcess. The caller
	 * however fails with the following exception
	 * org.jbpm.workflow.instance.WorkflowRuntimeException: [invoice-approval:55
	 * - HOD Approval:2] -- null ............. caused by
	 * java.lang.NullPointerException at
	 * org.jbpm.process.workitem.wsht.GenericHTWorkItemHandler
	 * .executeWorkItem(GenericHTWorkItemHandler.java:184) ..............
	 * 
	 * This method therefore must handle its own exceptions internally and mark
	 * the Trx for rollback in case an exception occurs to reverse the whole
	 * transaction
	 * 
	 * An Exception Log should also be inserted into ErroLog table to assist in
	 * debugging
	 */
	public void taskCompleted(TaskEvent event) {
		try {

			Task task = event.getTask();
			logger.debug(Thread.currentThread()
					+ "NotificationTaskEventListener  COMPLETING TASK - event TaskID="
					+ task.getId()
					+ " ProcessId=" + task.getTaskData().getProcessId());

			Long outputId = task.getTaskData().getOutputContentId();

			Map<String, Object> taskData = JBPMHelper
					.getMappedDataByContentId(outputId);

			Long processInstanceId = task.getTaskData().getProcessInstanceId();

			Map<String, Object> values = new HashMap<>();
			org.jbpm.process.instance.ProcessInstance instance = JBPMHelper.get().getProcessInstance(processInstanceId);

			// Task Data
			Set<String> keys = taskData.keySet();
			Map<String, Object> newValues = new HashMap<>();
			for (String key : keys) {
				Object value = taskData.get(key);
				newValues.put(key, value);
				logger.debug("NotificationTaskEventListener Task Data " + key
						+ "=" + value);
			}

			ContextInstance ctxInstance = null;

			if (instance != null) {
				ctxInstance = instance
						.getContextInstance(VariableScope.VARIABLE_SCOPE);
			}

			if (ctxInstance != null) {
				VariableScopeInstance variableScope = (VariableScopeInstance) ctxInstance;
				values = variableScope.getVariables();
			}

			// variable scope
			keys = values.keySet();
			for (String key : keys) {
				Object value = values.get(key);

				// if (!key.equals("isApproved")){
				// key = key.substring(0, 1).toUpperCase()
				// + key.substring(1); //IsApproved
				// }

				if (newValues.get(key) == null) {
					newValues.put(key, value);
					logger.debug("NotificationTaskEventListener Process Variable Key :: "
							+ key + " = " + value);
				}
			}

			Document doc = DocumentDaoHelper.getDocumentByProcessInstance(task
					.getTaskData().getProcessInstanceId());
			newValues.put("ownerId", doc.getOwner().getUserId());
			assert doc.getId() != null;
			newValues.put("documentId", doc.getId());

			//
			/*
			 * Duggan 19/09/2016 - Disabled default task completion emails. 
			 * 
			 * taskData.putAll(newValues);
			new CustomNotificationHandler().generate(taskData,
					NotificationType.TASKCOMPLETED_OWNERNOTE);
			new CustomNotificationHandler().generate(taskData,
					NotificationType.TASKCOMPLETED_APPROVERNOTE);
					*/

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Called on TaskEventListener.taskCreated(group allocation) and
	 * TaskEventListener.taskClaimed(Specific Allocation)
	 * 
	 * @param taskId
	 */
	private void onTaskCreated(Task task) {

		Status previousStatus = task.getTaskData().getPreviousStatus();
		if (previousStatus == Status.Ready) {
			// either this is a group task (had to be claimed, in which case
			// before-task notifications would be repetitive)
			// or after user was directly assigned the task, he revoked the
			// task, and has re-claimed it again
			return;
		}
		Map<String, Object> taskData = JBPMHelper.getMappedData(task);// Mapped
																		// Data
																		// -
																		// what
																		// is
																		// this?
																		// Is
																		// the
																		// doc

		Map<String, Object> values = null;
		Map<String, Object> newValues = new HashMap<>();

		Long sessionId = new Long(task.getTaskData().getProcessSessionId());

		org.jbpm.process.instance.ProcessInstance instance = JBPMHelper.get().getProcessInstance(task.getTaskData().getProcessInstanceId());
		VariableScopeInstance variableScope = (VariableScopeInstance) instance
				.getContextInstance(VariableScope.VARIABLE_SCOPE);
		values = variableScope.getVariables();

		Set<String> keys = values.keySet();

		for (String key : keys) {
			Object value = values.get(key);

			// Why am I doing this?
			// key = key.substring(0, 1).toUpperCase() + key.substring(1);
			newValues.put(key, value);
			logger.debug(key + "=" + value);
		}

		Object groupId = taskData.get("GroupId");
		Object actorId = taskData.get("ActorId");
		logger.debug("BPMSessionManager#onTaskCreated GroupId=" + groupId);
		logger.debug("BPMSessionManager#onTaskCreated ActorId=" + actorId);
		logger.debug("BPMSessionManager#onTaskCreated Priority="
				+ taskData.get("priority"));

		if (actorId == null && groupId == null) {
			throw new IllegalArgumentException("Subsequent Task '"
					+ JBPMHelper.get().getDisplayName(task)
					+ "' has no User or Group defined");
		}

		newValues.put("GroupId", groupId);
		newValues.put("ActorId", actorId);
		newValues.put("priority", taskData.get("priority"));
		if (newValues.get("priority") == null)
			newValues.put("priority", values.get("priority"));

		HumanTaskNode node = (HumanTaskNode) JBPMHelper.get().getNode(task);
		Long nodeId = node.getId();
		String nodeName = node.getName();

		ADTaskNotification notification = DB.getProcessDao()
				.getTaskNotification(nodeId, nodeName,
						task.getTaskData().getProcessId(),
						NotificationCategory.EMAILNOTIFICATION, Actions.CREATE);

		logger.debug("NodeId= " + nodeId + "; NodeName= " + nodeName
				+ "; processId= " + task.getTaskData().getProcessId()
				+ "; category= "
				+ NotificationCategory.EMAILNOTIFICATION.getName()
				+ "; Action= " + Actions.CREATE.getName());

		// Put all New Values into Task Data; This map has all inputs defined to
		// the human task
		taskData.putAll(newValues);

		/**
		 * 
		 *
		 *Duggan 19/09/2016 Disabled default task creation notifications
		 *
		Document doc = DocumentDaoHelper.getDocument(newValues);
		JBPMHelper.get().getEmailHandler().sendNotification(notification, doc, taskData);
		new CustomNotificationHandler().generate(taskData,
				NotificationType.APPROVALREQUEST_OWNERNOTE);
		new CustomNotificationHandler().generate(taskData,
				NotificationType.APPROVALREQUEST_APPROVERNOTE);
				*/
		
		if(actorId!=null){
			//Task Assigned directly to an actor
			Document doc = DocumentDaoHelper.getDocument(newValues);
			JBPMHelper.get().getEmailHandler().sendNotification(notification, doc, taskData);
		}

		// String processId = "beforetask-notification";
		// startProcess(processId, newValues);
	}

	@Override
	public void afterTaskActivatedEvent(TaskEvent event) {
		logger.info("NotificationTaskEventListener says Task Activated "
				+ JBPMHelper.get().getDisplayName(event.getTask()));
	}

	@Override
	public void afterTaskAddedEvent(TaskEvent event) {
		logger.info("NotificationTaskEventListener says Task Added "
				+ JBPMHelper.get().getDisplayName(event.getTask()));
		taskCreated(event);
	}

	@Override
	public void afterTaskCompletedEvent(TaskEvent event) {
		taskCompleted(event);
	}

	@Override
	public void afterTaskDelegatedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskExitedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskNominatedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskResumedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTaskSuspendedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskActivatedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskAddedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskClaimedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskCompletedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskDelegatedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskExitedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskFailedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskForwardedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskNominatedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskReleasedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskResumedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskSkippedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskStartedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskStoppedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTaskSuspendedEvent(TaskEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
