package com.duggan.workflow.server.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.dao.model.TaskStepModel;
import com.duggan.workflow.server.db.DB;

public class ProcessDaoImpl extends BaseDaoImpl{
	
	public ProcessDaoImpl(EntityManager em){
		super(em);
	}
	
	
	public void save(ProcessDefModel model){
		
		if(model.getId()!=null){
			em.merge(model);
		}else{
			em.persist(model);
		}
		
	}
	
	public ProcessDefModel getProcessDef(Long processDefId){
		return em.find(ProcessDefModel.class, processDefId);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProcessDefModel> getAllProcesses(){
		return em.createQuery("FROM ProcessDefModel p where p.isArchived=:isArchived order by p.name")
				.setParameter("isArchived", false).getResultList();
	}
	
	public void remove(ProcessDefModel model) {
		List<LocalAttachment> attachments = DB.getAttachmentDao().getAttachmentsForProcessDef(model);
		if(attachments!=null){
			for(LocalAttachment attachment: attachments){
				DB.getAttachmentDao().delete(attachment);
			}
		}
		em.remove(model);
	}

	@SuppressWarnings("unchecked")
	public List<ProcessDefModel> getProcessesForDocType(ADDocType type) {
		
		return em.createQuery("" +
				"select new ProcessDefModel(" +
				"p.id," +
				"p.name," +
				"p.processId," +
				"p.isArchived," +
				"p.description " +
				//"p.processDocuments" +
				") " +
				"from ProcessDefModel p " +
				"join p.documentTypes dt " +
				"where dt=:docType")
				.setParameter("docType", type)
				.getResultList();
		
//		return em.createQuery("select new ProcessDefModel(" +
//				"p.id," +
//				"p.name," +
//				"p.processId," +
//				"p.isArchived," +
//				"p.description " +
//				//"p.processDocuments" +
//				") " +
//				"FROM ProcessDefModel p " +
//				"where :docType in elements(p.documentTypes)")
//				.setParameter("docType", type)
//				.getResultList();
	}
	
	public void createStep(TaskStepModel step){
		em.persist(step);
	}


	public List<TaskStepModel> getTaskSteps(String processId, Long nodeId) {
		String hql = "FROM TaskStepModel t where t.processDef.processId=:processId";
		if(nodeId!=null){
			hql = hql.concat(" and t.nodeId=:nodeId");
		}else{
			hql = hql.concat(" and t.nodeId is null");
		}
		
		hql = hql.concat(" order by sequenceNo");
			
		Query query = em.createQuery(hql)
				.setParameter("processId", processId);
		
		if(nodeId!=null){
			query.setParameter("nodeId", nodeId);
		}
		
		return getResultList(query);
	}


	public TaskStepModel getTaskStepBySequenceNo(Long processDefId, Long nodeId,
			int sequenceNo) {
		
		String hql = "FROM TaskStepModel t where t.processDef.id=:processDefId and sequenceNo=:sequenceNo";
		if(nodeId!=null){
			hql = hql.concat(" and t.nodeId=:nodeId");
		}else{
			hql = hql.concat(" and t.nodeId is null");
		}
			
		Query query = em.createQuery(hql)
				.setParameter("sequenceNo", sequenceNo)
				.setParameter("processDefId", processDefId);
		
		if(nodeId!=null){
			query.setParameter("nodeId", nodeId);
		}
		
		return getSingleResultOrNull(query);
	}


	public List<TaskStepModel> getTaskStepsAfterSeqNo(Long processDefId,
			Long nodeId, int sequenceNo) {
		String hql = "FROM TaskStepModel t where t.processDef.id=:processDefId and sequenceNo>:sequenceNo";
		
		if(nodeId!=null){
			hql = hql.concat(" and t.nodeId=:nodeId");
		}else{
			hql = hql.concat(" and t.nodeId is null");
		}
			
		Query query = em.createQuery(hql)
				.setParameter("sequenceNo", sequenceNo)
				.setParameter("processDefId", processDefId);
		
		if(nodeId!=null){
			query.setParameter("nodeId", nodeId);
		}
		
		return getResultList(query);
	}


	public int getStepCount(Long processDefId, Long nodeId) {
		
		String hql = "select count (t) FROM TaskStepModel t where t.processDef.id=:processDefId";
		
		if(nodeId!=null){
			hql = hql.concat(" and t.nodeId=:nodeId");
		}else{
			hql = hql.concat(" and t.nodeId is null");
		}
			
		Query query = em.createQuery(hql)
				.setParameter("processDefId", processDefId);
		
		if(nodeId!=null){
			query.setParameter("nodeId", nodeId);
		}
		
		Number count = getSingleResultOrNull(query);
		return count.intValue();
	}
	
}
