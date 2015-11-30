package com.duggan.workflow.server.dao;

import java.util.List;

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

	public byte[] getHTMLTemplate(String templateName) {
		String sql = "SELECT o.attachment from ADOutputDoc o where o.code=:code";
		Query query = getEntityManager().createQuery(sql).setParameter("code", templateName);
		
		LocalAttachment attachment = getSingleResultOrNull(query);
		
 		return attachment==null? null: attachment.getAttachment();
	}

}
