package org.wira.pioneer.integration;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

import com.duggan.workflow.server.rest.service.impl.OutgoingRequestImpl;
import com.duggan.workflow.shared.model.Document;

public class PioneerIntegrationWorkitemHandler implements WorkItemHandler {

	private Logger log = Logger.getLogger(PioneerIntegrationWorkitemHandler.class);
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager workItemMgr) {
		String uri = workItem.getParameter("uri").toString();
		Document document = (Document)workItem.getParameter("document");
		String clientCode = document.getValues().get("clientCode").toString();
		String idNo = document.getValues().get("idNo").toString();
		String trxNo = document.getValues().get("trxNo").toString();

		send(uri, trxNo, idNo, clientCode);
		workItemMgr.completeWorkItem(workItem.getId(), new HashMap<String, Object>());
	}

	private void send(String uri, String trxNo, String idNo, String clientCode) {
		if(!uri.endsWith("/")){
			uri = uri.substring(0, uri.length()-1);
		}
		String encodedUrl = uri+"?trxtNo="+trxNo+"&idNo="+idNo+"&clientCode="+clientCode;
		log.warn("Encoded uri: "+encodedUrl);
		new OutgoingRequestImpl().executePostCall(encodedUrl);
	}

		
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager workItemMgr) {
		
	}

}
