package com.duggan.workflow.server.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.duggan.workflow.server.ServerConstants;
import com.duggan.workflow.server.db.DB;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.server.rest.exception.WiraExceptionModel;
import com.duggan.workflow.server.rest.exception.WiraServiceException;
import com.duggan.workflow.server.rest.model.Request;
import com.duggan.workflow.server.rest.model.Response;
import com.duggan.workflow.server.rest.service.IncomingRequestService;
import com.duggan.workflow.server.rest.service.impl.IncomingRequestImpl;
import com.duggan.workflow.shared.model.HTUser;

@Path("/request/")
public class CommandBasedRestService{

	@GET
	@Path("/{name}")
	public String sayHello(@PathParam("name") String name){
		return "Hello "+name;
	}
	
	@POST
	@Path("/approval")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response execute(@Context HttpServletRequest httprequest, Request request){
		
		
		Response response = new Response();
		
		try{
			DB.beginTransaction();
			
			HTUser user = new HTUser();
			user.setUserId(request.getContext("ownerId").toString());
			httprequest.getSession().setAttribute(ServerConstants.USER,user);
			
			//check session
			SessionHelper.setHttpRequest(httprequest);

			IncomingRequestService service = new IncomingRequestImpl();
			
			service.executeClientRequest(request, response);
			
			DB.commitTransaction();			
		}catch(Exception e){
			DB.rollback();
			throw new WiraServiceException(WiraExceptionModel.getExceptionModel(e));
			
		}finally{
			
			DB.closeSession();
			SessionHelper.setHttpRequest(null);
			JBPMHelper.clearRequestData();
			
		}		
		
		return response;
	}
}
