package com.duggan.workflow.server.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.ADOutputDoc;

public class OutputDocumentDao extends BaseDaoImpl {

	public OutputDocumentDao(EntityManager em) {
		super(em);
	}

	public ADOutputDoc getOuputDocument(Long id) {
		return getById(ADOutputDoc.class, id);
	}

	public List<ADOutputDoc> getOutputDocuments() {
		
		String query = "FROM ADOutputDoc WHERE isActive=1";
		return getResultList(em.createQuery(query));
	}

}
