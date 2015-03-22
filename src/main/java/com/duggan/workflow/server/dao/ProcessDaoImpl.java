package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.drools.runtime.process.ProcessInstance;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.jbpm.task.Status;
import org.jbpm.task.Task;
import org.jbpm.task.query.TaskSummary;

import com.duggan.workflow.client.ui.admin.processitem.NotificationCategory;
import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.dao.model.ADTaskNotification;
import com.duggan.workflow.server.dao.model.ADTaskStepTrigger;
import com.duggan.workflow.server.dao.model.ADTrigger;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.dao.model.TaskStepModel;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.shared.model.Actions;
import com.duggan.workflow.shared.model.CaseFilter;
import com.duggan.workflow.shared.model.HTStatus;
import com.duggan.workflow.shared.model.ProcessDef;
import com.duggan.workflow.shared.model.ProcessLog;
import com.duggan.workflow.shared.model.TaskLog;
import com.duggan.workflow.shared.model.TriggerType;

public class ProcessDaoImpl extends BaseDaoImpl {

	public ProcessDaoImpl(EntityManager em) {
		super(em);
	}

	public void save(ProcessDefModel model) {

		if (model.getId() != null) {
			em.merge(model);
		} else {
			em.persist(model);
		}

	}

	public ProcessDefModel getProcessDef(Long processDefId) {
		return em.find(ProcessDefModel.class, processDefId);
	}

	public ProcessDefModel getProcessDef(String processId) {
		return getSingleResultOrNull(em
				.createQuery(
						"FROM ProcessDefModel p "
								+ "where p.isArchived=:isArchived "
								+ "and p.processId=:processId")
				.setParameter("isArchived", false)
				.setParameter("processId", processId));
	}

	@SuppressWarnings("unchecked")
	public List<ProcessDefModel> getAllProcesses() {
		return em
				.createQuery(
						"FROM ProcessDefModel p where p.isArchived=:isArchived order by p.name")
				.setParameter("isArchived", false).getResultList();
	}

	public void remove(ProcessDefModel model) {
		List<LocalAttachment> attachments = DB.getAttachmentDao()
				.getAttachmentsForProcessDef(model);
		if (attachments != null) {
			for (LocalAttachment attachment : attachments) {
				DB.getAttachmentDao().delete(attachment);
			}
		}
		em.remove(model);
	}

	@SuppressWarnings("unchecked")
	public List<ProcessDefModel> getProcessesForDocType(ADDocType type) {

		return em
				.createQuery(
						"" + "select new ProcessDefModel(" + "p.id,"
								+ "p.name," + "p.processId,"
								+ "p.isArchived,"
								+ "p.description "
								+
								// "p.processDocuments" +
								") " + "from ProcessDefModel p "
								+ "join p.documentTypes dt "
								+ "where dt=:docType")
				.setParameter("docType", type).getResultList();

		// return em.createQuery("select new ProcessDefModel(" +
		// "p.id," +
		// "p.name," +
		// "p.processId," +
		// "p.isArchived," +
		// "p.description " +
		// //"p.processDocuments" +
		// ") " +
		// "FROM ProcessDefModel p " +
		// "where :docType in elements(p.documentTypes)")
		// .setParameter("docType", type)
		// .getResultList();
	}

	public void createStep(TaskStepModel step) {
		em.persist(step);
	}

	public List<TaskStepModel> getTaskSteps(String processId, Long nodeId) {
		String hql = "FROM TaskStepModel t where t.processDef.processId=:processId";
		if (nodeId != null) {
			hql = hql.concat(" and t.nodeId=:nodeId");
		} else {
			hql = hql.concat(" and t.nodeId is null");
		}

		hql = hql.concat(" order by sequenceNo");

		Query query = em.createQuery(hql).setParameter("processId", processId);

		if (nodeId != null) {
			query.setParameter("nodeId", nodeId);
		}

		return getResultList(query);
	}

	public TaskStepModel getTaskStepBySequenceNo(Long processDefId,
			Long nodeId, int sequenceNo) {

		String hql = "FROM TaskStepModel t where t.processDef.id=:processDefId and sequenceNo=:sequenceNo";
		if (nodeId != null) {
			hql = hql.concat(" and t.nodeId=:nodeId");
		} else {
			hql = hql.concat(" and t.nodeId is null");
		}

		Query query = em.createQuery(hql)
				.setParameter("sequenceNo", sequenceNo)
				.setParameter("processDefId", processDefId);

		if (nodeId != null) {
			query.setParameter("nodeId", nodeId);
		}

		return getSingleResultOrNull(query);
	}

	public List<TaskStepModel> getTaskStepsAfterSeqNo(Long processDefId,
			Long nodeId, int sequenceNo) {
		String hql = "FROM TaskStepModel t where t.processDef.id=:processDefId and sequenceNo>:sequenceNo";

		if (nodeId != null) {
			hql = hql.concat(" and t.nodeId=:nodeId");
		} else {
			hql = hql.concat(" and t.nodeId is null");
		}

		Query query = em.createQuery(hql)
				.setParameter("sequenceNo", sequenceNo)
				.setParameter("processDefId", processDefId);

		if (nodeId != null) {
			query.setParameter("nodeId", nodeId);
		}

		return getResultList(query);
	}

	public int getStepCount(Long processDefId, Long nodeId) {

		String hql = "select count (t) FROM TaskStepModel t where t.processDef.id=:processDefId";

		if (nodeId != null) {
			hql = hql.concat(" and t.nodeId=:nodeId");
		} else {
			hql = hql.concat(" and t.nodeId is null");
		}

		Query query = em.createQuery(hql).setParameter("processDefId",
				processDefId);

		if (nodeId != null) {
			query.setParameter("nodeId", nodeId);
		}

		Number count = getSingleResultOrNull(query);
		return count.intValue();
	}

	public List<ADTrigger> getTriggers() {
		Query query = em.createQuery("FROM ADTrigger t where isActive=:active")
				.setParameter("active", 1);

		return getResultList(query);
	}

	public int getTaskCount(Long taskStepId, TriggerType type) {
		Query query = em
				.createQuery(
						"SELECT COUNT(t) from ADTaskStepTrigger t "
								+ "where t.taskStep.id=:taskStepId "
								+ "and t.type=:type")
				.setParameter("taskStepId", taskStepId)
				.setParameter("type", type);

		Number count = getSingleResultOrNull(query);

		return count.intValue();
	}

	public List<ADTaskStepTrigger> getTaskStepTriggers(Long taskStepId,
			TriggerType type) {
		Query query = em
				.createQuery(
						"FROM ADTaskStepTrigger t where t.isActive=:active "
								+ "and t.taskStep.id=:taskStepId and t.type=:type")
				.setParameter("active", 1)
				.setParameter("taskStepId", taskStepId)
				.setParameter("type", type);

		return getResultList(query);
	}

	public void cascadeDeleteTrigger(ADTrigger po) {

		Query query = em.createQuery(
				"delete ADTaskStepTrigger t where t.trigger=:trigger")
				.setParameter("trigger", po);
		query.executeUpdate();

		em.remove(po);
	}

	public void cascadeDelete(TaskStepModel taskStep) {

		Query query = em.createQuery(
				"delete ADTaskStepTrigger t where t.taskStep=:taskStep")
				.setParameter("taskStep", taskStep);
		query.executeUpdate();

		em.remove(taskStep);
	}

	public List<Object[]> getCurrentActualOwnersByProcessInstanceId(
			long processInstanceId, Status targetStatus, boolean isEqualToStatus) {

		String sql = "select id,actualowner_id from task "
				+ " inner join peopleassignments_potowners o on (o.task_id=id) "
				+ " where processinstanceid=? "
				+ (isEqualToStatus ? " and status=?" : " and status!=?");

		Query query = em.createNativeQuery(sql)
				.setParameter(1, processInstanceId)
				.setParameter(2, targetStatus.name());

		return getResultList(query);

	}

	public List<Object[]> getCurrentPotentialOwnersByProcessInstanceId(
			long processInstanceId, Status targetStatus, boolean isEqualToStatus) {

		String sql = "select id,o.entity_id from task "
				+ " inner join peopleassignments_potowners o on (o.task_id=id) "
				+ " where processinstanceid=? "
				+ (isEqualToStatus ? " and status=?" : " and status!=?");

		Query query = em.createNativeQuery(sql)
				.setParameter(1, processInstanceId)
				.setParameter(2, targetStatus.name());

		return getResultList(query);

	}

	public int getInstanceStatus(Long processInstanceId) {
		if (processInstanceId == null) {
			return -1;
		}

		String sql = "select status from processinstancelog where processinstanceid=:processInstanceId";
		Number status = getSingleResultOrNull(em.createNativeQuery(sql)
				.setParameter("processInstanceId", processInstanceId));

		return status == null ? -1 : status.intValue();
	}

	public ADTaskNotification getTaskNotificationById(Long id) {

		return getSingleResultOrNull(em.createQuery(
				"from ADTaskNotification t where t.id=:id").setParameter("id",
				id));
	}

	public ADTaskNotification getTaskNotification(Long nodeId, String stepName,
			String processId, NotificationCategory category, Actions action) {
		ProcessDefModel def = getProcessDef(processId);
		if (def == null) {
			return null;
		}

		return getTaskNotification(nodeId, stepName, def.getId(), category,
				action);
	}

	public ADTaskNotification getTaskNotification(Long nodeId, String stepName,
			Long processDefId, NotificationCategory category, Actions action) {

		return getSingleResultOrNull(em
				.createQuery(
						"from ADTaskNotification t where "
								+ "t.nodeId=:nodeId and "
								// + "t.stepName=:stepName and "
								+ "t.processDefId=:processDefId and "
								+ "t.action=:action and "
								+ "t.category=:category")
				.setParameter("nodeId", nodeId)
				// .setParameter("stepName", stepName)
				.setParameter("processDefId", processDefId)
				.setParameter("action", action)
				.setParameter("category", category));
	}

	/**
	 * 
	 * @param processInstanceId
	 * @param language
	 *            default 'en-UK'
	 * @return
	 */
	public List<TaskLog> getProcessLog(Long processInstanceId, String language) {
		if (language == null) {
			language = "en-UK";
		}

		List<TaskLog> logs = new ArrayList<>();
		String sql = "select t.id,t.status,t.createdon, t.completedon, t.activationtime,t.expirationtime,"
				+ "t.processinstanceid, "
				+ "t.actualowner_id, i.text from task t "
				//+ "left join peopleassignments_potowners p  on p.task_id=t.id "
				+ "inner join i18ntext i on i.task_names_id=t.id "
				+ "where processinstanceid=:processinstanceid order by t.id";

		Query query = em.createNativeQuery(sql)
				.setParameter("processinstanceid", processInstanceId);

		List<Object[]> rows = getResultList(query);

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			Long id = (value = row[i++]) == null ? null : new Long(
					value.toString());
			String status = (value = row[i++]) == null ? null : value.toString();
			Date createdon = (value = row[i++]) == null ? null : (Date)value;
			Date completedon = (value = row[i++]) == null ? null : (Date)value;
			Date activationtime = (value = row[i++]) == null ? null : (Date)value;
			Date expirationtime= (value = row[i++]) == null ? null : (Date)value;
			Long processinstanceid = (value = row[i++]) == null ? null : new Long(
					value.toString());
			String actualOwner = (value = row[i++]) == null ? null : value
					.toString();
			String taskName = (value = row[i++]) == null ? null : value.toString();
			
			TaskLog log  = new TaskLog();
			log.setTaskId(id);
			log.setStatus(status);
			log.setCreatedon(createdon);
			log.setCompletedon(completedon);
			log.setActivationtime(activationtime);
			log.setExpirationtime(expirationtime);
			log.setProcessinstanceid(processinstanceid);
			if(actualOwner!=null){
				log.setActualOwner(JBPMHelper.get().getUser(actualOwner));
			}else{
				log.setPotOwner(JBPMHelper.get().getPotentialOwners(id));
			}
			
			try{
				log.setTaskName(JBPMHelper.get().getDisplayName(id));
			}catch(Exception e){
				log.setTaskName(taskName);
				log.setProcessLoaded(false);
			} 			
			
			logs.add(log);
			
		}
		return logs;
	}
	
	public List<ProcessLog> getProcessInstances(CaseFilter filter){
		if(filter==null){
			filter = new CaseFilter();
		}
		
		String sql = "select d.id docId, "
				+ "t.id taskid, "
				+ "l.processinstanceid, "
				+ "l.processid, "
				+ "l.start_date,l.end_date,"
				+ "d.subject caseNo, "
				+ "l.status processstatus,"
				+ "t.status, "
				+ "concat(u1.firstname,' ' ,u1.lastname) createdby,"
				+ "concat(u.firstname,' ' ,u.lastname) taskowner,"
				+ "i.text, "
				+ "(select array_to_string(array_agg(concat(b.firstname,' ' ,b.lastname,g.fullname)),',') "
				+ "from peopleassignments_potowners po "
				+ "left join buser b  on (b.userid=po.entity_id) "
				+ "left join bgroup g on (g.name=po.entity_id) "
				+ "where task_id=t.id) potowners "
				+ "from processinstancelog l "
				+ "left join task t "
				+ "on (t.processinstanceid=l.processinstanceid and t.status!='Completed') "
				+ "inner join localdocument d "
				+ "on (d.processinstanceid=l.processinstanceid)  "
				+ "left join buser u on (u.userid=t.actualowner_id) "
				+ "left join buser u1 on (u1.userid=d.createdby) "
				+ "left join i18ntext i on i.task_names_id=t.id ";
		
			boolean isFirst = true;
			
			if(filter.getProcessId()!=null){
				if(isFirst){
					sql = sql +" where ";
				}else{
					sql = sql +" and ";
				}
				isFirst = false;
				
				sql = sql+" l.processid=:processId";
			}
			
			if(filter.getCaseNo()!=null){
				if(isFirst){
					sql = sql +" where ";
				}else{
					sql = sql +" and ";
				}
				isFirst = false;
				
				sql = sql+" d.subject ilike :caseNo";
			}
			
			if(filter.getUserId()!=null){
				if(isFirst){
					sql = sql +" where ";
				}else{
					sql = sql +" and ";
				}
				isFirst = false;
				
				sql = sql+" (u.userid= :userId or u1.userid= :userId)";
			}
			
			sql = sql+ " order by l.processinstanceid desc";
		
		Query query = em.createNativeQuery(sql);
		if(filter.getProcessId()!=null){
			query.setParameter("processId", filter.getProcessId());
		}
		
		if(filter.getCaseNo()!=null){
			query.setParameter("caseNo", "%"+filter.getCaseNo()+"%");
		}
		
		if(filter.getUserId()!=null){
			query.setParameter("userId", filter.getUserId());
		}
		
		List<Object[]> rows = getResultList(query);
		List<ProcessLog> logs = new ArrayList<>();
		
		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			Long docId = (value = row[i++]) == null ? null : new Long(
					value.toString());
			Long taskId = (value = row[i++]) == null ? null : new Long(
					value.toString());
			Long processinstanceid = (value = row[i++]) == null ? null : new Long(
					value.toString());
			String processId = (value = row[i++]) == null ? null : value.toString();
			Date startDate = (value = row[i++]) == null ? null : (Date)value;
			Date endDate = (value = row[i++]) == null ? null : (Date)value;
			String caseNo = (value = row[i++]) == null ? null : value.toString();
			int processStatus = (value = row[i++]) == null ? null : new Integer(
					value.toString());
			String taskStatus = (value = row[i++]) == null ? null : value.toString();
			String initiator = (value = row[i++]) == null ? null : value.toString();
			String taskOwner = (value = row[i++]) == null ? null : value.toString();
			String taskName = (value = row[i++]) == null ? null : value.toString();
			
			String potOwners = (value = row[i++]) == null ? null : value.toString();
			
			ProcessLog log  = new ProcessLog();
			log.setTaskId(taskId);
			log.setTaskOwner(taskOwner);
			log.setCaseNo(caseNo);
			log.setDocId(docId);
			log.setInitiator(initiator);
			log.setPotOwners(potOwners);
			log.setProcessId(processId);
			log.setProcessinstanceid(processinstanceid);
			log.setTaskStatus(taskStatus);
			log.setProcessState(processStatus);
			log.setStartDate(startDate);
			log.setEndDate(endDate);
			try{
				log.setTaskName(JBPMHelper.get().getDisplayName(taskId));
			}catch(Exception e){
				log.setTaskName(taskName);
			} 	
			
			try{
				log.setProcessName(JBPMHelper.get().getProcessName(processId));
			}catch(Exception e){
				log.setProcessName(processId);
			} 	
			
			logs.add(log);
			
		}
		return logs;
	}

}
