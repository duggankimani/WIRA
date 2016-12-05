package com.duggan.workflow.server.helper.jbpm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.drools.compiler.kie.builder.impl.KieServicesImpl;
import org.jbpm.runtime.manager.impl.SimpleRegisterableItemsFactory;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.EnvironmentName;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskLifeCycleEventListener;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.Task;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.utils.KieHelper;

import xtension.workitems.FormValidationWorkItemHandler;
import xtension.workitems.GenerateNotificationWorkItemHandler;
import xtension.workitems.GenerateOutputDocWorkItemHandler;
import xtension.workitems.IntegrationWorkItemHandler;
import xtension.workitems.SMSWorkItemHandler;
import xtension.workitems.SendMailWorkItemHandler;
import xtension.workitems.UpdateActivityStatus;
import xtension.workitems.UpdateApprovalStatusWorkItemHandler;
import xtension.workitems.WiseDigitsDocumentIntegration;
import bitronix.tm.TransactionManagerServices;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.model.TaskDelegation;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.callback.DBUserGroupCallbackImpl;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Document;

/**
 * Key resources:
 * 
 * https://docs.jboss.org/jbpm/release/6.3.0.Final/jbpm-docs/html/ch07.html#jBPMTaskServiceAPI
 * https://docs.jboss.org/jbpm/v6.3/userguide/ch05.html#d0e1810
 * https://docs.jboss.org/jbpm/release/6.4.0.Final/jbpm-docs/html/ch17.html#_rest_serialization_jaxb_or_json
 * https://docs.jboss.org/jbpm/v6.0/userguide/wb.WorkbenchIntegration.html
 * https://docs.jboss.org/jbpm/v6.3/userguide/ch17.html
 * https://docs.jboss.org/jbpm/release/6.5.0.Final/jbpm-docs/html/ch25.html#jBPMReleaseNotes640
 * 
 * @author duggan
 *
 */
public class BPM6SessionManager implements WiraSessionManager {

	
		
	RuntimeManager manager;
	KieBase kbase = null;

	Logger logger = Logger.getLogger(getClass());

	List<Class<? extends ProcessEventListener>> processEventListeners = new ArrayList<>();
	List<Class<? extends TaskLifeCycleEventListener>> taskEventListeners = new ArrayList<>();

	public BPM6SessionManager() {
		this(new ArrayList<Class<? extends ProcessEventListener>>(),
				new ArrayList<Class<? extends TaskLifeCycleEventListener>>());
	}

	public BPM6SessionManager(
			List<Class<? extends ProcessEventListener>> processEventListeners,
			List<Class<? extends TaskLifeCycleEventListener>> taskEventListeners) {

		if (processEventListeners != null)
			this.processEventListeners = processEventListeners;

		if (taskEventListeners != null)
			this.taskEventListeners = taskEventListeners;

		RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory
				.get().newDefaultBuilder();

		// Add Assets
		addAssets(builder);

		RuntimeEnvironment environment = builder
				.entityManagerFactory(DB.getEntityManagerFactory())
				.addEnvironmentEntry(EnvironmentName.TRANSACTION_MANAGER, 
						TransactionManagerServices.getTransactionManager())
				// .knowledgeBase(getKieBase()) - Replaced with add resources
				// above
				.userGroupCallback(new DBUserGroupCallbackImpl()).get();

		// Register WorkItems
		SimpleRegisterableItemsFactory registerableItemsFactory = (SimpleRegisterableItemsFactory) environment
				.getRegisterableItemsFactory();
		registerWorkItems(registerableItemsFactory);

		manager = RuntimeManagerFactory.Factory.get()
				.newSingletonRuntimeManager(environment);

	}

	@Override
	public void addProcessEventListener(
			Class<? extends ProcessEventListener> listener) {
		if (!processEventListeners.contains(listener)) {
			processEventListeners.add(listener);
		}
	}

	@Override
	public void addTaskListener(
			Class<? extends TaskLifeCycleEventListener> listener) {
		if (!taskEventListeners.contains(listener)) {
			taskEventListeners.add(listener);
		}
	}

	private void registerWorkItems(
			SimpleRegisterableItemsFactory registerableItemsFactory) {
		registerableItemsFactory
				.addTaskListener(NotificationsTaskEventListener.class);
		for (Class<? extends TaskLifeCycleEventListener> clazz : taskEventListeners) {
			registerableItemsFactory.addTaskListener(clazz);
		}

		for (Class<? extends ProcessEventListener> clazz : processEventListeners) {
			registerableItemsFactory.addProcessListener(clazz);
		}

		// registerableItemsFactory.add;
		// register work item handlers
		registerableItemsFactory.addWorkItemHandler("UpdateLocal",
				UpdateApprovalStatusWorkItemHandler.class);

		registerableItemsFactory.addWorkItemHandler("GenerateSysNotification",
				GenerateNotificationWorkItemHandler.class);
		registerableItemsFactory.addWorkItemHandler(
				"ScheduleEmailNotification", SendMailWorkItemHandler.class);
		registerableItemsFactory.addWorkItemHandler(
				"WiseDigitsDocumentIntegration",
				WiseDigitsDocumentIntegration.class);

		// External Systems Integration Work Item
		registerableItemsFactory.addWorkItemHandler(
				"RestfulCommandIntegration", IntegrationWorkItemHandler.class);

		registerableItemsFactory.addWorkItemHandler("AutoValidateApplication",
				FormValidationWorkItemHandler.class);

		registerableItemsFactory.addWorkItemHandler("SendSMS",
				SMSWorkItemHandler.class);

		registerableItemsFactory.addWorkItemHandler("UpdateActivityStatus",
				UpdateActivityStatus.class);

		registerableItemsFactory.addWorkItemHandler("GenerateOutputDoc",
				GenerateOutputDocWorkItemHandler.class);

		// EmailWorkItemHandler emailHandler = new EmailWorkItemHandler(
		// EmailServiceHelper.getProperty("mail.smtp.host"),
		// EmailServiceHelper.getProperty("mail.smtp.port"),
		// EmailServiceHelper.getProperty("mail.smtp.user"),
		// EmailServiceHelper.getProperty("mail.smtp.password"));
		// emailHandler.getConnection().setStartTls(true);
		// registerableItemsFactory.addWorkItemHandler("Email",
		// emailHandler);

	}

	public KieSession getSession() {
		KieSession session = getRuntime().getKieSession();
		//new JPAWorkingMemoryDbLogger(session);
		// session.addEventListener(new NotificationsTaskEventListener());
		return session;
	}

	/**
	 * TODO: Per Process Session
	 * 
	 * @param id
	 * @param processId
	 * @return
	 */
	public KieSession getSession(Long id, String processId) {
		return getSession();
	}

	@Override
	public TaskService getTaskClient() {
		TaskService service = manager.getRuntimeEngine(EmptyContext.get())
				.getTaskService();
		return service;
	}

	/**
	 * Automatically called on Trx.commit/ rollback?
	 */
	@Override
	public void disposeSessions() {
		// and last dispose the runtime engine
		manager.disposeRuntimeEngine(getRuntime());
	}

	public RuntimeEngine getRuntime() {

		// then get RuntimeEngine out of manager - using empty context as
		// singleton does not keep track
		// of runtime engine as there is only one
		RuntimeEngine runtime = manager.getRuntimeEngine(EmptyContext.get());
		return runtime;
	}

	@Override
	public ProcessInstance startProcess(String processId,
			Map<String, Object> initialParams, Document doc) {

		KieSession session = getRuntime().getKieSession();
		ProcessInstance instance = session.createProcessInstance(processId,
				initialParams);
		doc.setProcessInstanceId(instance.getId());

		if (doc != null) {
			if (doc.getProcessInstanceId() == null)
				doc.setProcessInstanceId(instance.getId());

			doc.setSessionId(new Long(session.getId() + ""));
			logger.debug("## Setting SessionId : " + doc.getSessionId());
			DocumentDaoHelper.save(doc);
		}

		return session.startProcessInstance(instance.getId());
	}

	@Override
	public org.kie.api.definition.process.Process getProcess(String processId) {
		return kbase.getProcess(processId);
	}

	@Override
	public org.kie.api.definition.process.Process getProcess(
			long processInstanceId) {
		return getSession().getProcessInstance(processInstanceId).getProcess();
	}

	public void addAssets(RuntimeEnvironmentBuilder builder) {
		try {
			Resource resource = ResourceFactory.newByteArrayResource(IOUtils
					.toByteArray(getClass().getClassLoader()
							.getResourceAsStream(
									"ke.go.kna.KNAEditorialWorkflow.bpmn2")));
			builder.addAsset(resource, ResourceType.BPMN2);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Test Implementation
	 * 
	 * @return
	 */
	private KieBase getKieBase() {
		if (kbase == null) {
			KieHelper kieHelper = new KieHelper();
			try {
				Resource resource = ResourceFactory
						.newByteArrayResource(IOUtils
								.toByteArray(getClass()
										.getClassLoader()
										.getResourceAsStream(
												"ke.go.kna.KNAEditorialWorkflow.bpmn2")));
				kieHelper.addResource(resource, ResourceType.BPMN2);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			kbase = kieHelper.build();
		}

		// int i = 0;

		return kbase;
	}

	/**
	 * Loads multiple process resource files (BPMN, DRL etc) within a single
	 * KieBase - and StatefulSession
	 * <p>
	 * At the moment we do not mix guvnor changesets and manual config files
	 * upload
	 * <p>
	 * <p>
	 * 
	 * @param files
	 * @param types
	 * @param rootProcess
	 */
	public void loadKnowledge(List<byte[]> files, List<ResourceType> types,
			String rootProcess) {

		KieHelper kieHelper = new KieHelper();
		int i = 0;
		for (byte[] file : files) {
			Resource resource = ResourceFactory.newByteArrayResource(file);
			kieHelper.addResource(resource, types.get(i));
			++i;
		}

		KieServices kieServices = KieServices.Factory.get();
		kieServices.newKieFileSystem();

		kbase = kieHelper.build();
	}

	@Override
	public void execute(long taskId, String userId, Actions action,
			Map<String, Object> values) {
		Task task = getTaskClient().getTaskById(taskId);

		// initialize session - Each HT execution must run within an active
		// KieSession
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
			delegate(taskId, userId, (String) values.get("targetUserId"));
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

	private boolean complete(long taskId, String userId,
			Map<String, Object> values) {
		// completing tasks is a single individuals responsibility
		// Notifications & Emails sent after task completion must reflect this

		Task task = getTaskClient().getTaskById(taskId);
		if (task.getTaskData().getStatus() == Status.Created
				|| task.getTaskData().getStatus() == Status.Ready) {
			// start
			getTaskClient().start(taskId, userId);
		}

		values.put("ActorId", SessionHelper.getCurrentUser().getUserId());
		values.put("actorId", SessionHelper.getCurrentUser().getUserId());
		getTaskClient().complete(taskId, userId, values);
		return true;
	}

	private void delegate(long taskId, String userId, String targetUserId) {
		getTaskClient().delegate(taskId, userId, targetUserId);
		TaskDelegation delegation = new TaskDelegation(null, taskId, userId,
				targetUserId);
		DB.getDocumentDao().save(delegation);

	}

	@Override
	public void loadKnowledge(byte[] bytes, String processName) {

	}

	@Override
	public boolean isRunning(String processId) {
		return true;// kbase.getProcess(processId) != null;
	}

	@Override
	public void unloadKnowledgeBase(String processId) {
		// kbase.removeProcess(processId);
	}

	@Override
	public void upgradeProcessInstance(long processInstanceId, String processId) {

		// WorkflowProcessInstanceUpgrader.upgradeProcessInstance(manager,
		// processInstanceId, processId, new HashMap<String,Long>());
	}

	@Override
	public org.jbpm.process.instance.ProcessInstance getProcessInstance(
			long processInstanceId) {
		return (org.jbpm.process.instance.ProcessInstance) getSession()
				.getProcessInstance(processInstanceId);
	}

}
