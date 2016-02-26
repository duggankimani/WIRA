package com.duggan.workflow.server.rest.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.duggan.workflow.server.rest.exception.WiraExceptionModel;
import com.duggan.workflow.server.rest.model.Request;
import com.duggan.workflow.server.rest.model.Response;
import com.duggan.workflow.server.rest.service.OutgoingRequestService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class OutgoingRequestImpl implements OutgoingRequestService {

	static String serviceUri = null;

	static String propfile = "/general.properties";

	// String erpUrl = "http://ebusiness-duggansit.rhcloud.com/ebusiness/rest";

	static Logger logger = Logger.getLogger(OutgoingRequestImpl.class);

	static Properties properties = new Properties();

	static {
		try {
			InputStream is = OutgoingRequestImpl.class
					.getResourceAsStream(propfile);
			properties.load(is);
			String prop = properties.getProperty("ServiceUri");
			if (prop != null) {
				serviceUri = prop;
			} else {
				logger.warn("Property [ServiceUri] not found, using default value ["
						+ serviceUri + "]");
			}
		} catch (Exception e) {
			// logger.warn("Error Loading [erpUrl], using default value ["+erpUrl+"] :: Cause "+e.getMessage());
			// e.printStackTrace();
		}
	}

	/**
	 * Jersey Client
	 */
	private Client jclient;

	
	public OutgoingRequestImpl() {
		this(false);
	}
	
	public OutgoingRequestImpl(Boolean myConfiguration) {
		DefaultClientConfig config = new DefaultClientConfig(JAXBProviderImpl.class);
		if(myConfiguration){
			config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);	
		}
		jclient = Client.create(config);
		jclient.setConnectTimeout(15000);

	}
	

	@Override
	public Response executeCall(Request request) {
		String uri = null;

		Object URI = request.getContext("serviceURI");
		if (URI != null && !URI.toString().isEmpty()) {
			uri = URI.toString();
		}

		if (uri == null) {
			String log = "Trying ServiceUri Property from classpath:"
					+ propfile + ".. ";
			if (serviceUri == null) {
				logger.info(log + " - NO VALUE FOUND");
			} else {
				uri = serviceUri;
				logger.info(log + " - " + uri);
			}
		}

		return executeCall(request, uri);
	}

	@Override
	public Response executeCall(Request request, String serviceURI) {

		logger.info("Submitting Request : " + request);

		if (serviceURI == null || serviceURI.isEmpty()) {
			throw new IllegalArgumentException(
					"REST URI cannot be null for rest service");
		}

		WebResource resource = jclient.resource(serviceURI);

		ClientResponse clientResponse = null;

		try {
			clientResponse = resource.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, request);

			// response.getHeaders()
		} catch (Exception e) {

			// server unavailable
			throw new RuntimeException(e);
		}

		if (!clientResponse.getClientResponseStatus().equals(
				ClientResponse.Status.OK)) {

			WiraExceptionModel model = clientResponse
					.getEntity(WiraExceptionModel.class);

			throw new RuntimeException(model.getCause());
		}

		Response response = clientResponse.getEntity(Response.class);

		return response;

	}
	
	public void executePostCall(String urlEncodedString) {
		executePostCall(urlEncodedString, null);
	}

	public void executePostCall(String urlEncodedString, Map <String,Object> postBody) {
		logger.warn("Executing post: " + urlEncodedString);
		try {
			WebResource resource = jclient.resource(urlEncodedString);
			
			ClientResponse response = resource.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, postBody);
			
			// Check response status code
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			// display response
			String output = response.getEntity(String.class);
			System.out.println("Output from Server .... ");
			System.out.println(output + "\n");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Something wrong happened while executing the Post call ");
		}

	}

	public static void main(String[] args) {
		Map<String,Object> postBody = new HashMap<String,Object>();
		postBody.put("allocatedTo", "1");
		postBody.put("allocatedBy", "daniel");
		postBody.put("terminalId", "28");
		String url  = "http://localhost:8030/PioneerMSSQL/index.php/api/flexipay_server/postAllocation";
		new OutgoingRequestImpl(true)
				.executePostCall(url,postBody);
	}

}
