package com.duggan.workflow.server.helper.jbpm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.drools.SystemEventListenerFactory;
import org.drools.runtime.KnowledgeRuntime;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;
import org.jbpm.eventmessaging.EventResponseHandler;
import org.jbpm.eventmessaging.Payload;
import org.jbpm.process.workitem.wsht.LocalHTWorkItemHandler;
import org.jbpm.task.Content;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.TaskService;
import org.jbpm.task.event.TaskEventKey;
import org.jbpm.task.event.entity.TaskCompletedEvent;
import org.jbpm.task.event.entity.TaskEvent;
import org.jbpm.task.event.entity.TaskFailedEvent;
import org.jbpm.task.event.entity.TaskSkippedEvent;
import org.jbpm.task.service.local.LocalTaskService;
import org.jbpm.task.service.responsehandlers.AbstractBaseResponseHandler;
import org.jbpm.task.utils.ContentMarshallerHelper;
import org.jbpm.task.utils.OnErrorAction;

import com.duggan.workflow.server.db.DB;

public class BPMHTWorkItemHandler extends LocalHTWorkItemHandler {

	public BPMHTWorkItemHandler(KnowledgeRuntime session,
			OnErrorAction action) {
		super(session, action);
	}

	public BPMHTWorkItemHandler(TaskService client,
			KnowledgeRuntime session, OnErrorAction action) {
		super(session, action);
		setClient(client);
	}

	public BPMHTWorkItemHandler(TaskService client, KnowledgeRuntime session) {
		super(session);
		setClient(client);
	}

	public BPMHTWorkItemHandler(KnowledgeRuntime session) {
		super(session);
	}

	public BPMHTWorkItemHandler(KnowledgeRuntime session,
			boolean owningSessionOnly) {
		super(session);
	}

	protected void registerTaskEvents() {
		TaskService client = getClient();
//		TaskEventKey key = new TaskEventKey(TaskCompletedEvent.class, -1);
//		client.registerForEvent(key, false, new TaskCompletedHandler(true));
//		eventHandlers.put(key, new TaskCompletedHandler(true));
//		key = new TaskEventKey(TaskFailedEvent.class, -1);
//		client.registerForEvent(key, false, new TaskCompletedHandler(true));
//		eventHandlers.put(key, new TaskCompletedHandler(true));
//		key = new TaskEventKey(TaskSkippedEvent.class, -1);
//		client.registerForEvent(key, false, new TaskCompletedHandler(true));
//		eventHandlers.put(key, new TaskCompletedHandler(true));
		
		TaskCompletedHandler eventResponseHandler = new TaskCompletedHandler();
		TaskEventKey key = new TaskEventKey(TaskCompletedEvent.class, -1);
		client.registerForEvent(key, false, eventResponseHandler);
		eventHandlers.put(key, eventResponseHandler);
		key = new TaskEventKey(TaskFailedEvent.class, -1);
		client.registerForEvent(key, false, eventResponseHandler);
		eventHandlers.put(key, eventResponseHandler);
		key = new TaskEventKey(TaskSkippedEvent.class, -1);
		client.registerForEvent(key, false, eventResponseHandler);
		eventHandlers.put(key, eventResponseHandler);
	}

	
	private class TaskCompletedHandler extends AbstractBaseResponseHandler
			implements EventResponseHandler {

		private TaskService client = null;
		
		public TaskCompletedHandler(){
			this(false);
		}
		
		public TaskCompletedHandler(boolean createNew) {
			if(createNew){

				org.jbpm.task.service.TaskService ts = new org.jbpm.task.service.TaskService(DB.getEntityManagerFactory(),
		    			SystemEventListenerFactory.getSystemEventListener());
		    	client = new LocalTaskService(ts);	
			}else{
				
				client = BPMHTWorkItemHandler.this.getClient();
			}
		}

		public void execute(Payload payload) {
			TaskEvent event = (TaskEvent) payload.get();
			final long taskId = event.getTaskId();

			if (isOwningSessionOnly()
					&& (session instanceof StatefulKnowledgeSession)) {
				if (((StatefulKnowledgeSession) session).getId() != event
						.getSessionId()) {
					return;
				}
			}

			if (isLocal()) {
				handleCompletedTask(taskId);
			} else {
				Runnable runnable = new Runnable() {

					public void run() {
						handleCompletedTask(taskId);
					}
				};
				new Thread(runnable).start();
			}
		}

		public boolean isRemove() {
			return false;
		}

		public void handleCompletedTask(long taskId) {
			Task task = client.getTask(taskId);
			long workItemId = task.getTaskData().getWorkItemId();
			if (task.getTaskData().getStatus() == Status.Completed) {
				String userId = task.getTaskData().getActualOwner().getId();
				Map<String, Object> results = new HashMap<String, Object>();
				results.put("ActorId", userId);
				long contentId = task.getTaskData().getOutputContentId();
				if (contentId != -1) {
					Content content = client.getContent(contentId);
					Object result = ContentMarshallerHelper.unmarshall(
							content.getContent(), session.getEnvironment(),
							getClassLoader());
					results.put("Result", result);
					if (result instanceof Map) {
						Map<?, ?> map = (Map<?, ?>) result;
						for (Map.Entry<?, ?> entry : map.entrySet()) {
							if (entry.getKey() instanceof String) {
								results.put((String) entry.getKey(),
										entry.getValue());
							}
						}
					}

					session.getWorkItemManager().completeWorkItem(
							task.getTaskData().getWorkItemId(), results);
				} else {
					session.getWorkItemManager().completeWorkItem(workItemId,
							results);
				}
			} else {
				session.getWorkItemManager().abortWorkItem(workItemId);
			}
		}
	}
}
