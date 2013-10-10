package xtension.workitems;

import java.util.Map;

import org.apache.log4j.Logger;
import org.drools.process.instance.WorkItemHandler;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

import com.duggan.workflow.server.rest.model.Request;
import com.duggan.workflow.server.rest.model.Response;
import com.duggan.workflow.server.rest.service.IncomingRequestService;
import com.duggan.workflow.server.rest.service.OutgoingRequestService;
import com.duggan.workflow.server.rest.service.impl.IncomingRequestImpl;
import com.duggan.workflow.server.rest.service.impl.OutgoingRequestImpl;

public class IntegrationWorkItemHandler implements WorkItemHandler{

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Object commandName = workItem.getParameter("CommandName");
		
		if(commandName==null){
			throw new IllegalArgumentException("Comand name cannot be null");
		}
		
		Map<String, Object> params = workItem.getParameters();
		
		assert params.get("document") != null;
		
		System.err.println("Document for Integration :: "+params.get("document"));
		
		Request request = new Request();
		request.setContext(workItem.getParameters());
		request.setCommandName(commandName.toString());
		
		OutgoingRequestService service = new OutgoingRequestImpl();
		//Response response = service.executeCall(request);
		
		manager.completeWorkItem(workItem.getId(), workItem.getParameters());
		
	}
	
	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		
	}
	
}
