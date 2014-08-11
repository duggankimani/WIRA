package org.wira.pioneer.integration;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

import com.duggan.workflow.server.rest.service.impl.OutgoingRequestImpl;
import com.duggan.workflow.shared.model.Document;

public class PioneerIntegrationWorkitemHandler implements WorkItemHandler {

	private Logger log = Logger
			.getLogger(PioneerIntegrationWorkitemHandler.class);
	private Document document;
	private String uri;
	private WorkItem workItem;
	private Map<String,Object> postBody;
	private WorkItemManager workItemMgr;

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager workItemMgr) {
		this.workItem = workItem;
		this.workItemMgr = workItemMgr;
		uri = workItem.getParameter("uri").toString();
		if (uri.endsWith("/")) {
			uri = uri.substring(0, uri.length() - 1);
		}
		
		document = (Document) workItem.getParameter("document");
		String parameter = document.getValues().get("requestType").getValue()
				.toString();
		RequestType requestType = RequestType.getRequestType(parameter);

		createData(requestType);
		// Complete the process
		workItemMgr.completeWorkItem(workItem.getId(),
				new HashMap<String, Object>());

	}

	private void createData(RequestType requestType) {
		postBody = new HashMap<String,Object>();
		
		switch (requestType) {
		case MPESAIPN:
			String clCode = document.getValues().get("clCode").getValue()
					.toString();
			String idNo = document.getValues().get("idNumber").getValue()
					.toString();
			String mpesaCode = document.getValues().get("mpesaCode").getValue()
					.toString();
			
			postBody.put("clCode", clCode);
			postBody.put("idNo", idNo);
			postBody.put("mpesaCode", mpesaCode);
			send(uri,postBody);
			break;

		case ALLOCATIONREQUEST:
			System.err.println("Executed Allocation Request");
			String allocatedTo = document.getValues().get("allocatedTo")
					.getValue().toString();
			String terminalId = document.getValues().get("terminalId")
					.getValue().toString();
			String allocatedBy = workItem.getParameter("actorId").toString();
			postBody.put("allocatedTo", allocatedTo);
			postBody.put("allocatedBy", allocatedBy);
			postBody.put("terminalId", terminalId);
			send(uri,postBody);
			break;

		default:
			System.err.println("Nothing sent to PioneerApp");
			log.warn("Nothing was sent to PioneerApp");
			abortWorkItem(workItem, workItemMgr);
			break;
		}
	}

	private void send(String encodedUrl, Map<String,Object> postBody) {
		System.out.println("Encoded URL::" + encodedUrl);
		log.error("Encoded uri: " + encodedUrl);
		new OutgoingRequestImpl(true).executePostCall(encodedUrl,postBody);
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager workItemMgr) {

	}

}
