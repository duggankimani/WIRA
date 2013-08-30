package com.duggan.workflow.server.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jbpm.task.query.TaskSummary;

import com.duggan.workflow.client.model.TaskType;
import com.duggan.workflow.server.dao.model.DocumentModel;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.SearchFilter;
import com.google.gwt.dev.util.collect.HashMap;

/**
 * 
 * @author duggan
 *
 */
public class DocumentDaoImpl {

	EntityManager em;
	
	public DocumentDaoImpl(EntityManager em){
		this.em = em;
	}
	
	@SuppressWarnings("unchecked")
	public List<DocumentModel> getAllDocuments(DocStatus status){
		
		return em.createQuery("FROM DocumentModel d where status=:status and createdBy=:createdBy").
				setParameter("status", status).
				setParameter("createdBy", SessionHelper.getCurrentUser().getId()).
				getResultList();
	}
	
	public DocumentModel getById(Long id){
		List lst = em.createQuery("FROM DocumentModel d where id= :id").setParameter("id", id).getResultList();
		
		if(lst.size()>0){
			return (DocumentModel)lst.get(0);
		}
		
		return null;
	}
	
	public DocumentModel saveDocument(DocumentModel document){
		
		if(document.getId()==null){
			document.setCreated(new Date());
			document.setStatus(DocStatus.DRAFTED);
			document.setCreatedBy(SessionHelper.getCurrentUser().getId());
		}else{
			document.setUpdated(new Date());
			document.setUpdatedBy(SessionHelper.getCurrentUser().getId());
		}
		
		em.persist(document);
		
		/*
		 * Do not flush - This reflects data in the database immediately and the BTM transaction 
		 * in my tests so far cannot rollback the flushed data - You can actually query the new values 
		 * directly from the database even as the transaction is ongoing - Could it be the isolation
		 * level being used in the db?
		 * 
		 */
		//em.flush();
		return document;
	}
	
	public void delete(DocumentModel document){
		
		em.remove(document);
		
	}

	public Integer count(DocStatus status) {

		 Long value = (Long)em.createQuery("select count(d) FROM DocumentModel d where status=:status and createdBy=:createdBy").
				setParameter("status", status).
				setParameter("createdBy", SessionHelper.getCurrentUser().getId()).getSingleResult();
		 
		 return value.intValue();
	}

	public List<DocumentModel> search(String subject) {
		List lst = em.createQuery("FROM DocumentModel d where subject like :subject")
				.setParameter("subject", "%"+subject+"%")
				.getResultList();
				
		return lst;
	}

	public DocType getDocumentType(Long documentId) {
		
		DocType type = (DocType)em.createQuery("select d.type FROM DocumentModel d where d.id=:documentId")
		.setParameter("documentId", documentId).getSingleResult();
		
		return type;
	}

	public Long getProcessInstanceIdByDocumentId(Long documentId) {
		Object processInstanceId = em.createQuery("select n.processInstanceId FROM DocumentModel n" +
				" where n.id=:documentId")
				.setParameter("documentId", documentId)
				.getSingleResult();
	
		return processInstanceId==null? null : (Long)processInstanceId;
	}
	
	public DocumentModel getDocumentByProcessInstanceId(Long processInstanceId){
		List lst = em.createQuery("FROM DocumentModel d where processInstanceId= :processInstanceId " +
				"and createdBy=:createdBy")
				.setParameter("processInstanceId", processInstanceId)
				.setParameter("createdBy", SessionHelper.getCurrentUser().getId())
				.getResultList();
		
		if(lst.size()>0){
			return (DocumentModel)lst.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unused")
	public List<DocumentModel> search(SearchFilter filter){
		
		String subject=filter.getSubject();
		Date startDate= filter.getStartDate();
		Date endDate = filter.getEndDate();
		Integer priority=filter.getPriority();
		String phrase=filter.getPhrase();
		DocType docType=filter.getDocType();
		Boolean hasAttachment=filter.hasAttachment();

		Map<String, Object> params = new HashMap<>();
		
		StringBuffer query = new StringBuffer("FROM DocumentModel d where ");
			
		boolean isFirst=true;
		if(subject!=null){
			isFirst=false;
			query.append("subject like :subject");
			params.put("subject", "%"+subject+"%");
		}
		
		if(!isFirst){
			query.append(" AND ");
			isFirst=true;//so that we dont add another AND
		}
		
		if(startDate!=null && endDate!=null){
			isFirst=false;
			query.append("created>:startDate " +
					"and " +
					"created<:endDate");
			
			params.put("startDate", startDate);
			params.put("endDate", endDate);
		}else if(startDate!=null){
			isFirst=false;
			query.append("STR_TO_DATE(DATE_FORMAT(created, '%d/%m/%y'), '%d/%m/%y')=:startDate");
			params.put("startDate", startDate);
		}else if(endDate!=null){
			isFirst=false;
			query.append("STR_TO_DATE(DATE_FORMAT(created, '%d/%m/%y'), '%d/%m/%y')=:endDate");
			params.put("endDate", endDate);
		}
		
		if(!isFirst){
			query.append(" AND ");
			isFirst=true;//so that we dont add another AND
		}
		
		if(priority!=null){
			isFirst=false;
			query.append("priority=:priority");
			params.put("priority", priority);
		}
		
		if(!isFirst){
			query.append(" AND ");
			isFirst=true;//so that we dont add another AND
		}
		
		if(phrase!=null){
			isFirst=false;
			query.append("description like :phrase");
			params.put("phrase", "%"+phrase+"%");
		}
		
		if(!isFirst){
			query.append(" AND ");
			isFirst=true;//so that we dont add another AND
		}
		
		if(docType!=null){
			isFirst=false;
			query.append("type=:docType");
			params.put("docType", docType);
		}
		
		if(!isFirst){
			query.append(" AND ");
			isFirst=true;//so that we dont add another AND
		}
		
		{
			isFirst=false;
			query.append("createdBy=:createdBy");
			//params.put("createdBy", SessionHelper.getCurrentUser().getId());
			params.put("createdBy", "calcacuervo");
		}
		
		if(isFirst){
			//we ended up with an and at the end
			query = new StringBuffer(query.subSequence(0, query.length()-5).toString());
		}
		
		System.err.println("SEARCH SQL - "+query.toString());
		
		Query hquery = em.createQuery(query.toString());
		Set<String> keys = params.keySet();
		
		for(String key: keys){
			hquery.setParameter(key, params.get(key));
		}
		
		List list = hquery.getResultList();
		
		return list;
	}
	
	public List<TaskSummary> search(SearchFilter filter, boolean tasks){
		String query = "select "+
		   "distinct "+
		   "new org.jbpm.task.query.TaskSummary( "+
		    "t.id, "+
		    "t.taskData.processInstanceId, "+
		    "name.text, "+
		    "subject.text, "+
		    "description.text, "+
		    "t.taskData.status, "+
		    "t.priority, "+
		    "t.taskData.skipable, "+
		    "actualOwner, "+
		    "createdBy, "+
		    "t.taskData.createdOn, "+
		    "t.taskData.activationTime, "+
		    "t.taskData.expirationTime, "+
		    "t.taskData.processId, "+
		    "t.taskData.processSessionId) "+
		"from "+
		    "Task t "+ 
		    "left join t.taskData.createdBy as createdBy "+
		    "left join t.taskData.actualOwner as actualOwner "+ 
		    "left join t.subjects as subject "+
		    "left join t.descriptions as description "+
		    "left join t.names as name, "+
		    "OrganizationalEntity potentialOwners "+
		"where "+
		    "t.archived = 0 and "+
		    "(t.taskData.actualOwner.id = :userId "+
		   " or  "+
		    	"( potentialOwners.id = :userId or potentialOwners.id in (:groupIds) ) and "+
		    	"potentialOwners in elements ( t.peopleAssignments.potentialOwners ) "+
		    ") "+
		    
		     "and "+

		    "( "+
		    "name.language = :language "+
		    "or t.names.size = 0 "+
		    ") and "+
		    
		    "( "+
		   " subject.language = :language "+
		   " or t.subjects.size = 0 "+
		   " ) and "+

		    "( "+
		    "description.language = :language "+
		    "or t.descriptions.size = 0 "+ 
		    ")and  "+
		    "t.taskData.processInstanceId = :processInstanceId "+
		    "and "+
		    "t.taskData.expirationTime is null ";
		
		
		
		
		
		Query hquery = em.createQuery(query);		
		List list = hquery.getResultList();
		
		return list;
	}

}
