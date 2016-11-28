package com.duggan.workflow.server.dao.helper;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;

public class RestClient {

	String serverUrl = "http://localhost:9090/kie-wb/rest";
	
	/**
	 * Jersey Client
	 */
	private Client jclient;
	private static RestClient client;

	private RestClient(){
		DefaultClientConfig config = new DefaultClientConfig(KIEJaxbBinding.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);	
		jclient = Client.create(config);
		jclient.addFilter(new HTTPBasicAuthFilter("workbench", "workbench1!"));
		jclient.setConnectTimeout(15000);
	}
	
	public static RestClient getInstance(){
		if(client==null){
			synchronized (RestClient.class) {
				if(client==null){
					client = new RestClient();
				}
			}
		}
		
		return client;
	}

	public Client getJclient() {
		return jclient;
	}
	
	public String executeGet(String uri, Object request) {
		WebResource resource = jclient.resource(serverUrl+uri);

		ClientResponse clientResponse = null;

		try {
			clientResponse = resource.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.get(ClientResponse.class);
					//.post(ClientResponse.class, request);

			// response.getHeaders()
		} catch (Exception e) {

			// server unavailable
			throw new RuntimeException(e);
		}

		if (clientResponse.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
			throw new RuntimeException("Unexpected HTTP Status ["+clientResponse.getStatus()+"]");
		}

		String response = clientResponse.getEntity(String.class);

		return response;

	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

}
