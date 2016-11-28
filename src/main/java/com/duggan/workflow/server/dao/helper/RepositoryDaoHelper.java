package com.duggan.workflow.server.dao.helper;

public class RepositoryDaoHelper {

	public void getRepositories(){
		RestClient client = RestClient.getInstance();
		String out = client.executeGet("/repositories", null);
		System.err.println(">> Output - "+out);
	}
}
