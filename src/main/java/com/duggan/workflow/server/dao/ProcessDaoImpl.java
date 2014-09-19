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
		int sequence = step.getSequenceNo();
		ProcessDefModel model = step.getProcessDef();
		if(sequence==0){			
			int size = model.getTaskSteps().size();
			step.setSequenceNo(sequence = size+1);
		}else{
			for(TaskStepModel s: model.getTaskSteps()){
				if(!s.equals(step)){
					if(s.getSequenceNo()>= sequence){
						s.setSequenceNo(s.getSequenceNo()+1);
						em.persist(s);
					}
				}				
			}
		}
		
		em.persist(step);
	}


	public List<TaskStepModel> getTaskSteps(String processId, Long nodeId) {
		String hql = "FROM TaskStepModel t where t.processDef.processId=:processId";
		if(nodeId!=null){
			hql = hql.concat(" and t.nodeId=:nodeId");
		}else{
			hql = hql.concat(" and t.nodeId is null");
		}
			
		Query query = em.createQuery(hql)
				.setParameter("processId", processId);
		
		if(nodeId!=null){
			query.setParameter("nodeId", nodeId);
		}
		
		return getResultList(query);
	}
	
}
