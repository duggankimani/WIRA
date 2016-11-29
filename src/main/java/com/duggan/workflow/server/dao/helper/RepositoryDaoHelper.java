package com.duggan.workflow.server.dao.helper;


public class RepositoryDaoHelper {

	/**
	 * Refs: 
	 * http://mswiderski.blogspot.co.ke/2015/09/unified-kie-execution-server-part-2.html
	 * 
	 * 
	 */
	public void getRepositories(){
		RestClient client = RestClient.getInstance();
		String json = client.executeGet("/repositories", null);
		System.err.println(">> Output - "+json);
	}
}
