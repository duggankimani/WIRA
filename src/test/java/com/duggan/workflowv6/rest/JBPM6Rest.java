package com.duggan.workflowv6.rest;

import org.jbpm.services.task.impl.model.TaskImpl;
import org.jbpm.services.task.query.TaskSummaryImpl;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.RepositoryDaoHelper;

public class JBPM6Rest {

	@Test
	public void getRepositories(){
		RepositoryDaoHelper helper = new RepositoryDaoHelper();
		helper.getRepositories();
		
		TaskImpl impl;
		
	}
}
