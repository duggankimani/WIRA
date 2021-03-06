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
import org.jbpm.task.TaskData;
import org.jbpm.task.TaskService;
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
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.TaskDelegation;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.email.EmailUtil;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.BooleanValue;
import com.duggan.workflow.shared.model.DateValue;
import com.duggan.workflow.shared.model.Delegate;
import com.duggan.workflow.shared.model.Doc;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.model.HTSummary;
import com.duggan.workflow.shared.model.HTask;
import com.duggan.workflow.shared.model.LongValue;
import com.duggan.workflow.shared.model.NodeDetail;
import com.duggan.workflow.shared.model.SearchFilter;
import com.duggan.workflow.shared.model.StringValue;
import com.duggan.workflow.shared.model.TaskNode;
import com.duggan.workflow.shared.model.Value;
import com.duggan.workflow.shared.model.form.ProcessMappings;
import com.ilesteban.processimage.ProcessImageProcessor;
import com.ilesteban.processimage.ProcessImageProcessorConfiguration;
import com.ilesteban.processimage.transformation.TaskColorTransformationJob;
import com.wira.commons.shared.models.HTUser;
import com.wira.commons.shared.models.UserGroup;

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
			Node l;
			// By Setting the jbpm.usergroup.callback property with the call
			// back class full name, task service will use this to validate the
			// user/group exists and its permissions are ok.
			// System.setProperty("jbpm.usergroup.callback",
			// "org.jbpm.task.identity.LDAPUserGroupCallbackImpl");

			System.setProperty("jbpm.usergroup.callback", "org.jbpm.task.identity.DBUserGroupCallbackImpl");
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

	public LocalTaskService getTaskClient() {
		return sessionManager.getTaskClient();
	}

	/**
	 * This method clears the runtime environment when the application is shutdown
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

		// Document clone = summary.clone(); // Not sure why we clone here
		// clone.setId(summary.getId());
		initialParams.put("document", summary);

		Map<String, Value> vals = summary.getValues();
		Collection<Value> values = vals.values();

		for (Value val : values) {
			if (val != null) {
				initialParams.put(val.getKey(), val.getValue());
			}
		}

		assert summary.getProcessId() != null;
		String processId = summary.getProcessId();
		ProcessMigrationHelper.start(processId);

		ProcessInstance processInstance = sessionManager.startProcess(summary.getProcessId(), initialParams, summary);
		assert (ProcessInstance.STATE_ACTIVE == processInstance.getState());

	}

	public Task getSysTask(Long taskId) {
		return sessionManager.getTaskClient().getTask(taskId);
	}

	public void getProcessCounts(String processId, Map<TaskType, Integer> counts) {

		Long inbox = (Long) DB.getEntityManager().createNamedQuery("TasksAssignedByProcessIdAndStatus")
				.setParameter("language", "en-UK").setParameter("status", getStatusesForTaskType(TaskType.INBOX))
				.setParameter("processId", processId).getSingleResult();
		counts.put(TaskType.INBOX, inbox.intValue());

		Long participated = (Long) DB.getEntityManager().createNamedQuery("TasksAssignedByProcessIdAndStatus")
				.setParameter("language", "en-UK").setParameter("status", getStatusesForTaskType(TaskType.PARTICIPATED))
				.setParameter("processId", processId).getSingleResult();
		counts.put(TaskType.PARTICIPATED, participated.intValue());

		Long suspended = (Long) DB.getEntityManager().createNamedQuery("TasksAssignedByProcessIdAndStatus")
				.setParameter("language", "en-UK").setParameter("status", Status.Suspended)
				.setParameter("processId", processId).getSingleResult();
		counts.put(TaskType.SUSPENDED, suspended.intValue());

	}

	/**
	 * Count the number of tasks - completed/ or new
	 * 
	 * @param userId
	 * @param userId
	 * @param counts
	 */
	public void getCount(String processRefId, String userId, HashMap<TaskType, Integer> counts) {

		String processId = "";
		// Count drafts & initiated documents
		if (processRefId != null) {
			processId = DB.getProcessDao().getProcessId(processRefId);
			DocumentDaoHelper.getCounts(processId, userId, counts);
		} else {
			DocumentDaoHelper.getCounts(null, userId, counts);
		}

		if (processId == null) {
			processId = "";
		}

		// Count Tasks

		List<UserGroup> groups = LoginHelper.getHelper().getGroupsForUser(userId);
		List<String> groupIds = new ArrayList<>();
		for (UserGroup group : groups) {
			groupIds.add(group.getName());
		}

		if (groupIds.isEmpty()) {
			// DUGGAN - 25/10/2016 - ADDED TO FIX HIBERNATE 'unexpected end of subtree
			// errors'
			// CAUSED BY EMPTY IN() STATEMENTS IN THE HQL QUERIES BELOW
			groupIds.add("UNDEFINED");
		}

		Number count = (Number) DB.getEntityManager()
				.createNamedQuery("TasksAssignedCountAsPotentialOwnerByStatusWithGroups").setParameter("userId", userId)
				.setParameter("groupIds", groupIds).setParameter("language", "en-UK")
				.setParameter("processId", processId).setParameter("status", getStatusesForTaskType(TaskType.INBOX))
				.getSingleResult();
		counts.put(TaskType.INBOX, count.intValue());
		
		/**
		 * If John & James share the Role HOD_DEV John Claims, Starts and completes a
		 * task, should that task be presented to James as one of his completed tasks?
		 * This mechanism creates the possibility of this scenario happening TODO: Test
		 * the query with two users sharing a role
		 */
		Number count2 = (Number) DB.getEntityManager().createNamedQuery("TasksOwnedCount")
				.setParameter("userId", userId).setParameter("language", "en-UK").setParameter("processId", processId)
				.setParameter("status", Status.Completed).getSingleResult();

		// Inprogress participated requests
		int c = counts.get(TaskType.PARTICIPATED) == null ? 0 : counts.get(TaskType.PARTICIPATED);
		counts.put(TaskType.PARTICIPATED, count2.intValue() + c);

		Number count3 = (Number) DB.getEntityManager().createNamedQuery("TasksOwnedCount")
				.setParameter("userId", userId).setParameter("language", "en-UK").setParameter("processId", processId)
				.setParameter("status", Status.Suspended).getSingleResult();
		counts.put(TaskType.SUSPENDED, count3.intValue());

		counts.put(TaskType.UNASSIGNED, DB.getDocumentDao().getUnassigned(processId));
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
		case COMPLETED:
			statuses = Arrays.asList(Status.Completed);
			break;

		}
		return statuses;
	}

	public List<HTSummary> getTasksForUser(String processId, String userId, Long processInstanceId, int offset,
			int length) {
		return getTasksForUser(processId, userId, processInstanceId, false, offset, length);
	}

	@SuppressWarnings("unchecked")
	public List<HTSummary> getTasksForUser(String processId, String userId, Long processInstanceId,
			boolean isLoadAsAdmin, int offset, int length) {
		List<UserGroup> groups = LoginHelper.getHelper().getGroupsForUser(userId);
		List<String> groupIds = new ArrayList<>();
		for (UserGroup group : groups) {
			groupIds.add(group.getName());
		}

		if (groupIds.isEmpty()) {
			// DUGGAN - 25/10/2016 - ADDED TO FIX HIBERNATE 'unexpected end of subtree
			// errors'
			// CAUSED BY EMPTY IN() STATEMENTS IN THE HQL QUERIES BELOW
			groupIds.add("UNDEFINED");
		}

		List<TaskSummary> ts = new ArrayList<>();

		if (isLoadAsAdmin) {
			// Load Tasks As System administrator
			ts = DB.getEntityManager().createNamedQuery("TaskAssignedAsBizAdministratorByProcessId")
					.setParameter("userId", userId).setParameter("language", "en-UK")
					.setParameter("processInstanceId", processInstanceId == null ? -1 : processInstanceId.intValue())
					.setParameter("processId", processId == null ? "" : processId).setFirstResult(offset)
					.setMaxResults(length).getResultList();
		} else {

			ts = DB.getEntityManager().createNamedQuery("TasksOwnedBySubjectAndProcessId")
					.setParameter("userId", userId).setParameter("language", "en-UK").setParameter("groupIds", groupIds)
					.setParameter("processInstanceId", processInstanceId == null ? -1 : processInstanceId.intValue())
					.setParameter("processId", processId == null ? "" : processId).setFirstResult(offset)
					.setMaxResults(length).getResultList();
		}

		return translateSummaries(ts);
	}

	public List<HTSummary> searchTasks(String processId, String userId, SearchFilter filter) {
		filter.setProcessId(processId);
		filter.setUserId(userId);
		List<TaskSummary> tasks = DB.getDocumentDao().searchTasks(filter);

		return translateSummaries(tasks);
	}

	/**
	 * 
	 * @param processId
	 * @param taskName
	 * @return Input/Output parameter mappings for tasks
	 */
	public Collection<String> getProcessData(String processId, String taskName) {
		org.drools.definition.process.Process process = sessionManager.getProcess(processId);

		WorkflowProcessImpl workflow = (WorkflowProcessImpl) process;

		Node[] nodes = workflow.getNodes();

		for (Node node : nodes) {

			if (node instanceof HumanTaskNode) {
				HumanTaskNode htnode = (HumanTaskNode) node;
				Object name = htnode.getWork().getParameter("TaskName");

				if (name != null && name.equals(taskName)) {
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
	 *            This is the username of the user whose tasks are to be retrieved
	 * @param type
	 * @return List This is a list of human task summaries retrieved for the user
	 * 
	 */
	public List<HTSummary> getTasksForUser(String processId, String userId, TaskType type, int offset, int length) {

		String language = "en-UK";
		if (!LoginHelper.get().existsUser(userId)) {
			throw new RuntimeException("User " + userId + " Unknown!!");
		}

		if (processId == null) {
			processId = "";
		}

		if (type == null) {
			type = TaskType.INBOX;
		}

		List<TaskSummary> ts = new ArrayList<>();

		switch (type) {
		case PARTICIPATED:
		case COMPLETED:
			// approvals - show only items I have approved

			// ts = sessionManager.getTaskClient().getTasksOwned(
			// userId,
			// Arrays.asList(Status.Completed), language);

			ts = DB.getDocumentDao().getTasksOwnedPerProcess(processId, userId, Arrays.asList(Status.Completed),
					language, offset, length);

			break;
		case INBOX:
			ts = DB.getDocumentDao().getTasksOwnedPerProcess(processId, userId, getStatusesForTaskType(type), language,
					offset, length);
			break;

		case SUSPENDED:
			// ts = sessionManager.getTaskClient().getTasksOwned(
			// userId,
			// Arrays.asList(Status.Suspended), language);

			ts = DB.getDocumentDao().getTasksOwnedPerProcess(processId, userId, Arrays.asList(Status.Suspended),
					language, offset, length);
			break;
		case UNASSIGNED:
			ts = DB.getDocumentDao().getUnassignedTasks(processId, offset, length);
			break;
		default:
			break;
		}

		int toIndex = offset + length;
		if (offset > ts.size()) {
			return new ArrayList<HTSummary>();
		}

		toIndex = Math.min(toIndex, ts.size());

		return translateSummaries(ts.subList(offset, toIndex));

	}

	public List<HTSummary> translateSummaries(List<TaskSummary> ts) {

		List<HTSummary> tasks = new ArrayList<>();
		for (TaskSummary summary : ts) {

			HTSummary task = new HTSummary(summary.getId());
			Task master_task = sessionManager.getTaskClient().getTask(summary.getId());

			copy(task, master_task);
			tasks.add(task);
			// call for test
			// getTask(userId, summary.getId());

		}

		return tasks;
	}

	public BPMSessionManager getSessionManager() {
		return sessionManager;
	}

	public HTSummary getTaskSummary(Long taskId) {

		HTSummary summ = new HTSummary(taskId);
		copy(summ, getSysTask(taskId));

		return summ;
	}

	private void copy(HTSummary task, Task master_task) {

		Map<String, Object> content = getMappedData(master_task);
		Document doc = getDocument(content);

		assert doc != null;

		task.setCreated(master_task.getTaskData().getCreatedOn());
		task.setCaseNo(doc.getCaseNo());
		if (task.getCaseNo() == null) {
			Object subject = doc.get("subject");// for backward compatibility
			if (subject != null) {
				task.setCaseNo(subject.toString());
			}
		}
		task.setDescription(doc.getDescription());
		task.setPriority(doc.getPriority());
		task.setDocumentRef(doc.getId());
		task.setRefId(doc.getRefId());
		task.setCompletedOn(master_task.getTaskData().getCompletedOn());

		String processId = master_task.getTaskData().getProcessId();
		task.setProcessId(processId);
		task.setProcessInstanceId(master_task.getTaskData().getProcessInstanceId());

		task.setHasAttachment(DB.getAttachmentDao().getHasAttachment(doc.getId()));

		try {
			WorkflowProcessImpl process = (WorkflowProcessImpl) sessionManager.getProcess(processId);
			task.setProcessName(process.getName());

			Node node = getNode(master_task);
			task.setNodeId(node.getId());
			task.setNodeName(node.getName());
		} catch (Exception e) {
		}

		task.setHasAttachment(DB.getAttachmentDao().getHasAttachment(doc.getId()));
		Status status = master_task.getTaskData().getStatus();
		task.setStatus(HTStatus.valueOf(status.name().toUpperCase()));

		if (status == Status.Completed) {

			loadProgressInfo(task, master_task.getTaskData().getProcessInstanceId());

		} else {
			TaskDelegation taskdelegation = DB.getDocumentDao().getTaskDelegationByTaskId(master_task.getId());

			if (taskdelegation != null) {
				Delegate delegate = new Delegate(taskdelegation.getId(), taskdelegation.getTaskId(),
						taskdelegation.getUserId(), taskdelegation.getDelegateTo());
				task.setDelegate(delegate);
			}

			User user = null;
			if ((user = master_task.getTaskData().getActualOwner()) != null) {
				task.setTaskActualOwner(getUser(user.getId()));
			} else {
				task.setPotentialOwners(getPotentialOwners(master_task));
			}

		}

		List<I18NText> names = master_task.getNames();

		if (names.size() > 0) {
			task.setTaskName(names.get(0).getText());
		}

		Deadlines deadlines = master_task.getDeadlines();
		if (deadlines != null) {
			List<Deadline> startDeadlines = deadlines.getStartDeadlines();
			if (startDeadlines != null)
				if (startDeadlines.size() > 0) {
					Deadline deadline = startDeadlines.get(startDeadlines.size() - 1);
					Date date = deadline.getDate();
					task.setStartDateDue(date);
				}

			List<Deadline> endDeadlines = deadlines.getEndDeadlines();
			if (endDeadlines != null)
				if (endDeadlines.size() > 0) {
					Deadline deadline = endDeadlines.get(endDeadlines.size() - 1);
					Date date = deadline.getDate();
					task.setEndDateDue(date);
				}

		}

		try {
			// Exception thrown if process not started
			task.setName(getDisplayName(master_task));
		} catch (Exception e) {
		}

		if (doc.getRefId() != null) {
			Document model = null;
			model = DB.getDocumentDao().getDocJson(doc.getRefId());
			task.setDocStatus(model == null ? null : model.getStatus());
			task.setOwner(model.getOwner());
			task.setCurrentTaskName(model.getCurrentTaskName());
			task.setCurrentTaskId(model.getCurrentTaskId());
		}

		task.setPriority(doc.getPriority());
		task.setDocumentDate(doc.getDocumentDate());
		task.setValues(doc.getValues());
		if (task instanceof HTask) {
			task.setDetails(doc.getDetails());
			task.setValues(doc.getValues() == null ? new HashMap<String, Value>() : doc.getValues());
			task.setPriority(doc.getPriority());
			task.setDocumentDate(doc.getDocumentDate());

			// TaskDelegation taskdelegation =
			// DB.getDocumentDao().getTaskDelegationByTaskId(master_task.getId());
			//
			// if(taskdelegation!=null){
			// Delegate delegate = new Delegate(taskdelegation.getId(),
			// taskdelegation.getTaskId(), taskdelegation.getUserId(),
			// taskdelegation.getDelegateTo());
			// ((HTask)task).setDelegate(delegate);
			// }

		}
	}

	public HTUser getUser(String id) {

		return LoginHelper.get().getUser(id, false);
	}

	public String getPotentialOwners(Long taskId) {
		return getPotentialOwners(getSysTask(taskId));
	}

	public String getPotentialOwners(Task master_task) {

		List<OrganizationalEntity> entitiesList = master_task.getPeopleAssignments().getPotentialOwners();

		String entities = "";
		if (entities != null)
			for (OrganizationalEntity entity : entitiesList) {
				entities = entities.concat(entity.getId() + ", ");
			}

		if (!entities.isEmpty()) {
			entities = entities.substring(0, entities.length() - 2);
			return entities;
		}

		return null;
	}

	public List<String> getPotentialOwnersAsList(Task master_task) {

		List<OrganizationalEntity> entitiesList = master_task.getPeopleAssignments().getPotentialOwners();

		List<String> potOwners = new ArrayList<String>();
		for (OrganizationalEntity entity : entitiesList) {
			potOwners.add(entity.getId());
		}

		return potOwners;
	}

	public void loadProgressInfo(Doc task, long processInstanceId) {
		// then how far is the process now?
		// ProcessInstanceLog log =
		// JPAProcessInstanceDbLog.findProcessInstance(processInstanceId);
		int instanceStatus = DB.getProcessDao().getInstanceStatus(processInstanceId);
		if (instanceStatus == -1) {
			return;
		}

		if (instanceStatus == 2) {
			// Process Completed
			task.setProcessStatus(HTStatus.COMPLETED);
		} else {
			// where is my request?
			List<Object[]> actualOwners = DB.getProcessDao()
					.getCurrentActualOwnersByProcessInstanceId(processInstanceId, Status.Completed, false);

			// Task Id,
			for (Object[] row : actualOwners) {
				if (row[1] != null) {
					task.setTaskActualOwner(LoginHelper.get().getUser(row[1].toString(), false));
				}
			}

			List<Object[]> potOwners = DB.getProcessDao()
					.getCurrentPotentialOwnersByProcessInstanceId(processInstanceId, Status.Completed, false);

			String entities = "";
			// Task Id,
			for (Object[] row : potOwners) {
				if (row[1] != null) {
					entities = entities.concat(row[1].toString() + ", ");
				}
			}
			if (!entities.isEmpty()) {
				entities = entities.substring(0, entities.length() - 2);
				task.setPotentialOwners(entities);
			}
		}
	}

	private Value getValue(Object val) {

		Value value = null;

		if (val instanceof Boolean) {
			value = new BooleanValue((Boolean) val);
		}

		if (val instanceof String) {
			value = new StringValue(val.toString());
		}

		if (val instanceof Integer) {
			Long longVal = new Long((Integer) val);
			value = new LongValue(longVal);
		}
		if (val instanceof Long) {
			value = new LongValue((Long) val);
		}

		if (val instanceof Date) {
			value = new DateValue((Date) val);
		}

		if (val instanceof Value) {
			value = (Value) val;
		}

		return value;
	}

	/**
	 * This method retrieves the full task object, which provides more comprehensive
	 * details for a task
	 * 
	 * @param taskId
	 *            This is the task Id of the task to be retrieved
	 * @return HTask Human Task DTO object retrieved
	 */
	public HTask getTask(long taskId) {
		Task task = sessionManager.getTaskClient().getTask(taskId);
		return getTask(task);
	}

	public HTask getTask(Task task) {

		Long taskId = task.getId();
		// Human Task
		HTask myTask = new HTask(taskId);
		copy(myTask, task);
		// task.get

		List<I18NText> descriptions = task.getDescriptions();
		if (myTask.getDescription() == null) {
			myTask.setDescription(descriptions.get(0).getText());
		}

		List<I18NText> names = task.getNames();
		if (names.size() > 0) {
			String taskName = names.get(0).getText();
			myTask.setTaskName(taskName);
		}

		List<I18NText> subjects = task.getSubjects();// translations
		if (myTask.getCaseNo() == null) {
			myTask.setCaseNo(subjects.get(0).getText());
		}

		int priority = task.getPriority();
		if (myTask.getPriority() == null)
			myTask.setPriority(priority);

		// int version = task.getVersion();
		// myTask.setVersion(version);

		// deadlines.getEndDeadlines();
		// Delegation delegation = task.getDelegation();

		// PeopleAssignments assignments = task.getPeopleAssignments();
		// User user = assignments.getTaskInitiator();

		// List<OrganizationalEntity> entities = assignments.getRecipients();

		// myTask.setData(data);

		return myTask;
	}

	// public HTData getTaskData(Task task){
	//
	// // HT DATA
	// HTData data = new HTData();
	//
	// // TASK DATA
	// TaskData taskData = task.getTaskData();
	// String docType = taskData.getDocumentType();
	// data.setDocType(docType);
	//
	// long workId = taskData.getWorkItemId();
	// data.setWorkId(workId);
	//
	// // owner
	// User actualOwner = taskData.getActualOwner();
	// if (actualOwner != null) {
	// HTUser taskOwner = new HTUser(actualOwner.getId());
	// data.setActualOwner(taskOwner);
	// }
	//
	// // comments
	// List<Comment> comments = taskData.getComments();
	// if (comments != null)
	// for (Comment comment : comments) {
	// HTComment htComment = new HTComment();
	// htComment.setAddedAt(comment.getAddedAt());
	// htComment.setId(comment.getId());
	// htComment.setText(comment.getText());
	//
	// User addedBy = comment.getAddedBy();
	// if (addedBy != null)
	// htComment.setAddedBy(new HTUser(addedBy.getId()));
	//
	// }
	//
	// Date completedOn = taskData.getCompletedOn();
	// data.setCompletedOn(completedOn);
	//
	// User createdBy = taskData.getCreatedBy();
	// if (createdBy != null) {
	// data.setCreatedBy(new HTUser(createdBy.getId()));
	// }
	//
	// AccessType accessType = taskData.getDocumentAccessType();
	//
	// if (accessType != null) {
	// data.setDocAccessType(HTAccessType.valueOf(accessType.name()
	// .toUpperCase()));
	// }
	//
	// long contentId = taskData.getDocumentContentId();
	// data.setContentId(contentId);
	//
	// Status taskDataStatus = taskData.getStatus();
	// if (taskDataStatus != null) {
	// data.setStatus(HTStatus
	// .valueOf(taskDataStatus.name().toUpperCase()));
	// }
	//
	// String taskDataOutputType = taskData.getOutputType();
	// data.setOutputType(taskDataOutputType);
	//
	// Date expiryTime = taskData.getExpirationTime();
	// data.setExpiryTime(expiryTime);
	//
	// long parentId = taskData.getParentId();
	// data.setParentId(parentId);
	//
	// Status previousStatus = taskData.getPreviousStatus();
	// if (previousStatus != null) {
	// data.setPreviousStatus(HTStatus.valueOf(previousStatus.name()
	// .toUpperCase()));
	// }
	//
	// return data;
	// }

	public static Map<String, Object> getMappedData(Task task) {

		Long outputId = null;
		Map<String, Object> map = new HashMap<>();

		if (task.getTaskData() != null) {
			if (task.getTaskData().getStatus() == Status.Completed) {
				outputId = task.getTaskData().getOutputContentId();
				if (outputId != null) {
					map = getMappedDataByContentId(outputId);
				}
			}

			Long contentId = null;
			if (contentId == null && !map.containsKey("documentOut")) {
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
	 * This method returns the Values passed when the task was initiated. Several
	 * methods of retrieving these parameters are offered online but the use of
	 * {@link ContentMarshallerHelper} is what worked in this instance
	 * 
	 * <p>
	 * The use of {@link ObjectInputStream} to read the bytes failed with an
	 * {@link OptionalDataException}; trying to fix this did not work.
	 * 
	 * <p>
	 * Further, if the Content value of the JBPM task(<i>JBPM Task properties</i>)
	 * is set, it overrides any inputs(map) passed to the process when the task is
	 * created {@link #createApprovalRequest(HTSummary)}
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

		byte[] objectinBytes = sessionManager.getTaskClient().getContent(contentId).getContent();

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
	public void execute(long taskId, String userId, Actions action, Map<String, Object> values) {
		sessionManager.execute(taskId, userId, action, values);
		if (action == Actions.STOP) {

			HTask task = getTask(taskId);

			HTUser owner = task.getOwner();
			if (owner != null) {
				String names = owner.getFullName();
				if (names == null) {
					names = "Sir/ Madam";
				}
				String processName = getProcessName(task.getProcessId());
				String subject = task.getName() + " " + task.getCaseNo() + " Aborted";

				String email = "Dear " + names + "" + "<p>" + "Your Request " + processName + " - " + task.getCaseNo()
						+ " has been aborted. Kindly contact the "
						+ "Administrator if you did not request to abort the request.<p>" + "<p>Thank you.";

				EmailUtil.sendEmail(subject, email, task, owner.getEmail());
			}
		}

	}

	/**
	 * 
	 * @return PNG Diagram
	 * @throws IOException
	 */
	public InputStream getProcessMap(long processInstanceId) throws IOException {

		ProcessInstanceLog log = JPAProcessInstanceDbLog.findProcessInstance(processInstanceId);

		if (log == null) {
			return null;
		}

		int instanceStatus = log.getStatus();
		String processId = log.getProcessId();

		List<LocalAttachment> attachment = DB.getAttachmentDao()
				.getAttachmentsForProcessDef(DB.getProcessDao().getProcessDef(processId), true);

		if (attachment.size() != 1 || !attachment.get(0).getName().endsWith("svg")) {
			throw new RuntimeException("Cannot Generate ProcessMap; SVG Image not found");
		}

		byte[] svgImage = attachment.get(0).getAttachment();

		// Create a new Configuration object to set the padd to use in the label
		// of the tasks
		ProcessImageProcessorConfiguration config = new ProcessImageProcessorConfiguration();
		config.setDefaultextPad(20.0f);

		// Create a processor instance for test1.svg
		ProcessImageProcessor processor = new ProcessImageProcessor(new ByteArrayInputStream(svgImage), config);

		org.drools.definition.process.Process process = sessionManager.getProcess(processId);

		WorkflowProcessImpl wfprocess = (WorkflowProcessImpl) process;

		for (Node node : wfprocess.getNodes()) {

			long nodeId = node.getId();

			List<NodeInstanceLog> nodeLogInstance = JPAProcessInstanceDbLog.findNodeInstances(processInstanceId,
					new Long(nodeId).toString());

			if (nodeLogInstance.size() > 0) {
				// HumanTaskNode ht = (HumanTaskNode) node;
				String name = node.getName();

				boolean isCompletedNode = nodeLogInstance.size() % 2 == 0 || instanceStatus == 3;// Completed or exited
				processor.addTransformationJob(
						new TaskColorTransformationJob(name, isCompletedNode ? "#00ff00" : "#ff0000"));
			}
		}
		// apply the transformations
		processor.applyTransformationJobs(true);

		return processor.toPNG();
	}

	public List<NodeDetail> getWorkflowProcessDia(long processInstanceId) {

		ProcessInstanceLog log = JPAProcessInstanceDbLog.findProcessInstance(processInstanceId);

		if (log == null) {
			// --
			logger.warn("Invalid State : ProcessInstanceLog is null; ProcessInstanceId=" + processInstanceId
					+ "; Document = " + DocumentDaoHelper.getDocumentByProcessInstance(processInstanceId));
			return new ArrayList<>();
		}

		return getWorkflowProcessDia(log);
	}

	public List<NodeDetail> getWorkflowProcessDia(ProcessInstanceLog log) {

		List<NodeDetail> details = new ArrayList<>();

		long processInstanceId = log.getProcessInstanceId();

		String processId = log.getProcessId();
		// JPAProcessInstanceDbLog.findNodeInstances(processInstanceId);

		org.drools.definition.process.Process process = sessionManager.getProcess(processId);

		WorkflowProcessImpl wfprocess = (WorkflowProcessImpl) process;

		for (Node node : wfprocess.getNodes()) {

			long nodeId = node.getId();

			List<NodeInstanceLog> nodeLogInstance = JPAProcessInstanceDbLog.findNodeInstances(processInstanceId,
					new Long(nodeId).toString());

			if (nodeLogInstance.size() > 0) {
				// Executed nodes only
				Object x = node.getMetaData().get("x");
				Object y = node.getMetaData().get("x");
				Object width = node.getMetaData().get("width");
				Object height = node.getMetaData().get("height");

				if (node instanceof SubProcessNode) {
					SubProcessNode n = (SubProcessNode) node;
					List<ProcessInstanceLog> list = JPAProcessInstanceDbLog.findSubProcessInstances(processInstanceId);

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
				if (node instanceof HumanTaskNode || node instanceof StartNode || node instanceof EndNode) {
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

					if (node instanceof HumanTaskNode) {
						HumanTaskNode htn = (HumanTaskNode) node;
						Object groups = htn.getWork().getParameter("GroupId");
						Object actors = htn.getWork().getParameter("ActorId");

						if (groups != null) {
							loadGroups(groups.toString(), detail);
						}

						if (actors != null) {
							loadActors(actors.toString(), detail);
						}
					}

					// ((HumanTaskNode)node).getOutMappings().get("isApproved");
				}
			}
		}

		return details;

	}

	public List<TaskNode> getWorkflowProcessNodes(String processId) {

		List<TaskNode> details = new ArrayList<>();
		org.drools.definition.process.Process process = sessionManager.getProcess(processId);

		WorkflowProcessImpl wfprocess = (WorkflowProcessImpl) process;

		for (Node node : wfprocess.getNodes()) {

			if (node instanceof SubProcessNode) {
				SubProcessNode n = (SubProcessNode) node;
				details.addAll(getWorkflowProcessNodes(n.getProcessId()));
			}

			// Ignore all other nodes - Only work pick human Task Nodes
			if (node instanceof HumanTaskNode) {

				HumanTaskNode humanTask = (HumanTaskNode) node;

				TaskNode detail = new TaskNode();
				String name = node.getName();
				detail.setNodeId(node.getId());
				detail.setName(name);
				detail.setDisplayName(name);

				Object group = humanTask.getWork().getParameter("GroupId");
				Object actor = humanTask.getWork().getParameter("ActorId");
				detail.setGroupId(group == null ? null : group.toString());
				detail.setActorId(actor == null ? null : actor.toString());

				details.add(detail);
			}

		}

		return details;

	}

	/**
	 * Comma separated List of actors
	 * 
	 * @param actors
	 * @param detail
	 */
	private void loadActors(String actors, NodeDetail detail) {
		String[] actorIds = actors.split(",");
		for (String userId : actorIds) {
			HTUser user = LoginHelper.get().getUser(userId.trim());
			detail.addUser(user);
		}
	}

	/**
	 * Comma separated list of groups
	 * 
	 * @param groups
	 * @param detail
	 */
	private void loadGroups(String groups, NodeDetail detail) {
		String[] groupIds = groups.split(",");
		for (String groupId : groupIds) {
			UserGroup group = LoginHelper.get().getGroupById(groupId);
			List<HTUser> lst = LoginHelper.get().getUsersForGroup(groupId);
			detail.addAllUsers((ArrayList<HTUser>) lst);
			detail.addGroup(group);
		}
	}

	public Object getActors(Long processInstanceId, String nodeId) {
		//
		List<NodeInstanceLog> log = JPAProcessInstanceDbLog.findNodeInstances(processInstanceId, nodeId);

		logger.debug("Logs:: " + log);
		if (log != null && !log.isEmpty()) {
			NodeInstanceLog nil = log.get(0);

			logger.debug("Class >> " + nil.getClass());
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
	 * What happens to this process if you try to execute it and it fails/throws an
	 * exception
	 * 
	 * @param processId
	 * @param contextInfo
	 */
	public void startProcess(String processId, Map<String, Object> contextInfo) {
		sessionManager.startProcess(processId, contextInfo);
	}

	public static String getProcessDetails(Long processInstanceId) {

		ProcessInstanceLog log = JPAProcessInstanceDbLog.findProcessInstance(processInstanceId);
		String processId = log.getProcessId();

		return "[Process " + processId + "; ProcessInstanceId " + processInstanceId + "L]";
	}

	public void loadKnowledge(byte[] bytes, String processName) {
		sessionManager.loadKnowledge(bytes, processName);
	}

	public void loadKnowledge(List<byte[]> files, List<ResourceType> types, String rootProcess) {
		sessionManager.loadKnowledge(files, types, rootProcess);
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

	public String getDisplayName(Long taskId) {
		Task task = sessionManager.getTaskClient().getTask(taskId);
		return getDisplayName(task);
	}

	/**
	 * Returns the 'Name' property of a task (Node Name)
	 * <p>
	 * This process is inefficient - TODO: Find a better way to do this
	 * <p>
	 * 
	 * @param taskId
	 * @return
	 */
	public String getDisplayName(Task task) {
		//
		Node node = getNode(task);
		if (node != null) {
			return node.getName();
		}

		return null;
	}

	public Node getNode(Task task) {
		String processId = task.getTaskData().getProcessId();
		String taskName = getTaskName(task.getId());

		org.drools.definition.process.Process droolsProcess = sessionManager.getProcess(processId);
		WorkflowProcessImpl wfprocess = (WorkflowProcessImpl) droolsProcess;

		for (Node node : wfprocess.getNodes()) {

			if (node instanceof HumanTaskNode) {
				HumanTaskNode htnode = (HumanTaskNode) node;
				Object nodeTaskName = htnode.getWork().getParameter("TaskName");

				if (nodeTaskName != null)
					if (nodeTaskName.equals(taskName)) {
						return htnode;
					}

			}
		}

		return null;
	}

	public ProcessMappings getProcessDataMappings(long taskId) {
		Task task = getSysTask(taskId);
		String taskName = getTaskName(taskId);
		String processId = task.getTaskData().getProcessId();

		return getProcessDataMappings(processId, taskName);
	}

	public ProcessMappings getProcessDataMappings(String processId, String taskName) {
		ProcessMappings processData = new ProcessMappings();
		org.drools.definition.process.Process droolsProcess = sessionManager.getProcess(processId);
		WorkflowProcessImpl wfprocess = (WorkflowProcessImpl) droolsProcess;

		// log.debug("Globals:: "+wfprocess.get);

		for (Node node : wfprocess.getNodes()) {

			if (node instanceof HumanTaskNode) {
				HumanTaskNode htnode = (HumanTaskNode) node;
				Object nodeTaskName = htnode.getWork().getParameter("TaskName");

				logger.debug("1. Searching for TaskName> " + taskName + " :: Node TaskName >> " + nodeTaskName);

				if (nodeTaskName != null) {
					String nodeName = nodeTaskName.toString();
					if (nodeName.startsWith("#{")) {
						// dynamic Node Name
						nodeTaskName = htnode.getWork().getParameter("taskName");
					}
				}
				logger.debug("2. Searching for TaskName> " + taskName + " :: Node TaskName >> " + nodeTaskName);

				if (nodeTaskName != null)
					if (nodeTaskName.equals(taskName)) {
						HashMap<String, String> params = new HashMap<String, String>();
						params.putAll(htnode.getInMappings());
						processData.setInputMappings(params);

						params = new HashMap<String, String>();
						params.putAll(htnode.getOutMappings());
						processData.setOutMappings(params);
					}

			}
		}

		return processData;
	}

	public List<Long> getTaskIdsForUser(String userId) {
		Query query = DB.getEntityManager().createNamedQuery("TasksOwnedIds").setParameter("userId", userId)
				.setParameter("language", "en-UK").setParameter("status",
						Arrays.asList(Status.Completed, Status.Created, Status.InProgress, Status.Suspended,
								// Status.Error,
								// Status.Exited,
								// Status.Failed,
								// Status.Obsolete,
								Status.Ready, Status.Reserved));

		List<Long> ids = query.getResultList();

		return ids;
	}

	public String getProcessName(String processId) {
		String name = null;

		try {
			WorkflowProcessImpl process = (WorkflowProcessImpl) sessionManager.getProcess(processId);
			name = process.getName();
		} catch (Exception e) {
		}

		return name;
	}

	public void reassignTaskAsAdmin(Long taskId, String targetUserId) {
		Task task = getTaskClient().getTask(taskId);
		String username = "NoCurrentUser";
		User user = task.getTaskData().getActualOwner();
		if(user != null) {
			username = user.getId();
		}
		
		if(username.equals("NoCurrentUser")) {
			assignTask(taskId, targetUserId);
		}else {
			reassignTask(taskId, username, targetUserId);
		}
		
	}
	
	/**
	 * Borrowed from https://developer.jboss.org/thread/221510
	 * <p>
	 * Note that only the current owner of a task can reassign/ forward a task to someone else!, This means 
	 * the administrator is limited in as far as reassignments are concerned.
	 * <p>
	 * 
	 * @param taskId
	 * @param userId Current owner of the task
	 */
	public void reassignTask(Long taskId, String username, String targetUserId) {

		TaskService taskService = getTaskClient();
		Task task = getTaskClient().getTask(taskId);
		TaskData taskSummary = task.getTaskData();

		// check if username is in potentialOwners
		boolean userInPotentialOwners = false;
		for (OrganizationalEntity entity : task.getPeopleAssignments().getPotentialOwners()) {
			if (entity.getId().equals(username)) {
				userInPotentialOwners = true;
				break;
			}
		}

		if (!userInPotentialOwners) {
			org.jbpm.task.User actualOwner = task.getTaskData().getActualOwner();

			// we claim the task just to set actualOwner to current user
			if (taskSummary.getStatus() == Status.Ready
					&& (actualOwner == null || !actualOwner.getId().equals(username))) {
				taskService.claim(task.getId(), username);
				actualOwner = task.getTaskData().getActualOwner();
			}

			// if actualOwner is current user, we add it to potentialOwnersn,
			// just to call forward - see
			// https://community.jboss.org/thread/221465?tstart=0
			task.getPeopleAssignments().getPotentialOwners().add(actualOwner);
		}

		taskService.forward(taskId, username, targetUserId);

		// forward reassigns the task.
		// if the task was already assigned to other groups / users, those groups /
		// users are not removed from potentialOwners
		// we must do it explicitly
		PeopleAssignments peopleAssignments = task.getPeopleAssignments();
		List<OrganizationalEntity> clonedList = peopleAssignments.getPotentialOwners();
		for (OrganizationalEntity entity : clonedList) {
			if (!entity.getId().equals(targetUserId) && !entity.getId().equals("Administrator")) {
				peopleAssignments.getPotentialOwners().remove(entity);
			}
		}

	}

	/**
	 * Works for unassigned items!
	 * 
	 * @param taskId
	 * @param userId
	 */
	public void assignTask(Long taskId, String userId) {

		if (userId.equals("Administrator")) {
			throw new RuntimeException("Reassignment to Administrator is forbidden, please select a different user.");
		}

		Task task = DB.getEntityManager().find(Task.class, taskId);
		task.getTaskData().setStatus(Status.Ready);

		// OrganizationalEntity entity = DB.getEntityManager().createQuery("FROM User u
		// where u.userId=:userId").setParameter("userId", userId);

		PeopleAssignments peopleAssign = new PeopleAssignments();
		List<OrganizationalEntity> entities = new ArrayList<>();
		entities.add(new User(userId));
		peopleAssign.setPotentialOwners(entities);
		task.setPeopleAssignments(peopleAssign);
		DB.getEntityManager().persist(task);
		DB.getEntityManager().flush();
		DB.getEntityManager().refresh(task);

		// Email
		// HTask doc = getTask(task);
		// HTUser user = DB.getUserGroupDao().getUser(userId).toHTUser();
		// String fullName = user.getFullName();
		// if(fullName==null) {
		// fullName = "Sir/ Madam";
		// }
		//
		// String email = "Dear "+fullName+"<p>"
		// + "Task "+doc.getName()+" case number <a href=\"@@DocumentURL\">
		// "+doc.getCaseNo()+"</a>"
		// + " has been re-assigned to you by the workflow Administrator."
		// + "<p>"
		// + "Please click <href=\"@@DocumentURL\">here</a> to view the task."
		// + "<p>Thank you.<p>";
		// EmailUtil.sendEmail("New Task "+doc.getTaskName()+" "+doc.getCaseNo(),
		// email, doc, user.getEmail());
	}

	public void setCounts(HTUser htuser) {
		String userId = htuser.getUserId();
		assert userId != null;

		HashMap<TaskType, Integer> counts = new HashMap<>();
		getCount(null, userId, counts);

		htuser.setParticipated(counts.get(TaskType.PARTICIPATED));
		htuser.setInbox(counts.get(TaskType.INBOX));
	}

	public void upgradeProcessInstance(long processInstanceId, String processId) {
		sessionManager.upgradeProcessInstance(processInstanceId, processId);
	}

}
