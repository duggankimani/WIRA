package com.duggan.workflow.server.helper.jbpm;

import static com.duggan.workflow.server.helper.dao.DocumentDaoHelper.getDocument;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.SystemEventListenerFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.process.Node;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.KnowledgeRuntime;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.executor.commands.SendMailCommand;
import org.jbpm.process.audit.JPAProcessInstanceDbLog;
import org.jbpm.process.audit.JPAWorkingMemoryDbLogger;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.core.Process;
import org.jbpm.process.workitem.email.EmailWorkItemHandler;
import org.jbpm.process.workitem.wsht.GenericHTWorkItemHandler;
import org.jbpm.process.workitem.wsht.LocalHTWorkItemHandler;
import org.jbpm.task.AccessType;
import org.jbpm.task.Comment;
import org.jbpm.task.Deadlines;
import org.jbpm.task.Delegation;
import org.jbpm.task.I18NText;
import org.jbpm.task.OrganizationalEntity;
import org.jbpm.task.PeopleAssignments;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.TaskData;
import org.jbpm.task.User;
import org.jbpm.task.event.TaskEventKey;
import org.jbpm.task.event.entity.TaskCompletedEvent;
import org.jbpm.task.event.entity.TaskFailedEvent;
import org.jbpm.task.event.entity.TaskSkippedEvent;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.local.LocalTaskService;
import org.jbpm.task.utils.ContentMarshallerHelper;
import org.jbpm.workflow.core.impl.WorkflowProcessImpl;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.jbpm.workflow.core.node.StartNode;

import xtension.workitems.GenerateNotificationWorkItemHandler;
import xtension.workitems.SendMailWorkItemHandler;
import xtension.workitems.UpdateApprovalStatusWorkItemHandler;
import bitronix.tm.TransactionManagerServices;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.DocumentDaoImpl;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.email.EmailServiceHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTAccessType;
import com.duggan.workflow.shared.model.HTComment;
import com.duggan.workflow.shared.model.HTData;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.model.NodeDetail;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.test.LDAPAuth;

/**
 * This is a Helper Class for all JBPM associated requests.
 * It provides utility methods to execute tasks and retrieve task information from the JBPM environment
 * 
 * @author duggan
 *
 */
public class JBPMHelper implements Closeable{

	private LocalTaskService service;
	
	private KnowledgeBase kbase;
	private StatefulKnowledgeSession session;
	JPAWorkingMemoryDbLogger mlogger;
	private static JBPMHelper helper;
	
	private JBPMHelper(){
		try{
	        // By Setting the jbpm.usergroup.callback property with the call
	        // back class full name, task service will use this to validate the
	        // user/group exists and its permissions are ok.
	        System.setProperty("jbpm.usergroup.callback",
	                "org.jbpm.task.identity.LDAPUserGroupCallbackImpl");
	        
			session = this.initializeSession();
			//ConsoleLogger
			KnowledgeRuntimeLoggerFactory.newConsoleLogger(session);
			
			//register work item handlers
			session.getWorkItemManager().registerWorkItemHandler("UpdateLocal", new UpdateApprovalStatusWorkItemHandler());
			session.getWorkItemManager().registerWorkItemHandler("GenerateSysNotification",new GenerateNotificationWorkItemHandler());
			session.getWorkItemManager().registerWorkItemHandler("ScheduleEmailNotification",new SendMailWorkItemHandler());
			
			EmailWorkItemHandler emailHandler = new EmailWorkItemHandler(
					EmailServiceHelper.getProperty("smtp.host"),
					EmailServiceHelper.getProperty("smtp.port"),
					EmailServiceHelper.getProperty("smtp.user"),
					EmailServiceHelper.getProperty("smtp.password"));
			emailHandler.getConnection().setStartTls(true);
			session.getWorkItemManager().registerWorkItemHandler("Email", emailHandler);
			
			GenericHTWorkItemHandler htHandler = this.createTaskHandler(session);
			session.getWorkItemManager().registerWorkItemHandler("Human Task", htHandler);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	private GenericHTWorkItemHandler createTaskHandler(
			StatefulKnowledgeSession ksession) {
			
    	TaskService ts = new TaskService(DB.getEntityManagerFactory(),
    			SystemEventListenerFactory.getSystemEventListener());
    	
    	LocalTaskService taskService = new LocalTaskService(ts);
    	
    	LocalHTWorkItemHandler taskHandler = new LocalHTWorkItemHandler(taskService, ksession);//new BPMHTWorkItemHandler(taskService, ksession);
    	this.service = taskService;
    	return taskHandler;
	}

	/**
	 * Creates a StatefulKnowledgeSession
	 * 
	 * @return {@link StatefulKnowledgeSession}
	 */
	private StatefulKnowledgeSession initializeSession() {
		
		if(session!=null)
			return (StatefulKnowledgeSession) session;
		
		
    	KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    	//builder.add(new ClassPathResource("invoice-approval.bpmn"), ResourceType.BPMN2);
    	builder.add(new ClassPathResource("invoice-approval.bpmn"), ResourceType.BPMN2);
    	builder.add(new ClassPathResource("send-email.bpmn"), ResourceType.BPMN2);
    	builder.add(new ClassPathResource("beforetask-notification.bpmn"), ResourceType.BPMN2);
    	builder.add(new ClassPathResource("aftertask-notification.bpmn"), ResourceType.BPMN2);
    	
    	kbase = builder.newKnowledgeBase();
    	
    	//Initializing a stateful session from JPAKnowledgeService    	
    	Environment env = KnowledgeBaseFactory.newEnvironment();
    	env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, DB.getEntityManagerFactory());
    	env.set(EnvironmentName.TRANSACTION_MANAGER, TransactionManagerServices.getTransactionManager());
    	
    	session = JPAKnowledgeService.newStatefulKnowledgeSession(kbase, null, env);
    	    	
    	//Process Logger - to Provide data for querying process status
    	//:- How far a particular approval has gone
    	mlogger = new JPAWorkingMemoryDbLogger(session);
		
		return session;
	}
	
	//not thread safe
	public static JBPMHelper get(){
		if(helper==null){
			helper = new JBPMHelper();
		}
		
		return helper;
	}

	@Override
	public void close() throws IOException {
		
	}

	/**
	 * This method clears the runtime environment when the application is shutdown
	 * 
	 */
	public static void destroy() {
		JBPMHelper h = JBPMHelper.get();
		
		if(h!=null){
			try {
				h.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method creates initiates an approval process for a document. 
	 * 
	 * @param summary This is the document to be submitted for approval
	 */
	public void createApprovalRequest(Document summary){
		
		Map<String, Object> initialParams = new HashMap<String, Object>();		
		//initialParams.put("user_self_evaluation", "calcacuervo");
		initialParams.put("subject", summary.getSubject());//Human Tasks need this
		initialParams.put("description", summary.getDescription());//Human Tasks need this
		initialParams.put("documentId", summary.getId().toString());
		initialParams.put("ownerId", SessionHelper.getCurrentUser()==null? "System": SessionHelper.getCurrentUser().getId());
		initialParams.put("value", null);
		initialParams.put("priority", summary.getPriority());
		
		ProcessInstance processInstance = session.startProcess("invoice-approval", initialParams);
		//processInstance.getId(); - Use this to link a document with a process instance + later for history generation
		summary.setProcessInstanceId(processInstance.getId());
		DocumentDaoHelper.save(summary);
		
		assert (ProcessInstance.STATE_ACTIVE ==processInstance.getState());
		
	}
	
	/**
	 * Count the number of tasks - completed/ or new
	 * 
	 * @param userId
	 * @param counts
	 */
	public void getCount(String userId, HashMap<TaskType, Integer> counts){	

		List<UserGroup> groups = LoginHelper.getHelper().getGroupsForUser(userId);
		List<String> groupIds = new ArrayList<>();
		for(UserGroup group: groups){
			groupIds.add(group.getName());
		}
		
		Long count = (Long)DB.getEntityManager().createNamedQuery("TasksAssignedCountAsPotentialOwnerByStatusWithGroups")
				.setParameter("userId", userId)
				.setParameter("groupIds", groupIds)
				.setParameter("language", "en-UK")
				.setParameter("status", getStatusesForTaskType(TaskType.APPROVALREQUESTNEW))
				.getSingleResult();
		counts.put(TaskType.APPROVALREQUESTNEW, count.intValue());
		
		/**
		 * If John & James share the Role HOD_DEV
		 * John Claims, Starts and completes a task, should
		 * that task be presented to James as one of his completed tasks?
		 * This mechanism creates the posibility of this scenario happening
		 * TODO: Test the query with two users sharing a role
		 */
		Long count2 = (Long)DB.getEntityManager().createNamedQuery("TasksOwnedCount")
		.setParameter("userId", userId)
		.setParameter("language", "en-UK")
		.setParameter("status", Status.Completed)
		.getSingleResult();		
		counts.put(TaskType.APPROVALREQUESTDONE, count2.intValue());

	}
	
	public List<Status> getStatusesForTaskType(TaskType type){
		
		List<Status> statuses = new ArrayList<>();
		
		switch (type) {
		case APPROVALREQUESTNEW:
			statuses = Arrays.asList(Status.Created,
					Status.InProgress,
					//Status.Error,
					//Status.Exited,
					//Status.Failed,
					//Status.Obsolete,
					Status.Ready,
					Status.Reserved,
					Status.Suspended
					);
			break;
		case APPROVALREQUESTDONE:
			statuses = Arrays.asList(Status.Completed);
			break;			
		}
		return statuses;
	}
	
	
	public List<HTSummary>  getTasksForUser(String userId, Long processInstanceId){
		List<UserGroup> groups = LoginHelper.getHelper().getGroupsForUser(userId);
		List<String> groupIds = new ArrayList<>();
		for(UserGroup group: groups){
			groupIds.add(group.getName());
		}
		
		@SuppressWarnings("unchecked")
		List<TaskSummary> ts = DB.getEntityManager().createNamedQuery("TasksOwnedBySubject")
		.setParameter("userId", userId)
		.setParameter("language", "en-UK")
		.setParameter("groupIds", groupIds)
		.setParameter("processInstanceId", processInstanceId)
		.getResultList();
		
		return translateSummaries(ts);
	}
	/**
	 * 
	 * This method retrieves all tasks assigned to a user.
	 * <p>
	 * @param userId This is the username of the user whose tasks are to be retrieved
	 * @param type 
	 * @return List This is a list of human task summaries retrieved for the user
	 * 
	 */
	public List<HTSummary> getTasksForUser(String userId, TaskType type){
		
		if(!LoginHelper.get().getLdapQuery().existsUser(userId)){
			throw new RuntimeException("User "+userId+" Unknown!!");
		}
				
		if(type==null){
			type = TaskType.APPROVALREQUESTNEW;
		}
		
		List<TaskSummary> ts = new ArrayList<>();
		
		switch (type) {
		case APPROVALREQUESTDONE:
			//approvals - show only items I have approved
			ts = this.service.getTasksOwned(userId, Arrays.asList(Status.Completed), "en-UK");
			
			break;
		case APPROVALREQUESTNEW:
			ts = this.service.getTasksAssignedAsPotentialOwner(userId, "en-UK");
			break;

		default:
			break;
		}
		
		return translateSummaries(ts);
		
	}
	
	public List<HTSummary> translateSummaries(List<TaskSummary> ts){
		
		List<HTSummary> tasks = new ArrayList<>();
		for(TaskSummary summary : ts){
			
			HTSummary task = new HTSummary(summary.getId());
			Task master_task = this.service.getTask(summary.getId());
			
			copy(task, master_task);
			tasks.add(task);
			//call for test
			//getTask(userId, summary.getId());
			
		}
		
		return tasks;
	}
	
	private void copy(HTSummary task, Task master_task) {

		Map<String, Object> content = getMappedData(master_task);
		Document doc = getDocument(content);
					
		task.setCreated(master_task.getTaskData().getCreatedOn());
		task.setDateDue(master_task.getTaskData().getCreatedOn());			
		task.setSubject(doc.getSubject());
		task.setDescription(doc.getDescription());
		task.setPriority(doc.getPriority());
		task.setDocumentRef(doc.getId());
		//task.setTaskName(summary.getName());		//TODO: LOOK INTO JBPM TASKSUMMARY / TASK USAGES
		//task.setTaskName(master_task.getNames().);
		task.setTaskName(doc.getSubject());
		Status status = master_task.getTaskData().getStatus();
		task.setStatus(HTStatus.valueOf(status.name().toUpperCase()));
	}


	/**
	 * This method retrieves the full task object, which provides more comprehensive details for a task 
	 * 
	 * @param taskId This is the task Id of the task to be retrieved
	 * @return HTask Human Task DTO object retrieved
	 */
	public HTask getTask(long taskId){

		//Human Task
		HTask myTask = new HTask(); 
		
		Task task = this.service.getTask(taskId);
		
		List<I18NText> descriptions =task.getDescriptions();
		myTask.setDescription(descriptions.get(0).getText());
		
		List<I18NText> names = task.getNames();
		myTask.setName(names.get(0).getText());
		
		List<I18NText> subjects = task.getSubjects();//translations
		myTask.setSubject(subjects.get(0).getText());
		
		int priority = task.getPriority();
		myTask.setPriority(priority);
		
		Long id = task.getId();
		myTask.setId(id);
		
		int version = task.getVersion();
		myTask.setVersion(version);
		
		Deadlines deadlines = task.getDeadlines();
		//deadlines.getEndDeadlines();		
		Delegation delegation = task.getDelegation();		
			
		PeopleAssignments assignments = task.getPeopleAssignments();
		//User user = assignments.getTaskInitiator();
		
		List<OrganizationalEntity> entities = assignments.getRecipients();
		

		//HT DATA
		HTData data = new HTData();
		
		//TASK DATA
		TaskData taskData = task.getTaskData();
		String docType = taskData.getDocumentType();
		data.setDocType(docType);
		
		long workId = taskData.getWorkItemId();
		data.setWorkId(workId);
		
		//owner
		User actualOwner= taskData.getActualOwner();
		if(actualOwner!=null){
			HTUser taskOwner = new HTUser(actualOwner.getId());
			data.setActualOwner(taskOwner);
		}
				
		//comments
		List<Comment> comments = taskData.getComments();
		if(comments!=null)
		for(Comment comment: comments){
			HTComment htComment = new HTComment();
			htComment.setAddedAt(comment.getAddedAt());
			htComment.setId(comment.getId());
			htComment.setText(comment.getText());
			
			User addedBy = comment.getAddedBy();
			if(addedBy!=null)
				htComment.setAddedBy(new HTUser(addedBy.getId()));
			
		}
		
		Date completedOn = taskData.getCompletedOn();
		data.setCompletedOn(completedOn);
		
		User createdBy = taskData.getCreatedBy();
		if(createdBy!=null){
			data.setCreatedBy(new HTUser(createdBy.getId()));
		}
	
		AccessType accessType = taskData.getDocumentAccessType();
		
		if(accessType!=null){
			data.setDocAccessType(HTAccessType.valueOf(accessType.name().toUpperCase()));
		}
		
		long contentId = taskData.getDocumentContentId();
		data.setContentId(contentId);
		
		Status taskDataStatus = taskData.getStatus();
		if(taskDataStatus!=null){
			data.setStatus(HTStatus.valueOf(taskDataStatus.name().toUpperCase()));
		}
		
		String taskDataOutputType = taskData.getOutputType();
		data.setOutputType(taskDataOutputType);
		
		Date expiryTime = taskData.getExpirationTime();
		data.setExpiryTime(expiryTime);
		
		//AccessType faultAccessType = taskData.getFaultAccessType();
		
		long parentId = taskData.getParentId();
		data.setParentId(parentId);
		
		Status previousStatus = taskData.getPreviousStatus();
		if(previousStatus!=null){
			data.setPreviousStatus(HTStatus.valueOf(previousStatus.name().toUpperCase()));
		}

		myTask.setData(data);
		
		return myTask;
	}
	
	/**
	 * <p>
	 * This method returns the Values passed when the task was initiated
	 * Several methods of retrieving these parameters are offered
	 * online but the use of {@link ContentMarshallerHelper} is what worked in this instance
	 * 
	 * <p>
	 * The use of {@link ObjectInputStream} to read the bytes failed with an {@link OptionalDataException}; trying
	 * to fix this did not work.
	 * 
	 * <p>
	 * Further, if the Content value of the JBPM task(<i>JBPM Task properties</i>) is set, it overrides
	 * any inputs(map) passed to the process when the task is created {@link #createApprovalRequest(HTSummary)}
	 * 
	 * <p>
	 * @param task
	 * @return Parameter-Value Map for a task
	 */
	private Map<String, Object> getMappedData(Task task) {
		
		Map<String, Object> params = new HashMap<>();
		
		Long contentId= task.getTaskData()==null? null : task.getTaskData().getDocumentContentId();
		
		if(contentId==null){
			return params;
		}
		
		byte[] objectinBytes = service.getContent(contentId).getContent();
		
		assert objectinBytes.length>0;
		
		ObjectInputStream is=null;
		try{
			//is = new ObjectInputStream(new ByteArrayInputStream(objectinBytes));
			Object o = ContentMarshallerHelper.unmarshall(objectinBytes, null);
			
			if(o instanceof Map){
				params = (Map<String, Object>)o;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(is!=null){
				try{
					is.close();
				}catch(Exception e){}				
			}
			
		}
		
		return params;
	}
		
	private boolean complete(long taskId, String userId, Map<String, Object> values){
		
		Map<String, Object> content = getMappedData(service.getTask(taskId));
		content.putAll(values);
		//completing tasks is a single individuals responsibility
		//Notifications & Emails sent after task completion must reflect this
		content.put("ActorId", SessionHelper.getCurrentUser().getId());
		this.service.completeWithResults(taskId, userId, content);
		//this.service.complete(taskId, userId, null);
		return true;
	}
	

	/**
	 * This method provides a generic way for task execution. 
	 * 
	 * @param taskId This is the taskId of the task
	 * @param userId This is the user executing the task
	 * @param action This is the action to be executed
	 */
	public void execute(long taskId, String userId, Actions action, Map<String, Object> values) {
		
		switch(action){
		case CLAIM:
			get().service.claim(taskId, userId);
			break;
		case COMPLETE:
			get().complete(taskId, userId, values);			
			break;
		case DELEGATE:
			//get().service.delegate(taskId, userId, targetUserId);
			break;
		case FORWARD:
			//get().service.forward(taskId, userId, targetEntityId)
			break;
		case RESUME:
			get().service.resume(taskId, userId);
			break;
		case REVOKE:
			//TODO: investigate what revoke actually does
			get().service.release(taskId, userId);
			break;
		case START:
			get().service.start(taskId, userId);
			break;
		case STOP:
			get().service.stop(taskId, userId);
			break;
		case SUSPEND:
			get().service.suspend(taskId, userId);
			break;
		}
				
	}
	
	public List<Node> getProcessDia(long processInstanceId){
		
		List<Node> nodes = new ArrayList<>();
		
		String processDefId = JPAProcessInstanceDbLog.findProcessInstance(processInstanceId).getProcessId();
		org.drools.definition.process.Process process = kbase.getProcess(processDefId);
				
		WorkflowProcessImpl wfprocess = (WorkflowProcessImpl)process;
			
		for(Node node : wfprocess.getNodes()){
			
			long nodeId = node.getId();
			List<NodeInstanceLog> nodeLogInstance =
					JPAProcessInstanceDbLog.findNodeInstances(processInstanceId, new Long(nodeId).toString());
		
			String nodeName = node.getName();					
			if(nodeLogInstance.size() > 0){
				//Executed nodes only
				Object x = node.getMetaData().get("x");
				Object y = node.getMetaData().get("x");
				Object width = node.getMetaData().get("width");
				Object height = node.getMetaData().get("height");
			
				//System.err.println(node.getName());
				//Ignore all other nodes - Only work pick human Task Nodes
				if(node instanceof HumanTaskNode || node instanceof StartNode || node instanceof EndNode){
					//HumanTaskNode ht = (HumanTaskNode) node;	
					nodes.add(node);
				}
			}
		}
		
		return nodes;

	}


	public List<NodeDetail> getWorkflowProcessDia(Long processInstanceId) {
		List<Node> nodes = getProcessDia(processInstanceId);
		
		List<NodeDetail> details= new ArrayList<>();
		
		boolean hasEndNode=false;
		for(Node node: nodes){
			//find status of the node if human node:: approved/ Rejected
			
			NodeDetail detail = new NodeDetail();
			String name = node.getName();
			detail.setName(name);
			details.add(detail);
			detail.setStartNode(node instanceof StartNode);
			detail.setEndNode(node instanceof EndNode);			
			//assuming current task is the last task unless its the end node
			
			if(node instanceof EndNode){
				hasEndNode=true;
			}
		}
		
		if(!hasEndNode && details.size()>0){
			details.get(details.size()-1).setCurrentNode(true);
		}
		
		return details;
	}


	public HTSummary getSummary(Long taskId) {
		
		HTSummary summary = new HTSummary(taskId);
		
		Task master_task = this.service.getTask(taskId);
		
		copy(summary, master_task);
		 
		return summary;
	}


	/**
	 * What happens to this process if you try to execute it and 
	 * it fails/throws an exception
	 * @param processId
	 * @param contextInfo
	 */
	public void startProcess(String processId, Map<String, Object> contextInfo) {		
		session.startProcess(processId, contextInfo);
				
	}
		
	
}
