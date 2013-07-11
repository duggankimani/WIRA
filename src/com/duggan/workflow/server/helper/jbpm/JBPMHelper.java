package com.duggan.workflow.server.helper.jbpm;

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

import org.drools.KnowledgeBase;
import org.drools.SystemEventListenerFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
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
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.TaskService;
import org.jbpm.task.service.local.LocalTaskService;
import org.jbpm.task.utils.ContentMarshallerHelper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.ldap.server.ApacheDSContainer;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTAccessType;
import com.duggan.workflow.shared.model.HTComment;
import com.duggan.workflow.shared.model.HTData;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.HTask;

import static com.duggan.workflow.server.helper.dao.DocumentDaoHelper.*;

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
	LocalHTWorkItemHandler taskHandler;
	
	private static JBPMHelper helper;
	
	private JBPMHelper(){
		try{
	        // By Setting the jbpm.usergroup.callback property with the call
	        // back class full name, task service will use this to validate the
	        // user/group exists and its permissions are ok.
	        System.setProperty("jbpm.usergroup.callback",
	                "org.jbpm.task.identity.LDAPUserGroupCallbackImpl");
	        

			session = this.initializeSession();
			KnowledgeRuntimeLoggerFactory.newConsoleLogger(session);
			
			GenericHTWorkItemHandler htHandler = this.createTaskHandler(session);
			session.getWorkItemManager().registerWorkItemHandler("Human Task", htHandler);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
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
		initialParams.put("subject", summary.getSubject());
		initialParams.put("description", summary.getDescription());
		initialParams.put("documentId", summary.getId());
		initialParams.put("value", null);
		
		ProcessInstance processInstance = session.startProcess("invoice-approval", initialParams);
		
		assert (ProcessInstance.STATE_ACTIVE ==processInstance.getState());
		
	}
	
	public void setCount(String userId, HashMap<TaskType, Integer> counts){
		List approvalTasks = getTasksForUser(userId, TaskType.APPROVALREQUESTNEW);
		counts.put(TaskType.APPROVALREQUESTNEW, approvalTasks.size());
		
		List completed = getTasksForUser(userId, TaskType.APPROVALREQUESTDONE);
		counts.put(TaskType.APPROVALREQUESTDONE, completed.size());
	}
	
	/**
	 * 
	 * This method retrieves all tasks assigned to a user.
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
			ts = this.service.getTasksAssignedAsPotentialOwnerByStatus(userId,
					Arrays.asList(Status.Completed), 
					"en-UK");
		case APPROVALREQUESTNEW:
			ts = this.service.getTasksAssignedAsPotentialOwnerByStatus(userId,
					Arrays.asList(Status.Created,
							Status.InProgress,
							Status.Error,
							Status.Exited,
							Status.Failed,
							Status.Obsolete,
							Status.Ready,
							Status.Reserved,
							Status.Suspended), 
					"en-UK");
			
			 ts=  this.service.getTasksAssignedAsPotentialOwner(userId, "en-UK");
			break;

		default:
			break;
		}
		
		
		List<HTSummary> tasks = new ArrayList<>();
		for(TaskSummary summary : ts){
			
			HTSummary task = new HTSummary(summary.getId());
			Task master_task = this.service.getTask(summary.getId());
			Map<String, Object> content = getMappedData(master_task);
			Document doc = getDocument(content);
						
			task.setCreated(summary.getCreatedOn());
			task.setDateDue(summary.getCreatedOn());			
			task.setSubject(doc.getSubject());
			task.setDescription(doc.getDescription());
			task.setPriority(doc.getPriority());
			task.setDocumentRef(doc.getId());
			//task.setLastUpdate(summary.get);			
			task.setTaskName(summary.getName());		
			
			task.setStatus(HTStatus.valueOf(summary.getStatus().name().toUpperCase()));
			tasks.add(task);
			
			//call for test
			//getTask(userId, summary.getId());
			
		}
		
		return tasks;
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
	 * 
	 * This method returns the Values passed when the task was initiated
	 * Several methods of retrieving these parameters are offered
	 * online but the use of {@link ContentMarshallerHelper} is what worked
	 * for me. 
	 * 
	 * The use of {@link ObjectInputStream} to read the bytes failed with an {@link OptionalDataException}; trying
	 * to fix this did not work.
	 * 
	 * Further, if the Content value of the JBPM task(JBPM Task properties) is set, it overrides
	 * any inputs(map) passed to the process when the task is created {@link #createApprovalRequest(HTSummary)}
	 * 
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

	/**
	 * This method claims a task
	 * 
	 * @param taskId Task Id of the task to be claimed
	 * @param userId the username of the person claiming the task
	 * @return true
	 */
	private boolean claim(long taskId, String userId){
		
		this.service.claim(taskId, userId);
		
		return true;
	}
	
	
	private boolean start(long taskId, String userId){
		
		this.service.start(taskId, userId);
		
		return true;
	}
	
	private boolean complete(long taskId, String userId, Map<String, Object> values){
		
		Map<String, Object> content = getMappedData(service.getTask(taskId));
		content.putAll(values);
		
		this.service.completeWithResults(taskId, userId, content);
		
		return true;
	}
	
	
	private GenericHTWorkItemHandler createTaskHandler(
			StatefulKnowledgeSession ksession) {
		
		if(taskHandler!=null){
			return taskHandler;
		}

    	TaskService ts = new TaskService(DB.getEntityManagerFactory(),
    			SystemEventListenerFactory.getSystemEventListener());
    	
    	LocalTaskService taskService = new LocalTaskService(ts);
    	
    	taskHandler = new LocalHTWorkItemHandler(taskService, ksession);
    	
    	this.service = taskService;
    	
    	return taskHandler;
	}

	private StatefulKnowledgeSession initializeSession() {
		
		if(session!=null)
			return (StatefulKnowledgeSession) session;
		
		
    	KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    	//builder.add(new ClassPathResource("invoice-approval.bpmn"), ResourceType.BPMN2);
    	builder.add(new ClassPathResource("invoice-approval.bpmn"), ResourceType.BPMN2);
    	
    	kbase = builder.newKnowledgeBase();
    	
		return session=kbase.newStatefulKnowledgeSession();
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
			System.err.println("Completing Document -- TaskId "+taskId);
			for(String key:values.keySet()){
				System.err.println("var>>  "+key+" : "+values.get(key));
			}
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
			break;
		case START:
			get().start(taskId, userId);
			break;
		case STOP:
			get().service.start(taskId, userId);
			break;
		case SUSPEND:
			get().service.suspend(taskId, userId);
			break;
		}
				
	}
	
	
	
}
