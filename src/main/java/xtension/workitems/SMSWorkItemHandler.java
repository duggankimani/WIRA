package xtension.workitems;

import java.util.HashMap;

import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

import com.duggan.workflow.server.sms.SMSIntegration;
import com.duggan.workflow.shared.model.Document;

public class SMSWorkItemHandler implements WorkItemHandler {

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		String telephone = workItem.getParameter("telNo").toString();
		String message = workItem.getParameter("message").toString();
		
		try{
			Document document = (Document)workItem.getParameter("document");
			message = message.replace("{subject}", document.getCaseNo());
		}catch(Exception e){}
	
		SMSIntegration integration = new SMSIntegration();
		integration.send(telephone, message);
		manager.completeWorkItem(workItem.getId(), new HashMap<String, Object>());
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		// TODO Auto-generated method stub
		
	}

}
