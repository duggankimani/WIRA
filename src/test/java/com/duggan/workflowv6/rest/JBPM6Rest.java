package com.duggan.workflowv6.rest;

import org.junit.Test;

import com.duggan.workflow.server.dao.helper.RepositoryDaoHelper;

public class JBPM6Rest {

	@Test
	public void getRepositories(){
		RepositoryDaoHelper helper = new RepositoryDaoHelper();
		helper.getRepositories();
	}
}
