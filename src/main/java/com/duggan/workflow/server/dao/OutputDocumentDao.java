package com.duggan.workflow.server.dao;

import javax.persistence.EntityManager;

import com.duggan.workflow.server.dao.model.ADOutputDoc;

public class OutputDocumentDao extends BaseDaoImpl {

	public OutputDocumentDao(EntityManager em) {
		super(em);
	}

	public ADOutputDoc getOuputDocument(Long id) {
		return getById(ADOutputDoc.class, id);
	}

}
