package com.duggan.workflow.server.helper.jbpm;

import static com.duggan.workflow.server.dao.helper.DocumentDaoHelper.getDocument;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.drools.builder.ResourceType;
import org.drools.definition.process.Node;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.process.audit.JPAProcessInstanceDbLog;
import org.jbpm.process.audit.NodeInstanceLog;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jbpm.task.Deadline;
import org.jbpm.task.Deadlines;
import org.jbpm.task.I18NText;
import org.jbpm.task.OrganizationalEntity;
import org.jbpm.task.PeopleAssignments;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.User;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.local.LocalTaskService;
import org.jbpm.task.utils.ContentMarshallerHelper;
import org.jbpm.workflow.core.impl.WorkflowProcessImpl;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.jbpm.workflow.core.node.StartNode;
import org.jbpm.workflow.core.node.SubProcessNode;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.dao.model.TaskDelegation;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.shared.exceptions.ProcessInitializationException;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.Delegate;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.HTUser;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.model.LongValue;
import com.duggan.workflow.shared.model.NodeDetail;
import com.duggan.workflow.shared.model.SearchFilter;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.TaskNode;
import com.duggan.workflow.shared.model.UserGroup;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.ProcessMappings;
import com.ilesteban.processimage.ProcessImageProcessor;
import com.ilesteban.processimage.ProcessImageProcessorConfiguration;
import com.ilesteban.processimage.transformation.TaskColorTransformationJob;

/**
 * This is a Helper Class for all JBPM associated requests. It provides utility
 * methods to execute tasks and retrieve task information from the JBPM
 * environment
 * 
 * @author duggan
 * 
 */
public class JBPMHelper implements Closeable {

	private BPMSessionManager sessionManager;
	private static JBPMHelper helper;
	static Logger logger = Logger.getLogger(JBPMHelper.class);

	private JBPMHelper() {
		try {
			// By Setting the jbpm.usergroup.callback property with the call
			// back class full name, task service will use this to validate the
			// user/group exists and its permissions are ok.
			// System.setProperty("jbpm.usergroup.callback",
			// "org.jbpm.task.identity.LDAPUserGroupCallbackImpl");

			System.setProperty("jbpm.usergroup.callback",
					"org.jbpm.task.identity.DBUserGroupCallbackImpl");
			sessionManager = new BPMSessionManager();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	// not thread safe
	public static JBPMHelper get() {
		if (helper == null) {
			helper = new JBPMHelper();
		}

		return helper;
	}

	@Override
	public void close() {
		assert sessionManager != null;

		sessionManager.disposeSessions();
	}

	public LocalTaskService getTaskClient(){
		return sessionManager.getTaskClient();
	}
	
	/**
	 * This method clears the runtime environment when the application is
	 * shutdown
	 * 
	 */
	public static void clearRequestData() {
		JBPMHelper h = JBPMHelper.get();

		if (h != null) {
			try {
				h.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method creates initiates an approval process for a document.
	 * 
	 * @param summary
	 *            This is the document to be submitted for approval
	 */
	public void createApprovalRequest(String userId, Document summary) {

		Map<String, Object> initialParams = new HashMap<String, Object>();
	
		initialParams.put("documentId", summary.getId().toString());
		initialParams.put("ownerId", userId);
		initialParams.put("priority", summary.getPriority());
		
		Document clone = summary.clone(); //Not sure why we clone here
		clone.setId(summary.getId());
		initialParams.put("document", clone);

		Map<String, Value> vals = summary.getValues();
		Collection<Value> values = vals.values();
		
		for (Value val : values) {
			if(val!=null){
				initialParams.put(val.getKey(), val.getValue());
			}
		}
		
		ProcessInstance processInstance = sessionManager.startProcess(
				getProcessId(summary.getType()), initialParams, summary);
		assert (ProcessInstance.STATE_ACTIVE == processInstance.getState());

	}

	public String getProcessId(DocumentType type) {

		ADDocType adtype = DocumentDaoHelper.getType(type);
		List<ProcessDefModel> processDefs = DB.getProcessDao()
				.getProcessesForDocType(adtype);

		if (processDefs == null || processDefs.isEmpty()) {
			throw new ProcessInitializationException(
					"Could not start process: "
							+ "No process definition found for DocType= ["
							+ type + "]");
		}

		if (processDefs.size() > 1) {
			throw new ProcessInitializationException(
					"Could not start process: More than 1 process definition "
							+ "found for document [" + type + "]");
		}

		Iterator<ProcessDefModel> iter = processDefs.iterator();
		ProcessDefModel model = null;
		while (iter.hasNext()) {
			model = iter.next();
		}

		String processId = model.getProcessId();

		ProcessMigrationHelper.start(model, false);

		return processId;
	}
	
	public Task getSysTask(Long taskId){
		return sessionManager.getTaskClient().getTask(taskId);
	}

	/**
	 * Count the number of tasks - completed/ or new
	 * 
	 * @param userId
	 * @param counts
	 */
	public void getCount(String userId, HashMap<TaskType, Integer> counts) {
		
		//Count drafts & initiated documents
		DocumentDaoHelper.getCounts(userId, counts);

		//Count Tasks
		
		List<UserGroup> groups = LoginHelper.getHelper().getGroupsForUser(
				userId);
		List<String> groupIds = new ArrayList<>();
		for (UserGroup group : groups) {
			groupIds.add(group.getName());
		}

		Long count = (Long) DB
				.getEntityManager()
				.createNamedQuery(
						"TasksAssignedCountAsPotentialOwnerByStatusWithGroups")
				.setParameter("userId", userId)
				.setParameter("groupIds", groupIds)
				.setParameter("language", "en-UK")
				.setParameter("status",
						getStatusesForTaskType(TaskType.INBOX))
				.getSingleResult();
		counts.put(TaskType.INBOX, count.intValue());

		/**
		 * If John & James share the Role HOD_DEV John Claims, Starts and
		 * completes a task, should that task be presented to James as one of
		 * his completed tasks? This mechanism creates the possibility of this
		 * scenario happening TODO: Test the query with two users sharing a role
		 */
		Long count2 = (Long) DB.getEntityManager()
				.createNamedQuery("TasksOwnedCount")
				.setParameter("userId", userId)
				.setParameter("language", "en-UK")
				.setParameter("status", Status.Completed).getSingleResult();
		
		//Inprogress participated requests
		int c = counts.get(TaskType.PARTICIPATED)==null? 0: counts.get(TaskType.PARTICIPATED);
		counts.put(TaskType.PARTICIPATED, count2.intValue()+c);
		
		Long count3 = (Long) DB.getEntityManager()
				.createNamedQuery("TasksOwnedCount")
				.setParameter("userId", userId)
				.setParameter("language", "en-UK")
				.setParameter("status", Status.Suspended).getSingleResult();
		counts.put(TaskType.SUSPENDED, count3.intValue());
		
		counts.put(TaskType.UNASSIGNED, DB.getDocumentDao().getUnassigned());
	}

	public List<Status> getStatusesForTaskType(TaskType type) {

		List<Status> statuses = new ArrayList<>();

		switch (type) {
		case INBOX:
			statuses = Arrays.asList(Status.Created, Status.InProgress,
			// Status.Error,
			// Status.Exited,
			// Status.Failed,
			// Status.Obsolete,
					Status.Ready, Status.Reserved);
			break;
		case PARTICIPATED:
		case APPROVALREQUESTDONE:
			statuses = Arrays.asList(Status.Completed);
			break;
			
		}
		return statuses;
	}

	public List<HTSummary> getTasksForUser(String userId, Long processInstanceId) {
		List<UserGroup> groups = LoginHelper.getHelper().getGroupsForUser(
				userId);
		List<String> groupIds = new ArrayList<>();
		for (UserGroup group : groups) {
			groupIds.add(group.getName());
		}

		@SuppressWarnings("unchecked")
		List<TaskSummary> ts = DB.getEntityManager()
				.createNamedQuery("TasksOwnedBySubject")
				.setParameter("userId", userId)
				.setParameter("language", "en-UK")
				.setParameter("groupIds", groupIds)
				.setParameter("processInstanceId", processInstanceId)
				.getResultList();

		return translateSummaries(ts);
	}

	public List<HTSummary> searchTasks(String userId, SearchFilter filter) {
		List<TaskSummary> tasks = DB.getDocumentDao().searchTasks(userId,
				filter);

		return translateSummaries(tasks);
	}

	/**
	 * 
	 * @param processId
	 * @param taskName
	 * @return Input/Output parameter mappings for tasks
	 */
	public Collection<String> getProcessData(String processId, String taskName){
		org.drools.definition.process.Process process = sessionManager.getProcess(processId);
		
		WorkflowProcessImpl workflow = (WorkflowProcessImpl)process;
		
		Node [] nodes = workflow.getNodes();
		
		for(Node node: nodes){
			
			if(node instanceof HumanTaskNode){
				HumanTaskNode htnode = (HumanTaskNode)node;
				Object name = htnode.getWork().getParameter("TaskName");
				
				if(name!=null && name.equals(taskName)){					
					Collection<String> vals = htnode.getInMappings().values();
					return vals;
				}
					
					
			}
		}
		return null;
	}
	
	/**
	 * 
	 * This method retrieves all tasks assigned to a user.
	 * <p>
	 * 
	 * @param userId
	 *            This is the username of the user whose tasks are to be
	 *            retrieved
	 * @param type
	 * @return List This is a list of human task summaries retrieved for the
	 *         user
	 * 
	 */
	public List<HTSummary> getTasksForUser(String userId, TaskType type) {

		if (!LoginHelper.get().existsUser(userId)) {
			throw new RuntimeException("User " + userId + " Unknown!!");
		}

		if (type == null) {
			type = TaskType.INBOX;
		}

		List<TaskSummary> ts = new ArrayList<>();

		switch (type) {
		case PARTICIPATED:
		case APPROVALREQUESTDONE:
			// approvals - show only items I have approved
			ts = sessionManager.getTaskClient().getTasksOwned(userId,
					Arrays.asList(Status.Completed), "en-UK");

			break;
		case INBOX:
//			ts = sessionManager.getTaskClient()
//					.getTasksAssignedAsPotentialOwner(userId, "en-UK");
			ts = sessionManager.getTaskClient()
					.getTasksAssignedAsPotentialOwnerByStatus(userId, 
							getStatusesForTaskType(type), 
							"en-UK");
			break;
		case SUSPENDED:
			ts = sessionManager.getTaskClient().getTasksOwned(userId,
					Arrays.asList(Status.Suspended), "en-UK");
			break;
		case UNASSIGNED:
			ts = DB.getDocumentDao().getUnassignedTasks();
			break;
		default:
			break;
		}

		return translateSummaries(ts);

	}

	public List<HTSummary> translateSummaries(List<TaskSummary> ts) {

		List<HTSummary> tasks = new ArrayList<>();
		for (TaskSummary summary : ts) {

			HTSummary task = new HTSummary(summary.getId());
			Task master_task = sessionManager.getTaskClient().getTask(
					summary.getId());

			copy(task, master_task);
			tasks.add(task);
			// call for test
			// getTask(userId, summary.getId());

		}

		return tasks;
	}

	private void copy(HTSummary task, Task master_task) {

		Map<String, Object> content = getMappedData(master_task);
		Document doc = getDocument(content);

		assert doc != null;

		task.setCreated(master_task.getTaskData().getCreatedOn());
		task.setSubject(doc.getSubject());
		task.setDescription(doc.getDescription());
		task.setPriority(doc.getPriority());
		task.setDocumentRef(doc.getId());
		task.setCompletedOn(master_task.getTaskData().getCompletedOn());
		
		String processId =master_task.getTaskData().getProcessId(); 
		task.setProcessId(processId);
		task.setProcessInstanceId(master_task.getTaskData()
				.getProcessInstanceId());
		
		task.setHasAttachment(DB.getAttachmentDao().getHasAttachment(doc.getId()));

		try{
			WorkflowProcessImpl process = (WorkflowProcessImpl)sessionManager.getProcess(processId);
			task.setProcessName(process.getName());
			
			Node node = getNode(master_task);
			task.setNodeId(node.getId());
			task.setNodeName(node.getName());
		}catch(Exception e){}
		
		task.setHasAttachment(DB.getAttachmentDao().getHasAttachment(doc.getId()));
		Status status = master_task.getTaskData().getStatus();
		task.setStatus(HTStatus.valueOf(status.name().toUpperCase()));
		
		if(status==Status.Completed){
			
			loadProgressInfo(task,master_task.getTaskData().getProcessInstanceId());
			
		}else{
			TaskDelegation taskdelegation = DB.getDocumentDao().getTaskDelegationByTaskId(master_task.getId());
			
			if(taskdelegation!=null){
				Delegate delegate = new Delegate(taskdelegation.getId(), 
						taskdelegation.getTaskId(), taskdelegation.getUserId(),
						taskdelegation.getDelegateTo());
				task.setDelegate(delegate);
			}
			
			User user = null;
			if((user=master_task.getTaskData().getActualOwner())!=null){
				task.setTaskActualOwner(LoginHelper.get().getUser(user.getId(), false));
			}else{
				List<OrganizationalEntity> entitiesList = master_task.getPeopleAssignments().getPotentialOwners();
				
				String entities = "";
				if(entities!=null)
				for(OrganizationalEntity entity : entitiesList){
					entities = entities.concat(entity.getId()+", ");
				}
				
				if(!entities.isEmpty()){
					entities = entities.substring(0, entities.length()-2);
					task.setPotentialOwners(entities);
				}
			}
			
		}

		List<I18NText> names = master_task.getNames();

		if (names.size() > 0) {
			task.setTaskName(names.get(0).getText());
		}
	
		Deadlines deadlines = master_task.getDeadlines();
		if(deadlines!=null){
			List<Deadline> startDeadlines = deadlines.getStartDeadlines();
			if(startDeadlines!=null)
			if(startDeadlines.size()>0){
				Deadline deadline = startDeadlines.get(startDeadlines.size()-1);
				Date date = deadline.getDate();
				task.setStartDateDue(date);
			}
			
			List<Deadline> endDeadlines = deadlines.getEndDeadlines();
			if(endDeadlines!=null)
			if(endDeadlines.size()>0){
				Deadline deadline = endDeadlines.get(endDeadlines.size()-1);
				Date date = deadline.getDate();
				task.setEndDateDue(date);
			}
			
		}
		
		try{
			//Exception thrown if process not started
			task.setName(getDisplayName(master_task));
		}catch(Exception e){}
		
		
		if (task instanceof HTask) {
			task.setDetails(doc.getDetails());
			task.setValues(doc.getValues());
			task.setPriority(doc.getPriority());
			task.setDocumentDate(doc.getDocumentDate());
			task.setDocStatus(DB.getDocumentDao().getById(doc.getId()).getStatus());
			task.setOwner(doc.getOwner());
		
//			TaskDelegation taskdelegation = DB.getDocumentDao().getTaskDelegationByTaskId(master_task.getId());
//			
//			if(taskdelegation!=null){
//				Delegate delegate = new Delegate(taskdelegation.getId(), 
//						taskdelegation.getTaskId(), taskdelegation.getUserId(),
//						taskdelegation.getDelegateTo());
//				((HTask)task).setDelegate(delegate);
//			}
			
		}
	}

	public void loadProgressInfo(Doc task, long processInstanceId) {
		//then how far is the process now?
		//ProcessInstanceLog log = JPAProcessInstanceDbLog.findProcessInstance(processInstanceId);
		int instanceStatus = DB.getProcessDao().getInstanceStatus(processInstanceId);
		if(instanceStatus==-1){
			return;
		}
		
		if(instanceStatus==2){
			//Process Completed
			task.setProcessStatus(HTStatus.COMPLETED);
		}else{
			//where is my request?
			List<Object[]> actualOwners = DB.getProcessDao()
					.getCurrentActualOwnersByProcessInstanceId(processInstanceId, Status.Completed, false);
			
			//Task Id, 
			for(Object[] row: actualOwners){
				if(row[1]!=null){
					task.setTaskActualOwner(LoginHelper.get().getUser(row[1].toString(), false));
				}
			}
			
			List<Object[]> potOwners = DB.getProcessDao()
					.getCurrentPotentialOwnersByProcessInstanceId(processInstanceId, Status.Completed, false);
			
			String entities = "";
			//Task Id, 
			for(Object[] row: potOwners){
				if(row[1]!=null){
					entities = entities.concat(row[1].toString()+", ");
				}
			}
			if(!entities.isEmpty()){
				entities = entities.substring(0, entities.length()-2);
				task.setPotentialOwners(entities);
			}
		}
	}

	private Value getValue(Object val) {

		Value value = null;
		
		if (val instanceof Boolean) {
			value = new BooleanValue((Boolean)val);
		}

		if (val instanceof String) {
			value = new StringValue(val.toString());
		}

		if (val instanceof Integer) {
			Long longVal = new Long((Integer)val);
			value = new LongValue(longVal);
		}
		if (val instanceof Long) {
			value = new LongValue((Long)val);
		}
		
		if (val instanceof Date) {
			value = new DateValue((Date)val);
		}
		
		if(val instanceof Value){
			value=(Value)val;
		}
		
		return value;
	}

	/**
	 * This method retrieves the full task object, which provides more
	 * comprehensive details for a task
	 * 
	 * @param taskId
	 *            This is the task Id of the task to be retrieved
	 * @return HTask Human Task DTO object retrieved
	 */
	public HTask getTask(long taskId) {

		// Human Task
		HTask myTask = new HTask(taskId);

		Task task = sessionManager.getTaskClient().getTask(taskId);
		
		copy(myTask, task);
		// task.get

		List<I18NText> descriptions = task.getDescriptions();
		if(myTask.getDescription()==null){
			myTask.setDescription(descriptions.get(0).getText());
		}

		List<I18NText> names = task.getNames();
		if(names.size()>0){
			String taskName = names.get(0).getText();
			myTask.setTaskName(taskName);
		}

		List<I18NText> subjects = task.getSubjects();// translations
		if(myTask.getSubject()==null){
			myTask.setSubject(subjects.get(0).getText());
		}

		int priority = task.getPriority();
		if(myTask.getPriority()==null)
			myTask.setPriority(priority);

		// int version = task.getVersion();
		// myTask.setVersion(version);

		
		// deadlines.getEndDeadlines();
		//Delegation delegation = task.getDelegation();

		//PeopleAssignments assignments = task.getPeopleAssignments();
		// User user = assignments.getTaskInitiator();

		//List<OrganizationalEntity> entities = assignments.getRecipients();

		// myTask.setData(data);

		return myTask;
	}
	
//	public HTData getTaskData(Task task){
//
//		// HT DATA
//		HTData data = new HTData();
//
//		// TASK DATA
//		TaskData taskData = task.getTaskData();
//		String docType = taskData.getDocumentType();
//		data.setDocType(docType);
//
//		long workId = taskData.getWorkItemId();
//		data.setWorkId(workId);
//
//		// owner
//		User actualOwner = taskData.getActualOwner();
//		if (actualOwner != null) {
//			HTUser taskOwner = new HTUser(actualOwner.getId());
//			data.setActualOwner(taskOwner);
//		}
//
//		// comments
//		List<Comment> comments = taskData.getComments();
//		if (comments != null)
//			for (Comment comment : comments) {
//				HTComment htComment = new HTComment();
//				htComment.setAddedAt(comment.getAddedAt());
//				htComment.setId(comment.getId());
//				htComment.setText(comment.getText());
//
//				User addedBy = comment.getAddedBy();
//				if (addedBy != null)
//					htComment.setAddedBy(new HTUser(addedBy.getId()));
//
//			}
//
//		Date completedOn = taskData.getCompletedOn();
//		data.setCompletedOn(completedOn);
//
//		User createdBy = taskData.getCreatedBy();
//		if (createdBy != null) {
//			data.setCreatedBy(new HTUser(createdBy.getId()));
//		}
//
//		AccessType accessType = taskData.getDocumentAccessType();
//
//		if (accessType != null) {
//			data.setDocAccessType(HTAccessType.valueOf(accessType.name()
//					.toUpperCase()));
//		}
//
//		long contentId = taskData.getDocumentContentId();
//		data.setContentId(contentId);
//
//		Status taskDataStatus = taskData.getStatus();
//		if (taskDataStatus != null) {
//			data.setStatus(HTStatus
//					.valueOf(taskDataStatus.name().toUpperCase()));
//		}
//
//		String taskDataOutputType = taskData.getOutputType();
//		data.setOutputType(taskDataOutputType);
//
//		Date expiryTime = taskData.getExpirationTime();
//		data.setExpiryTime(expiryTime);
//
//		long parentId = taskData.getParentId();
//		data.setParentId(parentId);
//
//		Status previousStatus = taskData.getPreviousStatus();
//		if (previousStatus != null) {
//			data.setPreviousStatus(HTStatus.valueOf(previousStatus.name()
//					.toUpperCase()));
//		}
//
//		return data;
//	}

	public static Map<String, Object> getMappedData(Task task) {

		
		Long outputId=null;
		Map<String, Object> map = new HashMap<>();
		
		if(task.getTaskData() != null){
			if(task.getTaskData().getStatus()==Status.Completed){
				outputId=task.getTaskData().getOutputContentId();
				if(outputId!=null){
					map = getMappedDataByContentId(outputId);
				}
			}
		
			Long contentId=null;
			if(contentId==null && !map.containsKey("documentOut")){
				contentId = task.getTaskData().getDocumentContentId();
				map.putAll(getMappedDataByContentId(contentId));
			}
			
		}
		
					
		return map;
	}

	public static Map<String, Object> getMappedDataByContentId(Long contentId) {
		return get().getMappedData(contentId);
	}

	/**
	 * <p>
	 * This method returns the Values passed when the task was initiated.
	 * Several methods of retrieving these parameters are offered online but the
	 * use of {@link ContentMarshallerHelper} is what worked in this instance
	 * 
	 * <p>
	 * The use of {@link ObjectInputStream} to read the bytes failed with an
	 * {@link OptionalDataException}; trying to fix this did not work.
	 * 
	 * <p>
	 * Further, if the Content value of the JBPM task(<i>JBPM Task
	 * properties</i>) is set, it overrides any inputs(map) passed to the
	 * process when the task is created
	 * {@link #createApprovalRequest(HTSummary)}
	 * 
	 * <p>
	 * 
	 * @param task
	 * @return Parameter-Value Map for a task
	 */
	private Map<String, Object> getMappedData(Long contentId) {

		Map<String, Object> params = null;

		if (contentId == null) {
			return params;
		}

		params = new HashMap<>();

		byte[] objectinBytes = sessionManager.getTaskClient()
				.getContent(contentId).getContent();

		assert objectinBytes.length > 0;

		ObjectInputStream is = null;
		try {
			// is = new ObjectInputStream(new
			// ByteArrayInputStream(objectinBytes));
			Object o = ContentMarshallerHelper.unmarshall(objectinBytes, null);

			if (o instanceof Map) {
				params = (Map<String, Object>) o;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}

		}

		return params;
	}

	/**
	 * This method provides a generic way for task execution.
	 * 
	 * @param taskId
	 *            This is the taskId of the task
	 * @param userId
	 *            This is the user executing the task
	 * @param action
	 *            This is the action to be executed
	 */
	public void execute(long taskId, String userId, Actions action,
			Map<String, Object> values) {

		sessionManager.execute(taskId, userId, action, values);

	}
	
	/**
	 * 
	 * @return PNG Diagram
	 * @throws IOException 
	 */
	public InputStream getProcessMap(long processInstanceId) throws IOException{

		ProcessInstanceLog log = JPAProcessInstanceDbLog
				.findProcessInstance(processInstanceId);
		if(log==null){
			return null;
		}
		String processId = log.getProcessId();

		
		List<LocalAttachment> attachment = DB.getAttachmentDao().getAttachmentsForProcessDef(
				DB.getProcessDao().getProcessDef(processId), true);
		
		if(attachment.size()!=1 || !attachment.get(0).getName().endsWith("svg")){
			throw new RuntimeException("Cannot Generate ProcessMap; SVG Image not found");
		}
		
		byte[] svgImage = attachment.get(0).getAttachment();

        //Create a new Configuration object to set the padd to use in the label of the tasks
        ProcessImageProcessorConfiguration config = new ProcessImageProcessorConfiguration();
        config.setDefaultextPad(20.0f);
        
        //Create a processor instance for test1.svg
        ProcessImageProcessor processor = new ProcessImageProcessor(new ByteArrayInputStream(svgImage), config);
        
		org.drools.definition.process.Process process = sessionManager
				.getProcess(processId);

		WorkflowProcessImpl wfprocess = (WorkflowProcessImpl) process;
		
		for (Node node : wfprocess.getNodes()) {

			long nodeId = node.getId();
			
			List<NodeInstanceLog> nodeLogInstance = JPAProcessInstanceDbLog
					.findNodeInstances(processInstanceId,
							new Long(nodeId).toString());

			if (nodeLogInstance.size() > 0) {
				// HumanTaskNode ht = (HumanTaskNode) node;
				String name = node.getName();
				
				boolean isCompletedNode = nodeLogInstance.size()%2==0;
				processor.addTransformationJob(
						new TaskColorTransformationJob(name, isCompletedNode?"#00ff00":"#ff0000"));
			}
		}
		  //apply the transformations
        processor.applyTransformationJobs(true);
        
        return processor.toPNG();
	}

	public List<NodeDetail> getWorkflowProcessDia(long processInstanceId) {

		ProcessInstanceLog log = JPAProcessInstanceDbLog
				.findProcessInstance(processInstanceId);

		if (log == null) {
			// --
			logger.warn("Invalid State : ProcessInstanceLog is null; ProcessInstanceId="
					+ processInstanceId
					+ "; Document = "
					+ DocumentDaoHelper
							.getDocumentByProcessInstance(processInstanceId));
			return new ArrayList<>();
		}

		return getWorkflowProcessDia(log);
	}

	public List<NodeDetail> getWorkflowProcessDia(ProcessInstanceLog log) {

		List<NodeDetail> details = new ArrayList<>();

		long processInstanceId = log.getProcessInstanceId();

		String processId = log.getProcessId();
		// JPAProcessInstanceDbLog.findNodeInstances(processInstanceId);

		org.drools.definition.process.Process process = sessionManager
				.getProcess(processId);

		WorkflowProcessImpl wfprocess = (WorkflowProcessImpl) process;
		
		for (Node node : wfprocess.getNodes()) {

			long nodeId = node.getId();
			
			List<NodeInstanceLog> nodeLogInstance = JPAProcessInstanceDbLog
					.findNodeInstances(processInstanceId,
							new Long(nodeId).toString());

			if (nodeLogInstance.size() > 0) {
				// Executed nodes only
				Object x = node.getMetaData().get("x");
				Object y = node.getMetaData().get("x");
				Object width = node.getMetaData().get("width");
				Object height = node.getMetaData().get("height");

				if (node instanceof SubProcessNode) {
					SubProcessNode n = (SubProcessNode) node;
					List<ProcessInstanceLog> list = JPAProcessInstanceDbLog
							.findSubProcessInstances(processInstanceId);

					for (ProcessInstanceLog subprocess : list) {
						// n.get
						// if(n.getProcessId().equals(
						// subprocess.getProcessId())){
						Long subprocessId = subprocess.getId();
						assert !subprocessId.equals(processInstanceId);

						details.addAll(getWorkflowProcessDia(subprocess));
						// }
					}
				}
				
				// log.debug(node.getName());
				// Ignore all other nodes - Only work pick human Task Nodes
				if (node instanceof HumanTaskNode || node instanceof StartNode
						|| node instanceof EndNode) {
					// HumanTaskNode ht = (HumanTaskNode) node;
					assert nodeLogInstance.size() == 2;
					NodeDetail detail = new NodeDetail();
					String name = node.getName();
					detail.setName(name);
					detail.setStartNode(node instanceof StartNode);
					detail.setEndNode(node instanceof EndNode);
					detail.setCurrentNode(nodeLogInstance.size() == 1);
					detail.setNodeId(node.getId());
					details.add(detail);
					
					
					if(node instanceof HumanTaskNode){
						HumanTaskNode htn = (HumanTaskNode)node;
						Object groups = htn.getWork().getParameter("GroupId");
						Object actors = htn.getWork().getParameter("ActorId");
						
						if(groups!=null){
							loadGroups(groups.toString(), detail);
						}
						
						if(actors!=null){
							loadActors(actors.toString(), detail);
						}
					}
					
					
					//((HumanTaskNode)node).getOutMappings().get("isApproved");
				}
			}
		}

		return details;

	}

	public List<TaskNode> getWorkflowProcessNodes(String processId) {

		List<TaskNode> details = new ArrayList<>();
		org.drools.definition.process.Process process = sessionManager
				.getProcess(processId);

		WorkflowProcessImpl wfprocess = (WorkflowProcessImpl) process;
		
		for (Node node : wfprocess.getNodes()) {

			if (node instanceof SubProcessNode) {
				SubProcessNode n = (SubProcessNode) node;
				details.addAll(getWorkflowProcessNodes(n.getProcessId()));
			}
			
			// Ignore all other nodes - Only work pick human Task Nodes
			if (node instanceof HumanTaskNode) {

				TaskNode detail = new TaskNode();
				String name = node.getName();
				detail.setNodeId(node.getId());
				detail.setName(name);
				detail.setDisplayName(name);
				details.add(detail);
			}
	
		}

		return details;

	}
	
	
	/**
	 * Comma separated List of actors
	 * @param actors
	 * @param detail
	 */
	private void loadActors(String actors, NodeDetail detail) {
		String[] actorIds = actors.split(",");
		for(String userId: actorIds){
			HTUser user = LoginHelper.get().getUser(userId.trim());
			detail.addUser(user);
		}
	}

	/**
	 * Comma separated list of groups
	 * @param groups
	 * @param detail
	 */
	private void loadGroups(String groups, NodeDetail detail) {
		String [] groupIds = groups.split(",");
		for(String groupId: groupIds){
			UserGroup group = LoginHelper.get().getGroupById(groupId);
			List<HTUser> lst = LoginHelper.get().getUsersForGroup(groupId);
			detail.addAllUsers(lst);
			detail.addGroup(group);
		}
	}

	public Object getActors(Long processInstanceId, String nodeId){
		//
		List<NodeInstanceLog> log = JPAProcessInstanceDbLog.findNodeInstances(processInstanceId, nodeId); 
		
		
		logger.debug("Logs:: "+log);
		if(log!=null && !log.isEmpty()){
			NodeInstanceLog nil = log.get(0);
			
			logger.debug("Class >> "+nil.getClass());			
		}
		
		return null;
		
	}

	public HTSummary getSummary(Long taskId) {

		HTSummary summary = new HTask(taskId);

		Task master_task = sessionManager.getTaskClient().getTask(taskId);

		copy(summary, master_task);

		return summary;
	}

	/**
	 * What happens to this process if you try to execute it and it fails/throws
	 * an exception
	 * 
	 * @param processId
	 * @param contextInfo
	 */
	public void startProcess(String processId, Map<String, Object> contextInfo) {
		sessionManager.startProcess(processId, contextInfo);
	}

	public static String getProcessDetails(Long processInstanceId) {

		ProcessInstanceLog log = JPAProcessInstanceDbLog
				.findProcessInstance(processInstanceId);
		String processId = log.getProcessId();

		return "[Process " + processId + "; ProcessInstanceId "
				+ processInstanceId + "L]";
	}

	public void loadKnowledge(byte[] bytes, String processName) {
		sessionManager.loadKnowledge(bytes, processName);
	}

	public void loadKnowledge(List<byte[]> files, List<ResourceType> types,
			String rootProcess) {
		sessionManager.loadKnowledge(files, types,rootProcess);
	}


	public boolean isProcessingRunning(String processId) {
		return sessionManager.isRunning(processId);
	}

	public void stop(String processId) {
		sessionManager.unloadKnowledgeBase(processId);
	}

	/**
	 * Returns the 'Task Name' Property value
	 * 
	 * @param taskId
	 * @return
	 */
	public String getTaskName(Long taskId) {

		Task task = sessionManager.getTaskClient().getTask(taskId);

		String name = null;

		if (task.getNames().size() > 0)
			name = task.getNames().get(0).getText();

		return name;
	}
	
	public String getDisplayName(Long taskId){
		Task task = sessionManager.getTaskClient().getTask(taskId);
		return getDisplayName(task);
	}
	/**
	 * Returns the 'Name' property of a task (Node Name)
	 * <p>
	 * This process is inefficient - TODO: Find a better way to do this
	 * <p>
	 * @param taskId
	 * @return
	 */
	public String getDisplayName(Task task){
		//
		Node node = getNode(task);
		if(node!=null){
			return node.getName();
		}
		
		return null;
	}
	
	public Node getNode(Task task){
		String processId=task.getTaskData().getProcessId();
		String taskName = getTaskName(task.getId());
				
		org.drools.definition.process.Process droolsProcess = sessionManager.getProcess(processId);
		WorkflowProcessImpl wfprocess = (WorkflowProcessImpl) droolsProcess;
		task.getTaskData().getWorkItemId();
		
		for (Node node : wfprocess.getNodes()) {
			
			if(node instanceof HumanTaskNode){
				HumanTaskNode htnode = (HumanTaskNode) node;
				Object nodeTaskName = htnode.getWork().getParameter("TaskName");
				
				if(nodeTaskName!=null)
				if(nodeTaskName.equals(taskName)){
					return htnode;					
				}
				
			}
		}
		
		return null;
	}
	
	public ProcessMappings getProcessDataMappings(long taskId){
		Task task = getSysTask(taskId);
		String taskName = getTaskName(taskId);
		String processId = task.getTaskData().getProcessId();
		
		return getProcessDataMappings(processId, taskName);
	}
	
	public ProcessMappings getProcessDataMappings(String processId, String taskName){
		ProcessMappings processData = new ProcessMappings();
		org.drools.definition.process.Process droolsProcess = sessionManager.getProcess(processId);
		WorkflowProcessImpl wfprocess = (WorkflowProcessImpl) droolsProcess;

	//	log.debug("Globals:: "+wfprocess.get);
		
		for (Node node : wfprocess.getNodes()) {
			
			if(node instanceof HumanTaskNode){
				HumanTaskNode htnode = (HumanTaskNode) node;
				Object nodeTaskName = htnode.getWork().getParameter("TaskName");
				
				logger.debug("1. Searching for TaskName> "+taskName+" :: Node TaskName >> "+nodeTaskName);
				
				if(nodeTaskName!=null){
					String nodeName = nodeTaskName.toString();
					if(nodeName.startsWith("#{")){
						//dynamic Node Name
						nodeTaskName=htnode.getWork().getParameter("taskName");
					}
				}
				logger.debug("2. Searching for TaskName> "+taskName+" :: Node TaskName >> "+nodeTaskName);
				
				
				if(nodeTaskName!=null)
				if(nodeTaskName.equals(taskName)){
					processData.setInputMappings(htnode.getInMappings());
					processData.setOutMappings(htnode.getOutMappings());					
				}
				
			}
		}
	
		return processData;
	}

	public List<Long> getTaskIdsForUser(String userId) {
		Query query = DB.getEntityManager().createNamedQuery("TasksOwnedIds")
				.setParameter("userId", userId)
				.setParameter("language", "en-UK")
				.setParameter("status", Arrays.asList(Status.Completed, Status.Created, 
						Status.InProgress,Status.Suspended,
						// Status.Error,
						// Status.Exited,
						// Status.Failed,
						// Status.Obsolete,
								Status.Ready, Status.Reserved));
		
		List<Long> ids  = query.getResultList();
		
		return ids;
	}

	public String getProcessName(String processId) {
		String name = null;
		
		try{
			WorkflowProcessImpl process = (WorkflowProcessImpl)sessionManager.getProcess(processId);
			name = process.getName();
		}catch(Exception e){}
		
		return name;
	}

	public void assignTask(Long taskId, String userId) {
		
		Task task = DB.getEntityManager().find(Task.class, taskId);
		
		PeopleAssignments peopleAssign = new PeopleAssignments();
		List<OrganizationalEntity> entities = new ArrayList<>();
		entities.add(new User(userId));
		peopleAssign.setPotentialOwners(entities);
		task.setPeopleAssignments(peopleAssign);
		DB.getEntityManager().persist(task);
		
		//WorkItemNodeInstance i;
	}

	public void setCounts(HTUser htuser) {
		String userId = htuser.getUserId();
		assert userId!=null;
		
		HashMap<TaskType,Integer> counts = new HashMap<>();
		getCount(userId, counts);
		
		htuser.setParticipated(counts.get(TaskType.PARTICIPATED));
		htuser.setInbox(counts.get(TaskType.INBOX));
	}

}
