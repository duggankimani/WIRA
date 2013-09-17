package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.PO;
import com.duggan.workflow.server.dao.model.ProcessDefModel;
import com.duggan.workflow.server.dao.model.ProcessDocModel;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.DocType;

public class ProcessDaoImpl {

	EntityManager em;
	
	public ProcessDaoImpl(EntityManager em){
		this.em = em;
	}
	
	protected void prepare(PO model){
		if(model.getId()==null){
			model.setCreated(new Date());
			model.setCreatedBy(SessionHelper.getCurrentUser().getId());
		}else{
			model.setUpdated(new Date());
			model.setUpdatedBy(SessionHelper.getCurrentUser().getId());
		}
	}
	
	public void save(ProcessDefModel model){
		prepare(model);
		List<ProcessDocModel> docs = model.getProcessDocuments();
		for(ProcessDocModel doc: docs){
			prepare(doc);
		}
		em.persist(model);
	}
	
	public void save(ProcessDocModel model){
		prepare(model);
		em.persist(model);
	}
	
	public ProcessDefModel getProcessDef(Long processDefId){
		return em.find(ProcessDefModel.class, processDefId);
	}
	
	public ProcessDocModel getProcessDoc(Long processDocId){
		return em.find(ProcessDocModel.class, processDocId);
	}
	
	public ProcessDocModel getProcessDoc(DocType docType){
		List lst =  em.createQuery("FROM ProcessDocModel d where d.docType=:docType")
				.setParameter("docType", docType)
				.getResultList();
		
		if(lst.isEmpty())
			return null;
		
		return (ProcessDocModel)lst.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProcessDefModel> getAllProcesses(){
		return em.createQuery("FROM ProcessDefModel p where p.isArchived=:isArchived")
				.setParameter("isArchived", false).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ProcessDocModel> getAllProcessesDocs(){
		return em.createQuery("FROM ProcessDocModel d where p.isArchived=:isArchived")
				.setParameter("isArchived", false)
				.getResultList();
	}

	public void remove(ProcessDefModel model) {
		em.remove(model);
	}
}
