package com.duggan.workflow.server.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.ADDocType;
import com.duggan.workflow.server.dao.model.LocalAttachment;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.db.DB;

public class ProcessDaoImpl extends BaseDaoImpl{
	
	public ProcessDaoImpl(EntityManager em){
		super(em);
	}
	
	
	public void save(ProcessDefModel model){
		prepare(model);
		
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
		return em.createQuery("FROM ProcessDefModel p where p.isArchived=:isArchived")
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
	
}
