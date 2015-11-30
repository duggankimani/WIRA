package xtension.workitems;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

import com.duggan.workflow.server.rest.model.Request;
import com.duggan.workflow.server.rest.model.Response;
import com.duggan.workflow.server.rest.service.OutgoingRequestService;
import com.duggan.workflow.server.rest.service.impl.OutgoingRequestImpl;
import com.duggan.workflow.shared.model.Document;
import com.duggan.workflow.shared.model.Value;

/**
 * Inbuilt Restful Interface handler 
 * 
 * @author duggan
 *
 */
public class IntegrationWorkItemHandler implements WorkItemHandler{

	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		Object commandName = workItem.getParameter("commandName");
		
		if(commandName==null){
			throw new IllegalArgumentException("Comand name cannot be null");
		}
		String serviceUrl = workItem.getParameter("serviceURI")==null? null:
			workItem.getParameter("serviceURI").toString();
		String serviceCode = workItem.getParameter("serviceCode")==null? null:
			workItem.getParameter("serviceCode").toString();
		
		if(serviceUrl==null){
			if(serviceCode!=null){
				serviceUrl = ".....TODO :Get Service URL from Service Code...";
			}else{
				throw new IllegalArgumentException("ServiceURL required to execute command \""+
			commandName+"\"");
			}
		}
				
		logger.debug("CommandName=  "+commandName);		
		
		Request request = new Request();
		request.setContext(getValues(workItem.getParameters()));
		
		request.setCommandName(commandName.toString());
		//request.setBusinessKey(businessKey); -- Async Services will need this
		
		OutgoingRequestService service = new OutgoingRequestImpl();
		Response response = service.executeCall(request);
		
		manager.completeWorkItem(workItem.getId(), response.getContext());
		
	}
	
	/**
	 * TODO: The current mechanism is only able to Serialize primitives
	 * We need to provide Custom Objects Serialization  
	 * 
	 * @param parameters
	 * @return
	 */
	private Map<String, Object> getValues(Map<String, Object> parameters) {
		Map<String,Object> values = new HashMap<>();
		
		Set<String> keyset = parameters.keySet();
		for(String key: keyset){
			Object value = parameters.get(key);
			if(value instanceof Document){
				Map<String, Value> vals = ((Document)value).getValues();
				Collection<Value> docValues = vals.values();
				
				for (Value val : docValues) {
					if(val!=null){
						values.put(val.getKey(), val.getValue());
					}
				}
			}else if(value instanceof Serializable){
				values.put(key, value);
			}
		}
		return values;
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		
	}
	
}
