package com.duggan.workflow.server.rest.service.impl;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.jbpm.executor.api.CommandContext;

import com.duggan.workflow.server.dao.helper.DocumentDaoHelper;
import com.duggan.workflow.server.dao.helper.FormDaoHelper;
import com.duggan.workflow.server.dao.model.ADValue;
import com.duggan.workflow.server.helper.auth.LoginHelper;
import com.duggan.workflow.server.helper.jbpm.JBPMHelper;
import com.duggan.workflow.server.rest.exception.CommandNotFoundException;
import com.duggan.workflow.server.rest.model.BusinessKey;
import com.duggan.workflow.server.rest.model.Data;
import com.duggan.workflow.server.rest.model.Detail;
import com.duggan.workflow.server.rest.model.KeyValuePair;
import com.duggan.workflow.server.rest.model.Request;
import com.duggan.workflow.server.rest.model.Response;
import com.duggan.workflow.server.rest.service.IncomingRequestService;
import com.duggan.workflow.shared.model.DataType;
import com.duggan.workflow.shared.model.DocStatus;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.DocumentType;
import com.duggan.workflow.shared.model.Value;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;

public class IncomingRequestImpl implements IncomingRequestService {

	public static final String NEWAPPROVALREQUESTCOMMAND = "NEWAPPROVALREQUESTCOMMAND";
	//private static final String WORKFLOWCALLOUTCOMMAND = "WORKFLOWCALLOUTCOMMAND";

	private static Logger logger = Logger.getLogger(IncomingRequestImpl.class);
	
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
			throw new CommandNotFoundException("Command ["+commandName+"] not found");
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

//		Object processInstanceId;
//		Object sessionId;
//		Object status;
		
		Document doc = new Document();
		
		Object documentDate = request.getContext("docDate");
		if(documentDate==null){
			documentDate = new Date();
		}
		
		Object owner = request.getContext("ownerId");
		Object docType = request.getContext("docType");
		
		throwExceptionIfNull("Document Date",documentDate);
		throwExceptionIfNull("Owner ID",owner);
		throwExceptionIfNull("Document Type",docType);
		
		Map<String,Object> context = request.getContext();
		doc.setDocumentDate(new Date());//(documentDate);
		doc.setOwner(LoginHelper.get().getUser(owner.toString()));
		
		logger.debug(">>> DocType = "+docType);
		DocumentType type = DocumentDaoHelper.getDocumentType(docType.toString());
		context.put("docType", type);
		doc.setType(type);
		
		for(String key: context.keySet()){
			Object value = context.get(key);
			
			if(value!=null){
				if(key.equals("description")){
					doc.setDescription(value.toString());
				}
				
				if(key.equals("subject")){
					doc.setSubject(value.toString());
				}
				
				if(key.equals("priority")){
					doc.setPriority(new Integer(value.toString()));
				}
				
//				if(value instanceof Map){
//					doc.addDetail(createLine(key, ((Map<String, Object>)value)));
//					continue;
//				}
			}else{ 
				continue;
				//value is null - go to next loop
			}
		
			doc.setValue(key, getValue(key, value));
			
		}
		
		List<Detail> details = request.getDetails();
		if(details!=null){
			for(Detail detail: details){
				doc.addDetail(createLine(detail.getName(), detail.getDetails()));
			}
		}
	
		return doc;
	}
	
	

	private Value getValue(String key, Object value) {
		ADValue advalue = new ADValue();
		advalue.setFieldName(key);			
		
		String stringValue=null;
		Boolean booleanValue=null;
		Long longValue=null;
		Double doubleValue=null;
		Date dateValue=null;
		DataType type=null;
		
		if(value instanceof String){
			stringValue = value.toString();
			type=DataType.STRING;
		}else if(value instanceof Boolean){
			booleanValue = (Boolean)value;
			value=booleanValue;
			type=DataType.BOOLEAN;
		}else if(value instanceof Long){
			longValue=(Long)value;
			value=longValue;
			type=DataType.INTEGER;
		}else if(value instanceof Integer){
			longValue = new Long(value.toString());
			value=longValue;
			type=DataType.INTEGER;
		}else if(value instanceof Double){
			doubleValue = (Double)value;
			value=doubleValue;
			type=DataType.DOUBLE;
		}else if(value instanceof Number){
			doubleValue = ((Number)value).doubleValue();
			value=doubleValue;
			type=DataType.DOUBLE;
		}else if(value instanceof Date){
			dateValue = (Date)value;
			value=dateValue;
			type=DataType.DATE;
		}
		advalue.setStringValue(stringValue);
		advalue.setBooleanValue(booleanValue);
		advalue.setLongValue(longValue);
		advalue.setDoubleValue(doubleValue);
		advalue.setDateValue(dateValue);
		
		Value fieldValue = FormDaoHelper.getValue(advalue, type);
	
		return fieldValue;
	}

	private DocumentLine createLine(String key, Map<String, Object> map) {

		DocumentLine line = new DocumentLine();
		line.setName(key);
		
		for(String field: map.keySet()){
			Object value = map.get(field);
			Value fieldValue = getValue(field, value);			
			line.addValue(field, fieldValue);
		}
		
		return line;
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
		//ctx.setData("docType", DocType.LPO.name());
		ctx.setData("subject", "LPO/8023/12");
		ctx.setData("docDate", "20/09/2013");
		ctx.setData("value", "100,000Ksh");
		ctx.setData("BPartner", "Alfred & co LTD");
		ctx.setData("description", "This is the description");
		ctx.setData("ownerId", "salaboy");
		ctx.setData("priority", new Integer(1));
		//ctx.setData("id", new Long(3L));

		Request request = new Request(NEWAPPROVALREQUESTCOMMAND, key, ctx.getData());

		 marshalJson(request);
//		//
//		 marshalXml(request);
		//
		Response response = new OutgoingRequestImpl().executeCall(request);

		assert response != null;
		
		assert response.getBusinessKey()!=null;

		logger.debug("Response >>>>> " + response);
	}

	private static void marshalXml(Request request) throws Exception {
		JAXBContext jaxbCtx = JAXBContext.newInstance(Request.class,
				CommandContext.class, Response.class, Data.class,
				KeyValuePair.class);

		Marshaller marshaller = jaxbCtx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter writer = new StringWriter();
		marshaller.marshal(request, writer);
		logger.debug("Request= " + writer.toString());
	}

	private static void marshalJson(Request request) throws Exception {
		JSONJAXBContext jaxbCtx = new JSONJAXBContext(JSONConfiguration
				.natural().build(), Request.class);

		JSONMarshaller marshaller = jaxbCtx.createJSONMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		StringWriter writer = new StringWriter();
		marshaller.marshallToJSON(request, writer);
		logger.debug("Request= " + writer.toString());
	}

}
