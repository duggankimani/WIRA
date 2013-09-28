package com.duggan.workflow.server.rest.service.impl;

import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.db.DBTrxProvider;
import com.duggan.workflow.server.rest.model.Request;
import com.duggan.workflow.server.rest.model.Response;
import com.duggan.workflow.server.rest.service.OutgoingRequestService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class OutgoingRequestImpl implements OutgoingRequestService{

	static String erpUrl = "http://127.0.0.1:8888/rest";
	
	//String erpUrl = "http://ebusiness-duggansit.rhcloud.com/ebusiness/rest";
	
	static Logger logger = Logger.getLogger(OutgoingRequestImpl.class);
	
	static Properties properties = new Properties();
	
	static{
		try{
			InputStream is = OutgoingRequestImpl.class.getResourceAsStream("/general.properties");
			properties.load(is);
			String prop = properties.getProperty("erpUrl");
			if(prop!=null){
				erpUrl = prop;
			}else{
				logger.warn("Property [erpUrl] not found, using default value ["+erpUrl+"]");
			}
		}catch(Exception e){
			logger.warn("Error Loading [erpUrl], using default value ["+erpUrl+"] :: Cause "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Jersey Client
	 */
	private Client jclient;
	
	public OutgoingRequestImpl(){
		DefaultClientConfig config = new DefaultClientConfig(JAXBProviderImpl.class);
		jclient = Client.create(config);

	}
	
	@Override
	public Response executeCall(Request request) {
		
		logger.info("Submitting Request : "+request);

		String uri = erpUrl + "/request/approval";

		WebResource resource = jclient.resource(uri);

		ClientResponse response = null;

		try {
			response = resource.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, request);
			
			// response.getHeaders()
		} catch (Exception e) {

			// server unavailable
			throw new RuntimeException(e);
		}

		if (response.getClientResponseStatus().equals(
				ClientResponse.Status.INTERNAL_SERVER_ERROR)) {

			RuntimeException e = new RuntimeException(response.getEntity(String.class));
			
			throw e;
		}

		return response.getEntity(Response.class);
	}

}
