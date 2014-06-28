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
		String clCode = document.getValues().get("clCode").getValue().toString();
		String idNo = document.getValues().get("idNumber").getValue().toString();
		String mpesaCode = document.getValues().get("mpesaCode").getValue().toString();

		send(uri, mpesaCode, idNo, clCode);
		workItemMgr.completeWorkItem(workItem.getId(), new HashMap<String, Object>());
	}

	private void send(String uri, String mpesaCode, String idNo, String clientCode) {
		if(!uri.endsWith("/")){
			uri = uri.substring(0, uri.length()-1);
		}
		String encodedUrl = uri+"?mpesaCode="+mpesaCode+"&idNo="+idNo+"&clientCode="+clientCode;
		
		System.out.println("Encoded::"+encodedUrl); 
		log.warn("Encoded uri: "+encodedUrl);
		new OutgoingRequestImpl().executePostCall(encodedUrl);
	}

		
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager workItemMgr) {
		
	}

}
