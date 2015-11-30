package org.wira.pioneer.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import com.duggan.workflow.server.rest.service.impl.OutgoingRequestImpl;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.GridValue;
import com.duggan.workflow.shared.model.Value;

public class PioneerIntegrationWorkitemHandler implements WorkItemHandler {

	private Logger log = Logger
			.getLogger(PioneerIntegrationWorkitemHandler.class);

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager workItemMgr) {
		Document document;

		String uri = workItem.getParameter("uri").toString();
		if (uri.endsWith("/")) {
			uri = uri.substring(0, uri.length() - 1);
		}

		document = (Document) workItem.getParameter("document");
		String parameter = document.getValues().get("requestType").getValue()
				.toString();
		RequestType requestType = RequestType.getRequestType(parameter);

		createData(requestType, document, uri, workItem.getParameters());
		// Complete the process
		workItemMgr.completeWorkItem(workItem.getId(),
				new HashMap<String, Object>());

	}

	private void createData(RequestType requestType, Document document,
			String uri, Map<String, Object> parameters) {
		Map<String, Object> postBody = new HashMap<String, Object>();

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
			send(uri, postBody);
			break;

		case ALLOCATIONREQUEST:
			System.err.println("Executed Allocation Request");
			String allocatedTo = document.getValues().get("allocatedTo")
					.getValue().toString();
			String terminalId = document.getValues().get("terminalId")
					.getValue().toString();
			String allocatedBy = parameters.get("actorId").toString();
			postBody.put("allocatedTo", allocatedTo);
			postBody.put("allocatedBy", allocatedBy);
			postBody.put("terminalId", terminalId);
			send(uri, postBody);
			break;

		case DEALLOCATIONREQUEST:
			Value values = document.getValues().get("summaryTable");
			List<DocumentLine> docLines = new ArrayList<DocumentLine>();
			if (values == null) {
				docLines = document.getDetails().get("summaryTable");
			} else {
				Collection<DocumentLine> lines = ((GridValue) values)
						.getValue();
				if (lines != null) {
					docLines.addAll(lines);
				}
			}

			// This is a row of values (to String returns key:value sets for
			// each column)
			// eg trxTotal:500,customerCount:40 etc
			/*
			 *
			 * System.err.println("Document Lines>>>" + line); //To get the
			 * value of a row & its column Value col1Row1=
			 * line.getValue("colName"); Object actualValue
			 * =col1Row1.getValue(); //if you know the col was a double/currency
			 * field Double actual = ((DoubleValue)col1Row1).getValue();
			 */
			
			DocumentLine line = docLines.get(0);
			postBody.put("actorId", parameters.get("actorId").toString());
			postBody.put("allocationId", line.getValue("allocationId").getValue());
			System.err.println("allocationId>>"+line.getValue("allocationId").getValue());
			send(uri, postBody);
			break;

		default:
			System.err.println("Nothing sent to PioneerApp");
			log.warn("Nothing was sent to PioneerApp");
			// abortWorkItem(workItem, workItemMgr);
			throw new RuntimeException("Nothing to Run");
		}
	}

	private void send(String encodedUrl, Map<String, Object> postBody) {
		System.out.println("Encoded URL::" + encodedUrl);
		log.error("Encoded uri: " + encodedUrl);
		new OutgoingRequestImpl(true).executePostCall(encodedUrl, postBody);
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager workItemMgr) {

	}

}
