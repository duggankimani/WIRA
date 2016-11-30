package com.duggan.workflow.test.v6.rest;

import org.junit.Before;
import org.junit.Test;

import com.duggan.workflow.server.dao.helper.RepositoryDaoHelper;

public class JBPM6Rest {
	
	RepositoryDaoHelper helper = null;
	
	@Before
	public void init(){
		helper = new RepositoryDaoHelper();
	}
	
	@Test
	public void getRepositories(){
		helper.getRepositories();
	}
	
	@Test
	public void getProjects(){
		String repositoryName  = "wira";
		helper.getProjects(repositoryName);
	}
}
