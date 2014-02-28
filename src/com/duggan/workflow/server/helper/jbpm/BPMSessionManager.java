package com.duggan.workflow.server.helper.jbpm;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.SystemEventListenerFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.process.Process;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.process.audit.JPAWorkingMemoryDbLogger;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.ContextInstance;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.process.workitem.email.EmailWorkItemHandler;
import org.jbpm.process.workitem.wsht.GenericHTWorkItemHandler;
import org.jbpm.process.workitem.wsht.LocalHTWorkItemHandler;
import org.jbpm.task.Content;
import org.jbpm.task.Task;
import org.jbpm.task.event.TaskEventListener;
import org.jbpm.task.event.entity.TaskUserEvent;
import org.jbpm.task.service.DefaultEscalatedDeadlineHandler;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.TaskClientHandler;
import org.jbpm.task.service.TaskClientHandler.TaskOperationResponseHandler;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.local.LocalTaskService;

import xtension.workitems.GenerateNotificationWorkItemHandler;
import xtension.workitems.IntegrationWorkItemHandler;
import xtension.workitems.SendMailWorkItemHandler;
import xtension.workitems.UpdateApprovalStatusWorkItemHandler;

import bitronix.tm.TransactionManagerServices;

import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.NotificationDaoHelper;
import com.duggan.workflow.server.dao.model.TaskDelegation;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.email.EmailServiceHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.ApproverAction;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.Notification;
import com.duggan.workflow.shared.model.NotificationType;

/**
 * 
 * @author duggan
 * 
 */
class BPMSessionManager {

	private TaskService service;

	private static final ThreadLocal<Map<Long, StatefulKnowledgeSession>> sessionStore = new ThreadLocal<>();

	private static final ThreadLocal<LocalTaskService> ltsStore = new ThreadLocal<>();
	private static final ThreadLocal<GenericHTWorkItemHandler> htHandler = new ThreadLocal<>();
	private static final ThreadLocal<NotificationTaskEventListener> eventListener = new ThreadLocal<>();

	Boolean autoLoad = true;

	public BPMSessionManager() {

		service = new TaskService(DB.getEntityManagerFactory(),
				SystemEventListenerFactory.getSystemEventListener(),
				new DefaultEscalatedDeadlineHandler(EmailServiceHelper.getProperties()));
		// service.

	}

	// processId - KnowledgeBase Map
	private Map<String, KnowledgeBase> processKnowledgeMap = new HashMap<>();

	private Logger logger = Logger.getLogger(BPMSessionManager.class);

	/**
	 * 
	 * @param processId
	 *            (ID of the process to be initialized-- e.g invoice-process)
	 * @return
	 */
	private StatefulKnowledgeSession getSession(String processId) {

		StatefulKnowledgeSession session = initializeSession(processId);

		logger.debug("GETSESSION RETURNED SESSION: " + session.getId()
				+ session.toString());

		return session;
	}

	private Map<Long, StatefulKnowledgeSession> getSessionStore() {
		Map<Long, StatefulKnowledgeSession> sessionMap = sessionStore.get();
		if (sessionMap == null) {
			synchronized (BPMSessionManager.class) {
				if (sessionStore.get() == null) {
					sessionMap = new HashMap<>();
					sessionStore.set(sessionMap);
				} else {
					sessionMap = sessionStore.get();
				}
			}
		}

		return sessionMap;
	}

	/**
	 * 
	 * @param sessionId
	 *            --Id of the session to be retrieved
	 * @return
	 */
	private StatefulKnowledgeSession getSession(Long sessionId, String processId) {
		StatefulKnowledgeSession session = null;

		if (getSessionStore().get(sessionId) != null) {
			logger.debug("Retrieving from STORE:  session [" + sessionId
					+ "] for process: [" + processId + "]");
			session = getSessionStore().get(sessionId);
		} else {
			session = initializeSession(sessionId, processId);
		}

		logger.debug("GETSESSION RETURNED SESSION: " + session.getId()
				+ session.toString());
		return session;
	}

	private void registerWorkItemHandlers(StatefulKnowledgeSession session) {
		
		// ConsoleLogger
		KnowledgeRuntimeLoggerFactory.newConsoleLogger(session);

		// register work item handlers
		session.getWorkItemManager().registerWorkItemHandler("UpdateLocal",
				new UpdateApprovalStatusWorkItemHandler());
		session.getWorkItemManager().registerWorkItemHandler(
				"GenerateSysNotification",
				new GenerateNotificationWorkItemHandler());
		session.getWorkItemManager().registerWorkItemHandler(
				"ScheduleEmailNotification", new SendMailWorkItemHandler());
		
		//External Systems Integration Work Item 
		session.getWorkItemManager().registerWorkItemHandler("Integration",
				new IntegrationWorkItemHandler());

		EmailWorkItemHandler emailHandler = new EmailWorkItemHandler(
				EmailServiceHelper.getProperty("mail.smtp.host"),
				EmailServiceHelper.getProperty("mail.smtp.port"),
				EmailServiceHelper.getProperty("mail.smtp.user"),
				EmailServiceHelper.getProperty("mail.smtp.password"));
		emailHandler.getConnection().setStartTls(true);
		session.getWorkItemManager().registerWorkItemHandler("Email",
				emailHandler);

		LocalTaskService taskClient = getTaskClient();
		taskClient.connect();
		assert taskClient != null;

		GenericHTWorkItemHandler taskHandler = new LocalHTWorkItemHandler(
				taskClient, session);
		htHandler.set(taskHandler);
		// Only handle events related to the session that started the task being
		// completed
		taskHandler.setOwningSessionOnly(true);
		taskHandler.connect();

		logger.debug(Thread.currentThread().toString() + "Session: "
				+ session.getId() + " : " + session.toString()
				+ "; REGISTERED NEW TASK HANDLER: " + taskHandler
				+ " with LocalTaskService : " + taskClient);

		session.getWorkItemManager().registerWorkItemHandler("Human Task",
				taskHandler);
	}

	public boolean isRunning(String processId) {
		return processKnowledgeMap.get(processId) != null;
	}

	/**
	 * Assuming each request starts/requires at most 1 KnowlegdeSession; a
	 * single LocalTaskService Object stored in a thread local should suffice;
	 * <p/>
	 * The assumption is based on the type of requests made
	 * <ul>
	 * <li>Foward for approval (1 doc, 1 process)
	 * <li>Start, Suspend, Resume, Claim, Complete etc > all these are requests
	 * related to a single task & therefore document; meaning one process and
	 * one session.
	 * </ul>
	 * <p/>
	 * This will only change if other processes (e.g. notification processes)
	 * linked to independent KnowledgeBases & therefore processes are started in
	 * code
	 * 
	 * <p/>
	 * Also note the relationship between LocalTaskService and the
	 * StatefulSession <br/>
	 * StatefulSession > WorkItemHandler > LocalTaskService > TaskService
	 * 
	 * @return {@link LocalTaskService}
	 */
	public LocalTaskService getTaskClient() {

		LocalTaskService lts = ltsStore.get();

		if (lts == null) {
			lts = new LocalTaskService(service);
			NotificationTaskEventListener listener = new NotificationTaskEventListener();
			lts.addEventListener(listener);
			ltsStore.set(lts);
			eventListener.set(listener);

			logger.debug(Thread.currentThread().toString()
					+ "INITIALISED NEW LocalTaskService : " + lts.toString());
		}

		logger.debug(Thread.currentThread().toString()
				+ "RETRIEVED LocalTaskService : " + lts.toString());
		
		return lts;
	}

	private KnowledgeBase getKnowledgeBase(String processId, boolean forceReload) {

		logger.debug("Loading knowledgebase for process: " + processId
				+ "; forceReload= " + forceReload);
		KnowledgeBase kbase = processKnowledgeMap.get(processId);
		if (kbase != null && !forceReload) {
			return kbase;
		}

		throw new RuntimeException("Could not find Knowledgebase for process "
				+ processId
				+ ". Kindly report this issue to your administrator.");
	}

	/**
	 * Loads multiple process resource files (BPMN, DRL etc)
	 * within a single KnowledgeBase - and StatefulSession
	 * <p>
	 * At the moment we do not mix guvnor changesets and manual config files upload
	 * <p> 
	 * <p>
	 * @param files
	 * @param types
	 * @param rootProcess
	 */
	public KnowledgeBase loadKnowledge(List<byte[]> files, List<ResourceType> types,
			String rootProcess) {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		int i=0;
		for(byte[] file: files){
			Resource resource = ResourceFactory.newByteArrayResource(file);
			kbuilder.add(resource, types.get(i));
			++i;
		}
		
		if(kbuilder.hasErrors()){
			StringBuffer errors = new StringBuffer();
			for(KnowledgeBuilderError error: kbuilder.getErrors()){
				errors.append(error.getMessage()+"\n");
			}
			if(errors.length()>0){
				throw new RuntimeException(errors.toString());
			}
		}
		
		KnowledgeBase kbase = kbuilder.newKnowledgeBase();
		Collection<org.drools.definition.process.Process> processes = kbase
				.getProcesses();

		for (Process process : processes) {
			processKnowledgeMap.put(process.getId(), kbase);
		}
		
		return kbase;
	}
	
	public KnowledgeBase loadKnowledge(byte[] bytes, String processName) {

		KnowledgeBase kbase = null;

		Resource resourceset = ResourceFactory.newByteArrayResource(bytes);
		KnowledgeAgent kagent = KnowledgeAgentFactory
				.newKnowledgeAgent(processName);
		kagent.applyChangeSet(resourceset);

		kbase = kagent.getKnowledgeBase();
		
		ResourceFactory.getResourceChangeNotifierService().start();
		ResourceFactory.getResourceChangeScannerService().start();

		Collection<org.drools.definition.process.Process> processes = kbase
				.getProcesses();

		for (Process process : processes) {
			processKnowledgeMap.put(process.getId(), kbase);
		}

		return kbase;
	}

	private Environment getEnvironment() {
		Environment env = KnowledgeBaseFactory.newEnvironment();
		env.set(EnvironmentName.ENTITY_MANAGER_FACTORY,
				DB.getEntityManagerFactory());
		env.set(EnvironmentName.TRANSACTION_MANAGER,
				TransactionManagerServices.getTransactionManager());

		return env;
	}

	/**
	 * Creates a StatefulKnowledgeSession
	 * 
	 * @return {@link StatefulKnowledgeSession}
	 * @throws MalformedURLException
	 */
	private StatefulKnowledgeSession initializeSession(String processId) {
		return initializeSession(null, processId);
	}

	private StatefulKnowledgeSession initializeSession(Long sessionId,
			String processId) {
		KnowledgeBase kbase = getKnowledgeBase(processId, false);
		// Initializing a stateful session from JPAKnowledgeService
		Environment env = getEnvironment();

		StatefulKnowledgeSession session = null;

		if (sessionId == null) {
			session = JPAKnowledgeService.newStatefulKnowledgeSession(kbase,
					null, env);
			logger.debug(Thread.currentThread().getName()
					+ ": Created new session [" + session.getId()
					+ "] for process: [" + processId + "]");
		} else {
			logger.debug(Thread.currentThread().getName()
					+ ": Loading previous session [" + sessionId
					+ "] for process: [" + processId + "]");
			session = JPAKnowledgeService.loadStatefulKnowledgeSession(
					sessionId.intValue(), kbase, null, env);

		}

		registerWorkItemHandlers(session);

		// Process Logger - to Provide data for querying process status
		// :- How far a particular approval has gone
		JPAWorkingMemoryDbLogger mlogger = new JPAWorkingMemoryDbLogger(session);

		getSessionStore().put(new Long(session.getId()), session);
		assert getSessionStore().size() > 0;
		return session;
	}

	/**
	 * 
	 * This method disposes the Knowledge session active within the current
	 * thread.
	 * 
	 * <p/>
	 * In addition to disposing the Knowledge Session, it also disposes the
	 * following objects that rely on the session being disposed:
	 * <ul>
	 * <li>HumanTask WorkItemHandler {@linkplain GenericHTWorkItemHandler}
	 * <li>TaskClient - {@link LocalTaskService}
	 * <li>De-registers all EventListeners registered on the {@link TaskService}
	 * (If not deregistered, multiple event listeners including those linked to
	 * disposed sessions will respond to events)
	 * </ul>
	 * 
	 * <p/>
	 * The above should be the first items you look out of if you get the
	 * dreaded Error: <br/>
	 * <b>
	 * "IllegalStateException: Illegal method call. This session was previously disposed"
	 * </b>
	 * 
	 */
	public void disposeSessions() {
		logger.debug(Thread.currentThread().getName()
				+ ": Disposing Sessions..............");
		Map<Long, StatefulKnowledgeSession> sessionz = getSessionStore();

		Set<Long> keys = sessionz.keySet();

		for (Long key : keys) {
			logger.debug(Thread.currentThread().getName()
					+ ": Disposing SessionID [" + key + "] : "
					+ sessionz.get(key).toString());
			sessionz.get(key).dispose();
		}

		if (ltsStore.get() != null) {
			try {
				if (htHandler.get() != null)
					htHandler.get().dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
			ltsStore.get().dispose();// LocalTaskService
			service.removeEventListener(eventListener.get());

			htHandler.set(null);
			ltsStore.set(null);
			eventListener.set(null);
		}

		sessionStore.set(null);
		// clear ltsStore
		ltsStore.set(null);
	}

	public ProcessInstance startProcess(String processId,
			Map<String, Object> parameters) {
		return startProcess(processId, parameters, null);
	}

	public ProcessInstance startProcess(String processId,
			Map<String, Object> parameters, Document summary) {

		StatefulKnowledgeSession session = getSession(processId);

		return startProcess(session, processId, parameters, summary);
	}

	private ProcessInstance startProcess(StatefulKnowledgeSession session,
			String processId, Map<String, Object> parameters, Document summary) {

		logger.debug(">>> Starting process " + processId + "; Doc=" + summary);
		
		ProcessInstance instance = session.createProcessInstance(processId, parameters);
		
		if (summary != null) {
			if(summary.getProcessInstanceId()==null)
				summary.setProcessInstanceId(instance.getId());
			
			summary.setSessionId(new Long(session.getId() + ""));
			logger.debug("## Setting SessionId : " + summary.getSessionId());
			DocumentDaoHelper.save(summary);
		}

		
		session.startProcessInstance(instance.getId());
		
		return instance;
	}

	public void execute(long taskId, String userId, Actions action,
			Map<String, Object> values) {

		Task task = getTaskClient().getTask(taskId);

		// initialize session - Each HT execution must run within an active
		// StatefulKnowledgeSession
		getSession(new Long(task.getTaskData().getProcessSessionId()), task
				.getTaskData().getProcessId());

		switch (action) {
		case CLAIM:
			getTaskClient().claim(taskId, userId);
			break;
		case COMPLETE:
			complete(taskId, userId, values);
			break;
		case DELEGATE:
			delegate(taskId, userId, (String)values.get("targetUserId"));
			break;
		case FORWARD:
			// get().getTaskClient().forward(taskId, userId, targetEntityId)
			break;
		case RESUME:
			getTaskClient().resume(taskId, userId);
			break;
		case REVOKE:
			// TODO: investigate what revoke actually does
			getTaskClient().release(taskId, userId);
			break;
		case START:
			getTaskClient().start(taskId, userId);
			break;
		case STOP:
			getTaskClient().stop(taskId, userId);
			break;
		case SUSPEND:
			getTaskClient().suspend(taskId, userId);
			break;
		
		}

	}

	private void delegate(long taskId, String userId, String targetUserId) {
		getTaskClient().delegate(taskId, userId, targetUserId);
		TaskDelegation delegation = new TaskDelegation(null, taskId, userId, targetUserId);
		DB.getDocumentDao().save(delegation);
		
	}

	private boolean complete(long taskId, String userId,
			Map<String, Object> values) {

		Map<String, Object> content = JBPMHelper.getMappedData(getTaskClient()
				.getTask(taskId));
		content.putAll(values);
		// completing tasks is a single individuals responsibility
		// Notifications & Emails sent after task completion must reflect this
		content.put("ActorId", SessionHelper.getCurrentUser().getUserId());
		// sessionManager.getTaskService().completeWithResults(taskId, userId,
		// content);
		getTaskClient().completeWithResults(taskId, userId, content);
		// sessionManager.getTaskService().complete(taskId, userId, null);
		return true;
	}

	/**
	 * 
	 * Class to generate automatic notifications for Human Tasks
	 * 
	 * @author duggan
	 * 
	 */
	class NotificationTaskEventListener implements TaskEventListener {

		@Override
		public void taskStopped(TaskUserEvent event) {
		}

		@Override
		public void taskStarted(TaskUserEvent event) {
		}

		@Override
		public void taskSkipped(TaskUserEvent event) {
		}

		@Override
		public void taskReleased(TaskUserEvent event) {
		}

		@Override
		public void taskForwarded(TaskUserEvent event) {
		}

		@Override
		public void taskFailed(TaskUserEvent event) {
		}

		/**
		 * SEND BEFORE TASK NOTIFICATIONS
		 * 
		 * TODO: Note: If an exception occurs here, the error if silently
		 * handled by the caller - meaning its not propagated back to
		 * startProcess. The caller however generates the following exception
		 * org.jbpm.workflow.instance.WorkflowRuntimeException:
		 * [invoice-approval:55 - HOD Approval:2] -- null ............. caused
		 * by java.lang.NullPointerException at
		 * org.jbpm.process.workitem.wsht.GenericHTWorkItemHandler
		 * .executeWorkItem(GenericHTWorkItemHandler.java:184) ..............
		 * 
		 * This method therefore must handle its own exceptions internally and
		 * mark the Trx for rollback in case an exception occurs to reverse the
		 * whole transaction
		 * 
		 * An Exception Log should also be inserted into ErroLog table to assist
		 * in debugging
		 */
		@Override
		public void taskCreated(TaskUserEvent event) {
			// session.startProcess(processId, parameters)

			try {
				Task task = getTaskClient().getTask(event.getTaskId());

				Map<String, Object> taskData = JBPMHelper.getMappedData(task);

				Map<String, Object> values = null;
				Map<String, Object> newValues = new HashMap<>();

				Long documentId = Long.parseLong(taskData.get("documentId")
						.toString());
				Document doc = DocumentDaoHelper.getDocument(documentId);
				Long sessionId = doc.getSessionId();

				if (sessionId == null) {
					throw new IllegalArgumentException(
							"SessionId must not be null!!!");
				}

				StatefulKnowledgeSession session = getSession(sessionId, task
						.getTaskData().getProcessId());

				org.jbpm.process.instance.ProcessInstance instance = (org.jbpm.process.instance.ProcessInstance) session
						.getProcessInstance(task.getTaskData()
								.getProcessInstanceId());
				VariableScopeInstance variableScope = (VariableScopeInstance) instance
						.getContextInstance(VariableScope.VARIABLE_SCOPE);
				values = variableScope.getVariables();

				Set<String> keys = values.keySet();

				for (String key : keys) {
					Object value = values.get(key);

					key = key.substring(0, 1).toUpperCase() + key.substring(1);
					newValues.put(key, value);
				}

				newValues.put("GroupId", taskData.get("GroupId"));
				newValues.put("ActorId", taskData.get("ActorId"));
				newValues.put("Priority", taskData.get("Priority"));
				if (newValues.get("Priority") == null)
					newValues.put("Priority", values.get("Priority"));

				String processId = "beforetask-notification";

				startProcess(processId, newValues);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		/**
		 * SEND AFTER TASK NOTIFICATIONS
		 * 
		 * TODO: Note: If an exception occurs here, the error if silently
		 * handled by the caller - meaning its not propagated back to
		 * startProcess. The caller however fails with the following exception
		 * org.jbpm.workflow.instance.WorkflowRuntimeException:
		 * [invoice-approval:55 - HOD Approval:2] -- null ............. caused
		 * by java.lang.NullPointerException at
		 * org.jbpm.process.workitem.wsht.GenericHTWorkItemHandler
		 * .executeWorkItem(GenericHTWorkItemHandler.java:184) ..............
		 * 
		 * This method therefore must handle its own exceptions internally and
		 * mark the Trx for rollback in case an exception occurs to reverse the
		 * whole transaction
		 * 
		 * An Exception Log should also be inserted into ErroLog table to assist
		 * in debugging
		 */
		@Override
		public void taskCompleted(TaskUserEvent event) {

			logger.debug(Thread.currentThread()
					+ "############ COMPLETING TASK - event TaskID= "
					+ event.getTaskId() + " :: SessionId= "
					+ event.getSessionId());

			Long sessionId = new Long(event.getSessionId());

			try {

				Task task = getTaskClient().getTask(event.getTaskId());
				Long outputId = task.getTaskData().getOutputContentId();

				Map<String, Object> taskData = JBPMHelper
						.getMappedDataByContentId(outputId);

				Map<String, Object> newValues = new HashMap<>();

				Long documentId = Long.parseLong(taskData.get("documentId")
						.toString());
				Document doc = DocumentDaoHelper.getDocument(documentId);
				Long processInstanceId = doc.getProcessInstanceId();

				StatefulKnowledgeSession session = getSession(sessionId, task
						.getTaskData().getProcessId());
				Map<String, Object> values = new HashMap<>();
				org.jbpm.process.instance.ProcessInstance instance = (org.jbpm.process.instance.ProcessInstance) session
						.getProcessInstance(processInstanceId);

				// Task Data
				Set<String> keys = taskData.keySet();
				for (String key : keys) {
					Object value = taskData.get(key);
					if (!key.equals("isApproved"))
						key = key.substring(0, 1).toUpperCase()
								+ key.substring(1);

					newValues.put(key, value);
//					System.err.println("###>>>>Task Data Key :: " + key + " = "
//							+ value);
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

					if (!key.equals("isApproved"))
						key = key.substring(0, 1).toUpperCase()
								+ key.substring(1);

					if (newValues.get(key) == null) {
						newValues.put(key, value);
//						System.err.println("###>>>>Process Variable Key :: "
//								+ key + " = " + value);
					}
				}

				Object ownerId = newValues.get("OwnerId");
				if (ownerId == null) {
					if (doc != null) {
						ownerId = doc.getOwner().getUserId();
						newValues.put("OwnerId", ownerId.toString());
					}
				}

				String processId = "aftertask-notification";

				//
				startProcess(processId, newValues);

			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

		@Override
		public void taskClaimed(TaskUserEvent event) {
		}
	}

	public Process getProcess(String processId) {
		KnowledgeBase kbase = getKnowledgeBase(processId, false);

		if (kbase != null) {
			return kbase.getProcess(processId);
		}

		return null;
	}

	/**
	 * Close knowledge Base
	 * 
	 * @param processId
	 */
	public void unloadKnowledgeBase(String processId) {
		KnowledgeBase kbase = processKnowledgeMap.get(processId);
		processKnowledgeMap.remove(processId);

		// other processes loaded with the process are still active and
		// referencing the kbase

		Set<String> processes = processKnowledgeMap.keySet();

		List<String> processesToRemove = new ArrayList<>();
		for (String process : processes) {
			if (kbase.equals(processKnowledgeMap.get(process))) {
				processesToRemove.add(process);
			}
		}

		for (String key : processesToRemove) {
			processKnowledgeMap.remove(key);
		}
	}

}
