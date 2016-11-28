package com.duggan.workflow.server.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import com.duggan.workflow.server.dao.model.ADOutputDoc;
import com.duggan.workflow.server.dao.model.LocalAttachment;

public class OutputDocumentDao extends BaseDaoImpl {

	public ADOutputDoc getOuputDocument(Long id) {
		return getById(ADOutputDoc.class, id);
	}

	public List<ADOutputDoc> getOutputDocuments() {
		
		String query = "FROM ADOutputDoc WHERE isActive=1";
		return getResultList(getEntityManager().createQuery(query));
	}
	
	public List<ADOutputDoc> getOutputDocuments(String processRefId, String searchTerm) {
		
		StringBuffer query = new StringBuffer("FROM ADOutputDoc WHERE (processRefId=:processRefId "
				+ "or processRefId is null) and isActive=1 ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		if(searchTerm!=null){
			query.append(" and (lower(name) like :searchTerm or "
					+ "lower(description) like :searchTerm or "
					+ "lower(code) like :searchTerm or "
					+ "lower(path) like :searchTerm) ");
			params.put("searchTerm", "%"+searchTerm+"%");
		}
		
		query.append(" order by name,description");
		
		Query emquery = em.createQuery(query.toString()).setParameter("processRefId", processRefId);
		for(String key:params.keySet()){
			emquery.setParameter(key, params.get(key));
		}
		
		return getResultList(emquery);
	}


	public byte[] getHTMLTemplate(String templateName) {
		String sql = "SELECT o.attachment from ADOutputDoc o where o.code=:code";
		Query query = getEntityManager().createQuery(sql).setParameter("code", templateName);
		
		LocalAttachment attachment = getSingleResultOrNull(query);
		
 		return attachment==null? null: attachment.getAttachment();
	}
	
	public byte[] getHTMLTemplateByOutputId(String outputRefId) {
		String sql = "SELECT o.attachment from ADOutputDoc o where o.refId=:refId";
		Query query = em.createQuery(sql).setParameter("refId", outputRefId);
		
		LocalAttachment attachment = getSingleResultOrNull(query);
		
 		return attachment==null? null: attachment.getAttachment();
	}

}
