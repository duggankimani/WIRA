package com.duggan.workflow.server.rest.service.impl;

import java.io.File;
import java.io.StringWriter;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.jbpm.executor.api.CommandContext;

import com.duggan.workflow.server.actionhandlers.CreateDocumentActionHandler;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.dao.DocumentDaoHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.helper.session.SessionHelper;
import com.duggan.workflow.server.rest.exception.CommandNotFoundException;
import com.duggan.workflow.server.rest.model.BusinessKey;
import com.duggan.workflow.server.rest.model.Data;
import com.duggan.workflow.server.rest.model.KeyValuePair;
import com.duggan.workflow.server.rest.model.Request;
import com.duggan.workflow.server.rest.model.Response;
import com.duggan.workflow.server.rest.service.IncomingRequestService;
import com.duggan.workflow.shared.exceptions.IllegalApprovalRequestException;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.DocType;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.responses.ApprovalRequestResult;
import com.google.inject.Inject;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class IncomingRequestImpl implements IncomingRequestService {

	private static final String NEWAPPROVALREQUESTCOMMAND = "NEWAPPROVALREQUESTCOMMAND";

	public IncomingRequestImpl() {

	}

	@Override
	public void executeClientRequest(Request request, Response response) {

		assert request != null;

		String commandName = request.getCommandName();

		assert commandName != null;

		switch (commandName) {
		case NEWAPPROVALREQUESTCOMMAND:
			createNewRequest(request, response);
			break;
			
		default:
			//new webapplicationexc
			response.setErrorCode("404");
			response.setErrorMessage("Command ["+commandName+"] not found");
			break;
		}

	}

	private Response createNewRequest(Request request, Response response) {

		//response.setContext(request.getContext());
		Document doc = createDocument(request);

		doc.setStatus(DocStatus.INPROGRESS);
		doc = DocumentDaoHelper.save(doc);

		if (doc.getProcessInstanceId() != null) {
			response.setError("500",
					"Cannot execute Approval Request - Document ["+doc+
					"] is already attached to another process "+
			JBPMHelper.getProcessDetails(doc.getProcessInstanceId()));
			//throw new IllegalApprovalRequestException(doc);
		}
	
		Object userId = request.getContext().get("ownerId");
		if (userId == null) {
			response.setError("405","OwnerId cannot be null");
		}

		JBPMHelper.get().createApprovalRequest(userId.toString(), doc);
		BusinessKey key = createBusinessKey(doc);
		response.setBusinessKey(key);
		
		return response;
	}

	private Document createDocument(Request request) {
		Document doc = new Document();
		
		Object description = request.getContext("description");
		Object documentDate = request.getContext("docDate");
		Object owner = request.getContext("ownerId");
		Object partner = request.getContext("BPartner");
		Object priority = request.getContext("priority");
		Object value = request.getContext("value");
		Object subject = request.getContext("subject");
		Object type = request.getContext("docType");
		
		throwExceptionIfNull("Document Date",description);
		throwExceptionIfNull("Owner ID",owner);
		throwExceptionIfNull("Subject",subject);
		throwExceptionIfNull("Document Type",type);
		
//		Object processInstanceId;
//		Object sessionId;
//		Object status;
		
		if(description!=null)
			doc.setDescription(description.toString());
		doc.setDocumentDate(new Date());//(documentDate);
		
		doc.setOwner(LoginHelper.get().getUser(owner.toString()));
		
		if(partner!=null)
			doc.setPartner(partner.toString());
		
		if(priority!=null)
			doc.setPriority(new Integer(priority.toString()));
		
		if(value!=null)
			doc.setValue(value.toString());
		
		doc.setSubject(subject.toString());
		doc.setType(DocType.valueOf(type.toString()));
		
//		doc.setProcessInstanceId(processInstanceId);
//		doc.setSessionId(sessionId);
//		doc.setStatus(status);
		
		return doc;
	}

	private void throwExceptionIfNull(String field, Object value) {
		if(value==null){
			throw new IllegalArgumentException("Field ["+field+"] is mandatory yet null was provided");
		}
	}

	private BusinessKey createBusinessKey(Document doc) {
		BusinessKey businessKey = new BusinessKey(
				doc.getId(), doc.getSessionId() , doc.getProcessInstanceId(), null);
		
		return businessKey;
	}
	
	
	//TESTING ZONE
	public static void main(String[] args) throws Exception {

		BusinessKey key = new BusinessKey(1L, 100L, 232L, 454L);

		CommandContext ctx = new CommandContext();
		ctx.setData("docType", DocType.LPO.name());
		ctx.setData("subject", "LPO/8023/12");
		ctx.setData("docDate", "20/09/2013");
		ctx.setData("value", "100,000Ksh");
		ctx.setData("BPartner", "Alfred & co LTD");
		ctx.setData("description", "This is the description");
		ctx.setData("ownerId", "salaboy");
		ctx.setData("priority", new Integer(1));
		//ctx.setData("id", new Long(3L));

		Request request = new Request("NEWAPPROVALREQUESTCOMMAND1", null, ctx.getData());

//		 marshalJson(request);
//		//
//		 marshalXml(request);
		//
		Response response = new OutgoingRequestImpl().executeCall(request);

		assert response != null;
		
		assert response.getBusinessKey()!=null;

		System.err.println("Response >>>>> " + response);
	}

	private static void marshalXml(Request request) throws Exception {
		JAXBContext jaxbCtx = JAXBContext.newInstance(Request.class,
				CommandContext.class, Response.class, Data.class,
				KeyValuePair.class);

		Marshaller marshaller = jaxbCtx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter writer = new StringWriter();
		marshaller.marshal(request, writer);
		System.err.println("Request= " + writer.toString());
	}

	private static void marshalJson(Request request) throws Exception {
		JSONJAXBContext jaxbCtx = new JSONJAXBContext(JSONConfiguration
				.natural().build(), Request.class);

		JSONMarshaller marshaller = jaxbCtx.createJSONMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter writer = new StringWriter();
		marshaller.marshallToJSON(request, writer);
		System.err.println("Request= " + writer.toString());
	}

}
